package cn.inctech.app.talents.model;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

import java.util.Date;

/**
 * 评论类,用于存储教师对学生评价内容
 */
@Data
public class Comment {

    private long t_id; //发出此评论的教师Id
    private long s_id; //学生Id
    private String content; //评论内容
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date; //评论日期
   
}
