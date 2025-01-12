package com.car.system.web.service.impl;


import com.car.common.enums.ResEnum;
import com.car.common.enums.StatusEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.ResultRes;
import com.car.common.utils.ListUtils;
import com.car.common.utils.TokenHelper;
import com.car.system.web.service.SysMenuService;
import com.car.system.web.service.SysUserService;
import com.car.system.client.response.menu.MenuListRes;
import com.car.system.client.response.menu.MenuTreeRes;
import com.car.system.web.common.constants.ConfigConsts;
import com.car.system.web.common.constants.AdminConstants;
import com.car.system.web.model.dto.SysMenuDto;
import com.car.system.web.model.SysMenu;
import com.car.system.web.model.SysRoleMenuRel;
import com.car.system.web.model.SysUser;
import com.car.system.web.mapper.SysMenuMapper;
import com.car.system.web.mapper.SysRoleMenuRelMapper;
import com.car.system.web.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    SysMenuMapper sysMenuMapper;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    ConfigConsts configConsts;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysRoleMenuRelMapper sysRoleMenuRelMapper;


    /**
     * 查询用户系统菜单
     * @param
     * @return
     */
    @Override
    public ResultRes<List<MenuTreeRes>> queryMenuListByUser() {
        SysUser user = sysUserService.getUserByName(TokenHelper.getUserName());
        if(StringUtils.isEmpty(user) || StsEnum.INVALID.getValue().equals(user.getSts())){
            log.error("通过用户ID：{}未匹配到用户信息",TokenHelper.getUserName());
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(),ResEnum.NON_EXISTENT.getDesc());
        }
        if(AdminConstants.DEFAULT_SUPPER_ACCESS_UUID.equals(user.getUuid())){
            //内置系统超级管理权限
            ResultRes<List<MenuTreeRes>> resultList = queryAllMenuList();
            return resultList;
        }
        List<MenuTreeRes> menuListRes = new ArrayList<>();
        //查询当前角色对应的菜单功能权限
        SysRoleMenuRel sysRoleMenuRel = new SysRoleMenuRel();
        sysRoleMenuRel.setRoleUuid(user.getRoleUuid());
        sysRoleMenuRel.setSts(StsEnum.ACTIVE.getValue());
        List<SysRoleMenuRel> roleMenuRels = sysRoleMenuRelMapper.select(sysRoleMenuRel);
        if(CollectionUtils.isEmpty(roleMenuRels)){
            //未匹配到对色菜单，直接输出空数据
            return ResultRes.success(menuListRes);
        }
        List<String> menuIdList = roleMenuRels.stream().map(SysRoleMenuRel::getMenuUuid).collect(Collectors.toList());
        List<SysMenuDto> menuDtoList = sysMenuMapper.queryMenuListByUuidList(menuIdList);
        //获取所有的父节点
        List<String> menuUuidList = new ArrayList<String>();
        for (SysMenuDto menu : menuDtoList) {
            if(!StringUtils.isEmpty(menu.getParentList())){
                String[] menus = menu.getParentList().split("/");
                menuUuidList.addAll(Arrays.asList(menus));
            }
        }
        //将集合数据去重复，且删除已查询出的节点
        menuUuidList = ListUtils.removeDuplicate(menuUuidList);
        List<String> removeList = new ArrayList<String>();
        for (String menuUuid : menuUuidList) {
            boolean isFlag = false;
            for (SysMenuDto sysMenuDto :menuDtoList) {
                if(menuUuid.equals(sysMenuDto.getUuid())){
                    isFlag = true;
                    break;
                }
            }
            if(isFlag){
                removeList.add(menuUuid);
            }
        }
        menuUuidList.removeAll(removeList);
        if(!CollectionUtils.isEmpty(menuUuidList)){
            List<SysMenuDto> menuParentList = sysMenuMapper.queryMenuListByUuidList(menuUuidList);
            menuDtoList.addAll(menuParentList);
        }
        List<SysMenu> menuList = new ArrayList<>();
        for (SysMenuDto dto : menuDtoList) {
            SysMenu menu = new SysMenu();
            BeanUtils.copyProperties(dto,menu);
            menuList.add(menu);
        }
        //获取树形数据
        menuListRes = getTreeMenuList(menuList);
        return ResultRes.success(menuListRes);
    }

    /**
     * 获取所有系统菜单
     * @return
     */
    @Override
    public ResultRes queryAllMenuList() {
        SysMenu searchMenu = new SysMenu();
        searchMenu.setStatus(StatusEnum.NORMAL.getValue());
        searchMenu.setSts(StsEnum.ACTIVE.getValue());
        List<SysMenu> menuList = sysMenuMapper.queryAllMenuList(searchMenu);
        return ResultRes.success(getTreeMenuList(menuList));
    }

    /**
     * 获取树形数据
     * @param menuList
     * @return
     */
    public List<MenuTreeRes> getTreeMenuList(List<SysMenu> menuList){
        List<MenuTreeRes> baseMenuList = new ArrayList<>();
        for (SysMenu menu: menuList) {
            MenuTreeRes menuTreeRes = new MenuTreeRes();
            BeanUtils.copyProperties(menu,menuTreeRes);
            baseMenuList.add(menuTreeRes);
        }
        //将数据递归封装
        MenuListRes menuListRes = new MenuListRes();
        return recursiveBuildingMenu(baseMenuList,AdminConstants.DEFAULT_MENU_ROOT_NODE);
    }

    /**
     * 数据递归计算
     * @param menuList
     * @return
     */
    private List<MenuTreeRes> recursiveBuildingMenu(List<MenuTreeRes> menuList, String parentNode) {
        List<MenuTreeRes> recursiveList = new ArrayList<MenuTreeRes>();
        for (MenuTreeRes menu : menuList) {
            if (menu.getParentId().equals(parentNode)) {
                List<MenuTreeRes> list = recursiveBuildingMenu(menuList, menu.getUuid());
                menu.setChildList(list);
                recursiveList.add(menu);
            }
        }
        return recursiveList;
    }
}
