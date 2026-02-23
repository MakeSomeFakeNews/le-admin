package me.dqq.leadmin.vo;

import lombok.Data;
import java.util.List;

@Data
public class MenuOptionVO {
    private String id;
    private String title;
    private List<MenuOptionVO> children;
}
