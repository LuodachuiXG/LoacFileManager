package cc.loac.impl;

/**
 * 回调接口
 */
public interface IMFileManager {
    /**
     * 在 MFileManager 中调用在 Home 中回调刷新所有选项卡的 MFileManager 中数据
     */
    void onRefresh();

    /**
     * 在 MFileManager 中调用在 Home 中添加选项卡
     */
    void onAddTab();

    /**
     * 在 MFileManager 中调用在 Home 中删除指定选项卡
     * @param tabIndex 删除的选项卡索引
     */
    void onDelTab(int tabIndex);

    /**
     * 在 MFileManager 中调用在 Home 更新指定选项卡的名称
     * @param name 要设置的新的 Tab 名
     * @param tabIndex 选项卡索引
     */
    void onTabRename(String name, int tabIndex);
}
