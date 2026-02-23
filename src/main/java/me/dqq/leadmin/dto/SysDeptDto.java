package me.dqq.leadmin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SysDeptDto {
    private Long id;
    private Long parentId;
    @NotBlank(message = "部门名称不能为空")
    private String name;
    private Integer sort;
    private Integer status;
    private String description;
}
