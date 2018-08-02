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
import cn.inctech.app.talents.model.Delivery;
import cn.inctech.app.talents.model.Position;
import cn.inctech.app.talents.service.ETRMSService;
import cn.inctech.app.talents.service.EnterpriseService;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class EnterpriseAction {

    /**
     * 获取企业所有收到的简历
     *
     * @param companyId 企业Id
     * @param sessionId 用于判断登录状态的sessionId
     * @param keyWord   搜索的关键字
     * @param page      页码
     * @param state     状态(左边边栏)
     * @param tabIndex  选项卡(未筛选简历中的4个tab)
     * @return
     */
    @RequestMapping(value = "resumeManage.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String resumeManage(long companyId, String sessionId, String keyWord, int page, int state, String tabIndex) {
        JSONObject result = new JSONObject();
        JSONObject jsonObject = null;
        if (etrmsService.loginState(TYPE, companyId, sessionId)) {
            List<Delivery> deliveryList = enterpriseService.resumeManage(state, tabIndex, keyWord, page, companyId);
            if (deliveryList != null) {
                jsonObject = JSONObject.parseObject(JSONResult.RESUME_SEARCH_SUCCESS);
                result.put("resume_info", JSONArray.parseArray(JSONObject.toJSONString(deliveryList)));
                result.put("resume_counts", deliveryList.size());
            } else {
                jsonObject = JSONObject.parseObject(JSONResult.RESUME_SEARCH_FAILED);
            }
        } else {
            return JSONResult.INVALID_LOGIN;
        }
        result.putAll(jsonObject);
        return JSONObject.toJSONString(result);
    }


    /**
     * 发布职位信息
     *
     * @param companyId 企业Id
     * @param sessionId 用于判断登录状态的sessionId
     * @param position  职位信息
     * @return
     */
    @RequestMapping(value = "postPosition.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String postPosition(long companyId, String sessionId, Position position) {
        if (etrmsService.loginState(TYPE, companyId, sessionId)) {
            if (enterpriseService.postPosition(companyId, position)) {
                return JSONResult.POSITION_POST_SUCCESS;
            } else {
                return JSONResult.POSITION_POST_FAILED;
            }
        } else {
            return JSONResult.INVALID_LOGIN;
        }
    }

    /**
     * 获取企业发布的岗位
     *
     * @param companyId 企业Id
     * @param sessionId 用于判断登录状态的sessionId
     * @param page      页码
     * @return
     */
    @RequestMapping(value = "getMyPosition.do", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getMyPosition(long companyId, String sessionId, int page) {
        JSONObject result = new JSONObject();
        JSONObject jsonObject = null;
        if (etrmsService.loginState(TYPE, companyId, sessionId)) {
            List<Position> positions = enterpriseService.getMyPosition(companyId);
            if (positions != null) {
                jsonObject = JSONObject.parseObject(JSONResult.GET_MY_POSITION_SUCCESS);
                result.put("count", positions.size());
                result.put("jobs", JSONArray.parseArray(JSONObject.toJSONString(ETRMSUtil.splitResultByPage(positions, page))));
            } else {
                jsonObject = JSONObject.parseObject(JSONResult.GET_MY_POSITION_FAILED);
            }
        } else {
            return JSONResult.INVALID_LOGIN;
        }
        result.putAll(jsonObject);
        return JSONObject.toJSONString(result);
    }
    
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int TYPE = 2; //代表用户类型为企业

    @Autowired
    private ETRMSService etrmsService;

    @Autowired
    private EnterpriseService enterpriseService;
}
