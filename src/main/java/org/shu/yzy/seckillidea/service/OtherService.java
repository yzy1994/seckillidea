package org.shu.yzy.seckillidea.service;

import org.shu.yzy.seckillidea.domain.MiaoshaUser;

import java.awt.image.BufferedImage;

public interface OtherService {
    /** 返回的生成验证码图片，并将用户的验证码答案存入redis */
    BufferedImage createVerifyCode(MiaoshaUser user, long goodId);
}
