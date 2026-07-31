package com.zs.lawyer.cases.info.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class CaseHomeHearingVO {

    @Schema(description = "案件ID")
    private Long caseInfoId;

    @Schema(description = "案件名称")
    private String caseName;

    @Schema(description = "诉讼地位")
    private String litigationStatus;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "开庭时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date courtTime;

    @Schema(description = "法院/仲裁委员会")
    private String court;




}
