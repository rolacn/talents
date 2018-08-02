package cn.inctech.app.talents.mapper;

import java.util.List;

import cn.inctech.app.talents.model.Delivery;
import cn.inctech.app.talents.model.Position;

public interface EnterpriseMapper {

    List<Delivery> unScreenedResume(String keyWord, int page, long companyId, int tabIndex);

    List<Delivery> invitedResume(String keyWord, int page, long companyId);

    List<Delivery> passedResume(String keyWord, int page, long companyId);

    List<Delivery> inappropriateResume(String keyWord, int page, long companyId);

    int postPosition(Position position);

    List<Position> getMyPosition(long companyId);

    List<Delivery> interviewedResume(String keyWord, int i, long companyId);
}
