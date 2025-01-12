package com.car.system.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StatusEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.car.system.web.service.SysRoleService;
import com.car.system.client.request.role.AddRoleReq;
import com.car.system.client.request.role.QueryRoleListReq;
import com.car.system.client.request.role.UpdateRoleReq;
import com.car.system.client.response.role.QueryRoleListRes;
import com.car.system.client.response.role.RoleDetailRes;
import com.car.system.web.mapper.SysRoleMapper;
import com.car.system.web.mapper.SysRoleMenuRelMapper;
import com.car.system.web.model.SysRole;
import com.car.system.web.model.SysRoleMenuRel;
import com.car.system.web.model.dto.SysRoleDto;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xlj
 */
@Service
@Slf4j
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Autowired
    SysRoleMenuRelMapper sysRoleMenuRelMapper;

    /**
     * 新增or修改判断角色名称重复
     * @param roleName
     * @param uuid
     * @return
     */
    public SysRole queryRoleName(String roleName, String uuid) {
        SysRole role = sysRoleMapper.queryRoleByRoleName(roleName, uuid);
        return role;
    }

    /**
     * 检查角色是否存在
     * @param addRoleReq
     * @return
     */
    @Override
    public ResultRes checkRole(AddRoleReq addRoleReq) {
        //检查当前角色的功能权限跟数据权限是否已存在相同的角色
        Integer menuCount = CollectionUtils.isEmpty(addRoleReq.getMenuList())?0:addRoleReq.getMenuList().size();
        List<SysRole> roleList = sysRoleMapper.queryRepeatNumRole(menuCount);
        if(CollectionUtils.isEmpty(roleList)){
            //不存在重复的角色选项
            return ResultRes.success();
        }
        //存在数量一致的情况，进行判断数据是否相同
        List<String> roleNameList = new ArrayList<>();
        for (SysRole role : roleList) {
            boolean menuFlag = true;
            if(!CollectionUtils.isEmpty(addRoleReq.getMenuList())){
                //查询所有的功能权限
                SysRoleMenuRel searchMenu = new SysRoleMenuRel();
                searchMenu.setRoleUuid(role.getUuid());
                searchMenu.setSts(StsEnum.ACTIVE.getValue());
                List<SysRoleMenuRel> menuList = sysRoleMenuRelMapper.select(searchMenu);
                List<String> menuUuidList = menuList.stream().map(SysRoleMenuRel::getMenuUuid).collect(Collectors.toList());
                //删除数组中的数据，删除后还存在值，证明数据不相等
                List<String> tempList = new ArrayList<>();;
                tempList.addAll(addRoleReq.getMenuList());
                tempList.removeAll(menuUuidList);
                if(CollectionUtils.isEmpty(tempList)){
                    menuFlag = false;
                }
            }
            if(!menuFlag){
                roleNameList.add(role.getRoleName());
            }
        }
        //如果roleNameList为null，则标识没有重复的，否则存在重复的
        if(!CollectionUtils.isEmpty(roleNameList)){
            return ResultRes.error(ResEnum.AUTHORITY_EXIST_DATA.getValue(),ResEnum.AUTHORITY_EXIST_DATA.getDesc(), JSONArray.toJSONString(roleNameList));
        }
        return ResultRes.success();
    }


    /**
     * 新增角色信息
     * @param addRoleReq
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @LcnTransaction
    public ResultRes addRole(AddRoleReq addRoleReq) {
        SysRole sysRole = queryRoleName(addRoleReq.getRoleName(), "");
        if(!StringUtils.isEmpty(sysRole)){
            log.error("角色名称重复。入参{},出参{}",addRoleReq,sysRole);
            throw new BusinessException(ResEnum.ADD_UPDATE_ROLE_FAIL.getValue(),ResEnum.ADD_UPDATE_ROLE_FAIL.getDesc());
        }
        SysRole role = new SysRole();
        role.setRemarks(addRoleReq.getRemarks());
        role.setRoleName(addRoleReq.getRoleName());
        role.setStatus(StatusEnum.NORMAL.getValue());
        role.setSts(StsEnum.ACTIVE.getValue());
        role.setCreatedBy(TokenHelper.getUserName());
        role.setCreatedTime(new Date());
        role.setUuid(UuidUtils.getUuid());
        int insertRoleCount = sysRoleMapper.insert(role);
        if (insertRoleCount <= 0) {
            log.error("新增用户角色受影响行数为0，执行异常，请求参数为：{}", JSONArray.toJSONString(role));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR.getValue(), ResEnum.INSERT_DB_ERROR.getDesc());
        }
        //插入用户菜单数据权限
        insertRoleMenu(addRoleReq.getMenuList(),role.getUuid());
        //返回输出数据ID
        return ResultRes.success(role.getUuid());
    }



    /**
     * 修改角色信息
     *
     * @param updateRoleReq
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    @Override
    public ResultRes updateRole(UpdateRoleReq updateRoleReq) {
        SysRole sysRole = queryRoleName(updateRoleReq.getRoleName(), updateRoleReq.getUuid());
        if(!StringUtils.isEmpty(sysRole)){
            log.error("角色名称重复。入参{},出参{}",updateRoleReq,sysRole);
            throw new BusinessException(ResEnum.ADD_UPDATE_ROLE_FAIL.getValue(),ResEnum.ADD_UPDATE_ROLE_FAIL.getDesc());
        }
        SysRole role = sysRoleMapper.selectByPrimaryKey(updateRoleReq.getUuid());
        if (role == null) {
            log.error("通过角色ID：{}未匹配到对应角色信息，手动抛出异常", updateRoleReq.getUuid());
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(), ResEnum.NON_EXISTENT.getDesc());
        }
        role.setRemarks(updateRoleReq.getRemarks());
        //将角色名称转化成拼音大写
        role.setRoleCode(updateRoleReq.getRoleName());
        role.setRoleName(updateRoleReq.getRoleName());
        role.setLastUpdatedBy(TokenHelper.getUserName());
        role.setLastUpdatedTime(new Date());
        int updateRoleCount = sysRoleMapper.updateByPrimaryKeySelective(role);
        if (updateRoleCount <= 0) {
            log.error("编辑用户角色受影响行数为0，执行异常，请求参数为：{}", JSONArray.toJSONString(role));
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR.getValue(), ResEnum.UPDATE_DB_ERROR.getDesc());
        }
        //插入用户菜单数据权限
        insertRoleMenu(updateRoleReq.getMenuList(),role.getUuid());
        return ResultRes.success();
    }

    /**
     * 删除角色信息
     * @param uuid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public ResultRes deleteRole(String uuid) {
        SysRole role = sysRoleMapper.selectByPrimaryKey(uuid);
        if (StringUtils.isEmpty(role) || StsEnum.INVALID.getValue().equals(role.getSts())) {
            log.error("通过角色ID：{}未匹配到对应角色信息，手动抛出异常", uuid);
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(), ResEnum.NON_EXISTENT.getDesc());
        }
        //检查当前角色是否已被用户使用
        Integer userCount = sysRoleMapper.queryUserRoleCount(uuid);
        if (userCount > 0) {
            throw new BusinessException(ResEnum.ROLE_USER_EXIST.getValue(), ResEnum.ROLE_USER_EXIST.getDesc());
        }
        role.setSts(StsEnum.INVALID.getValue());
        role.setLastUpdatedTime(new Date());
        role.setLastUpdatedBy(TokenHelper.getUserName());
        int updateCount = sysRoleMapper.updateByPrimaryKey(role);
        if (updateCount <= 0) {
            log.error("删除用户角色受影响行数为0，执行异常，请求参数为：{}", JSONArray.toJSONString(role));
            throw new BusinessException(ResEnum.DELETE_DB_ERROR.getValue(), ResEnum.DELETE_DB_ERROR.getDesc());
        }
        return ResultRes.success();
    }

    /**
     * 查询角色详情
     *
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<RoleDetailRes> queryRoleDetail(String uuid) {
        SysRole role = sysRoleMapper.selectByPrimaryKey(uuid);
        if (role == null) {
            log.error("通过角色ID：{}未匹配到对应角色信息，手动抛出异常", uuid);
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(), ResEnum.NON_EXISTENT.getDesc());
        }
        RoleDetailRes res = new RoleDetailRes();
        BeanUtils.copyProperties(role, res);

        res.setMenuList(getMenuDetail(role.getUuid()));
        return ResultRes.success(res);
    }

    /**
     * 查询角色列表
     * @param param
     * @return
     */
    @Override
    public PageRes<List<QueryRoleListRes>> queryRoleList(QueryRoleListReq param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<SysRoleDto> roleList = sysRoleMapper.queryRoleList(param);
        PageInfo<SysRoleDto> pageInfo = new PageInfo<>(roleList);
        List<QueryRoleListRes> userResList = new ArrayList<QueryRoleListRes>();
        for (SysRoleDto role : roleList) {
            QueryRoleListRes res = new QueryRoleListRes();
            BeanUtils.copyProperties(role, res);
            userResList.add(res);
        }
        return PageRes.success(userResList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }


    /**
     * 获取角色权限菜单
     * @param roleUuid
     * @return
     */
    public List<String> getMenuDetail(String roleUuid) {
        //查询角色所有的菜单权限
        SysRoleMenuRel searchMenu = new SysRoleMenuRel();
        searchMenu.setRoleUuid(roleUuid);
        searchMenu.setSts(StsEnum.ACTIVE.getValue());
        List<SysRoleMenuRel> menuRelList = sysRoleMenuRelMapper.select(searchMenu);
        List<String> dataList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(menuRelList)){
            dataList = menuRelList.stream().map(SysRoleMenuRel::getMenuUuid).collect(Collectors.toList());
        }
        return dataList;
    }


    /**
     * 插入菜单角色信息
     * @param menuList
     * @param roleUuid
     */
    private void insertRoleMenu(List<String> menuList, String roleUuid) {
        //删除原有角色菜单数据信息
        SysRoleMenuRel delete = new SysRoleMenuRel();
        delete.setRoleUuid(roleUuid);
        sysRoleMenuRelMapper.delete(delete);

        //将数据插入到DB
        for (String menuId : menuList) {
            SysRoleMenuRel roleMenuRel = new SysRoleMenuRel();
            roleMenuRel.setUuid(UuidUtils.getUuid());
            roleMenuRel.setMenuUuid(menuId);
            roleMenuRel.setRoleUuid(roleUuid);
            roleMenuRel.setCreatedTime(new Date());
            roleMenuRel.setSts(StsEnum.ACTIVE.getValue());
            roleMenuRel.setCreatedBy(TokenHelper.getUserName());
            sysRoleMenuRelMapper.insert(roleMenuRel);
        }
    }


}
