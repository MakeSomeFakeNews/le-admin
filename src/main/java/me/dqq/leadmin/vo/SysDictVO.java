package me.dqq.leadmin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysDictVO {
    private String id;
    private String createUserString;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private String name;
    private String code;
    private Integer sort;
    private Integer status;
    private String description;
    private List<SysDictDataVO> list;
}
