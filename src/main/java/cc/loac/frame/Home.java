package cc.loac.frame;

import cc.loac.common.Tool;
import cc.loac.dao.MyIni;
import cc.loac.myenum.OS;
import javafx.scene.text.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Home extends JFrame implements ActionListener, WindowListener, ComponentListener {
    private final MyIni myIni = MyIni.getInstance();
    // 记录窗口默认位置
    private Point point = myIni.getHomeLocation();
    // 记录窗口默认大小
    private Dimension dimension = myIni.getHomeSize();

    private JPanel panel_borderLayout;
    private JList jList_rootDir;
    // jList_rootDir 的列表数据
    private List<String> jList_rootDirData = new ArrayList<>();

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
        panel_borderLayout = new JPanel(new BorderLayout());
        panel_borderLayout.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Component component = e.getComponent();
                jList_rootDir.setPreferredSize(new Dimension(component.getWidth() / 4, 0));
            }
        });

        jList_rootDir = new JList();
        jList_rootDir.setMinimumSize(new Dimension(200, 0));


        panel_borderLayout.add(jList_rootDir, BorderLayout.WEST);
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
        assert rootDirs != null;
        for(File rootDir: rootDirs) {
            String dir = Tool.getOSName() == OS.OS_WINDOW ? rootDir.getPath() : "/" + rootDir.getName();
            jList_rootDirData.add(dir);
        }

        // 设置 jList_rootDirData 列表参数
        jList_rootDir.setListData(jList_rootDirData.toArray());
    }


    /**
     * 按钮点击事件
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

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
}
