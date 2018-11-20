package com.tseenola.jijin.myjijing.des;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by lenovo on 2017/4/10.
 * 描述：DES 加密默认实现
 *
 */

public class DesImpl implements IDes {
    protected String transformation;

    protected static DesImpl mDesImpl;

    protected DesImpl() {
        transformation = "DES";
    }

    public synchronized static DesImpl getInstance() {
        if(mDesImpl == null) {
            mDesImpl = new DesImpl();
        }

        return mDesImpl;
    }

    @Override
    public byte[] encrypt(byte[] pKey, byte[] pData)throws Exception {
        checkParams(pKey,pData);
        SecretKeySpec secretKey = new SecretKeySpec(pKey, "DES");
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte result [] = cipher.doFinal(pData);
        return result;
    }

    @Override
    public byte[] encrypt(String pHexStrKey, String pHexStrData) throws Exception {
        byte lKeys[] = hexStringToByte(pHexStrKey);
        byte lDatas [] = hexStringToByte(pHexStrData);
        return encrypt(lKeys,lDatas);
    }

    @Override
    public byte[] decrypt(byte[] pKey, byte[] pData) throws Exception {
        checkParams(pKey,pData);
        SecretKeySpec secretKey = new SecretKeySpec(pKey, "DES");
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte result [] = cipher.doFinal(pData);
        return result;
    }

    @Override
    public byte[] decrypt(String pHexStrKey, String pHexStrData) throws Exception {
        byte lKeys[] = hexStringToByte(pHexStrKey);
        byte lDatas [] = hexStringToByte(pHexStrData);
        return decrypt(lKeys,lDatas);
    }

    /**
     * 检查传入的DES参数是否正确
     * @param pKey
     * @param pData
     */
    protected void checkParams(byte[] pKey, byte[] pData){
        if (pKey.length%8 != 0){
            throw new IllegalArgumentException("密钥长度必须为8的倍数");
        }

        if (pKey.length%8 != 0){
            throw new IllegalArgumentException("被加密数据长度必须为8的倍数");
        }
    }

    /**
     * 16进制字符串转换为byte 数组
     *
     * @param hex
     * @return
     */
    private byte[] hexStringToByte( String hex) {
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
}
