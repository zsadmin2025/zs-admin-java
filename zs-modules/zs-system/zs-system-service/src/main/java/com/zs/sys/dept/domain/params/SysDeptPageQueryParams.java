package com.zs.sys.dept.domain.params;

import com.zs.common.core.page.BasePageParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author zsadmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDeptPageQueryParams extends BasePageParams implements Serializable {

    private String deptName;
    private Long sysDeptId;
}
