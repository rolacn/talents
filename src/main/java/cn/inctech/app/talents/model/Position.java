package cn.inctech.app.talents.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 职位类,用于存储职位信息
 * 父类为Enterprise,表示职位和企业的关联(多对一)
 */
@Data
@ToString
@EqualsAndHashCode(callSuper=false)
public class Position extends Enterprise {

    private long u_id; //用户Id,用于判断是否收藏

    private long p_id; //职位Id
    private long c_id; //公司Id
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date p_create; //职位发布日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date p_end; //职位截止日期
    private float p_salary; //薪水
    private int p_demand; //需求人数
    private String p_work_location; //工作地点
    private String p_education; //要求学历
    private String p_time; //工作时间
    private String p_length; //工作时长
    private String p_temptation; //职位诱惑
    private String p_duty; //职位名称
    private String p_industry; //行业分类
    private String p_direction; //方向分类
    private String p_job; //工作分类
    private String p_qualifications; //职位描述

}
