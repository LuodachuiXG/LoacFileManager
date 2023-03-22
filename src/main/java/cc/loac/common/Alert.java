package cc.loac.common;

import javax.swing.*;

public class Alert {
    // 信息框默认标题
    private static final String TITLE = "温馨提示";

    public static void error(String errMsg) {
        error(errMsg, TITLE);
    }

    public static void error(String errMsg, String title) {
        JOptionPane.showMessageDialog(null, errMsg, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void info(String msg) {
        info(msg, TITLE);
    }

    public static void info(String msg, String title) {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String msg) {
        return confirm(msg, TITLE);
    }


    public static boolean confirm(String msg, String title) {
        int result = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }

    public static String inputWithValue(String msg, String value) {
        return input(msg, TITLE, value);
    }

    public static String input(String msg) {
        return input(msg, TITLE, "");
    }

    public static String input(String msg, String title, String value) {
        return (String) JOptionPane.showInputDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE, null, null, value);
    }

}
