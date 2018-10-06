package org.shu.yzy.seckillidea.Enum;


public enum ResultEnum {
    SUCCESS(0, "success"),
    SERVER_ERROR(101, "服务器异常"),
    BIND_ERROR(102, "参数异常"),
    REQUEST_ILLEGAL(103, "请求非法"),
    ACCESS_LIMIT_REACHED(104, "访问太频繁"),
    SESSION_ERROR(201, "Session出错了"),
    PASSWORD_EMPTY(202, "密码不能为空"),
    MOBILE_EMPTY(203, "手机号不能为空"),
    MOBILE_ERROR(204, "手机号错误"),
    MOBILE_NOT_EXIST(205, "手机号不存在"),
    PASSWORD_ERROR(206, "密码错误"),
    MOBILE_EXISTED(207, "手机号已注册"),
    ORDER_NOT_EXIST(301, "订单号不存在"),
    SECKILL_OVER(401, "秒杀结束了"),
    REPEATED_SECKILL(402, "重复秒杀"),
    SECKILL_FAILED(403, "秒杀失败"),
    SECKILL_NOT_STARTED(404, "秒杀未开始"),
    SECKILL_FALSE_VC(405, "秒杀验证码错误"),
    SECKILL_NOT_EXIST(406, "秒杀项不存在"),
    SECKILL_WAIT_RESULT(407, "等待秒杀结果"),
    SECKILL_VC_OVERDUED(408, "秒杀验证码过期了"),
    SECKILL_SUCCESS(409, "秒杀成功");

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResultEnum{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
