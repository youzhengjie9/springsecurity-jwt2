package com.boot.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import com.boot.entity.*;
import com.boot.service.*;
import com.boot.utils.EasyExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 导出excel服务impl
 *
 * @author youzhengjie
 * @date 2022/10/22 11:42:51
 */
@Service
@Slf4j
public class ExportExcelServiceImpl implements ExportExcelService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private EasyExcelUtil easyExcelUtil;

    private static final String EXPORT_USER_PREFIX="用户表";

    private static final String EXPORT_ROLE_PREFIX="角色表";

    private static final String EXPORT_MENU_PREFIX="菜单表";

    private static final String EXPORT_LOGIN_LOG_PREFIX="登录日志表";

    private static final String EXPORT_OPER_LOG_PREFIX="操作日志表";

    /**
     * 导出所有用户
     *
     * @param response       响应
     */
    @Override
    public void exportAllUser(HttpServletResponse response) {

        //查询出数据，后面这些数据会生成excel表
        List<User> userList = userService.lambdaQuery()
                .select(User::getId,
                        User::getUserName,
                        User::getNickName,
                        User::getStatus,
                        User::getEmail,
                        User::getPhone,
                        User::getSex,
                        User::getCreateTime,
                        User::getUpdateTime,
                        User::getDelFlag)
                .list();

        easyExcelUtil.writeExcel(userList,User.class,EXPORT_USER_PREFIX,response);
    }

    /**
     * 导出所有角色
     *
     * @param response       响应
     */
    @Override
    public void exportAllRole(HttpServletResponse response) {

        List<Role> roleList = roleService.lambdaQuery()
                .select(Role::getId,
                        Role::getName,
                        Role::getRoleKey,
                        Role::getStatus,
                        Role::getDelFlag,
                        Role::getCreateTime,
                        Role::getUpdateTime,
                        Role::getRemark)
                .list();

        easyExcelUtil.writeExcel(roleList, Role.class,EXPORT_ROLE_PREFIX,response);
    }

    @Override
    public void exportAllMenu(HttpServletResponse response) {

        List<Menu> menuList = menuService.lambdaQuery()
                .select(Menu::getId,
                        Menu::getParentId,
                        Menu::getMenuName,
                        Menu::getPath,
                        Menu::getComponent,
                        Menu::getStatus,
                        Menu::getVisible,
                        Menu::getPerms,
                        Menu::getType,
                        Menu::getCreateTime,
                        Menu::getUpdateTime,
                        Menu::getDelFlag,
                        Menu::getSort,
                        Menu::getRemark)
                .orderByAsc(Menu::getSort)
                .list();

        easyExcelUtil.writeExcel(menuList, Menu.class,EXPORT_MENU_PREFIX,response);
    }

    @Override
    public void exportAllLoginLog(HttpServletResponse response) {

        List<LoginLog> loginLogList = loginLogService.lambdaQuery()
                .select(LoginLog::getId,
                        LoginLog::getUsername,
                        LoginLog::getIp,
                        LoginLog::getAddress,
                        LoginLog::getBrowser,
                        LoginLog::getOs,
                        LoginLog::getLoginTime,
                        LoginLog::getDelFlag)
                .orderByDesc(LoginLog::getLoginTime)
                .list();

        easyExcelUtil.writeExcel(loginLogList, LoginLog.class,EXPORT_LOGIN_LOG_PREFIX,response);
    }

    @Override
    public void exportAllOperationLog(HttpServletResponse response) {

        List<OperationLog> operationLogList = operationLogService.lambdaQuery()
                .select(OperationLog::getId,
                        OperationLog::getUsername,
                        OperationLog::getType,
                        OperationLog::getUri,
                        OperationLog::getTime,
                        OperationLog::getIp,
                        OperationLog::getAddress,
                        OperationLog::getBrowser,
                        OperationLog::getOs,
                        OperationLog::getOperTime,
                        OperationLog::getDelFlag)
                .orderByDesc(OperationLog::getOperTime)
                .list();
        easyExcelUtil.writeExcel(operationLogList, OperationLog.class,EXPORT_OPER_LOG_PREFIX,response);
    }
}
