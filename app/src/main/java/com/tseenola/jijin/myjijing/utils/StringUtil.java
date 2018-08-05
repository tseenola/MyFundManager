package com.tseenola.jijin.myjijing.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import org.litepal.util.LogUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author Administrator
 */
public class StringUtil {
    private static Random random = new Random();
    private static long lastClickTime;

    public static String getRandomNum(int length) {
        String val = "0123456789";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int start = random.nextInt(val.length() - 1);
            builder.append(val.substring(start, start + 1));
        }
        return builder.toString();
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 30000) {
            lastClickTime = time;
            return true;
        }
        return false;
    }

    public static String getAppVersion(Context context) {
        String appVersion = null;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersion = info.versionName;   //版本名
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    public static boolean isIpv4(String ipAddress) {
        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        return is(ip, ipAddress);
    }

    /**
     * @param from  正则规则
     * @param check 需要验证的字符串
     * @return
     */
    private static boolean is(String from, String check) {
        return Pattern.compile(from).matcher(check).matches();
    }

    public static boolean isNumber(String str) {
        String check = "^(([1-9]\\d*)(\\.\\d{1,2})?)$|(0\\.0?([1-9]\\d?))$";
        return is(check, str);
    }

    public static boolean isNumberString(String str) {
        String check = "^(([0]*[1-9]{1}\\d*)|([0]{1}))" + "(\\.(\\d){0,1})?$";
        return is(check, str);
    }

    public static boolean isNullOrEmpty(String str) {
        if (str == null || str.equals(""))
            return true;
        else
            return false;
    }

    /**
     * 显示/隐藏输入法
     *
     * @param context Context
     * @param view    　需要显示／隐藏输入法的控件
     * @param isShow  　show/hide
     */
    public static void ShowHideInput(Context context, final View view, boolean isShow) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imm.showSoftInput(view, 0);
                }
            }, 400);// 显示键盘
        } else {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 通过二磁数据判断是否是IC卡
     */
    public static boolean isChipCard(String pszTrack2Data) {
        int i;
        if (pszTrack2Data == null || pszTrack2Data.equals("")) {
            return false;
        }

        if ((i = pszTrack2Data.indexOf("=")) != -1) {
            String pszSeperator = pszTrack2Data.substring(i + 5, i + 5 + 1);
            if (pszSeperator.equals("2") || pszSeperator.equals("6")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    /**
     * 获取加*卡号
     */
    public static String getStarPan(String string) {
        if (TextUtils.isEmpty(string)){
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(string.substring(0, 6));

        String String2 = string.substring(6, string.length() - 4);
        for (int i = 0; i < String2.length(); i++)
            stringBuffer.append("*");

        stringBuffer.append(string.substring(string.length() - 4, string.length()));
        return stringBuffer.toString();
    }



    /**
     * 获取签名文件存储位置
     */
    public static File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    /**
     * 获取资金归集文件存储位置
     */
    public static File getAlbumStorageDocDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), albumName);
        if (!file.mkdirs()) {
            Log.e("getAlbumStorageDocDir", "Directory not created");
        }
        return file;
    }


    /**
     * 把卡号中间位置替换成F保留前6后4
     */
    public static String getPan(String string) {
        if (string.length() > 10) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(string.substring(0, 6));
            String String2 = string.substring(6, string.length() - 4);
            for (int i = 0; i < String2.length(); i++) {
                stringBuffer.append("F");
            }
            stringBuffer.append(string.substring(string.length() - 4, string.length()));
            return stringBuffer.toString();
        } else {
            return "";
        }
    }

    /*
    * To convert the InputStream to String we use the BufferedReader.readLine()
    * method. We iterate until the BufferedReader return null which means
    * there's no more data to read. Each line will appended to a StringBuilder
    * and returned as String.
    */
    public static String streamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /**
     * 获取卡片有效期
     * @param pTrack
     * @return
     */
    public static String getExpData(String pTrack){
        int index = pTrack.indexOf("=");
        if (index == -1) {
            index = pTrack.indexOf("D");
        }
        if (index != -1){
            return pTrack.substring(index + 1, index + 3) +pTrack.substring(index + 3, index + 5) ;
        }
        return "";
    }



    /**
     * 16进制字符串转为字符串
     * @return
     */
    public static String hexStringToStr(String pHexString){
        return new String(hexStringToByte(pHexString));
    }

    public static byte[] hexStringToByte( String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }


    /**
     * byte 转为16进制字符串
     *
     * @param bArray
     * @return
     */
    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 对指定数据进行填充，直到达到需要的长度
     *
     * @param dir     在左或者右填充
     * @param fill    长度不足用什么填充
     * @param content 被填充的内容
     * @param mastLen 需要达到的长度
     * @return
     */

    public static String fillContentBy(Dir dir, String fill, String content, int mastLen){
        if(fill.length()!=1){
            return "fill 参数错误！";
        }
        String x = fill;
        int contentLen = content.length();
        int needAddLen = mastLen - contentLen;
        if(needAddLen<=0){
            return content;
        }
        while(fill.length()<needAddLen){
            fill+=x;
        }
        if(dir== Dir.left){
            content = fill+content;
        }else{
            content = content+fill;
        }
        return content;
    }

    public enum Dir{
        left,right
    }


    public static String getOutCardName(String pOutCardNo){
        if (pOutCardNo == null || pOutCardNo.length()<6){
            return "";
        }
        if (pOutCardNo.startsWith("4")){
            return "VISA";
        }
        if (pOutCardNo.startsWith("5")){
            return "MASTER";
        }
        int cardNo6 = Integer.valueOf(pOutCardNo.substring(0,6));
        if (222100>=cardNo6 && cardNo6 <=272099){
            return "MASTER";
        }
        if (pOutCardNo.startsWith("30") || pOutCardNo.startsWith("36") ||
                pOutCardNo.startsWith("38") || pOutCardNo.startsWith("39")){
            return "DINERS";
        }
        if (pOutCardNo.startsWith("34") || pOutCardNo.startsWith("37")){
            return "AMEX";
        }
        if (pOutCardNo.startsWith("35")){
            return "JCB";
        }
        return "";
    }

    public static double getAmt(double pTransAmount){
        DecimalFormat pattern = new DecimalFormat("0.00");
        double d= Double.valueOf(pattern.format(pTransAmount));
        System.out.println("d"+d);
        return d;
    }




    /**
     * 获取加*卡号外卡
     */
    public static String getStarPanOut(String string) {
        if (TextUtils.isEmpty(string)){
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(string.substring(0, 4));

        String String2 = string.substring(4, 12);
        stringBuffer.append(" ");
        for (int i = 0; i < String2.length(); i++) {
            stringBuffer.append("*");
            if (i == 3){
                stringBuffer.append(" ");
            }
        }
        stringBuffer.append(" ");
        stringBuffer.append(string.substring(12, string.length()));
        return stringBuffer.toString();
    }
    /**
     * 获取外卡卡号
     */
    public static String getPanOut(String string){
        if (TextUtils.isEmpty(string)){
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i <string.length() ; i++) {
            stringBuffer.append(string.charAt(i));
            if ((i+1)%4 == 0){
                stringBuffer.append(" ");
            }
        }
        return stringBuffer.toString();

    }
    /**
     * string型小数转换成英文String
     */
    public static String getEnglishdecimal(String decimal){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < decimal.length(); i++) {
            if (decimal.charAt(i) == '0'){
                sb.append("ZERO  ");
            }else if (decimal.charAt(i) == '1'){
                sb.append("ONE  ");
            }else if (decimal.charAt(i) == '2'){
                sb.append("TWO  ");
            }else if (decimal.charAt(i) == '3'){
                sb.append("THREE  ");
            }else if (decimal.charAt(i) == '4'){
                sb.append("FOUR  ");
            }else if (decimal.charAt(i) == '5'){
                sb.append("FIVE  ");
            }else if (decimal.charAt(i) == '6'){
                sb.append("SIX  ");
            }else if (decimal.charAt(i) == '7'){
                sb.append("SEVEN  ");
            }else if (decimal.charAt(i) == '8'){
                sb.append("EIGHT  ");
            }else if (decimal.charAt(i) == '9'){
                sb.append("NINE  ");
            }else if (decimal.charAt(i) == '.'){
                sb.append("PT.  ");
            }
        }

        return sb.toString();
    }

    public static String bitmapToBase64(Bitmap bitmap) {

        // 要返回的字符串
        String reslut = null;

        ByteArrayOutputStream baos = null;

        try {

            if (bitmap != null) {

                baos = new ByteArrayOutputStream();
                /**
                 * 压缩只对保存有效果bitmap还是原来的大小
                 */
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);

                baos.flush();
                baos.close();
                // 转换为字节数组
                byte[] byteArray = baos.toByteArray();

                // 转换为字符串
                reslut = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return reslut;
    }


    /**
     *
     * @Title: base64ToBitmap
     * @Description:  base64l转换为Bitmap
     * @param @param base64String
     * @param @return    设定文件
     * @return Bitmap    返回类型
     * @throws
     */
    public static Bitmap base64ToBitmap(String base64String){

        byte[] decode = Base64.decode(base64String, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);

        return bitmap;
    }
}
