package cc.loac.common;


import cc.loac.myenum.OS;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Tool {
    // 获取 OS.NAME
    private static final String OS_NAME = System.getProperty("os.name").toUpperCase();

    /**
     * 获取 OS.NAME
     *
     * @return OS
     */
    public static OS getOSName() {
        if (OS_NAME.contains("WINDOWS")) {
            return OS.OS_WINDOW;
        } else if (OS_NAME.contains("LINUX")) {
            return OS.OS_LINUX;
        } else {
            return OS.OS_MACOS;
        }
    }

    /**
     * 格式化文件大小文字
     * 例如将 2048 格式化为 2KB
     * @param length
     * @return
     */
    public static String formatSize(double length) {
        double size = length / 1024;
        String result = "";
        if (size < 1024) {
            result = String.format("%.1f kB", size);
        } else if (size < 1048576) {
            result = String.format("%.1f MB", size / 1024);
        } else {
            result = String.format("%.1f GB", size / 1024 / 1024);
        }
        return result;
    }


    /**
     * 格式化日期
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar currentCal = Calendar.getInstance();
        String result = "";
        if (cal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR)) {
            // 文件修改年份和当前年份一致
            sdf = new SimpleDateFormat("MM 月 dd 日");
        } else {
            // 文件修改年份和当前年份不一致
            sdf = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
        }
        result = sdf.format(date);
        return result;
    }

    /**
     * 压缩zip
     * @param filePath 要压缩的文件
     * @param outPath 输出地址
     * @return 输出地址
     * @throws Exception
     */
    public static String compressFileByZIP(String filePath, String outPath) throws Exception {
        File file = new File(filePath);
        String outPutFileName = outPath + ".zip";
        ArrayList<File> fileList = new ArrayList<>();
        if (file.isDirectory()) {
            fileList.addAll(Arrays.asList(file.listFiles()));
        } else {
            fileList.add(file);
        }
        FileInputStream fileInputStream = null;
        CheckedOutputStream checkedOutputStream = new CheckedOutputStream(new FileOutputStream(outPutFileName), new Adler32());
        ZipOutputStream zipOutputStream = new ZipOutputStream(checkedOutputStream);
        for (File f : fileList) {
            if (f.isDirectory()) {
                continue;
            }
            zipOutputStream.putNextEntry(new ZipEntry(f.getName()));
            fileInputStream = new FileInputStream(f);
            byte[] bytes = new byte[1024];
            int read;
            while ((read = fileInputStream.read(bytes)) != -1) {
                zipOutputStream.write(bytes);
            }
        }
        byte[] bytes = new byte[1024];
        int read;
        while ((read = fileInputStream.read(bytes)) != -1) {
            zipOutputStream.write(bytes);
        }
        fileInputStream.close();
        zipOutputStream.close();
        return outPutFileName;
    }
}
