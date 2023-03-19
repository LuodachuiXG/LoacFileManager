package cc.loac.common;


import cc.loac.myenum.OS;

public class Tool {
    // 获取 OS.NAME
    private static final String OS_NAME = System.getProperty("os.name").toUpperCase();

    // 获取 OS.NAME
    public static OS getOSName() {
        if (OS_NAME.contains("WINDOWS")) {
            return OS.OS_WINDOW;
        } else if (OS_NAME.contains("LINUX")) {
            return OS.OS_LINUX;
        } else {
            return OS.OS_MACOS;
        }
    }
}
