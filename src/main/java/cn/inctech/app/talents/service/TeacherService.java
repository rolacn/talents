package cn.inctech.app.talents.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.inctech.app.common.util.ETRMSUtil;
import cn.inctech.app.talents.mapper.TeacherMapper;
import cn.inctech.app.talents.model.Student;
import cn.inctech.app.talents.model.Teacher;

@Transactional
@Service
public class TeacherService {

    @Resource
    TeacherMapper teacherMapper;

    public boolean modifyTeacherQualification(long t_id, String u_name, String t_unit, String t_number) {
        return teacherMapper.modifyTeacherQualification(t_id, u_name, t_unit, t_number) > 0;
    }

    public boolean modifyTeacherInfo(Teacher teacher) {
        teacher.setU_age(ETRMSUtil.getAgeByBirth(teacher.getU_birth().toString()));
        return teacherMapper.modifyTeacherInfo(teacher) > 0;
    }

    public boolean generateInvitationCode(long teacherId, String invitationCode, long generateTime, long timeLength) {
        return teacherMapper.generateInvitationCode(teacherId, invitationCode, generateTime, timeLength) > 0;
    }

    public List<Student> getStudentList(long teacherId, int state, int page) {
        return teacherMapper.getStudentList(teacherId, state, (page - 1) * 10);
    }

    //添加评论
    public boolean addStudentComment(long teacherId, long studentId, String content) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = df.format(date);
        return teacherMapper.addStudentComment(teacherId, studentId, content, formatDate) > 0;
    }


}
