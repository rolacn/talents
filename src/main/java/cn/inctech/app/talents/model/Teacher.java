package cn.inctech.app.talents.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 教师类,用于存储教师特有信息
 * 父类为User,表示Teacher为弱实体,需建立在User类存在的条件下
 */
@Data
@ToString
@EqualsAndHashCode(callSuper=false)
public class Teacher extends User {

    private long t_id; //教师Id
    private String t_invitation_code; //邀请码
    private long t_generate_time; //邀请码生成时间
    private long t_time_length; //邀请码有效时长
    private String t_education; //学历
    private String t_graduate; //毕业院校 or 最高学历院校
    private String t_unit; //任职单位
    private String t_industry; //所属行业
    private String t_department; //任职部门
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date t_service_date; //任职日期
    private String t_title; //职称
    private String t_job; //工作名称
    private String t_honor; //所获荣誉
    private String t_credentials; //证书
    private String t_achievement; //学术成就
    private String t_description; //个人介绍
    private String t_photo; //照片
    private String t_number;//职工号
    private String t_major; //专业
    private String t_experience; //工作经历
    private String t_office_add; //办公室地址
    private String t_research_direction; //研究方向
   
    
}
