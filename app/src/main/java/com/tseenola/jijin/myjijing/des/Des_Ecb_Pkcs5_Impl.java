package com.tseenola.jijin.myjijing.des;

/**DES加密的实现，以ECB为分组模式，PKCS5为填充规则
 * Created by KuCoffee on 2017/5/8.
 */

public class Des_Ecb_Pkcs5_Impl extends DesImpl {

    public Des_Ecb_Pkcs5_Impl() {
        transformation = "DES/ECB/PKCS5Padding";
    }

    public synchronized static DesImpl getInstance() {
        if(mDesImpl == null) {
            mDesImpl = new Des_Ecb_Pkcs5_Impl();
        }

        return mDesImpl;
    }
}
