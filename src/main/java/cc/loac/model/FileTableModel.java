package cc.loac.model;

import cc.loac.entity.FileTableItem;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileTableModel extends AbstractTableModel {

    // 存储文件表格项
    private List<FileTableItem> items;
    // 文件表格列名
    private static final String[] columns = {
            "图标", "名称", "大小", "修改时间"
    };

    /**
     * 默认构造函数
     */
    public FileTableModel() {
        items = new ArrayList<>();
    }

    /**
     * 带参构造函数
     * @param files 文件表格 File 数组
     */
    public FileTableModel(File[] files) {
        items = new ArrayList<>();
        for (File file : files) {
            FileTableItem fileTableItem = new FileTableItem();
            fileTableItem.setFile(file);
            items.add(fileTableItem);
        }
    }

    /**
     * 带参构造函数
     * @param list 文件表格 list 集合
     */
    public FileTableModel(List<FileTableItem> list) {
        items = list;
    }

    /**
     * 根据索引获取 FileTableItem
     * @param index 索引
     * @return FileTableItem
     */
    public FileTableItem getFileTableItem(int index) {
        return items.get(index);
    }

    /**
     * 根据文件名获取 FileTableItem
     * @param name 文件名
     * @return FileTableItem
     */
    public FileTableItem getFileTableItem(String name) {
        for (FileTableItem fileTableItem : items) {
            File file = fileTableItem.getFile();
            if (file.getName().equals(name)) {
                return fileTableItem;
            }
        }
        return null;
    }

    /**
     * 根据索引获取文件表格中 File 对象
     * @param index 索引
     * @return File
     */
    public File getFile(int index) {
        return items.get(index).getFile();
    }

    /**
     * 根据文件名获取文件表格中 File 对象
     * @param name 文件民
     * @return File
     */
    public File getFile(String name) {
        for (FileTableItem fileTableItem : items) {
            File file = fileTableItem.getFile();
            if (file.getName().equals(name)) {
                return file;
            }
        }
        return null;
    }

    /**
     * 设置文件表格中 File
     * @param file File
     * @param index 索引
     */
    public void setFile(File file, int index) {
        FileTableItem fileTableItem = new FileTableItem();
        fileTableItem.setFile(file);
        items.set(index, fileTableItem);
    }


    /**
     * 获取文件表格所有项
     * @return List<File>
     */
    public List<File> getFiles() {
        List<File> files = new ArrayList<>();
        for (FileTableItem fileTableItem : items) {
            files.add(fileTableItem.getFile());
        }
        return files;
    }

    /**
     * 设置文件表格所有项
     * @param files File 集合
     */
    public void setFiles(List<File> files) {
        items = new ArrayList<>();
        for (File file : files) {
            FileTableItem fileTableItem = new FileTableItem();
            fileTableItem.setFile(file);
            items.add(fileTableItem);
        }
        fireTableDataChanged();
    }

    /**
     * 设置文件表格所有项
     * @param files File 列表
     */
    public void setFiles(File[] files) {
        items = new ArrayList<>();
        for (File file : files) {
            FileTableItem fileTableItem = new FileTableItem();
            fileTableItem.setFile(file);
            items.add(fileTableItem);
        }
        fireTableDataChanged();
    }


    /**
     * 获取数据行数
     * @return int
     */
    @Override
    public int getRowCount() {
        return items.size();
    }

    /**
     * 获取列行
     * @return int
     */
    @Override
    public int getColumnCount() {
        return columns.length;
    }

    /**
     * 根据行列获取数据
     * @param rowIndex 行索引
     * @param columnIndex 列索引
     * @return Object
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        FileTableItem fileTableItem = items.get(rowIndex);
        switch (columnIndex) {
            case 0 -> {
                return fileTableItem.getIcon();
            }
            case 1 -> {
                return fileTableItem.getName();
            }
            case 2 -> {
                return fileTableItem.getSize();
            }
            case 3 -> {
                return fileTableItem.getLastModifyDate();
            }
            default -> System.err.println("getValueAt Error");
        }
        return null;
    }

    /**
     * 获取列名
     * @param column 列索引
     * @return String
     */
    @Override
    public String getColumnName(int column) {
        return columns[column];
    }


    /**
     * 获取列数据类型
     * @param columnIndex 列索引
     * @return Class
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return ImageIcon.class;
        }
        return String.class;
    }
}
