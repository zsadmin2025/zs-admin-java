package com.zs.gen.constants;

/**
 * 代码生成通用常量
 *
 */
public class GenConstants {


    /** 数据库字符串类型 */
    public static final String[] COLUMN_TYPE_STR = { "char", "varchar", "nvarchar", "varchar2" };

    /** 数据库文本类型 */
    public static final String[] COLUMN_TYPE_TEXT = { "tinytext", "text", "mediumtext", "longtext" };

    /** 数据库时间类型 */
    public static final String[] COLUMN_TYPE_TIME = { "datetime", "time", "date", "timestamp" };

    /** 数据库数字类型 */
    public static final String[] COLUMN_TYPE_NUMBER = { "tinyint", "smallint", "mediumint", "int", "number", "integer",
            "bit", "bigint", "float", "double", "decimal" };
    
    /** 数据库字符串类型（所有数据库） */
    public static final String[] COLUMN_TYPE_ALL_STR = { "varchar", "char", "text", "mediumtext", "longtext",
            "varchar2", "nvarchar2", "clob", "nclob", // Oracle/达梦
            "nvarchar", "nchar", "text", "ntext", // SQL Server
            "character varying", "character", "text" // PostgreSQL
    };
    
    /** 数据库长文本类型（所有数据库） */
    public static final String[] COLUMN_TYPE_ALL_LONG_TEXT = { "text", "mediumtext", "longtext",
            "clob", "nclob", // Oracle/达梦
            "text", "ntext", // SQL Server
            "text" // PostgreSQL
    };
    
    /** 数据库整数类型（所有数据库） */
    public static final String[] COLUMN_TYPE_ALL_INTEGER = { "int", "tinyint", "smallint", "mediumint", // MySQL
            "number", "integer", // Oracle/达梦
            "int", "smallint", "tinyint", // SQL Server
            "integer", "smallint", "int4" // PostgreSQL
    };
    
    /** 数据库长整数类型（所有数据库） */
    public static final String[] COLUMN_TYPE_ALL_LONG = { "bigint", // MySQL
            "number", // Oracle/达梦 (当长度大于10时)
            "bigint", // SQL Server
            "int8" // PostgreSQL
    };
    
    /** 数据库浮点类型（所有数据库） */
    public static final String[] COLUMN_TYPE_ALL_DECIMAL = { "decimal", "numeric", "double", "float", // MySQL
            "number", "float", "double precision", // Oracle/达梦
            "decimal", "numeric", "float", "real", "double", // SQL Server
            "decimal", "numeric", "float", "real", "double precision" // PostgreSQL
    };
    
    /** 数据库日期时间类型（所有数据库） */
    public static final String[] COLUMN_TYPE_ALL_DATETIME = { "date", "datetime", "timestamp", "time", // MySQL
            "date", "timestamp", "timestamp with time zone", "time", // Oracle/达梦
            "date", "datetime", "smalldatetime", "datetime2", "datetimeoffset", "time", "timestamp", // SQL Server
            "date", "timestamp", "timestamp with time zone", "time", "time with time zone" // PostgreSQL
    };
    
    /** 数据库布尔类型（所有数据库） */
    public static final String[] COLUMN_TYPE_ALL_BOOLEAN = { "boolean", "tinyint(1)", // MySQL
            "boolean", "number(1)", // Oracle/达梦
            "bit", "boolean", // SQL Server
            "boolean" // PostgreSQL
    };
    
    /** 页面不需要新增字段 */
    public static final String[] COLUMN_NAME_NOT_ADD = { "creator", "create_time", "updater", "update_time", "creator_dept", "tenant_id" };

    /** 页面不需要编辑字段 */
    public static final String[] COLUMN_NAME_NOT_EDIT = { "creator", "create_time", "updater", "update_time", "creator_dept", "tenant_id" };

    /** 页面不需要显示的列表字段 */
    public static final String[] COLUMN_NAME_NOT_LIST = { "creator", "create_time", "updater", "update_time", "creator_dept", "tenant_id" };

    /** 页面不需要查询字段 */
    public static final String[] COLUMN_NAME_NOT_QUERY = { "creator", "create_time", "updater", "update_time", "creator_dept", "tenant_id" };


    /** 文本框 */
    public static final String HTML_INPUT = "input";

    /** 文本域 */
    public static final String HTML_TEXTAREA = "textarea";

    /** 下拉框 */
    public static final String HTML_SELECT = "select";

    /** 单选框 */
    public static final String HTML_RADIO = "radio";

    /** 复选框 */
    public static final String HTML_CHECKBOX = "checkbox";

    /** 日期控件 */
    public static final String HTML_DATETIME = "datetime";

    /** 图片上传控件 */
    public static final String HTML_IMAGE_UPLOAD = "imageUpload";

    /** 文件上传控件 */
    public static final String HTML_FILE_UPLOAD = "fileUpload";

    /** 富文本控件 */
    public static final String HTML_EDITOR = "editor";

    /** 字符串类型 */
    public static final String TYPE_STRING = "String";

    /** 整型 */
    public static final String TYPE_INTEGER = "Integer";

    /** 长整型 */
    public static final String TYPE_LONG = "Long";

    /** 浮点型 */
    public static final String TYPE_DOUBLE = "Double";

    /** 高精度计算类型 */
    public static final String TYPE_BIG_DECIMAL = "BigDecimal";

    /** 时间类型 */
    public static final String TYPE_DATE = "Date";
    
    /** 布尔类型 */
    public static final String TYPE_BOOLEAN = "Boolean";

    /** 模糊查询 */
    public static final String QUERY_LIKE = "LIKE";

    /** 相等查询 */
    public static final String QUERY_EQ = "EQ";

    /** 需要 */
    public static final String REQUIRE = "1";

    /** 主键 */
    public static final String IS_PK = "1";
}
