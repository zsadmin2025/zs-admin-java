package com.zs.lawyer.cases.info.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.lawyer.cases.info.domain.entity.CaseInfoEntity;
import com.zs.lawyer.cases.info.domain.vo.CaseHomeHearingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 案件信息表 Mapper 接口
 * </p>
 *
 * @author zs
 * @since 2025-06-08 16:43:20
 */
@Mapper
public interface CaseInfoMapper extends BaseMapper<CaseInfoEntity> {

    String findMaxCodeByPrefix(String prefix);

    IPage<CaseInfoEntity> approvePage(Page<CaseInfoEntity> page,  @Param("params") Map<String, Object> params);

    List<CaseInfoEntity> getRecentThreeMonthRegisteredCase();

    List<CaseHomeHearingVO> getRecentOneMonthHearingCase();
}
