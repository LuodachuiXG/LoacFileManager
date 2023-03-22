package cc.loac.frame;

import cc.loac.common.Alert;
import cc.loac.common.Tool;
import cc.loac.dao.MyIni;
import cc.loac.myenum.OS;
import javafx.scene.input.KeyCode;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class Home extends JFrame implements ActionListener, WindowListener,
        ComponentListener, ListSelectionListener, KeyListener, MouseListener {
    private final MyIni myIni = MyIni.getInstance();
    // 记录窗口默认位置
    private Point point = myIni.getHomeLocation();

    // 记录窗口默认大小
    private Dimension dimension = myIni.getHomeSize();

    /* 菜单栏和菜单 */
    private JMenuBar menuBar;
    private JMenu menu_file;
    private JMenuItem menu_file_exit;

    /* 页面主布局 */
    private JPanel panel_borderLayout;

    /* ToolBar 面板及组件 */
    private JPanel panel_toolBar;
    private JButton button_toolBar_pre;
    private JButton button_toolBar_next;
    private JTextField textField_toolBar_path;
    private JButton button_toolBar_go;

    /* list_rootDir 列表 */
    private JList list_rootDir;
    private List<String> list_rootDirData = new ArrayList<>();

    /* table_files 表格，展示文件夹中文件 */
    private JTable table_files;
    // 存储当前表格实际显示的文件
    private List<File> table_files_Data;
    private String[] table_files_column = {"名称", "大小", "修改时间"};
    private DefaultTableModel table_files_model;
    private JScrollPane scrollPane_jList_files;
    private JPopupMenu popupMenu_table_files;
    private JMenuItem popupMenu_table_files_open;
    private JMenuItem popupMenu_table_files_compress;
    private JMenuItem popupMenu_table_files_newFile;
    private JMenuItem popupMenu_table_files_newDir;
    private JMenuItem popupMenu_table_files_del;
    private JMenuItem popupMenu_table_files_showHidden;
    private JMenuItem popupMenu_table_files_copy;
    private JMenuItem popupMenu_table_files_cut;
    private JMenuItem popupMenu_table_files_rename;

    // 记录当前文件位置
    private String currentPath;

    // 记录文件表格是否显示隐藏文件
    private boolean showHiddenFile = false;

    // 记录当前文件夹的数组，用于前进后退
    // 设置最大长度为 10 ，将在 updatePreAndNextButtonState 方法中约束
    private List<String> listPath = new ArrayList<>();
    // 当前地址在 listPath 中的索引
    private int listPath_index = 0;

    public Home() {
        super("Loac 文件管理器");
        // 初始化组件
        initComponent(this);

        // 初始化组件数据
        initComponentData(this);


        // 设置窗口
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension ds = toolkit.getScreenSize();
        if (point == null) {
            // 当前可能是第一次打开程序，没有设置过窗口位置
            // 设置默认位置为屏幕中间
            point = new Point();
            point.setLocation(ds.getWidth() / 2 - 450, ds.getHeight() / 2 - 350);
        }

        if (dimension == null) {
            // 当前可能是第一次打开程序，没有设置过窗口大小
            // 设置默认大小
            dimension = new Dimension();
            dimension.setSize(900, 700);
        }
        this.addWindowListener(this);
        this.setLocation(point);
        this.setSize(dimension);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }


    /**
     * 初始化组件
     * @param frame JFrame 引用
     */
    private void initComponent(JFrame frame) {
        /* 设置菜单栏和菜单 */
        menuBar = new JMenuBar();
        menu_file = new JMenu("文件");
        menu_file_exit = new JMenuItem("退出程序");
        menu_file_exit.addActionListener(this);
        menu_file.add(menu_file_exit);
        menuBar.add(menu_file);


        /* 设置主面板 BorderLayout */
        panel_borderLayout = new JPanel(new BorderLayout());
        // 设置主面板组件事件
        panel_borderLayout.addComponentListener(this);

        /* 设置 ToolBar 面板 */
        panel_toolBar = new JPanel(new GridBagLayout());
        button_toolBar_pre = new JButton("<");
        button_toolBar_next = new JButton(">");
        textField_toolBar_path = new JTextField("/");
        button_toolBar_go = new JButton("转到");

        button_toolBar_pre.addActionListener(this);
        button_toolBar_next.addActionListener(this);
        button_toolBar_go.addActionListener(this);

        // 按钮默认禁用
        button_toolBar_pre.setEnabled(false);
        button_toolBar_next.setEnabled(false);
        textField_toolBar_path.addKeyListener(this);
        // 向ToolBar 面板添加组件
        addComponent(panel_toolBar, button_toolBar_pre, 0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addComponent(panel_toolBar, button_toolBar_next, 1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addComponent(panel_toolBar, textField_toolBar_path, 2, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addComponent(panel_toolBar, button_toolBar_go, 3, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH);


        /* 初始化 BorderLayout 左侧用于显示系统根目录（盘符）的 List */
        list_rootDir = new JList();
        list_rootDir.addListSelectionListener(this);


        /* 初始化位于 center 用于展示文件夹和文件的 Table */
        table_files = new JTable(table_files_model) {
            // 禁止表格编辑
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table_files_model = new DefaultTableModel();
        // 设置 Table 的列名
        for (String columnName : table_files_column) {
            table_files_model.addColumn(columnName);
        }
        table_files.setModel(table_files_model);
        table_files.addMouseListener(this);
        // 将 Table 加入滚动面板
        scrollPane_jList_files = new JScrollPane(table_files);

        /* 设置 table_files 的右键弹出菜单 */
        popupMenu_table_files = new JPopupMenu();
        popupMenu_table_files_open = new JMenuItem("打开");
        popupMenu_table_files_newFile = new JMenuItem("新建文件");
        popupMenu_table_files_newDir = new JMenuItem("新建文件夹");
        popupMenu_table_files_compress = new JMenuItem("压缩");
        popupMenu_table_files_copy = new JMenuItem("复制到");
        popupMenu_table_files_cut = new JMenuItem("剪切到");
        popupMenu_table_files_del = new JMenuItem("删除文件");
        popupMenu_table_files_rename = new JMenuItem("重命名");
        popupMenu_table_files_showHidden = new JMenuItem("显示隐藏文件");

        // 添加菜单项点击事件
        popupMenu_table_files_open.addActionListener(this);
        popupMenu_table_files_newFile.addActionListener(this);
        popupMenu_table_files_newDir.addActionListener(this);
        popupMenu_table_files_compress.addActionListener(this);
        popupMenu_table_files_copy.addActionListener(this);
        popupMenu_table_files_cut.addActionListener(this);
        popupMenu_table_files_del.addActionListener(this);
        popupMenu_table_files_rename.addActionListener(this);
        popupMenu_table_files_showHidden.addActionListener(this);

        // 根据配置文件获取当前 “显示隐藏文件” 按钮要显示的文字
        showHiddenFile = myIni.getHomeTableFileShowHidden();
        updatePopMenuShowHiddenText(showHiddenFile);


        // 将菜单项添加到弹出菜单
        popupMenu_table_files.add(popupMenu_table_files_open);
        popupMenu_table_files.add(popupMenu_table_files_newFile);
        popupMenu_table_files.add(popupMenu_table_files_newDir);
        popupMenu_table_files.addSeparator();
        popupMenu_table_files.add(popupMenu_table_files_compress);
        popupMenu_table_files.addSeparator();
        popupMenu_table_files.add(popupMenu_table_files_copy);
        popupMenu_table_files.add(popupMenu_table_files_cut);
        popupMenu_table_files.addSeparator();
        popupMenu_table_files.add(popupMenu_table_files_del);
        popupMenu_table_files.add(popupMenu_table_files_rename);
        popupMenu_table_files.add(popupMenu_table_files_showHidden);


        /* 设置菜单栏 */
        frame.setJMenuBar(menuBar);

        /* 添加组件到主面板 */
        panel_borderLayout.add(list_rootDir, BorderLayout.WEST);
        panel_borderLayout.add(scrollPane_jList_files, BorderLayout.CENTER);
        panel_borderLayout.add(panel_toolBar, BorderLayout.NORTH);
        frame.add(panel_borderLayout);

    }

    /**
     * 初始化组建数据
     * @param frame JFrame 引用
     */
    private void initComponentData(JFrame frame) {
        // 根据不同系统获取根目录
        File[] rootDirs;
        if (Tool.getOSName() == OS.OS_WINDOW) {
            rootDirs = File.listRoots();
        } else {
            rootDirs = new File("/").listFiles();
        }
        // 将根目录文件夹（盘符）添加到 list_rootDirData，用作 list_rootDir 列表数据
        assert rootDirs != null;
        for(File rootDir: rootDirs) {
            String dir = Tool.getOSName() == OS.OS_WINDOW ? rootDir.getPath() : "/" + rootDir.getName();
            list_rootDirData.add(dir);
        }

        // 设置 list_rootDirData 列表参数
        list_rootDir.setListData(list_rootDirData.toArray());
    }

    /**
     * 根据主面板 panel_borderLayout 大小设置组件大小
     */
    private void updateComponentSize() {
        Dimension dimension = panel_borderLayout.getSize();
    }


    /**
     * 设置当前路径
     * @param path
     * @param isNotAddList 不将当前路径加入 listPath 集合中（如果已经是前进后退操作，这里设 true）
     */
    private void setCurrentPath(String path, boolean isNotAddList) {
        try {
            File pathFile = new File(path);
            if (!pathFile.exists()) {
                Alert.error("跳转失败，文件夹不存在");
                return;
            }
            if (!pathFile.isDirectory()) {
                Alert.error("跳转失败，当前跳转的不是文件夹");
                return;
            }

            File[] files = pathFile.listFiles();

            // 先清空 table_files_model
            table_files_model.setRowCount(0);
            // 清空 table_files_Data
            table_files_Data = new ArrayList<>();
            // 将当前根目录下的文件添加到 JList_filesData 列表
            for (int i = 0; i < files.length; i++) {
                File file = files[i];

                // 判断是否设置了允许显示隐藏文件
                if (file.isHidden() && !showHiddenFile) {
                    continue;
                }

                String[] rowData = new String[3];
                // 第一列是文件名
                rowData[0] = file.getName();
                // 第二列是文件大小或文件夹子项目数
                if (file.isDirectory()) {
                    // 如果是文件夹就显示文件夹下项目数量
                    File[] fs = file.listFiles();
                    rowData[1] = fs == null ? "0 个项目" : fs.length + " 个项目";
                } else {
                    rowData[1] = Tool.formatSize(file.length());
                }
                // 第三列是修改时间
                rowData[2] = Tool.formatDate(new Date(file.lastModified()));
                // 将行数据添加到 table
                table_files_model.addRow(rowData);
                // 将当前文件添加到
                table_files_Data.add(file);
            }

            // 设置当前目录地址到全局变量
            currentPath = path;
            // 设置工具栏地址栏文本
            textField_toolBar_path.setText(currentPath);

            if (isNotAddList) {
                // 不将当前地址加入 listPath，这里默认是前进后退操作
            } else {
                // 将当前地址加入 list
                listPathAdd(currentPath);
            }

            // 设置前进后退按钮状态
            updatePreAndNextButtonState();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            Alert.error("打开文件夹失败，可能当前文件夹有权限无法访问");
        }
    }


    /**
     * 将地址加入 listPath,以便前进后退操作
     * 不能大于 10
     * 加入后默认将当前索引指向 listPath 最后一个
     * @param path
     */
    private void listPathAdd(String path) {
        if (listPath.size() >= 10) {
            // 如果大于等于 10 就减去首元素
            listPath.remove(0);
            if (listPath_index >= 1) {
                listPath_index--;
            }
        }

        int index = listPath.indexOf(path);
        if (index >= 0) {
            // 有重复的就删除
            listPath.remove(index);
        }

        // 添加元素后将索引指向最后一个
        listPath.add(path);
        listPath_index = listPath.size() - 1;

    }

    /**
     * 根据 listPath 设置前进和后退按钮状态
     * 最大长度不超过 10
     */
    private void updatePreAndNextButtonState() {
        if (listPath.size() > 0) {
            if (listPath_index != 0 && listPath_index != listPath.size() - 1) {
                // listPath_index 位于首尾中间，前进后退按钮均启用
                button_toolBar_pre.setEnabled(true);
                button_toolBar_next.setEnabled(true);
            } else if (listPath_index == 0) {
                button_toolBar_pre.setEnabled(false);
                button_toolBar_next.setEnabled(true);
            } else if (listPath_index == listPath.size() - 1){
                button_toolBar_pre.setEnabled(true);
                button_toolBar_next.setEnabled(false);
            }
        } else {
            // listPath 长度小于等于 0，前进后退按钮均禁止
            button_toolBar_pre.setEnabled(false);
            button_toolBar_next.setEnabled(false);
        }
    }

    /**
     * 向指定 GridBagLayout 添加组件
     * @param container
     * @param component
     * @param gridX
     * @param gridY
     * @param gridWidth
     * @param gridHeight
     * @param anchor
     * @param fill
     */
    private void addComponent(Container container, Component component, int gridX, int gridY, int gridWidth, int gridHeight, double weightX, double weightY, int anchor, int fill ) {
        Insets insets = new Insets(0, 0, 0, 0);
        GridBagConstraints gbc = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, 0, 0);
        container.add(component, gbc);
    }

    /**
     * 设置表格弹出菜单项 “显示隐藏文件” 菜单当前显示文字
     * @param showHidden
     */
    private void updatePopMenuShowHiddenText(boolean showHidden) {
        if (showHidden) {
            popupMenu_table_files_showHidden.setText("不显示隐藏文件");
        } else {
            popupMenu_table_files_showHidden.setText("显示隐藏文件");
        }
    }

    /**
     * 保存数据并退出程序
     */
    private void exitApp() {
        // 在程序关闭时记录窗口大小和位置
        Dimension ds = this.getSize();
        Point pt = this.getLocation();
        myIni.setHomeLocation((int) pt.getX(), (int) pt.getY());
        myIni.setHomeSize((int) ds.getWidth(), (int) ds.getHeight());
        System.exit(0);
    }


    /**
     * 删除文件
     * @param files 文件数组
     */
    private void deleteFiles(File[] files) {
        int errorCount = 0;
        List<File> errorFiles = new ArrayList<>();
        for (File file : files) {
            if (!file.delete()) {
                errorCount++;
                errorFiles.add(file);
            }
        }

        // 删除失败数量 > 0，显示失败的文件
        if (errorCount > 0) {
            StringBuilder msg = new StringBuilder("删除成功：" + (files.length - errorCount) + "，失败：" + errorCount + "\n失败文件：\n");
            errorFiles.forEach(file -> msg.append(file.getPath()));
            Alert.error(msg.toString());
        } else {
            Alert.info("删除成功：" + files.length);
            // 刷新表格数据
            setCurrentPath(currentPath, false);
        }
    }

    /**
     * 重命名文件
     * @param file 文件对象
     * @param newName 新文件名
     */
    private void renameFile(File file, String newName) {
        String newPath = file.getParent() + File.separator + newName;
        File newFile = new File(newPath);
        if (!file.renameTo(newFile)) {
            Alert.error("重命名失败");
        } else {
            // 刷新表格数据
            setCurrentPath(currentPath, false);
        }
    }


    /**
     * 新建文件/文件夹
     * @param name 要生成的文件名（包括路径和名称）
     * @param isDir 新建文件是否是文件夹
     */
    private void createFile(String name, boolean isDir) {
        File file = new File(name);
        if (file.exists()) {
            Alert.error("新建失败，" + (isDir ? "文件夹" : "文件") + "已存在");
        } else {
            try {
                boolean b;
                if (isDir) {
                    b = file.mkdir();
                } else {
                    b = file.createNewFile();
                }
                if (b) {
                    // 新建成功，刷新文件表格
                    setCurrentPath(currentPath, false);
                } else {
                    Alert.error("新建" + (isDir ? "文件夹" : "文件") + "失败");
                }
            } catch (Exception e) {
                Alert.error("新建失败，" + e.getMessage());
            }
        }
    }


    /**
     * 复制或剪切
     * @param files 文件对象数组
     * @param isCut 是否是剪切
     */
    public void copyOrCut(File[] files, boolean isCut) {
        if (files.length == 0) {
            return;
        }
        String title = isCut ? "剪切到" : "复制到";
        // 设置默认目录为当前文件的目录
        JFileChooser jfc = new JFileChooser(files[0].getParent());
        // 设置目录选择窗口标题
        jfc.setDialogTitle(title);
        // 设置仅选择目录
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // 显示目录选择器
        int option = jfc.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            // 获取刚才选择的目录
            File newFile = jfc.getSelectedFile();

            // 错误数量
            int errorCount = 0;
            List<File> errorFiles = new ArrayList<>();
            for (File file : files) {
                if (isCut) {
                    // 剪切
                    if (!file.renameTo(new File(newFile.getPath() + File.separator + file.getName()))) {
                        errorCount++;
                        errorFiles.add(file);
                    }
                } else {
                    // 复制
                    try {
                        File copy = new File(newFile.getPath() + File.separator + file.getName());
                        Files.copy(file.toPath(), copy.toPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorCount++;
                        errorFiles.add(file);
                    }
                }
            }

            // 操作失败数量 > 0，显示失败的文件
            if (errorCount > 0) {
                StringBuilder msg = new StringBuilder((isCut ? "移动" : "复制") + "成功：" + (files.length - errorCount) +
                        "，失败：" + errorCount + "\n失败文件：\n");
                errorFiles.forEach(file -> msg.append(file.getPath()));
                Alert.error(msg.toString());
            } else {
                Alert.info((isCut ? "移动" : "复制") + "成功：" + files.length);
                // 刷新表格数据
                setCurrentPath(currentPath, false);
            }
        }
    }

    /**
     * 打开文件/文件夹
     * 如果是文件夹就跳转，是文件就打开
     * @param file
     */
    private void openFile(File file) {
        if (file.isDirectory()) {
            setCurrentPath(file.getPath(), false);
        } else {
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            } catch (Exception e) {
                Alert.error("打开文件失败，" + e.getMessage());
            }
        }
    }

    /**
     * 按钮点击事件
     * @param actionEvent the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source == menu_file_exit) {
            /* 菜单 文件-退出程序 点击事件 */
            exitApp();

        } else if (source == button_toolBar_go) {
            /* 工具栏转到按钮点击事件，跳转指定目录 */
            setCurrentPath(textField_toolBar_path.getText(), false);

        } else if (source == popupMenu_table_files_showHidden) {
            /* 文件表格右键菜单项-显示隐藏文件点击事件 */
            showHiddenFile = !showHiddenFile;
            myIni.setHomeTableFileShowHidden(showHiddenFile);
            updatePopMenuShowHiddenText(showHiddenFile);
            // 设置后刷新文件表格数据
            setCurrentPath(currentPath, false);

        } else if (source == popupMenu_table_files_del) {
            /* 文件表格右键菜单项-删除文件点击事件 */
            // 获取表格中选中的行
            int[] selectedRows = table_files.getSelectedRows();
            if (Alert.confirm("确定要删除选中的 " + selectedRows.length + " 个文件吗，此操作不可逆")) {
                // 将行中选择的索引转为 File 列表
                File[] files = new File[selectedRows.length];
                for (int i = 0; i < selectedRows.length; i++) {
                    files[i] = table_files_Data.get(selectedRows[i]);
                }
                // 删除文件
                deleteFiles(files);
            }

        } else if (source == popupMenu_table_files_rename) {
            /* 文件表格右键菜单项-重命名点击事件 */
            int selectedRow = table_files.getSelectedRow();
            File file = table_files_Data.get(selectedRow);
            String name = Alert.inputWithValue("输入新文件名：", file.getName());
            if (name == null || name.length() == 0) {
                return;
            }
            renameFile(file, name);

        } else if (source == popupMenu_table_files_newFile || source == popupMenu_table_files_newDir) {
            /* 文件表格右键菜单项-新建文件/新建文件夹点击事件 */
            String name = Alert.input("输入新建文件" + (source == popupMenu_table_files_newDir ? "夹" : "") + "名：");
            if (name == null || name.length() == 0) {
                return;
            }
            createFile(currentPath + File.separator + name, source == popupMenu_table_files_newDir);

        } else if (source == popupMenu_table_files_copy || source == popupMenu_table_files_cut) {
            /* 文件表格右键菜单项-复制到/剪切到点击事件 */
            // 将行中选择的索引转为 File 列表
            int[] selectedRows = table_files.getSelectedRows();
            File[] files = new File[selectedRows.length];
            for (int i = 0; i < selectedRows.length; i++) {
                files[i] = table_files_Data.get(selectedRows[i]);
            }
            // 复制/剪切文件
            copyOrCut(files, source != popupMenu_table_files_copy);

        } else if (source == popupMenu_table_files_open) {
            /* 文件表格右键菜单项-打开点击事件 */
            int selectedRow = table_files.getSelectedRow();
            File file = table_files_Data.get(selectedRow);
            openFile(file);

        } else if (source == button_toolBar_pre) {
            /* 工具栏-后退按钮点击事件 */
            setCurrentPath(listPath.get(--listPath_index), true);

        } else if (source == button_toolBar_next) {
            /* 工具栏-前进按钮点击事件 */
            setCurrentPath(listPath.get(++listPath_index), true);

        } else if (source == popupMenu_table_files_compress) {
            /* 工具栏-压缩按钮点击事件 */
            try {
                int selectedRow = table_files.getSelectedRow();
                File file = table_files_Data.get(selectedRow);
                String name = Alert.input("请输入压缩包文件名：");
                if (name == null || name.length() == 0) {
                    return;
                }
                Tool.compressFileByZIP(file.getPath(), file.getParent() + File.separator + name);
                Alert.info("压缩成功");
                // 刷新表格
                setCurrentPath(currentPath, false);
            } catch (Exception e) {
                e.printStackTrace();
                Alert.info("压缩失败，" + e.getMessage());
            }
        }
    }


    /**
     * 窗口打开事件
     * @param e the event to be processed
     */
    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * 窗口关闭按钮点击事件
     * @param windowEvent the event to be processed
     */
    @Override
    public void windowClosing(WindowEvent windowEvent) {
        exitApp();
    }

    /**
     * 窗口完全关闭事件
     * @param e the event to be processed
     */
    @Override
    public void windowClosed(WindowEvent e) {

    }

    /**
     * 窗口最小化事件
     * @param e the event to be processed
     */
    @Override
    public void windowIconified(WindowEvent e) {

    }

    /**
     * 窗口最小化还原事件
     * @param e the event to be processed
     */
    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    /**
     * 窗口完全激活事件
     * @param e the event to be processed
     */
    @Override
    public void windowActivated(WindowEvent e) {

    }

    /**
     * 窗口失去活性事件
     * @param e the event to be processed
     */
    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    /**
     * 组件大小改变事件
     * @param e the event to be processed
     */
    @Override
    public void componentResized(ComponentEvent e) {
        if (e.getSource() == panel_borderLayout) {
            // 主面板大小改变事件
            // 根据主面板大小更新组件大小
            updateComponentSize();
        }
    }

    /**
     * 组件移动事件
     * @param e the event to be processed
     */
    @Override
    public void componentMoved(ComponentEvent e) {

    }

    /**
     * 组件显示事件
     * @param e the event to be processed
     */
    @Override
    public void componentShown(ComponentEvent e) {

    }

    /**
     * 组件隐藏事件
     * @param e the event to be processed
     */
    @Override
    public void componentHidden(ComponentEvent e) {

    }

    /**
     * List Item 选择事件
     * @param listSelectionEvent the event that characterizes the change.
     */
    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (listSelectionEvent.getSource() == list_rootDir) {
            // 根目录 List Item 选择事件
            int index = list_rootDir.getSelectedIndex();
            setCurrentPath(list_rootDirData.get(index), false);
        }
    }

    /**
     * 键入某个键事件
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * 按下某个键事件
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == textField_toolBar_path) {
            // 地址框按下某个键事件
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                // 地址框按下回车键，跳转目录
                setCurrentPath(textField_toolBar_path.getText(), false);
            }
        }
    }

    /**
     * 松开某个键事件
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * 鼠标点击事件
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == table_files) {
            /* table_files 文件表格点击事件 */
            if (e.getButton() == MouseEvent.BUTTON3) {
                /* 鼠标右键点击事件 */

                // 先判断表格是否已经选中多行，如果已经选择多行就直接弹出菜单，否则选中右键的行
                int[] selectedRows = table_files.getSelectedRows();
                if (selectedRows.length <= 1) {
                    // 当前没有选中行或只选择了一行，这里把当前选中行修改为右键的行
                    // 根据鼠标位置获取右键行索引
                    int focusedRowIndex = table_files.rowAtPoint(e.getPoint());
                    if (focusedRowIndex < 0) {
                        return;
                    }
                    // 设置表格当前选中项是选中右键的行
                    table_files.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                }
                // 弹出菜单
                popupMenu_table_files.show(table_files, e.getX(), e.getY());
            } else if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                /* table_files 文件表格左键双击事件 */
                // 打开文件/文件夹
                int selectedRow = table_files.getSelectedRow();
                File file = table_files_Data.get(selectedRow);
                openFile(file);
            }
        }
    }

    /**
     * 鼠标按下事件
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * 鼠标松开事件
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * 鼠标进入事件
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * 鼠标离开事件
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }
}
