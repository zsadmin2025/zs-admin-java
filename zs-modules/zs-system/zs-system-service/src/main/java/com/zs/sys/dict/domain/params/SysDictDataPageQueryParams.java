package com.zs.sys.dict.domain.params;

import com.zs.common.core.page.BasePageParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zsadmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictDataPageQueryParams extends BasePageParams {


    private Long sysDictTypeId;
    private String dictType;
    private String dictLabel;
    private String dictValue;
    private Integer sort;
    private Integer status;
}
