package com.zs.sys.dict.domain.vo;

import com.zs.common.core.utils.TreeNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * @author zsadmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictDataVO extends TreeNode<SysDictDataVO> implements Serializable {


    private Long sysDictDataId;
    private String dictType;
    private Long sysDictTypeId;
    private Long pid;
    private String dictLabel;
    private String dictValue;
    private Integer sort;
    private Integer status;

    @SuppressWarnings("unused")
    public Long getSysDictDataId() {
        return sysDictDataId;
    }

    @SuppressWarnings("unused")
    public void setSysDictDataId(Long sysDictDataId) {
        this.sysDictDataId = sysDictDataId;
        this.setId(sysDictDataId);
    }
}
