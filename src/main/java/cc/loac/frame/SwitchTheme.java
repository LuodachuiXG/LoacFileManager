package cc.loac.frame;


import cc.loac.common.Alert;
import cc.loac.dao.MyIni;
import cc.loac.myenum.Theme;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * 切换主题 Frame
 */
public class SwitchTheme extends JFrame implements ListSelectionListener {
    
    private JList<String> list_themes;
    private final MyIni myIni = MyIni.getInstance();

    private final String[] themes = {"FlatLaf Light", "FlatLaf Dark", "FlatLaf IntelliJ", "FlatLaf Darcula",
            "FlatLaf macOS Light", "FlatLaf macOS Dark"};

    public SwitchTheme() {
        super("切换主题");

        initComponent();

        pack();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (dimension.getWidth() / 2 - getWidth() / 2), (int) (dimension.getHeight() / 2 - getHeight() / 2));
        setResizable(false);
        setAlwaysOnTop(true);
        setVisible(true);
    }

    /**
     * 初始化组件
     */
    private void initComponent() {
        list_themes = new JList<String>(themes);
        list_themes.addListSelectionListener(this);
        add(list_themes);
    }

    /**
     * list_theme 列表选择事件
     * @param e the event that characterizes the change.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == list_themes) {
            int index = list_themes.getSelectedIndex();
            try {
                switch (index) {
                    case 0 -> myIni.setCurrentTheme(Theme.FlatLafLight);
                    case 1 -> myIni.setCurrentTheme(Theme.FlatLafDark);
                    case 2 -> myIni.setCurrentTheme(Theme.FlatLafIntelliJ);
                    case 3 -> myIni.setCurrentTheme(Theme.FlatLafDarcula);
                    case 4 -> myIni.setCurrentTheme(Theme.FlatLafMacOSLight);
                    case 5 -> myIni.setCurrentTheme(Theme.FlatLafMacOSDark);
                }
                Alert.info("切换主题后需要重启");
                System.exit(0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
