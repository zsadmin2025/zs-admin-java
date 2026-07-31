package com.zs.lawyer.cases.hearing.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 案件开庭表
 * </p>
 *
 * @author zs
 * @since 2025-06-08 17:58:57
 */
@Getter
@Setter
@Schema(description = "案件开庭信息ageQueryParams对象")
public class CaseHearingPageQueryParams  extends BasePageParams implements Serializable {


}
