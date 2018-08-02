package cn.inctech.app.talents.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.inctech.app.common.util.ETRMSUtil;
import cn.inctech.app.talents.action.json.JSONResult;
import cn.inctech.app.talents.model.Delivery;
import cn.inctech.app.talents.model.Student;
import cn.inctech.app.talents.model.Teacher;
import cn.inctech.app.talents.service.ETRMSService;
import cn.inctech.app.talents.service.StudentService;

@Controller
public class StudentAction {

    /**
     * 获取导师列表
     *
     * @param studentId 学生Id
     * @param sessionId 用于判断登录状态的sessionId
     * @param state     状态(0,1,2)
     * @param page      页码
     * @return
     */
    @RequestMapping(value = "/getTeacherList.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getTeacherList(long studentId, String sessionId, int state, int page) {
        JSONObject result = new JSONObject();
        JSONObject jsonObject = null;
        if (etrmsService.loginState(TYPE, studentId, sessionId)) {
            List<Teacher> teachers = studentService.getTeacherList(studentId, state);
            if (teachers != null) {
                jsonObject = JSONObject.parseObject(JSONResult.GET_TEACHER_LIST_SUCCESS);
                result.put("teacher_count", teachers.size());
                result.put("teachers", JSONArray.parseArray(JSONObject.toJSONString(ETRMSUtil.splitResultByPage(teachers, page))));
            } else {
                return JSONResult.GET_TEACHER_LIST_FAILED;
            }
        } else {
            return JSONResult.INVALID_LOGIN;
        }
        result.putAll(jsonObject);
        return JSONObject.toJSONString(result);
    }

    /**
     * 通过邀请码将学生与老师绑定到一起
     *
     * @param studentId      学生Id
     * @param sessionId      用于判断登录状态的sessionId
     * @param invitationCode 邀请码
     * @return
     */
    @RequestMapping(value = "/blindByInvitationCode.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String blindByInvitationCode(long studentId, String sessionId, String invitationCode) {
        if (etrmsService.loginState(TYPE, studentId, sessionId)) {
            Teacher teacher = studentService.getTeacherByInvitationCode(invitationCode);
            if (teacher == null) {
                return JSONResult.INVALID_INVITATIONCODE;
            }
            if (studentService.blindByInvitationCode(studentId, invitationCode, teacher.getT_id())) {
                return JSONResult.BLIND_SUCCESS;
            } else {
                return JSONResult.BLIND_FAILED;
            }
        } else {
            return JSONResult.INVALID_LOGIN;
        }
    }

    /**
     * 修改学生个人信息
     *
     * @param student 学生信息
     * @return
     */
    @RequestMapping(value = "/modifyStudentInfo.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String modifyStudentInfo(Student student) {
        if (etrmsService.loginState(TYPE, student.getS_id(), student.getU_sessionId())) {
            logger.info("student: " + student.getS_id() + " " + student.getU_name());
            if (studentService.modifyStudentInfo(student)) {
                return JSONResult.MODIFY_SUCCESS;
            } else {
                return JSONResult.MODIFY_FAILED;
            }
        } else {
            return JSONResult.INVALID_LOGIN;
        }
    }


    /**
     * 学生在简历中心查看简历进度
     *
     * @param studentId 学生Id
     * @param sessionId 用于判断登录状态的sessionId
     * @param state     左边边栏(0,1,2)
     * @param page      页码
     * @return
     */
    @RequestMapping(value = "/getPostedResume.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getPostedResume(long studentId, String sessionId, int state, int page) {
        JSONObject result = new JSONObject();
        JSONObject jsonObject = null;
        if (etrmsService.loginState(TYPE, studentId, sessionId)) {
            List<Delivery> deliveryList = studentService.getPostedResume(studentId, state);
            if (deliveryList != null) {
                jsonObject = JSONObject.parseObject(JSONResult.GET_POSTED_RESUME_SUCCESS);
                result.put("resumes", JSONArray.parseArray(JSONObject.toJSONString(ETRMSUtil.splitResultByPage(deliveryList, page))));
                result.put("resume_count", deliveryList.size());
            } else {
                jsonObject = JSONObject.parseObject(JSONResult.GET_POSTED_RESUME_FAILED);
            }
        } else {
            return JSONResult.INVALID_LOGIN;
        }
        result.putAll(jsonObject);
        return JSONObject.toJSONString(result);
    }
    
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int TYPE = 0; //代表用户类型为学生

    @Autowired
    private ETRMSService etrmsService;

    @Autowired
    private StudentService studentService;

}
