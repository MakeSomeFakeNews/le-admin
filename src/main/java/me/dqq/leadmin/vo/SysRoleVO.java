package me.dqq.leadmin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysRoleVO {
    private String id;
    private String createUserString;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Boolean disabled;
    private String name;
    private String code;
    private Integer sort;
    private Integer status;
    private Integer type;
    private String description;
}
