package cn.inctech.app.talents.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 学生类,用于存储学生特有信息
 * 父类为User,表示Student为弱实体,需建立在User类存在的条件下
 */
@Data
@ToString
@EqualsAndHashCode(callSuper=false)
public class Student extends User {

    private long s_id; //学生Id
    private int c_state; //学生当前状态
    private String s_photo; //学生照片
    private String s_current_school; //学生当前就读院校
    private String s_education; //学历
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date s_school_enrollment; //学生入学时间
    private int s_school_length; //学校年限
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date s_graduate_year;  //毕业时间
    private String s_major; //专业
    private String s_major_course; //专业课程
    private String s_major_rank; //专业排名
    private String s_cet; //英语过级情况
    private String s_other_language; //其他语言过级情况
    private String s_qualification; //学生
    private String s_honor; //所获荣誉
    private String s_internship; //实习经验
    private String s_direction; //求职方向
    private String s_abroad; //出国经历
    private String s_training; //培训经历
    private String s_expect_jobs; //期望职位
    private String s_expect_industry; //期望行业

}
