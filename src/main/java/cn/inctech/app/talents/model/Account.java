package cn.inctech.app.talents.model;

import lombok.Data;

/**
 * 账户类,用于存储账户信息
 */
@Data
public class Account {

    private long a_id; //账户Id
    private String a_email; //邮箱
    private String a_phone; //电话
    private String a_password; //密码
    private String a_last_password; //上一次使用的密码

}
