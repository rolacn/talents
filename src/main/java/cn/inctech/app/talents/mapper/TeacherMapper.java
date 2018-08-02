package cn.inctech.app.talents.mapper;

import java.util.List;

import cn.inctech.app.talents.model.Student;
import cn.inctech.app.talents.model.Teacher;

public interface TeacherMapper {

    int modifyTeacherQualification(long t_id, String u_name, String t_unit, String t_number);

    int modifyTeacherInfo(Teacher teacher);

    int generateInvitationCode(long t_id, String t_invitationCode, long generateTime, long timeLength);

    List<Student> getStudentList(long teacherId, int state, int startIndex);

    //添加给学生的评论
    int addStudentComment(long teacherId, long studentId, String content, String date);

}
