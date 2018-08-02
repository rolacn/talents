package cn.inctech.app.talents.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.inctech.app.common.util.ETRMSUtil;
import cn.inctech.app.talents.mapper.ETRMSMapper;
import cn.inctech.app.talents.mapper.StudentMapper;
import cn.inctech.app.talents.model.Delivery;
import cn.inctech.app.talents.model.Student;
import cn.inctech.app.talents.model.Teacher;

@Transactional
@Service
public class StudentService {

    @Resource
    ETRMSMapper etrmsMapper;

    @Resource
    StudentMapper studentMapper;

    private long currentTime = 0;
    private long generateTime = 0;
    private long timeLength = 0;

    /**
     * 检查邀请码是否失效
     *
     * @param invitationCode 老师所提供的邀请码
     * @param teacherId      教师Id
     * @return true: 邀请码有效
     * false: 邀请码失效
     */
    private boolean checkInvitationCode(String invitationCode, long teacherId) {
        if (invitationCode.equals(etrmsMapper.getTeacherById(teacherId).getT_invitation_code())) {
            generateTime = studentMapper.getInvitationCodeGenerateTime(invitationCode);
            timeLength = studentMapper.getInvitationCodeTimeLength(invitationCode);
            long time = (currentTime - generateTime) / 1000;
            if (time < timeLength) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过老师发送的邀请码与老师建立关联
     *
     * @param studentId      学生Id
     * @param invitationCode 老师所提供的邀请码
     * @param teacherId      教师Id
     * @return true: 绑定成功
     * false: 绑定失败
     */
    public boolean blindByInvitationCode(long studentId, String invitationCode, long teacherId) {
        if (checkInvitationCode(invitationCode, teacherId)) {
            if (studentMapper.blindByInvitationCode(studentId, teacherId, new Date(), 1) > 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public Teacher getTeacherByInvitationCode(String invitationCode) {
        return studentMapper.getTeacherByInvitationCode(invitationCode);
    }

    public List<Delivery> getPostedResume(long studentId, int state) {
        return studentMapper.getPostedResume(studentId, state);
    }

    public boolean modifyStudentInfo(Student student) {
        student.setU_age(ETRMSUtil.getAgeByBirth(student.getU_birth().toString()));
        return studentMapper.modifyStudentInfo(student) > 0;
    }

    public List<Teacher> getTeacherList(long studentId, int state) {
        return studentMapper.getTeacherList(studentId, state);
    }
}
