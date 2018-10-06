package org.shu.yzy.seckillidea.redis;

public class SeckillKey extends BasePrefix{

    private SeckillKey(String prefix){
        super(prefix, 600);
    }

    /** 图形验证码答案Key */
    public static SeckillKey getSeckillVC = new SeckillKey("vc");

    /** 秒杀结果Key */
    public static SeckillKey getSeckillResult = new SeckillKey("seckillResult");

    public static String VC_SALT = "123abc";
}
