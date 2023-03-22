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
import java.util.ArrayList;
import java.util.Date;
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
    private String[] table_files_column = {"名称", "大小", "修改时间"};
    private DefaultTableModel table_files_model;
    private JScrollPane scrollPane_jList_files;
    private JPopupMenu popupMenu_table_files;
    private JMenuItem popupMenu_table_files_newFile;
    private JMenuItem popupMenu_table_files_newDir;
    private JMenuItem popupMenu_table_files_del;
    private JMenuItem popupMenu_table_files_showHidden;
    private JMenuItem popupMenu_table_files_copy;
    private JMenuItem popupMenu_table_files_cut;
    private JMenuItem popupMenu_table_files_paste;
    private JMenuItem popupMenu_table_files_rename;

    // 记录当前文件位置
    private String currentPath;

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
        popupMenu_table_files_newFile = new JMenuItem("新建文件");
        popupMenu_table_files_newDir = new JMenuItem("新建文件夹");
        popupMenu_table_files_copy = new JMenuItem("复制");
        popupMenu_table_files_cut = new JMenuItem("剪切");
        popupMenu_table_files_paste = new JMenuItem("粘贴");
        popupMenu_table_files_del = new JMenuItem("删除文件");
        popupMenu_table_files_rename = new JMenuItem("重命名");
        popupMenu_table_files_showHidden = new JMenuItem("显示隐藏文件");

        // 添加菜单项点击事件
        popupMenu_table_files_newFile.addActionListener(this);
        popupMenu_table_files_newDir.addActionListener(this);
        popupMenu_table_files_copy.addActionListener(this);
        popupMenu_table_files_cut.addActionListener(this);
        popupMenu_table_files_paste.addActionListener(this);
        popupMenu_table_files_del.addActionListener(this);
        popupMenu_table_files_rename.addActionListener(this);
        popupMenu_table_files_showHidden.addActionListener(this);

        // 根据配置文件获取当前是让 “显示隐藏文件” 菜单处于选中状态
        popupMenu_table_files_showHidden.setSelected(myIni.getHomeTableFileShowHidden());

        // 将菜单项添加到弹出菜单
        popupMenu_table_files.add(popupMenu_table_files_newFile);
        popupMenu_table_files.add(popupMenu_table_files_newDir);
        popupMenu_table_files.addSeparator();
        popupMenu_table_files.add(popupMenu_table_files_copy);
        popupMenu_table_files.add(popupMenu_table_files_cut);
        popupMenu_table_files.add(popupMenu_table_files_paste);
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
     */
    private void setCurrentPath(String path) {
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
            // 将当前根目录下的文件添加到 JList_filesData 列表
            for (int i = 0; i < files.length; i++) {
                File file = files[i];

                // 判断是否设置了允许显示隐藏文件
                boolean showHidden = myIni.getHomeTableFileShowHidden();
                if (file.isHidden() && !showHidden) {
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
                table_files_model.addRow(rowData);
            }

            currentPath = path;
            textField_toolBar_path.setText(currentPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Alert.error("打开文件夹失败，可能当前文件夹有权限无法访问");
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
     * 按钮点击事件
     * @param actionEvent the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source == menu_file_exit) {
            // 菜单 文件-退出程序 点击事件
            System.exit(0);
        } else if (source == button_toolBar_go) {
            // 工具栏转到按钮点击事件，跳转指定目录
            setCurrentPath(textField_toolBar_path.getText());
            try {

            } catch (Exception e) {
                throw new RuntimeException(e);
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
        // 在窗口关闭时记录窗口大小和位置
        Window window = windowEvent.getWindow();
        myIni.setHomeLocation(window.getX(), window.getY());
        myIni.setHomeSize(window.getWidth(), window.getHeight());
        System.exit(0);
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
            setCurrentPath(list_rootDirData.get(index));
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
                setCurrentPath(textField_toolBar_path.getText());
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
