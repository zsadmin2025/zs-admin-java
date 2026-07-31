package com.zs.lawyer.cases.hearing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.lawyer.cases.hearing.domain.entity.CaseHearingEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 案件开庭表 Mapper 接口
 * </p>
 *
 * @author zs
 * @since 2025-06-08 17:58:57
 */
@Mapper
public interface CaseHearingMapper extends BaseMapper<CaseHearingEntity> {

}
