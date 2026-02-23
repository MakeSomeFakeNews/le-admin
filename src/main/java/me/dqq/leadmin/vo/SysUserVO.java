package me.dqq.leadmin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysUserVO {
    private String id;
    private String createUserString;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Boolean disabled;
    private String username;
    private String nickname;
    private Integer gender;
    private String avatar;
    private String email;
    private String phone;
    private Integer status;
    private Integer type;
    private String description;
    private List<String> roleIds;
    private String roleNames;
    private String deptId;
    private String deptName;
    private List<String> permissions;
}
