package com.cv4j.piccrawler;

import com.cv4j.piccrawler.proxy.domain.Proxy;
import com.cv4j.piccrawler.proxy.task.ProxySerializeTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.UUID;

/**
 * Created by tony on 2017/10/10.
 */
public class Utils {

    static Logger logger=  LoggerFactory.getLogger(Utils.class);

    /**
     * 生成随机数<br>
     * GUID： 即Globally Unique Identifier（全球唯一标识符） 也称作 UUID(Universally Unique
     * IDentifier) 。
     * <p>
     * 所以GUID就是UUID。
     * <p>
     * GUID是一个128位长的数字，一般用16进制表示。算法的核心思想是结合机器的网卡、当地时间、一个随即数来生成GUID。
     * <p>
     * 从理论上讲，如果一台机器每秒产生10000000个GUID，则可以保证（概率意义上）3240年不重复。
     *
     * @return
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 创建默认的文件夹用于存放图片
     * @param directory
     */
    public static File mkDefaultDir(File directory) {

        directory = new File("images");
        if (!directory.exists()) {
            directory.mkdir();
        }

        return directory;
    }

    /**
     * 序列化对象
     * @param object
     * @throws Exception
     */
    public static void serializeObject(Object object,String filePath){
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            logger.info("序列化成功");
            oos.flush();
            fos.close();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 反序列化对象
     * @param path
     * @throws Exception
     */
    public static Object deserializeObject(String path) throws Exception {

        File file = new File(path);
        InputStream fis = new FileInputStream(file);
        ObjectInputStream ois = null;
        Object object = null;
        ois = new ObjectInputStream(fis);
        object = ois.readObject();
        fis.close();
        ois.close();
        return object;
    }

    /**
     * 是否丢弃代理
     * 失败次数大于３，且失败率超过60%，丢弃
     */
    public static boolean isDiscardProxy(Proxy proxy){

        int succTimes = proxy.getSuccessfulTimes();
        int failTimes = proxy.getFailureTimes();

        if(failTimes >= 3){
            double failRate = (failTimes + 0.0) / (succTimes + failTimes);
            if (failRate > 0.6){
                return true;
            }
        }
        return false;
    }
}
