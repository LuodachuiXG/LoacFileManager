package cc.loac.frame;

import cc.loac.common.Alert;
import cc.loac.common.Tool;
import cc.loac.component.MFileManager;
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

public class Home extends JFrame implements ActionListener, WindowListener {
    private final MyIni myIni = MyIni.getInstance();
    // 记录窗口默认位置
    private Point point = myIni.getHomeLocation();

    // 记录窗口默认大小
    private Dimension dimension = myIni.getHomeSize();

    /* 菜单栏和菜单 */
    private JMenuBar menuBar;
    private JMenu menu_file;
    private JMenuItem menu_file_exit;

    /* 文件管理器组件 */
    private MFileManager mFileManager;

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

        /* 设置菜单栏 */
        frame.setJMenuBar(menuBar);

        /* 添加 MFileManager 组件到面板 */
        mFileManager = new MFileManager();
        frame.add(mFileManager);
    }

    /**
     * 初始化组建数据
     * @param frame JFrame 引用
     */
    private void initComponentData(JFrame frame) {

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
     * 按钮点击事件
     * @param actionEvent the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source == menu_file_exit) {
            /* 菜单 文件-退出程序 点击事件 */
            exitApp();
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
}
