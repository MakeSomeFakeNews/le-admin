package me.dqq.leadmin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class SysRoleDto {
    private Long id;
    @NotBlank(message = "角色名称不能为空")
    private String roleName;
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;
    private Integer sort;
    private Integer status;
    private String remark;
    private List<Long> menuIds;
}
