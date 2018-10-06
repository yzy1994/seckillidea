package org.shu.yzy.seckillidea.redis;

public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();

}
