package casia.isi.zdr.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.zdr.util.FileUtil
 * @Description: TODO(FileUtil)
 * @date 2019/4/17 13:42
 */
public class FileUtil {

    /**
     * 从文件中读取最后处理的id
     *
     * @param lastIdFileName
     * @return
     */
    public static long getLastId(String lastIdFileName) {
        File dir = new File("cursor");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File lastIdFile = new File(dir, lastIdFileName);
        if (!lastIdFile.getAbsoluteFile().exists()) {
            try {
                lastIdFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }
        FileReader reader = null;
        String id = null;
        try {
            reader = new FileReader(lastIdFile);
            char[] buffer = new char[12];
            int read = reader.read(buffer);
            if (read == -1) {
                id = "0";
            } else {
                char[] realBuffer = new char[read];
                System.arraycopy(buffer, 0, realBuffer, 0, read);
                id = new String(realBuffer);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeReader(reader);
        }
        if (id != null && !"".equals(id)) {
            return Long.parseLong(id);
        }
        return 0;
    }

    /**
     * 更新最新处理的id
     *
     * @param filaname
     * @param autoId
     */
    public static void updateCursor(String filaname, String autoId) {

        File dir = new File("cursor");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, filaname);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(autoId);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeWriter(writer);
        }
    }

    /**
     * 读取文件
     *
     * @param file
     * @return
     */
    public static String getFileContent(File file) {
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line.trim()).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeReader(reader);
        }
        return sb.toString();
    }

    /**
     * 读取文件
     *
     * @param file
     * @return
     */
    public static Set<String> getFileContentByLine(File file) {
        Set<String> set = new HashSet<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                if ("".equals(line.trim()))
                    continue;
                set.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeReader(reader);
        }
        return set;
    }

    /**
     * 读取文件首行
     *
     * @param filePath
     * @return
     */
    public static String getFirstLine(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String line;
            if ((line = reader.readLine()) != null) {
                return line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeReader(reader);
        }
        return null;
    }


    /**
     * 按行读取文件
     *
     * @param fileName 文件名
     * @return List<String> 行列表
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static List<String> readFileByLine(String fileName) throws IOException {
        List<String> lineList = new ArrayList<String>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        String line = null;
        while ((line = reader.readLine()) != null) {
            lineList.add(line.trim());
        }

        return lineList;
    }

    /**
     * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
     *
     * @param fileName 文件名
     * @param content  文件内容
     */
    public static void writeFileByAddNew(String fileName, String content) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName, true), "UTF-8"));
            out.write(content + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写一个新文件
     *
     * @param fileName 文件名
     * @param content  文件内容
     */
    public static void writeFileByNewFile(String fileName, String content) {
        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName, false), "UTF-8"));
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param reader:文件读取器
     * @return
     * @Description: TODO(关闭文件READER)
     */
    public static void closeReader(FileReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param writer:文件写入器
     * @return
     * @Description: TODO(关闭文件WRITER)
     */
    private static void closeWriter(BufferedWriter writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param reader:文件读取器
     * @return
     * @Description: TODO(关闭文件READER)
     */
    private static void closeReader(BufferedReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        updateCursor("cursor/aa.txtr", "121_1212");
    }
}
