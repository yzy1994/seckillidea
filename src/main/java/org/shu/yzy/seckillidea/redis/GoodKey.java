package org.shu.yzy.seckillidea.redis;

public class GoodKey extends BasePrefix {

    private GoodKey(String prefix){
        super(prefix, 30);
    }

    private GoodKey(String prefix, int expireSeconds){
        super(prefix, expireSeconds);
    }

    public static GoodKey getGoodList = new GoodKey("goodList", 10);

    public static GoodKey getGoodKey = new GoodKey("good", 60);

    public static GoodKey getSeckillGoodKey = new GoodKey("seckillgood", 60);

    public static GoodKey getSeckillStock = new GoodKey("seckillStock", 600);
}
