package org.shu.yzy.seckillidea.service.impl;

import org.shu.yzy.seckillidea.domain.MiaoshaUser;
import org.shu.yzy.seckillidea.redis.RedisService;
import org.shu.yzy.seckillidea.redis.SeckillKey;
import org.shu.yzy.seckillidea.service.OtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Service
public class OtherServiceImpl implements OtherService {
    @Autowired
    RedisService redisService;

    @Override
    public BufferedImage createVerifyCode(MiaoshaUser user, long goodId) {

        int width = 70;
        int height = 32;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0,0,width,height);

        g.setColor(Color.black);
        g.drawRect(0,0,width-1, height-1);

        Random random = new Random();

        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }

        String verifyCode = generateVerifyCode(random);
        g.setColor(new Color(0, 100,0));
        g.setFont(new Font("Candara", Font.BOLD, 30));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        int res = calc(verifyCode);
        if(user!=null) {
            redisService.set(SeckillKey.getSeckillVC, user.getId() + "," + goodId, String.valueOf(res));
        }
        return image;
    }



    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[] {'+', '-', '*'};
    /**
     * + - *
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2;
        return exp;
    }
}
