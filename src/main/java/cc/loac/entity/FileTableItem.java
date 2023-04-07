package cc.loac.entity;

import cc.loac.common.Tool;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Date;

public class FileTableItem {
    private File file;
    private final FileSystemView fileSystemView = FileSystemView.getFileSystemView();


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Icon getIcon() {
        return fileSystemView.getSystemIcon(file);
    }

    public String getName() {
        return file.getName();
    }

    public String getSize() {
        if (file == null) {
            return Tool.formatSize(0);
        } else if (file.isDirectory()) {
            File[] f = file.listFiles();
            return f == null ? "0" : f.length + " 个项目";
        } else {
            return Tool.formatSize(file.length());
        }
    }

    public String getLastModifyDate() {
        return Tool.formatDate(new Date(file.lastModified()));
    }

    @Override
    public String toString() {
        return "FileTableItem{" +
                "file=" + file +
                ", fileSystemView=" + fileSystemView +
                '}';
    }
}
