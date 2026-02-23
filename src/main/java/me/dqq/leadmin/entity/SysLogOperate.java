package me.dqq.leadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_log_operate")
public class SysLogOperate {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String module;
    private String action;
    private String method;
    private String requestUrl;
    private String requestMethod;
    private String requestParams;
    private String responseResult;
    private Integer status;
    private String errorMsg;
    private Long operatorId;
    private String operatorName;
    private String ip;
    private String ipLocation;
    private Long duration;
    private LocalDateTime createTime;
}
