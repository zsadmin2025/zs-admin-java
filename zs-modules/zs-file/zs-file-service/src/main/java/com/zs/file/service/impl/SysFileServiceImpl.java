package com.zs.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.file.domain.entity.SysFileEntity;
import com.zs.file.mapper.SysFileMapper;
import com.zs.file.service.ISysFileService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFileEntity> implements ISysFileService {
}
