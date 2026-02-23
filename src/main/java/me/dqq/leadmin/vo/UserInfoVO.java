package me.dqq.leadmin.vo;

import lombok.Data;
import java.util.List;

@Data
public class UserInfoVO {
    private Long id;
    private String nickname;
    private String avatar;
    private List<String> roles;
    private List<String> permissions;
}
