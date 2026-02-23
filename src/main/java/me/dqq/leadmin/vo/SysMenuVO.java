package me.dqq.leadmin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysMenuVO {
    private String id;
    private String parentId;
    private String path;
    private String component;
    private String redirect;
    private Integer type;
    private String title;
    private String icon;
    private Boolean keepAlive;
    private Boolean hidden;
    private Integer sort;
    private String activeMenu;
    private Boolean breadcrumb;
    private Integer status;
    private List<String> roles;
    private String permission;
    private Boolean showInTabs;
    private Boolean affix;
    private Boolean alwaysShow;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private List<SysMenuVO> children;
}
