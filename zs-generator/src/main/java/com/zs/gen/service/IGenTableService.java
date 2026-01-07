package com.zs.gen.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.gen.domain.entity.GenTable;
import com.zs.gen.domain.model.TreeNode;
import com.zs.gen.domain.params.GenTablePageQueryParams;
import com.zs.gen.domain.params.GenTableParams;
import com.zs.gen.domain.vo.GenTableVO;

import java.io.IOException;
import java.util.List;

/**
 * 业务 服务层
 * 
 * @author zs
 */
public interface IGenTableService extends IService<GenTable>{

    /** 分页 **/
    PageResult<GenTableVO> page(GenTablePageQueryParams genTablePageQueryParams);

    /** 数据库分页 **/
    PageResult<GenTableVO> dbPage(GenTablePageQueryParams genTablePageQueryParams);

    /** 修改 **/
    void update(GenTable genTable);

    /** 导入表结构（保存） **/
    void importTableSave(List<String> tables);

    /** 生成代码 **/
    void generateCode(GenTableParams genTableParams);



    /** 删除业务 **/
    void deleteGenTableByIds(Long[] tableIds);

    /** 预览代码 **/
    List<TreeNode> previewCode(Long tableId);

    /** 获取表详细信息 **/
    GenTableVO getGenTableById(Long tableId);

    byte[] generateCodeZip(Long tableId) throws IOException;
}