package cn.inctech.app.talents.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.inctech.app.common.util.ETRMSUtil;
import cn.inctech.app.talents.action.json.JSONResult;
import cn.inctech.app.talents.model.Student;
import cn.inctech.app.talents.model.Teacher;
import cn.inctech.app.talents.service.ETRMSService;
import cn.inctech.app.talents.service.TeacherService;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class TeacherAction {

    /**
     * 获取教师个人信息详情
     *
     * @param teacherId 教师Id
     * @param sessionId 用于判断登录状态的sessionId
     * @return
     */
    @RequestMapping(value = "getTeacherInfo.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getTeacherInfo(long teacherId, String sessionId) {
        JSONObject jsonObject = null;
        JSONObject result = new JSONObject();
        if (etrmsService.loginState(TYPE, teacherId, sessionId)) {
            Teacher teacher = null;
            if (etrmsService.getTeacherById(teacherId) != null) {
                teacher = etrmsService.getTeacherById(teacherId);
                jsonObject = JSONObject.parseObject(JSONResult.GET_INFO_SUCCESS);
                result.put("user_info", (JSONObject) JSONObject.toJSON(teacher));
            } else {
                jsonObject = JSONObject.parseObject(JSONResult.GET_INFO_FAILED);
            }
        } else {
            jsonObject = JSONObject.parseObject(JSONResult.INVALID_LOGIN);
        }
        result.putAll(jsonObject);
        return JSONObject.toJSONString(result);
    }

    /**
     * 教师资质认证
     *
     * @param teacherId     教师Id
     * @param teacherName   教师姓名
     * @param teacherSchool 教师学校
     * @param teacherNum    职工号
     * @param sessionId     用于判断登录状态的sessionId
     * @return
     */
    @RequestMapping(value = "modifyQualification.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String checkQualification(long teacherId, String teacherName, String teacherSchool, String teacherNum, String sessionId) {
        if (etrmsService.loginState(TYPE, teacherId, sessionId)) {
            if (teacherService.modifyTeacherQualification(teacherId, teacherName, teacherSchool, teacherNum)) {
                return JSONResult.MODIFY_QUALIFICATION_SUCCESS;
            }
            return JSONResult.MODIFY_QUALIFICATION_FAILED;
        } else {
            return JSONResult.INVALID_LOGIN;
        }
    }

    /**
     * 修改教师基本信息
     *
     * @param teacher 将前端提交的参数映射为一个teacher对象
     * @return
     */
    @RequestMapping(value = "modifyTeacherInfo.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String modifyTeacherInfo(Teacher teacher) {
        if (etrmsService.loginState(TYPE, teacher.getT_id(), teacher.getU_sessionId())) {
            boolean modifyTeacher_info = teacherService.modifyTeacherInfo(teacher);
            if (modifyTeacher_info) {
                return JSONResult.MODIFY_SUCCESS;
            } else {
                return JSONResult.MODIFY_FAILED;
            }
        } else {
            return JSONResult.INVALID_LOGIN;
        }
    }

    /**
     * 生成邀请码,并与教师本人绑定
     *
     * @param teacherId  教师Id
     * @param sessionId  用于判断登录状态的sessionId
     * @param timeLength 邀请码有效时长
     * @return
     */
    @RequestMapping(value = "generateInvitationCode.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String generateInvitationCode(long teacherId, String sessionId, long timeLength) {
        JSONObject result = new JSONObject();
        if (etrmsService.loginState(TYPE, teacherId, sessionId)) {
            long generateTime = System.currentTimeMillis();
            String invitationCode = ETRMSUtil.getRandomString(8);
            if (teacherService.generateInvitationCode(teacherId, invitationCode, generateTime, timeLength)) {
                result.putAll(JSONObject.parseObject(JSONResult.GENERATE_SUCCESS));
                result.put("invitationCode", invitationCode);
                result.put("generateTime", generateTime);
                result.put("timeLength", timeLength);
                return JSONObject.toJSONString(result);
            } else {
                return JSONResult.GENERATE_FAILED;
            }
        } else {
            return JSONResult.INVALID_LOGIN;
        }
    }

    /**
     * 获取学生列表
     *
     * @param sessionId 用于判断登录状态的sessionId
     * @param teacherId 教师Id
     * @param page      页码
     * @param state     状态
     * @return
     */
    @RequestMapping(value = "getStudentList.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getStudentList(String sessionId, long teacherId, int page, int state) {
        JSONObject result = new JSONObject();
        if (etrmsService.loginState(TYPE, teacherId, sessionId)) {
            List<Student> studentList = teacherService.getStudentList(teacherId, state, page);
            if (studentList != null) {
                result.putAll(JSONObject.parseObject(JSONResult.GET_STUDENT_LIST_SUCCESS));
                result.put("students", JSONArray.parseArray(JSONObject.toJSONString(studentList)));
                return JSONObject.toJSONString(result);
            } else {
                return JSONResult.GET_STUDENT_LIST_FAILED;
            }
        } else {
            return JSONResult.INVALID_LOGIN;
        }
    }

    /**
     * 评论学生
     *
     * @param sessionId 用于判断登录状态的sessionId
     * @param content   评论内容
     * @param teacherId 教师Id
     * @param studentId 学生Id
     */
    @RequestMapping(value = "addStudentComment.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String addStudentComment(long teacherId, long studentId, String sessionId, String content) {
        String result = "";
        if (etrmsService.loginState(TYPE, teacherId, sessionId)) {
            if (teacherService.addStudentComment(teacherId, studentId, content)) {
                result = JSONResult.COMMENT_STUDENT_SUCCESS;
            } else {
                result = JSONResult.COMMENT_STUDENT_FAILED;
            }
        } else {
            result = JSONResult.INVALID_LOGIN;
        }
        return JSONObject.toJSONString(JSONObject.parseObject(result));
    }
    
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int TYPE = 1; //代表用户类型为教师

    @Autowired
    private ETRMSService etrmsService;

    @Autowired
    private TeacherService teacherService;
    
}
