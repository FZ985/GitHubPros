package expand.download2.utils;

import android.text.TextUtils;

import java.io.File;

/**
 * 文件工具类
 *
 * author：jfz
 * version：1.0.0
 * date：2017/8/27
 */
public class FileUtils {

    public final static String FILE_EXTENSION_SEPARATOR = ".";
    private static String mSeparator = File.separator;
    private static char mSeparatorChar = File.separatorChar;

    /**
     * @param filePath
     * @return 文件所在文件夹
     */
    public static String getFolderName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePos = filePath.lastIndexOf(File.separator);
        return (filePos == -1) ? "" : filePath.substring(0, filePos);
    }

    /**
     * @param filePath
     * @return 文件后缀
     */
    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extensionPos = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePos = filePath.lastIndexOf(File.separator);
        if (extensionPos == -1) {
            return "";
        }
        return (filePos >= extensionPos) ? "" : filePath.substring(extensionPos + 1);
    }

    /**
     * 创建文件目录
     *
     * @param filePath
     * @return
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * 创建文件目录
     *
     * @param filePath
     * @return
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }


    /**
     * 删除文件，可以是文件也可以是文件夹
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }

        File[] cFiles = file.listFiles();
        if (cFiles != null) {
            for (File f : file.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                } else {
                    deleteFile(f.getAbsolutePath());
                }
            }
        }

        return file.delete();
    }

    /**
     * @param path
     * @return 文件是否存在（不包含文件夹）
     */
    public static boolean fileExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    /**
     * 创建文件
     *
     * @param path
     * @return
     */
    public static File createFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        File file = new File(path);
        if (file.isFile()) {
            return file;
        }

        File parentFile = file.getParentFile();
        if (createFolder(parentFile) != null) {
            try {
                if (file.createNewFile()) {
                    return file;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 创建文件夹
     *
     * @param folder
     * @return
     */
    public static File createFolder(File folder) {
        if (folder == null) {
            return null;
        }

        if (folder.isDirectory()) {
            return folder;
        }

        File tmp = folder;
        while (tmp != null) {
            if (tmp.isFile()) {
                tmp.delete();
                break;
            }

            tmp = tmp.getParentFile();
        }

        folder.mkdirs();

        return folder;
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public static boolean delete(String path) {
        return !TextUtils.isEmpty(path) && delete(new File(path));
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public static boolean delete(File path) {
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            for (File file : files) {
                if (!delete(file)) {
                    return false;
                }
            }
        }
        return !path.exists() || path.delete();
    }

    /**
     * @param path
     * @return 文件名（包含后缀）
     */
    public static String getFileName(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }

        int query = path.lastIndexOf('?');
        if (query > 0) {
            path = path.substring(0, query);
        }

        int filenamePos = path.lastIndexOf(mSeparatorChar);
        return (filenamePos >= 0) ? path.substring(filenamePos + 1) : path;
    }

    /**
     * @param path
     * @return 文件名（不包含后缀）
     */
    public static String getFileShortName(String path) {
        String fileName = getFileName(path);
        int separatorIndex = fileName.lastIndexOf('.');
        return separatorIndex > 0 ? fileName.substring(0, separatorIndex) : fileName;
    }

    /**
     * @param path
     * @return 文件所在目录
     */
    public static String getFilePathDir(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        int separatorIndex = -1;

        if (path != null && path.startsWith(mSeparator)) {
            separatorIndex = path.lastIndexOf(mSeparatorChar);
        }

        return (separatorIndex == -1) ? mSeparator : path.substring(0, separatorIndex);
    }

}
