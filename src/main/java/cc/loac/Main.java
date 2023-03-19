package cc.loac;

import cc.loac.common.Tool;
import cc.loac.frame.Home;
import cc.loac.myenum.OS;
import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import org.ini4j.Wini;

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

    public static void main(String[] args) {
        // 设置主题
        FlatArcIJTheme.setup();
        init();
        new Home();
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