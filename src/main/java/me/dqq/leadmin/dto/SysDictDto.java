package me.dqq.leadmin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SysDictDto {
    private Long id;
    @NotBlank(message = "字典名称不能为空")
    private String name;
    @NotBlank(message = "字典编码不能为空")
    private String code;
    private Integer sort;
    private Integer status;
    private String description;
}
