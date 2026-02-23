package me.dqq.leadmin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SysDictDataDto {
    private Long id;
    private Long dictId;
    private String dictCode;
    @NotBlank(message = "字典项名称不能为空")
    private String name;
    @NotBlank(message = "字典项值不能为空")
    private String value;
    private String color;
    private Integer sort;
    private Integer status;
}
