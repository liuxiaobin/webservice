package com.yuxin.demo.logdown;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class LogsDownload {
    // 下载文件集合
    public static List<String> fileList = new ArrayList<String>();
    // 下载日志项目名称
    public static String projectName = "jfpt_common";
    // 文件下载日期
    public static String dateStr = "2020-03-27";
    // 符合的文件名
    public static String conname = projectName + ".log.{0}.gz";
    // 忽略的url
    public static String[] ignUrl = {};
    // 文件下载保存目录
    public static String downLoadPath = "D://logs/" + projectName + "/" + dateStr + "/";
    // 目录缓存文件
    public static String cacheUrlList = "D://logs/" + projectName + "cache.txt";

    public static void main(String[] args) throws MalformedURLException, IOException {
       // domnload("http://192.168.2.6:12346/");http://192.168.2.6:12347/
//        domnload("http://192.168.2.6:12347/");
        domnload("http://192.168.2.6:12347/");
    }

    public static void domnload(String url) {
        System.out.println("下载日志开始...");
        File file = new File(downLoadPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String now = dateFormat.format(new Date());
        if (dateStr.equals(now)) {
            dateStr = "newlog";
        }

        File listFile = new File(cacheUrlList);
        if (listFile.exists()) {
            System.out.println("目录缓存文件存在，直接读取...");
            FileInputStream inputStream = null;
            BufferedReader bufferedReader = null;
            try {
                // 下载连接缓存存在，直接读取
                inputStream = new FileInputStream(cacheUrlList);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                    fileList.add(str.replaceAll("\\{0\\}", dateStr));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("目录缓存文件不存在，重新分析...");
            long time = System.currentTimeMillis();
            urlDo("," + url);
            long time2 = System.currentTimeMillis();
            System.out.println("分析目录用时：" + (time2 - time) + "ms");
            // 缓存目录信息，下次直接读取
            FileWriter fw = null;
            try {
                listFile.createNewFile();
                fw = new FileWriter(listFile);
                for (int i = 0; i < fileList.size(); i++) {
                    fw.write(fileList.get(i).replaceAll(dateStr, "{0}") + "\r\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 多线程下载:4线程
        List<List<String>> lists = averageAssign(fileList, 4);
        for (int i = 0; i < lists.size(); i++) {
            DownloadTask task = new DownloadTask(i, lists.get(i), downLoadPath);
            task.start();
        }
    }

    /**
     * 解析url，获得内容
     * url格式：86@ccbsncom-1300@jfpt_common.log.0919newlog.gz,http://192.168.2.6:12345/86/ccbsncom-1300/jfpt_common.log.0919newlog.gz，
     * “,”分割，前面为目录结构与后面一一对应 其中“@”可能不存在与目录结构相似 可能 ex:过滤条件
     */
    public static void urlDo(String url) {
        // 上一层目录
        if (url.contains("..")) {
            return;
        }
        // 是文件
        if (url.charAt(url.length() - 1) != '/') {
            if (url.contains(conname.replaceAll("\\{0\\}", dateStr))) {// 符合文件名的
                // 是否忽略目录
                for (int i = 0; i < ignUrl.length; i++) {
                    if (url.contains(ignUrl[i])) {
                        return;
                    }
                }
                fileList.add(url);
            }
            return;
        }
        // 是目录
        String[] strs = url.split(",");
        String newName = strs[0];
        String newUrl = strs[1];
        try {
            Document doc = Jsoup.parse(new URL(newUrl), 60000);
            Elements links = doc.getElementsByTag("a");
            for (int i = 0; i < links.size(); i++) {
                String nextNode = links.get(i).text();
                String nextName = "".equals(newName) ? nextNode.replaceAll("/", "")
                        : newName + "@" + nextNode.replaceAll("/", "");
                String nextUrl = newUrl + nextNode;
                if(nextUrl.equals("http://192.168.2.6:12346/136/upfile_bak/201901/")){
                    continue;
                }else {
                    urlDo(nextName + "," + nextUrl);
                }

            }
        } catch (Exception e) {
            System.out.println("解析url错误[url:" + url + "]");
        }

    }

    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     *
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = source.size() % n; // (先计算出余数)
        int number = source.size() / n; // 然后是商
        int offset = 0;// 偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    /**
     * gz解压
     *
     * @param sourcedir
     */
    public static void unGzipFile(String sourcedir, boolean isDele) {
        String ouputfile = "";
        try {
            // 建立gzip压缩文件输入流
            FileInputStream fin = new FileInputStream(sourcedir);
            // 建立gzip解压工作流
            GZIPInputStream gzin = new GZIPInputStream(fin);
            // 建立解压文件输出流
            ouputfile = sourcedir.substring(0, sourcedir.lastIndexOf('.'));
            ouputfile = ouputfile.substring(0, ouputfile.lastIndexOf('.'));
            FileOutputStream fout = new FileOutputStream(ouputfile);

            int num;
            byte[] buf = new byte[1024];

            while ((num = gzin.read(buf, 0, buf.length)) != -1) {
                fout.write(buf, 0, num);
            }
            gzin.close();
            fout.close();
            fin.close();
            if (isDele) {
                new File(sourcedir).delete();
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        return;
    }
}

class DownloadTask extends Thread {
    private int i;
    private List<String> list;
    private String downLoadPath;

    public DownloadTask(int i, List<String> list, String downLoadPath) {
        this.list = list;
        this.downLoadPath = downLoadPath;
        this.i = i;
    }

    public void run() {
        // 文件下载
        System.out.println("线程【" + i + "】开始下载");
        long time = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            String[] str = list.get(i).split(",");
            String filename = str[0];
            String url = str[1];
            try {
                URL httpurl = new URL(url);
                File f = new File(downLoadPath + filename);
                if (f.exists()) {
                    f.delete();
                }
                FileUtils.copyURLToFile(httpurl, f);
                if (f.exists()) {
                    LogsDownload.unGzipFile(f.getPath(), true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long time2 = System.currentTimeMillis();
        System.out.println("线程【" + i + "】下载结束，用时：" + (time2 - time) + "ms");
    }

}
