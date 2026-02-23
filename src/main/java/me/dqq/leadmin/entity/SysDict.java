package me.dqq.leadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_dict")
public class SysDict extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String code;
    private Integer sort;
    private Integer status;
    private String description;
}
