package com.zs.lawyer.cases.statistic.service.impl;

import com.zs.common.core.enums.CaseStatusEnum;
import com.zs.lawyer.cases.info.domain.params.CaseInfoSelectQueryParams;
import com.zs.lawyer.cases.info.domain.vo.CaseHomeHearingVO;
import com.zs.lawyer.cases.info.domain.vo.CaseHomeVO;
import com.zs.lawyer.cases.info.domain.vo.CaseInfoVO;
import com.zs.lawyer.cases.info.service.CaseInfoService;
import com.zs.lawyer.cases.statistic.domain.vo.CaseInfoNumVO;
import com.zs.lawyer.cases.statistic.service.CaseInfoStatisticService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CaseInfoStatisticServiceImpl implements CaseInfoStatisticService {

    @Resource
    private CaseInfoService caseInfoService;

    @Override
    public CaseInfoNumVO getCaseInfoNum() {
        CaseInfoSelectQueryParams caseInfoSelectQueryParams = new CaseInfoSelectQueryParams();

        List<CaseInfoVO> caseInfoVOList = caseInfoService.getList(caseInfoSelectQueryParams);

        Integer total = caseInfoVOList.size();

        int followTotal = 0;
        int closedTotal = 0;
        int filingTotal = 0;

        for (CaseInfoVO vo : caseInfoVOList) {
            Integer status = vo.getCaseStatus();
            if (Objects.equals(status, CaseStatusEnum.PROGRESS.getCode())) {
                followTotal++;
            } else if (Objects.equals(status, CaseStatusEnum.CLOSED.getCode())) {
                closedTotal++;
            } else if (Objects.equals(status, CaseStatusEnum.FILING.getCode())) {
                filingTotal++;
            }
        }

        CaseInfoNumVO caseInfoNumVO = new CaseInfoNumVO();
        caseInfoNumVO.setTotal(total);
        caseInfoNumVO.setDoingTotal(followTotal);
        caseInfoNumVO.setClosedTotal(closedTotal);
        caseInfoNumVO.setFilingTotal(filingTotal);

        return caseInfoNumVO;
    }

    @Override
    public List<CaseHomeVO> getRecentThreeMonthRegisteredCase() {
        return caseInfoService.getRecentThreeMonthRegisteredCase();
    }

    @Override
    public List<CaseHomeHearingVO> getRecentOneMonthHearingCase() {
        return caseInfoService.getRecentOneMonthHearingCase();
    }
}
