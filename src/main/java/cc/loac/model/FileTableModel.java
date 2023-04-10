package cc.loac.model;

import cc.loac.entity.FileTableItem;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileTableModel extends AbstractTableModel {

    private List<FileTableItem> items;
    private static final String[] columns = {
            "图标", "名称", "大小", "修改时间"
    };

    public FileTableModel() {
        items = new ArrayList<>();
    }

    public FileTableModel(File[] files) {
        items = new ArrayList<>();
        for (File file : files) {
            FileTableItem fileTableItem = new FileTableItem();
            fileTableItem.setFile(file);
            items.add(fileTableItem);
        }
    }

    public FileTableModel(List<FileTableItem> list) {
        items = list;
    }

    public FileTableItem getFileTableItem(int index) {
        return items.get(index);
    }

    public FileTableItem getFileTableItem(String name) {
        for (FileTableItem fileTableItem : items) {
            File file = fileTableItem.getFile();
            if (file.getName().equals(name)) {
                return fileTableItem;
            }
        }
        return null;
    }

    public File getFile(int index) {
        return items.get(index).getFile();
    }

    public File getFile(String name) {
        for (FileTableItem fileTableItem : items) {
            File file = fileTableItem.getFile();
            if (file.getName().equals(name)) {
                return file;
            }
        }
        return null;
    }

    public void setFile(File file, int index) {
        FileTableItem fileTableItem = new FileTableItem();
        fileTableItem.setFile(file);
        items.set(index, fileTableItem);
    }


    public List<File> getFiles() {
        List<File> files = new ArrayList<>();
        for (FileTableItem fileTableItem : items) {
            files.add(fileTableItem.getFile());
        }
        return files;
    }

    public void setFiles(List<File> files) {
        items = new ArrayList<>();
        for (File file : files) {
            FileTableItem fileTableItem = new FileTableItem();
            fileTableItem.setFile(file);
            items.add(fileTableItem);
        }
        fireTableDataChanged();
    }

    public void setFiles(File[] files) {
        items = new ArrayList<>();
        for (File file : files) {
            FileTableItem fileTableItem = new FileTableItem();
            fileTableItem.setFile(file);
            items.add(fileTableItem);
        }
        fireTableDataChanged();
    }


    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

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

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return ImageIcon.class;
        }
        return String.class;
    }
}
