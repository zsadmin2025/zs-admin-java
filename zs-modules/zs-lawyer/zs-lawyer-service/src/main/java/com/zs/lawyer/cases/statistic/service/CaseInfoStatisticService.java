package com.zs.lawyer.cases.statistic.service;


import com.zs.lawyer.cases.info.domain.vo.CaseHomeHearingVO;
import com.zs.lawyer.cases.info.domain.vo.CaseHomeVO;
import com.zs.lawyer.cases.statistic.domain.vo.CaseInfoNumVO;

import java.util.List;

public interface CaseInfoStatisticService {

    /**
     *  案件数量统计
     * @return CaseInfoNumVO
     */
    CaseInfoNumVO getCaseInfoNum();

    /**
     * 最近三个月案件数量
     * @return List<CaseVO>
     */
    List<CaseHomeVO> getRecentThreeMonthRegisteredCase();

    /**
     * 最近一个月案件数量
     * @return List<CaseVO>
     */
    List<CaseHomeHearingVO> getRecentOneMonthHearingCase();
}
