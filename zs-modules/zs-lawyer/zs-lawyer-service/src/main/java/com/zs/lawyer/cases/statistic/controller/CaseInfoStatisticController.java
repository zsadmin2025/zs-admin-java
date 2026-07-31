package com.zs.lawyer.cases.statistic.controller;

import com.zs.common.core.core.Result;
import com.zs.lawyer.cases.info.domain.vo.CaseHomeHearingVO;
import com.zs.lawyer.cases.info.domain.vo.CaseHomeVO;
import com.zs.lawyer.cases.statistic.domain.vo.CaseInfoNumVO;
import com.zs.lawyer.cases.statistic.service.CaseInfoStatisticService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lawyer/cases/info/statistic")
public class CaseInfoStatisticController {

    @Resource
    private CaseInfoStatisticService caseInfoStatisticService;

    /**
     * 获取案件信息统计
     * @return CaseInfoNumVO
     */
    @GetMapping("caseInfoNum")
    public Result<CaseInfoNumVO> getCaseInfoNum(){
        CaseInfoNumVO caseInfoNumVO = caseInfoStatisticService.getCaseInfoNum();
        return new Result<CaseInfoNumVO>().ok(caseInfoNumVO);
    }

    /**
     * 获取近三个月登记的案件
     * @return List<CaseVO>
     */
    @GetMapping("recentThreeMonthRegisteredCase")
    public Result<List<CaseHomeVO>> getRecentThreeMonthRegisteredCase(){
        List<CaseHomeVO> list = caseInfoStatisticService.getRecentThreeMonthRegisteredCase();
        return new Result<List<CaseHomeVO>>().ok(list);
    }

    /**
     * 获取近一个月待开庭的案件
     * @return List<CaseVO>
     */
    @GetMapping("recentOneMonthHearingCase")
    public Result<List<CaseHomeHearingVO>> getRecentOneMonthHearingCase(){
        List<CaseHomeHearingVO> list = caseInfoStatisticService.getRecentOneMonthHearingCase();
        return new Result<List<CaseHomeHearingVO>>().ok(list);
    }
}
