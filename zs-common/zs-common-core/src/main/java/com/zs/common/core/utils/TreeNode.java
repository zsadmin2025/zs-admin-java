package com.zs.common.core.utils;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zsadmin
 */

@Data
public class TreeNode<T> implements Serializable {

    private Long id;
    private String name;
    private Long pid;

    private List<T> children = new ArrayList<>();

}
