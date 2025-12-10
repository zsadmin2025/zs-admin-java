package com.zs.common.core.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zsadmin
 */
@Data
public class SysDictDataDTO implements Serializable {

    private Long sysDictDataId;
    private Long sysDictTypeId;
    private String dictType;
    private String dictLabel;
    private String dictValue;
    private Integer sort;
    private Integer status;
}
