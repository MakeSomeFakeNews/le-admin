package me.dqq.leadmin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class SysUserDto {
    private Long id;
    @NotBlank(message = "用户名不能为空")
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String email;
    private String phone;
    private Integer gender;
    private Long deptId;
    private Integer userType;
    private String description;
    private Integer status;
    private List<Long> roleIds;
}
