package cc.loac.myenum;

public enum Theme {
    FlatLafLight(0),
    FlatLafDark(1),
    FlatLafIntelliJ(2),
    FlatLafDarcula(3),
    FlatLafMacOSLight(4),
    FlatLafMacOSDark(5),
    NONE(-1);

    private int index;

    Theme(int i) {
        this.index = i;
    }

    /**
     * 设置主题
     * @param theme Theme
     */
    public void setTheme(Theme theme) {
        this.index = theme.index;
    }

    /**
     * 获取当前 Theme 索引值
     * @return int
     */
    public int getTheme() {
        return index;
    }

    /**
     * 根据 index 索引获取 Theme
     * @param index 索引
     * @return Theme
     */
    public static Theme getTheme(int index) {
        switch (index) {
            case 0 -> {
                return Theme.FlatLafLight;
            }
            case 1 -> {
                return Theme.FlatLafDark;
            }
            case 2 -> {
                return Theme.FlatLafIntelliJ;
            }
            case 3 -> {
                return Theme.FlatLafDarcula;
            }
            case 4 -> {
                return Theme.FlatLafMacOSLight;
            }
            case 5 -> {
                return Theme.FlatLafMacOSDark;
            }
        }
        return Theme.NONE;
    }
}
