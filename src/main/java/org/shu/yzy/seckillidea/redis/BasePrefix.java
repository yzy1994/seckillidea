package org.shu.yzy.seckillidea.redis;

import lombok.Data;

public abstract class BasePrefix implements KeyPrefix{

    private String prefix;

    private int expireSeconds;

    public BasePrefix(String prefix, int expireSeconds) {
        this.prefix = prefix;
        this.expireSeconds = expireSeconds;
    }

    public BasePrefix(String prefix){
        this(prefix, 0);
    }

    @Override
    public int expireSeconds() {
        // 0 代表永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }

}
