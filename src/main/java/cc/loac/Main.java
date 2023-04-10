package cc.loac;

import cc.loac.common.Tool;
import cc.loac.dao.MyIni;
import cc.loac.frame.Home;
import cc.loac.myenum.OS;
import cc.loac.myenum.Theme;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.io.File;


public class Main {
    // 记录当前系统
    public static final OS os  = Tool.getOSName();

    // 记录程序在当前系统存储数据目录
    public static String dataDirPath =
            os == OS.OS_WINDOW ?
                    System.getProperty("user.home") + "\\AppData\\Local\\LoacFileManager" :
                    System.getProperty("user.home") + "/.local/share/LoacFileManager";

    // 记录程序 data.ini 文件位置
    public static String dataIniPath = dataDirPath + (os == OS.OS_WINDOW ? "\\data.ini" : "/data.ini");


    private final static MyIni myIni = MyIni.getInstance();

    public static void main(String[] args) {
        // 设置主题
        setTheme();

        init();
        new Home();
    }

    /**
     * 设置主题
     */
    public static void setTheme() {
        Theme theme = myIni.getCurrentTheme();
        switch (theme) {
            case FlatLafDark -> FlatDarkLaf.setup();
            case FlatLafIntelliJ -> FlatIntelliJLaf.setup();
            case FlatLafDarcula -> FlatDarculaLaf.setup();
            case FlatLafMacOSLight -> FlatMacLightLaf.setup();
            case FlatLafMacOSDark -> FlatMacDarkLaf.setup();
            default -> FlatLightLaf.setup();
        }
    }

    /**
     * 初始化
     */
    private static void init() {
        try {
            // 初始化数据目录和 data.ini
            File dataDir = new File(dataDirPath);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            File dataIni = new File(dataIniPath);
            if (!dataIni.exists()) {
                dataIni.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}