package cn.inctech.app.talents.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.inctech.app.common.util.ETRMSUtil;
import cn.inctech.app.talents.mapper.ETRMSMapper;
import cn.inctech.app.talents.model.Account;
import cn.inctech.app.talents.model.Comment;
import cn.inctech.app.talents.model.Delivery;
import cn.inctech.app.talents.model.Enterprise;
import cn.inctech.app.talents.model.Position;
import cn.inctech.app.talents.model.Student;
import cn.inctech.app.talents.model.Teacher;
import cn.inctech.app.talents.model.User;

/**
 * 
 * @author Administrator
 * service class should not use fastjson and needs refactoring --18-08-02
 */
@Transactional
@Service
public class ETRMSService {

    @Resource
    ETRMSMapper etrmsMapper;


    /**
     * 将随机生成的sessionId与用户绑定
     *
     * @param type      用户类型
     * @param u_id      用户id
     * @param sessionId 随机生成的sessionId
     */
    public void saveSessionId(int type, long u_id, String sessionId) {
        etrmsMapper.saveSessionId(u_id, sessionId, type);
    }


    /**
     * 用于检验用户的登录状态
     *
     * @param type      用户类型
     * @param _id       用户id
     * @param sessionId 用于判断登录状态的sessionId
     * @return true: 当前登录有效
     * false: 当前登录失效
     */
    public boolean loginState(int type, long _id, String sessionId) {
        if (type == 2) {
            Enterprise enterprise = getEnterpriseById(_id);
            if (enterprise.getC_sessionId().equals(sessionId)) {
                return true;
            }
        } else if (type == 0 || type == 1) {
            User user = getUserById(_id);
            if (user.getU_sessionId().equals(sessionId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过用户u_id以及类型从不同数据库表中查询user所有信息
     *
     * @param u_id
     * @param type
     * @return 存有用户信息的json对象
     */
    public JSONObject getUser(long u_id, int type) {
        JSONObject jsonObject = null;
        if (type == 0) {
            Student student = getStudentById(u_id);
            jsonObject = (JSONObject) JSONObject.toJSON(student);
        } else if (type == 1) {
            Teacher teacher = getTeacherById(u_id);
            jsonObject = (JSONObject) JSONObject.toJSON(teacher);
        }
        return jsonObject;
    }

    /**
     * 用户注册
     *
     * @param a_id          用户Id
     * @param user_phone    用户输入的邮箱/手机
     * @param user_password 用户输入的密码
     * @param type          用户类型
     * @param phone
     * @return true: 注册成功
     * false: 注册失败
     */
    public boolean userRegister(long a_id, String user_phone, String user_password, int type, boolean phone) {
        if (etrmsMapper.userRegister(a_id, user_phone, user_password) > 0) {
            if (phone) {
                etrmsMapper.userRegister_info(a_id, type, 0, user_phone);
            } else {
                etrmsMapper.userRegister_info(a_id, type, 1, user_phone);
            }
            if (type == 0) {
                etrmsMapper.studentRegister_info(a_id);
            } else if (type == 1) {
                etrmsMapper.teacherRegister_info(a_id);
            }
            return true;
        }
        return false;
    }


    /**
     * 公司注册
     *
     * @param c_id              公司Id
     * @param companyEmail      公司邮箱
     * @param register_password 注册密码
     * @return true: 注册成功
     * false: 注册失败
     */
    public boolean companyRegister(long c_id, String companyEmail, String register_password) {
        return etrmsMapper.companyRegister(c_id, companyEmail, register_password) > 0;
    }

    /**
     * 添加收藏
     *
     * @param user_id 用户Id
     * @param job_id  职位Id
     */
    public void addCollection(long user_id, long job_id) {
        etrmsMapper.addCollection(user_id, job_id);
    }

    /**
     * 取消收藏
     *
     * @param user_id 用户Id
     * @param job_id  职位Id
     */
    public void deleteCollection(long user_id, long job_id) {
        etrmsMapper.deleteCollection(user_id, job_id);
    }

    public Account userAuthenticationByEmail(String user_email, String user_password) {
        return etrmsMapper.userAuthenticationByEmail(user_email, user_password);
    }

    public Account userAuthenticationByPhone(String user_phone, String user_password) {
        return etrmsMapper.userAuthenticationByPhone(user_phone, user_password);
    }

    public Enterprise enterpriseAuthentication(String company_email, String company_password) {
        return etrmsMapper.enterpriseAuthentication(company_email, company_password);
    }

    /**
     * 判断手机号是否已注册
     *
     * @param userPhone 用户手机号
     * @return true: 已经注册
     * false: 未注册(可使用)
     */
    public boolean isPhoneExist(String userPhone) {
        return etrmsMapper.isPhoneExist(userPhone).size() > 1;
    }

    public User getUserById(long _id) {
        return etrmsMapper.getUserById(_id);
    }

    public Student getStudentById(long u_id) {
        return etrmsMapper.getStudentById(u_id);
    }

    public Teacher getTeacherById(long u_id) {
        return etrmsMapper.getTeacherById(u_id);
    }

    public Enterprise getEnterpriseById(long _id) {
        return etrmsMapper.getEnterpriseById(_id);
    }

    /**
     * 注销
     *
     * @param type      用户类型
     * @param sessionId 用于判断登录状态的sessionId
     * @return true: 注销成功
     * false: 注销失败
     */
    public boolean logout(int type, String sessionId) {
        if (etrmsMapper.logout(sessionId, type) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取职位信息
     *
     * @param keyWord         搜索关键字
     * @param page            搜索页码
     * @param userId          用户Id
     * @param p_industry      行业分类
     * @param p_direction     方向分类
     * @param p_job           工作分类
     * @param p_work_location 工作地点
     * @param flag            获取的职位是否为用户收藏(0代表所有职位中获取,其他代表从收藏中获取)
     * @return
     */
    public List<Position> getPositionList(String keyWord, int page, long userId, String p_industry,
                                          String p_direction,
                                          String p_job,
                                          String p_work_location, int flag) {
        if (keyWord == null) {
            keyWord = "";
        }
        if (p_industry == null) {
            p_industry = "";
        }
        if (p_direction == null) {
            p_direction = "";
        }
        if (p_job == null) {
            p_job = "";
        }
        if (p_work_location == null) {
            p_work_location = "";
        }
        return etrmsMapper.getPositionList(keyWord, (page - 1) * 10, userId, p_industry, p_direction, p_job, p_work_location, flag);
    }

    public Position getPositionInfo(long positionId) {
        return etrmsMapper.getPositionById(positionId);
    }

    public JSONArray getStudentComments(long studentId) {
        List<Comment> comments = etrmsMapper.getStudentComment(studentId);
        if (comments != null) {
            return JSONArray.parseArray(JSONObject.toJSONString(comments));
        } else {
            return null;
        }
    }

    /**
     * 投递简历(包括学生直接投递简历给企业以及学生投递简历给教师)
     *
     * @param companyId  企业Id
     * @param studentId  学生Id
     * @param positionId 职位Id
     * @param teacherId  教师Id
     * @param filePath   附件简历地址
     * @param method     投递方式
     * @return
     */
    public boolean postResume(long[] companyId, long[] studentId, long[] positionId, long[] teacherId, String filePath, int method) {
        if (method == 0) {//判断是否是学生投递简历给企业
            for (int i = 0; i < positionId.length; i++) {
                Delivery delivery = new Delivery(companyId[i], positionId[i], studentId[0], 0, filePath, method);
                delivery.setD_state(1);
                etrmsMapper.postResume(delivery);

            }
            return true;
        } else if (method == 4) {//判断是否为学生投递简历给老师
            for (int i = 0; i < teacherId.length; i++) {
                Delivery delivery = new Delivery(0, 0, studentId[0], teacherId[i], filePath, method);
                delivery.setD_state(1);
                etrmsMapper.postResume(delivery);
            }
            return true;
        }
        return false;
    }

    /**
     * 老师将自己名下学生推荐给企业
     *
     * @param companyId  企业Id
     * @param positionId 职位Id
     * @param teacherId  教师Id
     * @param studentId  学生Id
     */
    public void updateResume(long[] companyId, long[] positionId, long[] teacherId, long[] studentId) {
        //获取当前教师Id名下p_id不存在的所有投递记录
//        List<Delivery> result = etrmsMapper.getPositionIdIsNull(teacherId[0]);
//        if (result.size() != 0) {
//            etrmsMapper.deletePositionIdIsNull(teacherId[0]);
//            for (int i = 0; i < studentId.length; i++) {
//                for (int j = 0; j < result.size(); j++) {
//                    if (result.get(j).getS_id() == studentId[i]) {
//                        for (int k = 0; k < positionId.length; k++) {
//                            result.get(j).setP_id(positionId[k]);
//                            result.get(j).setC_id(companyId[k]);
//                            result.get(j).setD_date(ETRMSUtil.generateFormattedDate());
//                            long d_id = ETRMSUtil.generateUID("D");
//                            result.get(j).setD_id(d_id);
//                            result.get(j).setD_method(1);
//                            result.get(j).setD_state(1);
//                            etrmsMapper.postResume(result.get(j));
//                        }
//                    }
//                }
//            }
//        } else {
        for (int i = 0; i < studentId.length; i++) {
            for (int k = 0; k < positionId.length; k++) {
                Delivery delivery = new Delivery();
                delivery.setP_id(positionId[k]);
                delivery.setC_id(companyId[k]);
                delivery.setD_date(ETRMSUtil.generateFormattedDate());
                long d_id = ETRMSUtil.generateUID("D");
                delivery.setD_id(d_id);
                delivery.setD_method(1);
                delivery.setT_id(teacherId[0]);
                delivery.setS_id(studentId[i]);
                delivery.setD_state(1);
                etrmsMapper.postResume(delivery);
            }
        }
    }

    /**
     * 简历操作
     *
     * @param id        简历Id
     * @param optResult 操作结果
     * @return
     */
    public boolean resumeOpt(long id, int optResult) {
        return etrmsMapper.resumeOpt(id, optResult) > 0;
    }

    public boolean applyOpt(long studentId, long teacherId, int optResult) {
        return etrmsMapper.applyOpt(studentId,teacherId,optResult) > 0;
    }
}
