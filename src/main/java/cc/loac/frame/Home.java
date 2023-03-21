package cc.loac.frame;

import cc.loac.common.Tool;
import cc.loac.dao.MyIni;
import cc.loac.myenum.OS;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Home extends JFrame implements ActionListener, WindowListener, ComponentListener, ListSelectionListener {
    private final MyIni myIni = MyIni.getInstance();
    // 记录窗口默认位置
    private Point point = myIni.getHomeLocation();

    // 记录窗口默认大小
    private Dimension dimension = myIni.getHomeSize();

    // 菜单栏和菜单
    private JMenuBar menuBar;
    private JMenu menu_file;
    private JMenuItem menu_file_exit;

    private JPanel panel_borderLayout;

    // ToolBar 面板及组件
    private JPanel panel_toolBar;
    private JButton button_toolBar_pre;
    private JButton button_toolBar_next;
    private JTextField textField_toolBar_path;
    private JButton button_toolBar_go;

    // jList_rootDir 列表
    private JList jList_rootDir;
    private List<String> jList_rootDirData = new ArrayList<>();

    // jList_files 列表
    private JList jList_files;
    private JScrollPane scrollPane_jList_files;
    private List<String> jList_filesData = new ArrayList<>();


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
        // 设置菜单栏和菜单
        menuBar = new JMenuBar();
        menu_file = new JMenu("文件");
        menu_file_exit = new JMenuItem("退出程序");
        menu_file_exit.addActionListener(this);
        menu_file.add(menu_file_exit);
        menuBar.add(menu_file);


        // 设置主面板 BorderLayout
        panel_borderLayout = new JPanel(new BorderLayout());
        // 设置主面板组件事件
        panel_borderLayout.addComponentListener(this);

        // 设置 ToolBar 面板
        panel_toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        button_toolBar_pre = new JButton("<");
        button_toolBar_next = new JButton(">");
        textField_toolBar_path = new JTextField();
        button_toolBar_go = new JButton(">");
        // 按钮默认禁用
        button_toolBar_pre.setEnabled(false);
        button_toolBar_next.setEnabled(false);



        panel_toolBar.add(button_toolBar_pre);
        panel_toolBar.add(button_toolBar_next);
        panel_toolBar.add(textField_toolBar_path);
        panel_toolBar.add(button_toolBar_go);


        // 初始化 BorderLayout 左侧用于显示系统根目录（盘符）的 List
        jList_rootDir = new JList();
        jList_rootDir.addListSelectionListener(this);

        // 初始化 Center 用于展示文件夹的 List
        jList_files = new JList();
//        scrollPane_jList_files = new JScrollPane();
//        scrollPane_jList_files.add(jList_files);


        // 设置菜单栏
        frame.setJMenuBar(menuBar);
        // 添加组件到主面板
        panel_borderLayout.add(jList_rootDir, BorderLayout.WEST);
        panel_borderLayout.add(jList_files, BorderLayout.CENTER);
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
        // 将根目录文件夹（盘符）添加到 jList_rootDirData，用作 jList_rootDir 列表数据
        assert rootDirs != null;
        for(File rootDir: rootDirs) {
            String dir = Tool.getOSName() == OS.OS_WINDOW ? rootDir.getPath() : "/" + rootDir.getName();
            jList_rootDirData.add(dir);
        }

        // 设置 jList_rootDirData 列表参数
        jList_rootDir.setListData(jList_rootDirData.toArray());
    }

    /**
     * 根据主面板 panel_borderLayout 大小设置组件大小
     */
    private void updateComponentSize() {
        Dimension dimension = panel_borderLayout.getSize();

        // 设置 jList_rootDir 列表宽度
//        jList_rootDir.setSize(new Dimension((int) (dimension.getWidth() / 4), (int) dimension.getHeight()));

//        jList_files.setPreferredSize(new Dimension(scrollPane_jList_files.getWidth(), scrollPane_jList_files.getHeight()));


        // 设置 textField_toolBar_path 宽度
        textField_toolBar_path.setPreferredSize(
                new Dimension((int) (panel_toolBar.getWidth() * 0.9), button_toolBar_go.getHeight())
        );
    }


    /**
     * 设置当前 Path
     * @param path
     */
    private void setCurrentPath(String path) {
        currentPath = path;
        textField_toolBar_path.setText(currentPath);
    }


    /**
     * 按钮点击事件
     * @param actionEvent the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == menu_file_exit) {
            // 菜单 文件-退出程序 点击事件
            System.exit(0);
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
        if (listSelectionEvent.getSource() == jList_rootDir) {
            // 根目录 List Item 选择事件
            int index = jList_rootDir.getSelectedIndex();
            String path = jList_rootDirData.get(index);
            try {
                File[] files = new File(path).listFiles();
                jList_filesData.clear();
                // 将当前根目录下的文件添加到 JList_filesData 列表
                for (File file : files) {
                    jList_filesData.add(file.getName());
                }
                jList_files.setListData(jList_filesData.toArray());
                setCurrentPath(path);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "打开文件夹失败，可能文件夹有权限无法访问", "访问失败", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
