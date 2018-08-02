package cn.inctech.app.talents.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.inctech.app.common.util.ETRMSUtil;
import cn.inctech.app.common.util.ValidateCode;
import cn.inctech.app.talents.action.json.JSONResult;
import cn.inctech.app.talents.model.Account;
import cn.inctech.app.talents.model.Enterprise;
import cn.inctech.app.talents.model.Position;
import cn.inctech.app.talents.model.Student;
import cn.inctech.app.talents.service.ETRMSService;

@Controller
public class ETRMSAction {

    /**
     * 生成图片验证码并返回给前端界面
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return null
     * @throws Exception
     */
    @RequestMapping("/validateCode.do")
    public void validateCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.setDateHeader("Expires", 0);
        ValidateCode vCode = new ValidateCode(120, 30, 4, 100);
        logger.info("生成验证码: " + vCode.getCode());
        validateCode = vCode.getCode();
        response.setHeader("code", vCode.getCode());
        vCode.write(response.getOutputStream());
        return;
    }

    /**
     * 获取生成好的图形验证码
     *
     * @return
     */
    @RequestMapping("/getVcode.do")
    @ResponseBody
    public String getVcode() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", validateCode);
        return JSONObject.toJSONString(result);
    }

    /**
     * 检验手机号是否注册
     *
     * @param userPhone 手机号
     * @return
     */
    @RequestMapping(value = "/isPhoneExist.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String isPhoneExist(String userPhone) {
        JSONObject result = new JSONObject();
        result.put("user_phone", userPhone);
        if (etrmsService.isPhoneExist(userPhone)) {
            result.putAll(JSONObject.parseObject(JSONResult.PHONE_EXIST));
        } else {
            result.putAll(JSONObject.parseObject(JSONResult.PHONE_AVALIABLE));
        }
        return JSONObject.toJSONString(result);
    }

    /**
     * 用户注册操作(学生 / 教师 / 企业)
     *
     * @param userPhone 用户名
     * @param password  密码
     * @param type      类型
     * @return String
     */
    @RequestMapping(value = "/register.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String register(String userPhone, String password, int type) {
        JSONObject result = new JSONObject();
        JSONObject jsonObject = null;
        if (etrmsService.isPhoneExist(userPhone)) {
            return JSONResult.PHONE_AVALIABLE;
        }
        if (type == 2) {
            if (ETRMSUtil.isEmail(userPhone)) {
                long c_id = ETRMSUtil.generateUID("C");
                if (etrmsService.companyRegister(c_id, userPhone, password)) {
                    jsonObject = JSONObject.parseObject(JSONResult.REGISTER_TRUE);
                    result.put("user_info", etrmsService.getEnterpriseById(c_id));
                } else {
                    jsonObject = JSONObject.parseObject(JSONResult.REGISTER_FAILED);
                }
            } else {
                jsonObject = JSONObject.parseObject(JSONResult.REGISTER_FAILED_EMAIL);
            }
        } else {
            if (ETRMSUtil.isPhone(userPhone)) {
                long a_id = 0;
                if (type == 0) {
                    a_id = ETRMSUtil.generateUID("S");
                } else if (type == 1) {
                    a_id = ETRMSUtil.generateUID("T");
                }
                if (etrmsService.userRegister(a_id, userPhone, password, type, ETRMSUtil.isPhone(userPhone))) {
                    jsonObject = JSONObject.parseObject(JSONResult.REGISTER_TRUE);
                    if (type == 0) {
                        result.put("user_info", etrmsService.getStudentById(a_id));
                    } else if (type == 1) {
                        result.put("user_info", etrmsService.getTeacherById(a_id));
                    }
                } else {
                    jsonObject = JSONObject.parseObject(JSONResult.REGISTER_FAILED);
                }
            } else {
                jsonObject = JSONObject.parseObject(JSONResult.REGISTER_FAILED_PHONE);
            }
        }
        result.putAll(jsonObject);
        return JSONObject.toJSONString(result);
    }

    /**
     * 用户登录操作(学生/教师/企业)
     *
     * @param userPhone 用户登录所用账号(邮箱/手机) 企业只能用企业邮箱登录
     * @param password  用户账户密码
     * @param type      用户类型
     * @return
     */
    @RequestMapping(value = "/login.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String login(String userPhone, String password, int type) {
        JSONObject jsonObject = null;
        String sessionId = "";
        JSONObject result = new JSONObject();
        if (type == 2) {
            if (ETRMSUtil.isEmail(userPhone)) {
                if (etrmsService.enterpriseAuthentication(userPhone, password) != null) {
                    sessionId = ETRMSUtil.getRandomString(LENGTH);
                    Enterprise enterprise = etrmsService.enterpriseAuthentication(userPhone, password);
                    etrmsService.saveSessionId(type, enterprise.getC_id(), sessionId);
                    enterprise.setC_sessionId(sessionId);
                    jsonObject = JSONObject.parseObject(JSONResult.LOGIN_SUCCESS);
                    result.put("user_info", (JSONObject) JSONObject.toJSON(enterprise));
                } else {
                    jsonObject = JSONObject.parseObject(JSONResult.LOGIN_FAILED);
                }
            } else {
                jsonObject = JSONObject.parseObject(JSONResult.LOGIN_FAILED);
            }
        } else {
            if (ETRMSUtil.isPhone(userPhone)) {
                if (etrmsService.userAuthenticationByPhone(userPhone, password) != null) {
                    sessionId = ETRMSUtil.getRandomString(LENGTH);
                    Account account = etrmsService.userAuthenticationByPhone(userPhone, password);
                    jsonObject = JSONObject.parseObject(JSONResult.LOGIN_SUCCESS);
                    etrmsService.saveSessionId(type, account.getA_id(), sessionId);
                    result.put("user_info", etrmsService.getUser(account.getA_id(), type));
                } else {
                    jsonObject = JSONObject.parseObject(JSONResult.LOGIN_FAILED);
                }
            } else if (ETRMSUtil.isEmail(userPhone)) {
                if (etrmsService.userAuthenticationByEmail(userPhone, password) != null) {
                    sessionId = ETRMSUtil.getRandomString(LENGTH);
                    Account account = etrmsService.userAuthenticationByEmail(userPhone, password);
                    jsonObject = JSONObject.parseObject(JSONResult.LOGIN_SUCCESS);
                    etrmsService.saveSessionId(type, account.getA_id(), sessionId);
                    result.put("user_info", etrmsService.getUser(account.getA_id(), type));
                } else {
                    jsonObject = JSONObject.parseObject(JSONResult.LOGIN_FAILED);
                }
            } else {
                jsonObject = JSONObject.parseObject(JSONResult.LOGIN_FAILED);
            }
        }
        result.putAll(jsonObject);
        return JSONObject.toJSONString(result);
    }

    /**
     * 注销当前登录的账户
     *
     * @param type      用户类型
     * @param sessionId 用于判断登录状态的sessionId
     * @return
     */
    @RequestMapping(value = "/logout.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String logout(int type, String sessionId) {
        if (etrmsService.logout(type, sessionId)) {
            return JSONResult.LOGOUT_SUCCESS;
        }
        return JSONResult.LOGOUT_FAILED;
    }

    /**
     * 添加收藏
     *
     * @param type      用户类型
     * @param userId    用户Id
     * @param jobId     职位Id
     * @param sessionId 用于判断登录状态的sessionId
     * @return
     */
    @RequestMapping(value = "/addCollection.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String addCollection(int type, long userId, long jobId, String sessionId) {
        String result = "";
        JSONObject jsonObject = null;
        if (etrmsService.loginState(type, userId, sessionId)) {
            etrmsService.addCollection(userId, jobId);
            jsonObject = JSONObject.parseObject(JSONResult.COLLECTION_TRUE);
        } else {
            jsonObject = JSONObject.parseObject(JSONResult.INVALID_LOGIN);
        }
        result = JSONObject.toJSONString(jsonObject);
        return result;
    }

    /**
     * 删除收藏
     *
     * @param type      用户类型
     * @param userId    用户Id
     * @param jobId     职位Id
     * @param sessionId 用于判断登录状态的sessionId
     * @return String
     */
    @RequestMapping(value = "/deleteCollection.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String deleteCollection(int type, long userId, long jobId, String sessionId) {
        String result = "";
        JSONObject jsonObject = null;
        if (etrmsService.loginState(type, userId, sessionId)) {
            etrmsService.deleteCollection(userId, jobId);
            jsonObject = JSONObject.parseObject(JSONResult.DECOLLECTION_TRUE);
        } else {
            jsonObject = JSONObject.parseObject(JSONResult.INVALID_LOGIN);

        }
        result = JSONObject.toJSONString(jsonObject);
        return result;
    }

    /**
     * 获取职位信息
     *
     * @param type            用户类型
     * @param userId          用户Id
     * @param sessionId       用于判断登录状态的sessionId
     * @param page            搜索页码
     * @param keyWord         搜索关键字
     * @param flag            获取的职位是否为用户收藏(0代表所有职位中获取,其他代表从收藏中获取)
     * @param p_industry      行业分类
     * @param p_direction     方向分类
     * @param p_job           工作分类
     * @param p_work_location 工作地点
     * @return
     */
    @RequestMapping(value = "/getPositionList.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getPositionList(String type, long userId, String sessionId, int page, String keyWord, int flag, String p_industry,
                                  String p_direction,
                                  String p_job,
                                  String p_work_location) {
        if (!etrmsService.loginState(Integer.parseInt(type), userId, sessionId)) {
            return JSONResult.INVALID_LOGIN;
        }
        List<Position> positions = etrmsService.getPositionList(keyWord, page, userId, p_industry, p_direction, p_job, p_work_location, flag);
        return JSONResult.GET_POSITION_RESULT(flag, positions, page);
    }

    /**
     * 获取学生简历信息
     *
     * @param type      用户类型
     * @param userId    用户Id
     * @param sessionId 用于判断登录状态的sessionId
     * @param studentId 要查看的学生Id
     * @return
     */
    @RequestMapping(value = "/getStudentInfo.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getStudentInfo(int type, long userId, String sessionId, long studentId) {
        JSONObject result = new JSONObject();
        JSONObject jsonObject = null;
        if (etrmsService.loginState(type, userId, sessionId)) {
            Student student = etrmsService.getStudentById(studentId);
            if (student != null) {
                jsonObject = JSONObject.parseObject(JSONResult.GET_STUDENT_INFO_SUCCESS);
                result.put("student_info", student);
                result.put("comments_content", etrmsService.getStudentComments(studentId));
            } else {
                jsonObject = JSONObject.parseObject(JSONResult.GET_STUDENT_INFO_FAILED);
            }
        } else {
            return JSONResult.INVALID_LOGIN;
        }
        result.putAll(jsonObject);
        return JSONObject.toJSONString(result);
    }

    /**
     * 获取职位详情
     *
     * @param type       用户类型
     * @param userId     用户Id
     * @param sessionId  用于判断登录状态的sessionId
     * @param positionId 职位Id
     * @return
     */
    @RequestMapping(value = "/getPositionInfo.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getPositionInfo(int type, long userId, String sessionId, long positionId) {
        JSONObject result = new JSONObject();
        JSONObject jsonObject = null;
        if (etrmsService.loginState(type, userId, sessionId)) {
            Position position = etrmsService.getPositionInfo(positionId);
            if (position != null) {
                jsonObject = JSONObject.parseObject(JSONResult.GET_POSITION_INFO_SUCCESS);
                result.put("position_info", position);
            } else {
                jsonObject = JSONObject.parseObject(JSONResult.GET_POSITION_INFO_FAILED);
            }
        } else {
            return JSONResult.INVALID_LOGIN;
        }
        result.putAll(jsonObject);
        return JSONObject.toJSONString(result);
    }

    /**
     * 投递简历(通用)
     *
     * @param request HttpServletRequest
     * @return
     */
    @RequestMapping(value = "/postResume.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String postResume(HttpServletRequest request) {
        int type = Integer.parseInt(request.getParameter("type"));
        long userId = Long.parseLong(request.getParameter("userId"));
        String sessionId = request.getParameter("sessionId");
        long[] companyId = ETRMSUtil.stringArrToLongArr(request.getParameterMap().get("companyId[]"));
        long[] studentId = ETRMSUtil.stringArrToLongArr(request.getParameterMap().get("studentId[]"));
        long[] positionId = ETRMSUtil.stringArrToLongArr(request.getParameterMap().get("positionId[]"));
        long[] teacherId = ETRMSUtil.stringArrToLongArr(request.getParameterMap().get("teacherId[]"));
        String file = request.getParameter("file");
        String result = "";
        int method = 0;//0: 学生直接投递至企业  1: 教师推荐学生至企业  4: 学生将简历投递至教师
        if (etrmsService.loginState(type, userId, sessionId)) {
            if (type == 0) {//当前登录用户为学生
                if (teacherId == null && studentId != null && companyId != null) {//学生直接投递简历给企业
                    teacherId = new long[0];
                } else if (companyId == null && positionId == null && teacherId != null) {//学生将简历投递给老师
                    studentId = new long[]{userId};
                    companyId = new long[0];
                    positionId = new long[0];
                    method = 4;
                }
            } else if (type == 1) {//当前登录用户为教师
                if (studentId != null && teacherId != null && companyId != null && positionId != null) {
                    etrmsService.updateResume(companyId, positionId, teacherId, studentId);//教师将学生推荐给企业
                    result = JSONResult.POST_RESUME_SUCCESS;
                    return JSONObject.toJSONString(JSONObject.parseObject(result));
                }
            }
            if (etrmsService.postResume(companyId, studentId, positionId, teacherId, file, method)) {
                result = JSONResult.POST_RESUME_SUCCESS;
            } else {
                result = JSONResult.POST_RESUME_FAILED;
            }
        } else {
            result = JSONResult.INVALID_LOGIN;
        }
        return JSONObject.toJSONString(JSONObject.parseObject(result));
    }

    /**
     * 修改简历状态
     *
     * @param userId     用户Id
     * @param sessionId  用于判断登录状态的sessionId
     * @param deliveryId 简历Id
     * @param optResult  要修改的状态
     * @return
     */
    @RequestMapping(value = "/resumeOpt.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String resumeOpt(long userId, String sessionId, long deliveryId, int optResult, int type) {
        if (etrmsService.loginState(type, userId, sessionId)) {
            if (etrmsService.resumeOpt(deliveryId, optResult)) {
                return JSONResult.RESUME_OPT_SUCCESS;
            } else {
                return JSONResult.RESUME_OPT_FAILED;
            }
        } else {
            return JSONResult.INVALID_LOGIN;
        }
    }

    /**
     * 修改学生与教师间的关系
     *
     * @param type      用户类型
     * @param userId    当前登录的用户Id
     * @param studentId 学生Id
     * @param teacherId 教师Id
     * @param sessionId 用于判断登录状态的sessionId
     * @param optResult 操作结果(0-> 解除关系;1-> 刚刚申请;2-> 绑定)
     * @return
     */
    @RequestMapping(value = "/applyOpt.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String applyOpt(int type, long userId, long studentId, long teacherId, String sessionId, int optResult) {
        if (etrmsService.loginState(type, teacherId, sessionId)) {
            if (etrmsService.applyOpt(studentId, teacherId, optResult)) {
                return JSONResult.APPLY_OPT_SUCCESS;
            } else {
                return JSONResult.APPLY_OPT_FAILED;
            }
        } else {
            return JSONResult.INVALID_LOGIN;
        }
    }
    
    private static String validateCode = "";

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    static final int LENGTH = 32; //生成的sessionId的长度

    @Autowired
    private ETRMSService etrmsService;
    
}