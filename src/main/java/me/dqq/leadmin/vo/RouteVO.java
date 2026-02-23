package me.dqq.leadmin.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteVO {
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
    private List<RouteVO> children;
}
