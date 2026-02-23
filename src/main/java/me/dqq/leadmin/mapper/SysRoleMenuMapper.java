package me.dqq.leadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.dqq.leadmin.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    @Select("SELECT menu_id FROM sys_role_menu WHERE role_id = #{roleId}")
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

    @Select("SELECT DISTINCT menu_id FROM sys_role_menu WHERE role_id IN (${roleIds})")
    List<Long> selectMenuIdsByRoleIds(@Param("roleIds") String roleIds);
}
