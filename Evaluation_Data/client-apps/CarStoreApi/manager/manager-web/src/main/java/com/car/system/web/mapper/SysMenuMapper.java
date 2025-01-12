package com.car.system.web.mapper;


import com.car.system.web.model.dto.SysMenuDto;
import com.car.system.web.model.SysMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Repository
public interface SysMenuMapper extends Mapper<SysMenu> {


    /**
     * 查询用户对应的父级菜单
     * @param userUuid      用户ID
     * @param menuTerminal  菜单所属终端
     * @return
     */
    List<SysMenu> queryParentMenuListByUser(@Param("userUuid") String userUuid, @Param("menuTerminal") String menuTerminal);

    /**
     * 查询当前菜单模块下的所有子菜单
     *
     *
     *
     * @param userUuid
     * @param menuTerminal
     * @param menuUuid
     * @return
     */
    List<SysMenu> querySonMenuListByUser(@Param("userUuid") String userUuid, @Param("menuTerminal") String menuTerminal, @Param("menuUuid") String menuUuid);

    /**
     * 查询所有的父节点
     * @param menuTerminal
     * @return
     */
    List<SysMenu> queryParentMenuList(@Param("menuTerminal") String menuTerminal);

    /**
     * 查询父节点下的所有子节点菜单
     * @param menuTerminal
     * @param menuUuid
     * @return
     */
    List<SysMenu> querySonMenuList(@Param("menuTerminal") String menuTerminal, @Param("menuUuid") String menuUuid);

    /**
     * 获取所有系统菜单
     * @param searchMenu
     * @return
     */
    List<SysMenu> queryAllMenuList(SysMenu searchMenu);

    /**
     *
     * @param menuIdList
     * @return
     */
    List<SysMenuDto> queryMenuListByUuidList(@Param("menuIdList") List<String> menuIdList);
}
