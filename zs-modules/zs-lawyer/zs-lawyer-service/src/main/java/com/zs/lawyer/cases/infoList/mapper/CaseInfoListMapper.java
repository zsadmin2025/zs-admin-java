package com.zs.lawyer.cases.infoList.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.lawyer.cases.infoList.domain.entity.CaseInfoListEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 案件结案目录表 Mapper 接口
 * </p>
 *
 * @author zs
 * @since 2025-06-21 12:20:27
 */
@Mapper
public interface CaseInfoListMapper extends BaseMapper<CaseInfoListEntity> {

    /**
     * 获取案件结案目录表列表
     *
     * @param caseInfoId 案件信息表ID
     * @param caseType   案件类型
     * @return List<CaseInfoListEntity>
     */
    List<CaseInfoListEntity> getList(@Param("caseInfoId") Long caseInfoId, @Param("caseType") String caseType);
}
