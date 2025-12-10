package com.zs.common.mp.service;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface MpSysDeptService {

    Set<Long> getDeptAndChildrenDeptIds(Long deptId);
}
