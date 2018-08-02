package cn.inctech.app.talents.mapper;

import java.util.List;

import cn.inctech.app.talents.model.Account;
import cn.inctech.app.talents.model.Comment;
import cn.inctech.app.talents.model.Delivery;
import cn.inctech.app.talents.model.Enterprise;
import cn.inctech.app.talents.model.Position;
import cn.inctech.app.talents.model.Student;
import cn.inctech.app.talents.model.Teacher;
import cn.inctech.app.talents.model.User;

public interface ETRMSMapper {

    Account userAuthenticationByEmail(String user_email, String user_password);

    Account userAuthenticationByPhone(String user_phone, String user_password);

    Enterprise enterpriseAuthentication(String company_email, String company_password);

    Student getStudentById(long u_id);

    Teacher getTeacherById(long u_id);

    List<Account> isPhoneExist(String userPhone);

    Enterprise getEnterpriseById(long _id);

    User getUserById(long _id);

    void addCollection(long user_id, long job_id);

    void deleteCollection(long user_id, long job_id);

    int companyRegister(long c_id, String c_email, String c_password);

    int userRegister(long a_id, String user_phone, String user_password);

    void userRegister_info(long u_id, int u_remark, int isPhone, String user_phone);

    void studentRegister_info(long s_id);

    void teacherRegister_info(long t_id);

    //收藏搜索
    List<Position> getPositionList(String keyWord, int page, long userId, String p_industry,
                                   String p_direction,
                                   String p_job,
                                   String p_work_location, int flag);

    Teacher getTeacherByInvitation(String t_invitation_code);

    //获取学生评论
    List<Comment> getStudentComment(long studentId);

    Position getPositionById(long positionId);

    int saveSessionId(long u_id, String sessionId, int type);

    int logout(String sessionId, int type);

    //投递简历
    int postResume(Delivery delivery);

    //找到positionid等于空的
    List<Delivery> getPositionIdIsNull(long teacherId);

    //删除positionid等于空的信息
    void deletePositionIdIsNull(long teacherId);

    int resumeOpt(long id, int optResult);

    int applyOpt(long studentId, long teacherId, int optResult);
}
