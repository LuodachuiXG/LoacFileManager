package cc.loac.common;

import javax.swing.*;

public class Alert {
    /**
     * 信息框默认标题
     */
    private static final String TITLE = "温馨提示";

    /**
     * 错误消息框
     * @param errMsg 错误消息
     */
    public static void error(String errMsg) {
        error(errMsg, TITLE);
    }

    /**
     * 错误消息框
     * @param errMsg 错误消息
     * @param title 消息框标题
     */
    public static void error(String errMsg, String title) {
        JOptionPane.showMessageDialog(null, errMsg, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 信息消息框
     * @param msg 消息
     */
    public static void info(String msg) {
        info(msg, TITLE);
    }

    /**
     * 信息消息框
     * @param msg 信息
     * @param title 信息框标题
     */
    public static void info(String msg, String title) {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 确认消息框
     * @param msg 消息
     * @return 确认结果
     */
    public static boolean confirm(String msg) {
        return confirm(msg, TITLE);
    }


    /**
     * 确认消息框
     * @param msg 消息
     * @param title 消息框标题
     * @return 确认结果
     */
    public static boolean confirm(String msg, String title) {
        int result = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * 带有默认值的输入消息框
     * @param msg 消息
     * @param value 默认值
     * @return 输入消息
     */
    public static String inputWithValue(String msg, String value) {
        return input(msg, TITLE, value);
    }

    /**
     * 输入消息框
     * @param msg 消息
     * @return 输入消息
     */
    public static String input(String msg) {
        return input(msg, TITLE, "");
    }

    /**
     * 带有默认值的输入消息框
     * @param msg 消息
     * @param title 消息框标题
     * @param value 默认值
     * @return 输入消息
     */
    public static String input(String msg, String title, String value) {
        return (String) JOptionPane.showInputDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE, null, null, value);
    }

}
