package com.zs.common.core.excel.converter;


import cn.hutool.core.util.StrUtil;
import cn.idev.excel.converters.Converter;
import cn.idev.excel.converters.ReadConverterContext;
import cn.idev.excel.converters.WriteConverterContext;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.data.WriteCellData;
import jakarta.validation.constraints.NotNull;

/**
 * @author zsadmin
 */
public class GenderConverter implements Converter<Integer> {

    @NotNull
    @Override
    public Class<?> supportJavaTypeKey() {
        //对象属性类型
        return Integer.class;
    }

    @NotNull
    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        //CellData属性类型
        return CellDataTypeEnum.STRING;
    }


    @Override
    public Integer convertToJavaData(@NotNull ReadConverterContext<?> context) throws Exception {
        //CellData转对象属性
        String cellStr = context.getReadCellData().getStringValue();
        if (StrUtil.isEmpty(cellStr)) {
            return null;
        }
        if ("男".equals(cellStr)) {
            return 0;
        } else if ("女".equals(cellStr)) {
            return 1;
        } else {
            return null;
        }
    }

    @NotNull
    @Override
    public WriteCellData<?> convertToExcelData(@NotNull WriteConverterContext<Integer> context) throws Exception {
        //对象属性转CellData
        Integer cellValue = context.getValue();
        if (cellValue == null) {
            return new WriteCellData<>("");
        }
        if (cellValue == 0) {
            return new WriteCellData<>("男");
        } else if (cellValue == 1) {
            return new WriteCellData<>("女");
        } else {
            return new WriteCellData<>("");
        }
    }


}
