package com.zs.sys.notice.domain.params;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zsadmin
 */
@Data
public class SysNoticeDetailsUpdateParams implements Serializable {
    private Long sysNoticeDetailsId;
    private Integer status;
    private String readTime;
}
