package me.dqq.leadmin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SysMenuDto {
    private Long id;
    private Long parentId;
    @NotBlank(message = "菜单标题不能为空")
    private String title;
    private String name;
    private String path;
    private String component;
    private String redirect;
    private String icon;
    private Integer menuType;
    private String permission;
    private Integer sort;
    private Integer visible;
    private Integer keepAlive;
    private String activeMenu;
    private Integer breadcrumb;
    private Integer showInTabs;
    private Integer affix;
    private Integer alwaysShow;
    private Integer status;
}
