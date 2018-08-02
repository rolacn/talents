package cn.inctech.app.talents.action.json;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.inctech.app.common.util.ETRMSUtil;
import cn.inctech.app.talents.model.Position;

public class JSONResult {

    public static final String PHONE_EXIST = "{\"isPhoneExist\":true,\"msg\":\"手机号已存在,请重试\"}";
    public static final String PHONE_AVALIABLE = "{\"isPhoneExist\":false,\"msg\":\"手机号可以使用\"}";
    public static final String LOGIN_SUCCESS = "{\"isSuccess\":true,\"msg\":\"登录成功\"}";
    public static final String LOGIN_FAILED = "{\"isSuccess\":false,\"msg\":\"用户名或密码错误\"}";
    public static final String LOGOUT_SUCCESS = "{\"isSuccess\":true,\"msg\":\"注销成功\"}";
    public static final String LOGOUT_FAILED = "{\"isSuccess\":false,\"msg\":\"注销失败\"}";
    public static final String INVALID_LOGIN = "{\"isSuccess\":false,\"msg\":\"登录信息失效,请重新登录\"}";
    public static final String GET_INFO_SUCCESS = "{\"isSuccess\":true,\"msg\":\"教师信息获取成功\"}";
    public static final String GET_INFO_FAILED = "{\"isSuccess\":false,\"msg\":\"教师信息获取失败\"}";
    public static final String MODIFY_QUALIFICATION_SUCCESS = "{\"isSuccess\":true,\"msg\":\"资质认证信息提交成功,请等待管理员审核\"}";
    public static final String MODIFY_QUALIFICATION_FAILED = "{\"isSuccess\":false,\"msg\":\"资质认证信息提交失败,请重试\"}";
    public static final String MODIFY_SUCCESS = "{\"isSuccess\":true,\"msg\":\"信息修改成功\"}";
    public static final String MODIFY_FAILED = "{\"isSuccess\":false,\"msg\":\"信息修改失败,请重试\"}";
    public static final String GENERATE_SUCCESS = "{\"isSuccess\":true,\"msg\":\"成功生成邀请码\"}";
    public static final String GENERATE_FAILED = "{\"isSuccess\":false,\"msg\":\"生成邀请码失败,请重试\"}";
    public static final String BLIND_SUCCESS = "{\"isSuccess\":true,\"msg\":\"通过邀请码绑定成功\"}";
    public static final String BLIND_FAILED = "{\"isSuccess\":false,\"msg\":\"通过邀请码绑定失败,请检查邀请码\"}";
    public static final String REGISTER_TRUE = "{\"isSuccess\":true,\"msg\":\"注册成功\"}";
    public static final String REGISTER_FAILED = "{\"isSuccess\":true,\"msg\":\"注册失败\"}";
    public static final String REGISTER_FAILED_PHONE = "{\"isSuccess\":false,\"msg\":\"请输入正确的手机号\"}";
    public static final String REGISTER_FAILED_EMAIL = "{\"isSuccess\":false,\"msg\":\"请输入正确的邮箱\"}";
    public static final String COLLECTION_TRUE = "{\"isSuccess\":true,\"msg\":\"收藏成功\"}";
    public static final String DECOLLECTION_TRUE = "{\"isSuccess\":true,\"msg\":\"取消收藏成功\"}";
    public static final String GET_STUDENT_LIST_SUCCESS = "{\"isSuccess\":true,\"msg\":\"获取学生列表成功\"}";
    public static final String GET_STUDENT_LIST_FAILED = "{\"isSuccess\":false,\"msg\":\"获取学生列表失败\"}";
    public static final String GET_STUDENT_INFO_SUCCESS = "{\"isSuccess\":true,\"msg\":\"获取学生简历成功\"}";
    public static final String GET_STUDENT_INFO_FAILED = "{\"isSuccess\":false,\"msg\":\"获取学生简历失败\"}";
    public static final String RESUME_OPT_SUCCESS = "{\"isSuccess\":true,\"msg\":\"简历操作成功\"}";
    public static final String RESUME_OPT_FAILED = "{\"isSuccess\":false,\"msg\":\"简历操作失败\"}";
    public static final String RESUME_SEARCH_SUCCESS = "{\"isSuccess\":true,\"msg\":\"简历查询成功\"}";
    public static final String RESUME_SEARCH_FAILED = "{\"isSuccess\":false,\"msg\":\"简历查询失败\"}";
    public static final String GET_POSITION_INFO_SUCCESS = "{\"isSuccess\":true,\"msg\":\"获取职位详情成功\"}";
    public static final String GET_POSITION_INFO_FAILED = "{\"isSuccess\":false,\"msg\":\"获取职位详情失败\"}";
    public static final String COMMENT_STUDENT_SUCCESS = "{\"isSuccess\":true,\"msg\":\"评论学生成功\"}";
    public static final String COMMENT_STUDENT_FAILED = "{\"isSuccess\":false,\"msg\":\"评论学生失败\"}";
    public static final String POST_RESUME_SUCCESS = "{\"isSuccess\":true,\"msg\":\"投递简历成功\"}";
    public static final String POST_RESUME_FAILED = "{\"isSuccess\":false,\"msg\":\"投递简历失败\"}";
    public static final String POSITION_POST_SUCCESS = "{\"isSuccess\":true,\"msg\":\"职位发布成功\"}";
    public static final String POSITION_POST_FAILED = "{\"isSuccess\":false,\"msg\":\"职位发布失败\"}";
    public static final String GET_MY_POSITION_SUCCESS = "{\"isSuccess\":true,\"msg\":\"获取已发布职位成功\"}";
    public static final String GET_MY_POSITION_FAILED = "{\"isSuccess\":false,\"msg\":\"获取已发布职位失败\"}";
    public static final String GET_POSTED_RESUME_SUCCESS = "{\"isSuccess\":true,\"msg\":\"获取已投递简历成功\"}";
    public static final String GET_POSTED_RESUME_FAILED = "{\"isSuccess\":true,\"msg\":\"获取已投递简历失败\"}";
    public static final String GET_TEACHER_LIST_SUCCESS = "{\"isSuccess\":true,\"msg\":\"获取导师列表成功\"}";
    public static final String GET_TEACHER_LIST_FAILED = "{\"isSuccess\":false,\"msg\":\"获取导师列表失败\"}";
    public static final String APPLY_OPT_SUCCESS = "{\"isSuccess\":true,\"msg\":\"招募学生成功\"}";
    public static final String APPLY_OPT_FAILED = "{\"isSuccess\":false,\"msg\":\"已拒绝该学生\"}";
    public static final String INVALID_INVITATIONCODE = "{\"isSuccess\":false,\"msg\":\"邀请码无效\"}";
    private static final String GET_ALL_POSITION_SUCCESS = "{\"isSuccess\":true,\"msg\":\"获取所有职位信息成功\"}";
    private static final String GET_ALL_POSITION_FAILED = "{\"isSuccess\":false,\"msg\":\"获取所有职位信息失败\"}";
    private static final String GET_COLLECTED_POSITION_SUCCESS = "{\"isSuccess\":true,\"msg\":\"获取收藏职位信息成功\"}";
    private static final String GET_COLLECTED_POSITION_FAILED = "{\"isSuccess\":false,\"msg\":\"获取收藏职位信息失败\"}";

    /**
     * 得到获取职位信息结果
     *
     * @param flag      获取的职位是否为用户收藏(0代表所有职位中获取,其他代表从收藏中获取)
     * @param positions 从数据库获取的结果集
     * @param page
     * @return 获取职位信息结果
     */
    public static String GET_POSITION_RESULT(int flag, List<Position> positions, int page) {
        JSONObject jsonObject = null;
        JSONObject result = new JSONObject();
        if (flag == 0) {
            if (positions != null) {
                jsonObject = JSONObject.parseObject(GET_ALL_POSITION_SUCCESS);
                result.put("jobs", JSONArray.parseArray(JSONObject.toJSONString(ETRMSUtil.splitResultByPage(positions, page))));
                result.put("count", positions.size());
            } else {
                jsonObject = JSONObject.parseObject(GET_ALL_POSITION_FAILED);
            }
            result.putAll(jsonObject);
        } else {
            if (positions != null) {
                jsonObject = JSONObject.parseObject(GET_COLLECTED_POSITION_SUCCESS);
                result.put("count", positions.size());
                result.put("jobs", JSONArray.parseArray(JSONObject.toJSONString(ETRMSUtil.splitResultByPage(positions, page))));
            } else {
                jsonObject = JSONObject.parseObject(GET_COLLECTED_POSITION_FAILED);
            }
            result.putAll(jsonObject);
        }
        return JSONObject.toJSONString(result);
    }

}
