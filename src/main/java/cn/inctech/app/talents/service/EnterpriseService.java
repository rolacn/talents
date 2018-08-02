package cn.inctech.app.talents.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.inctech.app.common.util.ETRMSUtil;
import cn.inctech.app.talents.mapper.EnterpriseMapper;
import cn.inctech.app.talents.model.Delivery;
import cn.inctech.app.talents.model.Position;

@Transactional
@Service
public class EnterpriseService {

    @Resource
    EnterpriseMapper enterpriseMapper;

    /**
     * 所有简历
     *
     * @param state     简历状态(左边边栏)
     * @param tabIndex  选项卡(未筛选简历中的4个tab)用字符串是因为有为null的情况,int会报错,在使用的时候进行类型转换
     * @param keyWord   搜索的关键字
     * @param page      页码
     * @param companyId 企业Id
     * @return
     */
    public List<Delivery> resumeManage(int state, String tabIndex, String keyWord, int page, long companyId) {
        List<Delivery> deliveries = null;
        if (keyWord == null) {
            //当用户不进行搜索的话,keyWord为null
            keyWord = "";
        }
        if (tabIndex == null) {
            //当左边边栏不是未筛选简历的话,tabIndex为null
            tabIndex = "";
        }
        if (state == 1) {
            //未筛选简历
            deliveries = enterpriseMapper.unScreenedResume(keyWord, (page - 1) * 10, companyId, Integer.parseInt(tabIndex));
        } else if (state == 2) {
            //已邀请面试的简历
            deliveries = enterpriseMapper.invitedResume(keyWord, (page - 1) * 10, companyId);
        } else if (state == 3) {
            deliveries = enterpriseMapper.interviewedResume(keyWord, (page - 1) * 10, companyId);
        } else if (state == 4) {
            //已通过的简历
            deliveries = enterpriseMapper.passedResume(keyWord, (page - 1) * 10, companyId);
        } else if (state == 999) {
            //不合适的简历
            deliveries = enterpriseMapper.inappropriateResume(keyWord, (page - 1) * 10, companyId);
        }
        System.out.println(deliveries);
        return deliveries;
    }

    /**
     * 发布职位
     *
     * @param companyId 企业Id
     * @param position  职位对象
     * @return
     */
    public boolean postPosition(long companyId, Position position) {
        Position p = position;
        long p_id = ETRMSUtil.generateUID("P");
        p.setP_id(p_id);
        p.setC_id(companyId);
        p.setP_create(ETRMSUtil.generateFormattedDate());
        return enterpriseMapper.postPosition(p) > 0;
    }

    public List<Position> getMyPosition(long companyId) {
        return enterpriseMapper.getMyPosition(companyId);
    }
}
