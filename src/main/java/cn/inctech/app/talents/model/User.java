package cn.inctech.app.talents.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户类,用于存储用户基本信息
 */
public class User {

    private long u_id; //用户Id
    private String u_name; //用户名
    private String u_gender; //用户性别
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date u_birth; //生日
    private int u_age;//年龄
    private String u_phone_number; //手机号
    private String u_identity; //身份证号
    private String u_email; //电子邮箱
    private String u_wechat; //微信号
    private String u_qq; //qq号
    private String u_origin; //籍贯
    private String u_address; //现住址
    private String u_avator; //用户头像
    private String u_sessionId; //sessionId
    private int u_remark; //用户标识

    public long getU_id() {
        return u_id;
    }

    public void setU_id(long u_id) {
        this.u_id = u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_gender() {
        return u_gender;
    }

    public void setU_gender(String u_gender) {
        this.u_gender = u_gender;
    }

    public Date getU_birth() {
        return u_birth;
    }

    public void setU_birth(Date u_birth) {
        this.u_birth = u_birth;
    }

    public String getU_phone_number() {
        return u_phone_number;
    }

    public void setU_phone_number(String u_phone_number) {
        this.u_phone_number = u_phone_number;
    }

    public String getU_identity() {
        return u_identity;
    }

    public void setU_identity(String u_identity) {
        this.u_identity = u_identity;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }

    public String getU_wechat() {
        return u_wechat;
    }

    public void setU_wechat(String u_wechat) {
        this.u_wechat = u_wechat;
    }

    public String getU_qq() {
        return u_qq;
    }

    public void setU_qq(String u_qq) {
        this.u_qq = u_qq;
    }

    public String getU_origin() {
        return u_origin;
    }

    public void setU_origin(String u_origin) {
        this.u_origin = u_origin;
    }

    public String getU_address() {
        return u_address;
    }

    public void setU_address(String u_address) {
        this.u_address = u_address;
    }

    public String getU_avator() {
        return u_avator;
    }

    public void setU_avator(String u_avator) {
        this.u_avator = u_avator;
    }

    public String getU_sessionId() {
        return u_sessionId;
    }

    public void setU_sessionId(String u_sessionId) {
        this.u_sessionId = u_sessionId;
    }

    public int getU_remark() {
        return u_remark;
    }

    public void setU_remark(int u_remark) {
        this.u_remark = u_remark;
    }

    public int getU_age() {
        return u_age;
    }

    public void setU_age(int u_age) {
        this.u_age = u_age;
    }
}
