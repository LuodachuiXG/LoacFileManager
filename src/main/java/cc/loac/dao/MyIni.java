package cc.loac.dao;

import cc.loac.Main;
import cc.loac.myenum.Theme;
import org.ini4j.Wini;

import java.awt.*;
import java.io.File;

/**
 * Wini 操作
 */
public class MyIni {
    // Ini 配资文件操作类
    private static Wini _wini = null;

    // 对象实例
    public static MyIni _myIni = null;

    private static final String SECTION_HOME = "HOME";
    private static final String OPTION_LOCATION_X = "location_x";
    private static final String OPTION_LOCATION_Y = "location_y";
    private static final String OPTION_SIZE_WIDTH = "size_width";
    private static final String OPTION_SIZE_HEIGHT = "size_height";
    private static final String OPTION_TABLE_FILE_SHOW_HIDDEN = "table_file_show_hidden";

    private static final String SECTION_THEME = "THEME";
    private static final String OPTION_THEME_CURRENT = "current";

    /**
     * 获取实例
     * @return MyIni
     */
    public static MyIni getInstance() {
        try {
            if (_myIni == null) {
                // 初始化 wini
                _wini = new Wini(new File(Main.dataIniPath));
                _myIni = new MyIni();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _myIni;
    }

    /**
     * 获取 Home 窗口位置
     * @return Int
     */
    public Point getHomeLocation() {
        Point point = new Point();
        try {
            int x = Integer.parseInt(_wini.get(SECTION_HOME, OPTION_LOCATION_X));
            int y = Integer.parseInt(_wini.get(SECTION_HOME, OPTION_LOCATION_Y));
            point.setLocation(x, y);
        } catch (Exception e) {
            return null;
        }
        return point;
    }

    /**
     * 设置 Home 窗口位置
     * @param x 水平位置
     * @param y 垂直位置
     */
    public void setHomeLocation(int x, int y) {
        try {
            _wini.put(SECTION_HOME, OPTION_LOCATION_X, x);
            _wini.put(SECTION_HOME, OPTION_LOCATION_Y, y);
            _wini.store();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 Home 窗口大小
     * @return Dimension
     */
    public Dimension getHomeSize() {
        Dimension dimension = new Dimension();
        try {
            int width = Integer.parseInt(_wini.get(SECTION_HOME, OPTION_SIZE_WIDTH));
            int height = Integer.parseInt(_wini.get(SECTION_HOME, OPTION_SIZE_HEIGHT));
            dimension.setSize(width, height);
        } catch (Exception e) {
            return null;
        }
        return dimension;
    }

    /**
     * 设置 Home 窗口大小
     * @param width 宽度
     * @param height 高度
     */
    public void setHomeSize(int width, int height) {
        try {
            _wini.put(SECTION_HOME, OPTION_SIZE_WIDTH, width);
            _wini.put(SECTION_HOME, OPTION_SIZE_HEIGHT, height);
            _wini.store();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取 Home 窗口显示文件的列表是否允许显示隐藏文件
     */
    public boolean getHomeTableFileShowHidden() {
        Object obj = _wini.get(SECTION_HOME, OPTION_TABLE_FILE_SHOW_HIDDEN);
        String str = (obj == null ? "false" : String.valueOf(obj));
        return Boolean.parseBoolean(str);
    }

    /**
     * 设置 Home 窗口显示文件的列表是否允许显示隐藏文件
     * @param showHidden 是否允许显示隐藏文件
     */
    public void setHomeTableFileShowHidden(boolean showHidden) {
        try {
            _wini.put(SECTION_HOME, OPTION_TABLE_FILE_SHOW_HIDDEN, showHidden);
            _wini.store();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置当前主题
     * @param theme 主题
     */
    public void setCurrentTheme(Theme theme) {
        try {
            _wini.put(SECTION_THEME, OPTION_THEME_CURRENT, theme.getTheme());
            _wini.store();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前主题
     * @return Theme
     */
    public Theme getCurrentTheme() {
        String obj = _wini.get(SECTION_THEME, OPTION_THEME_CURRENT);
        obj = obj == null ? "-1" : obj;
        return Theme.getTheme(Integer.parseInt(obj));
    }
}
