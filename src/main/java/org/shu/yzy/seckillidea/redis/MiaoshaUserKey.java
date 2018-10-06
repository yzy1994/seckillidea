package org.shu.yzy.seckillidea.redis;

public class MiaoshaUserKey extends BasePrefix{

    public static final int TOKEN_EXPIRE = 3600;

    private MiaoshaUserKey(String prefix, int expireSeconds){
        super(prefix, expireSeconds);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey("tk", TOKEN_EXPIRE);

}
