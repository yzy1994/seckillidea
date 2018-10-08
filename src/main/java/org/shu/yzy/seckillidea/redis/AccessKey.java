package org.shu.yzy.seckillidea.redis;

public class AccessKey extends BasePrefix{

    private AccessKey(String prefix, int expireSeconds){
        super(prefix, expireSeconds);
    }

    public static AccessKey getAccessKey(int seconds){
        return new AccessKey("access", seconds);
    }
}
