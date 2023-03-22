package cc.loac.common;

import javax.swing.*;

public class Alert {
    public static void error(String errMsg) {
        JOptionPane.showMessageDialog(null, errMsg, "温馨提示", JOptionPane.ERROR_MESSAGE);
    }

    public static void error(String errMsg, String title) {
        JOptionPane.showMessageDialog(null, errMsg, title, JOptionPane.ERROR_MESSAGE);
    }
}
