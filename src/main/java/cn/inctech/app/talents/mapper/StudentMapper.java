package cn.inctech.app.talents.mapper;

import java.util.Date;
import java.util.List;

import cn.inctech.app.talents.model.Delivery;
import cn.inctech.app.talents.model.Student;
import cn.inctech.app.talents.model.Teacher;

public interface StudentMapper {

    int blindByInvitationCode(long s_id, long t_id, Date ts_date, int ts_state);

    Teacher getTeacherByInvitationCode(String invitationCode);

    long getInvitationCodeGenerateTime(String invitationCode);

    long getInvitationCodeTimeLength(String invitationCode);

    List<Delivery> getPostedResume(long studentId, int state);

    int modifyStudentInfo(Student student);

    List<Teacher> getTeacherList(long studentId, int state);
}
