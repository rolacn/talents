package cn.inctech.app.talents.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import cn.inctech.app.common.util.ETRMSUtil;
import lombok.Data;

/**
 * 简历类,用于存储用户投递简历行为
 */
@Data
public class Delivery {

    private String c_name; //企业名称
    private String p_duty; //职位名称
    private String u_name; //用户名
    private String u_gender; //用户性别
    private String s_current_school; //学生当前学校
    private String s_major; //专业
    private String s_education; //学历
    private String p_work_location; //工作地点

    private long d_id; //投递简历Id
    private long c_id; //企业Id
    private long p_id; //职位Id
    private long s_id; //学生Id
    private long t_id; //教师Id
    private int d_state; //状态
    private String d_file; //附件简历地址
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date d_date; //投递日期
    private int d_method; //投递方式(0->学生直接投递企业,1->教师推荐学生给企业,4->学生投递简历至教师)

    public Delivery() {
    }

    public Delivery(long c_id, long p_id, long s_id, long t_id, String filePath, int method) {
        this.d_id = ETRMSUtil.generateUID("D");
        this.c_id = c_id;
        this.d_file = filePath;
        this.p_id = p_id;
        this.s_id = s_id;
        this.t_id = t_id;
        this.d_date = ETRMSUtil.generateFormattedDate();
        this.d_state = 0;
        this.d_method = method;
    }
    
}