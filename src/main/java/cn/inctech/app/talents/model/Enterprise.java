package cn.inctech.app.talents.model;

import lombok.Data;

/**
 * 企业类,用于存储企业信息
 */
@Data
public class Enterprise {

    private long c_id; //企业Id
    private String c_email; //企业邮箱
    private String c_password; //登录密码
    private String c_name; //企业名称
    private String c_home_page; //公司主页
    private String c_industry; //所属行业
    private int c_scale; //公司规模
    private String c_address; //公司地址
    private String c_type; //公司类型
    private String c_register_number; //注册号
    private String c_founded_date; //建立日期
    private String c_register_captial; //注册资本
    private String c_welfare; //公司福利
    private String c_introduction; //公司简介
    private String c_description; //公司描述
    private String c_hremail; //HR邮箱
    private int c_position_numbers; //职位数量
    private String c_sessionId; //sessionId
   
}
