package com.zs.sys.dept.domain.vo;

import com.zs.common.core.utils.TreeNode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zsadmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDeptUserVO extends TreeNode<SysDeptUserVO> implements Serializable {

    private Long id;
    private Long pid;
    private String name;
    @NotNull
    private List<SysDeptUserVO> children = new ArrayList<>();
}
