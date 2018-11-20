package com.tseenola.jijin.myjijing.utils;

import android.text.format.DateFormat;
import android.util.Log;

import com.tseenola.jijin.myjijing.MyApplication;
import com.tseenola.jijin.myjijing.des.Des_Ecb_Pkcs5_Impl;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class LogUtil {
    private static File logPath = null;
    private static final byte[] factorBytes = "UrovoKey".getBytes();
    public static File getLogPath() {
        if (logPath == null) {
            logPath = new File(MyApplication.getInstance().getFilesDir().getAbsolutePath(), "log");
        }
        if (!logPath.exists()) {
            logPath.mkdirs();
        }
        return logPath;
    }

    public static void setLogPath(File logPath) {
        LogUtil.logPath = logPath;
        if (!logPath.exists()) {
            logPath.mkdirs();
        }
    }

    /**
     * 初始化Log4J
     */
    private static Logger init() {
        try {
            String fileName = DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()).toString() + ".log";
            File logFile = new File(getLogPath(), fileName);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            LogConfigurator logConfigurator = new LogConfigurator();
            logConfigurator.setFileName(logFile.getAbsolutePath());
            logConfigurator.setRootLevel(Level.DEBUG);
            logConfigurator.setLevel("org.apache", Level.ERROR);
            logConfigurator.setFilePattern("%d - [%c{2}:%-5p] - %m%n");
            logConfigurator.setMaxFileSize(Long.MAX_VALUE);
            logConfigurator.setMaxBackupSize(0);
            logConfigurator.setImmediateFlush(true);
            logConfigurator.configure();

            return Logger.getLogger("LogUtil");
        } catch (Exception ex) {
            Log.e("[LogUtil.init]", ex.getMessage());
            return null;
        }
    }

    private static byte abcd_to_asc(byte ucBcd) {
        byte ucAsc;
        ucBcd &= 0x0f;
        if (ucBcd <= 9) {
            ucAsc = (byte) (ucBcd + (byte) '0');
        } else {
            ucAsc = (byte) (ucBcd + (byte) 'A' - (byte) 10);
        }
        return ucAsc;
    }

    private static void BcdToAsc(byte[] sAscBuf, byte[] sBcdBuf, int iAscLen) {
        int i, j = 0;
        for (i = 0; i < iAscLen / 2; i++) {
            sAscBuf[j] = (byte) ((sBcdBuf[i] & 0xf0) >> 4);
            sAscBuf[j] = abcd_to_asc(sAscBuf[j]);
            j++;
            sAscBuf[j] = (byte) (sBcdBuf[i] & 0x0f);
            sAscBuf[j] = abcd_to_asc(sAscBuf[j]);
            j++;
        }
        if (iAscLen % 2 != 0) {
            sAscBuf[j] = (byte) ((sBcdBuf[i] & 0xf0) >> 4);
            sAscBuf[j] = abcd_to_asc(sAscBuf[j]);
        }
    }

    /**
     * 打印二进制byte报文
     */
    public static void hex(String tag, String write_str, byte[] BCD, int len) {
        try {
            byte[] ASC = new byte[len * 2];
            BcdToAsc(ASC, BCD, len * 2);
            String str = new String(ASC);

            info(tag, write_str + ":" + str);
        } catch (Exception e) {
            Log.e("[LogUtil.hex]", e.getMessage());
        }
    }

    public static void info(String tag, String message) {
        Logger logger = init();
        if (logger != null) {
            logger.info(tag + "==>" + message);
        }
    }


    /**
     * 删除日志
     */
    public static void recycleLog() {
        if (LogUtil.getLogPath() != null && LogUtil.getLogPath().exists() && LogUtil.getLogPath().isDirectory()) {
            File[] files = LogUtil.getLogPath().listFiles();
            if (files != null) {
                // 回收数天前的日志
                long currentTime = System.currentTimeMillis();
                for (int i = 0; i < files.length; ++i) {
                    File file = files[i];
                    if (file != null && (currentTime - file.lastModified()) > 100 * 24 * 60 * 60 * 1000L) {
                        file.delete();
                    }
                }
            }
        }
    }

    public static void hexWithEncryption(String tag, String write_str, byte[] BCD, int len) {
        byte[] bytes = new byte[len];
        System.arraycopy(BCD, 0, bytes, 0, len);
        byte[] result = null;
        try {
            result = Des_Ecb_Pkcs5_Impl.getInstance().encrypt(factorBytes, bytes);
            hex(tag, write_str, result, result.length);
        } catch (Exception e) {
            e.printStackTrace();
            info("hexWithEncryption", e.getMessage());
        }
    }
}
