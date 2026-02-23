package me.dqq.leadmin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysDictDataVO {
    private String id;
    private String name;
    private String value;
    private Integer status;
    private String color;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
