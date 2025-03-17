package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel("库存记录")
public class InventoryRecordDTO {
    @ApiModelProperty("记录ID")
    private Long id;
    
    @ApiModelProperty("库存ID")
    private Long inventoryId;
    
    @ApiModelProperty("操作类型(in:入库,out:出库)")
    private String operationType;
    
    @ApiModelProperty("操作数量")
    private Integer quantity;
    
    @ApiModelProperty("操作原因")
    private String reason;
    
    @ApiModelProperty("操作时间")
    private Date createdAt;
} 