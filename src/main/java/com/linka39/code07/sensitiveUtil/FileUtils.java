package com.linka39.code07.sensitiveUtil;



import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.log4j.*;
import java.util.List;

/**
 * 文件操作工具类
 */
public class FileUtils {

    private static Logger logger = Logger.getLogger(FileUtils.class);


    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtend(String filename) {
        return getExtend(filename, "");
    }

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtend(String filename, String defExt) {
        if ((filename != null) && (filename.length() > 0)) {
            int i = filename.lastIndexOf('.');

            if ((i > 0) && (i < (filename.length() - 1))) {
                return (filename.substring(i + 1)).toLowerCase();
            }
        }
        return defExt.toLowerCase();
    }

    /**
     * 获取文件名称[不含后缀名]
     *
     * @param
     * @return String
     */
    public static String getFilePrefix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex).replaceAll("\\s*", "");
    }

    /**
     * 获取文件名称[不含后缀名]
     * 不去掉文件目录的空格
     *
     * @param
     * @return String
     */
    public static String getFilePrefix2(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex);
    }

    /**
     * 文件复制
     * 方法摘要：这里一句话描述方法的用途
     *
     * @param
     * @return void
     */
    public static void copyFile(String inputFile, String outputFile) throws FileNotFoundException {
        File sFile = new File(inputFile);
        File tFile = new File(outputFile);
        FileInputStream fis = new FileInputStream(sFile);
        FileOutputStream fos = new FileOutputStream(tFile);
        int temp = 0;
        byte[] buf = new byte[10240];
        try {
            while ((temp = fis.read(buf)) != -1) {
                fos.write(buf, 0, temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断文件是否为图片<br>
     * <br>
     *
     * @param filename 文件名<br>
     *                 判断具体文件类型<br>
     * @return 检查后的结果<br>
     * @throws Exception
     */
    public static boolean isPicture(String filename) {
        // 文件名称为空的场合
        if (StringUtils.isEmpty(filename)) {
            // 返回不和合法
            return false;
        }
        // 获得文件后缀名
        //String tmpName = getExtend(filename);
        String tmpName = filename;
        // 声明图片后缀名数组
        String imgeArray[][] = {{"bmp", "0"}, {"dib", "1"},
                {"gif", "2"}, {"jfif", "3"}, {"jpe", "4"},
                {"jpeg", "5"}, {"jpg", "6"}, {"png", "7"},
                {"tif", "8"}, {"tiff", "9"}, {"ico", "10"}};
        // 遍历名称数组
        for (int i = 0; i < imgeArray.length; i++) {
            // 判断单个类型文件的场合
            if (imgeArray[i][0].equals(tmpName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断文件是否为DWG<br>
     * <br>
     *
     * @param filename 文件名<br>
     *                 判断具体文件类型<br>
     * @return 检查后的结果<br>
     * @throws Exception
     */
    public static boolean isDwg(String filename) {
        // 文件名称为空的场合
        if (StringUtils.isEmpty(filename)) {
            // 返回不和合法
            return false;
        }
        // 获得文件后缀名
        String tmpName = getExtend(filename);
        // 声明图片后缀名数组
        if (tmpName.equals("dwg")) {
            return true;
        }
        return false;
    }

    /**
     * 删除指定的文件
     *
     * @param strFileName 指定绝对路径的文件名
     * @return 如果删除成功true否则false
     */
    public static boolean delete(String strFileName) {
        File fileDelete = new File(strFileName);

        if (!fileDelete.exists() || !fileDelete.isFile()) {
            logger.info("错误: " + strFileName + "不存在!");
            return false;
        }

        logger.info("--------成功删除文件---------" + strFileName);
        return fileDelete.delete();
    }

    /**
     * @param @param  fileName
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: encodingFileName 2015-11-26 huangzq add
     * @Description: 防止文件名中文乱码含有空格时%20
     */
    public static String encodingFileName(String fileName) {
        String returnFileName = "";
        try {
            returnFileName = URLEncoder.encode(fileName, "UTF-8");
            returnFileName = StringUtils.replace(returnFileName, "+", "%20");
            if (returnFileName.length() > 150) {
                returnFileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
                returnFileName = StringUtils.replace(returnFileName, " ", "%20");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("Don't support this encoding ...");
        }
        return returnFileName;
    }

    /**
     * 写入txt文件
     *
     * @param file   文件地址
     * @param conent 写入内容
     */
    public static void continueWrite(String file, String conent) throws Exception {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
            out.write(conent);
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
     * 清空已有的文件内容，以便下次重新写入新的内容
     *
     * @param fileName
     */
    public static void clearInfoForFile(String fileName) {
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断对应目录内，文本内容所在的Url
     *
     * @param folder,search
     */
    public static ArrayList<String> search(File folder, String search) throws IOException {
        ArrayList<String> dirList = new ArrayList<String>();
        if(folder.isDirectory()){
            File[] fs = folder.listFiles();
            if(fs != null) {
                for(File f : fs) {
                    search(f,search);
                }
            }
        }else {
            int count = 0;
            BufferedReader bfr = new BufferedReader(new FileReader(folder));
            while(true){
                String line = bfr.readLine();
                ++count;
                if(line == null) {
                    break;
                }
                if(line.contains(search)) {
                    System.out.println(folder.getAbsolutePath()+"\tline:"+count);
                    dirList.add(folder.getAbsolutePath());
                }
            }
        }
        return null;
    }

    /**
     * 清空已有的文件内容是否已包含敏感词，返回其行数
     *
     * @param fileName
     */
    public static int haveSensitiveWord(String fileName, String context) throws Exception {

        int line = 0;
        File file = new File(fileName);
        //读取文件
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
        try {
            StringBuffer stringBuffer = new StringBuffer();
            if (file.isFile() && file.exists()) {
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt = null;
                //读取文件，将文件内容放入到set中
                while ((txt = bufferedReader.readLine()) != null) {
                    ++line;
                    if (context.equals(txt)) {
                        break;
                    }
                }
            } else {
                throw new Exception("文件不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            read.close();
        }
        return line;
    }

    /**
     * 去除文件中指定内容
     *
     * @return Set<String>
     * @throws Exception
     */
    public static void deleteSensitiveWordFile(String wordPath, String context) throws Exception {

        File file = new File(wordPath);
        //读取文件
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
        try {
            StringBuffer stringBuffer = new StringBuffer();
            if (file.isFile() && file.exists()) {
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt = null;
                //读取文件，将文件内容放入到set中
                while ((txt = bufferedReader.readLine()) != null) {
                    if (!context.equals(txt)) {
                        stringBuffer.append(txt);
                        stringBuffer.append("\n");
                    }
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            } else {
                throw new Exception("文件不存在");
            }
            clearInfoForFile(wordPath);
            continueWrite(wordPath, stringBuffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            read.close();
        }
    }
}
