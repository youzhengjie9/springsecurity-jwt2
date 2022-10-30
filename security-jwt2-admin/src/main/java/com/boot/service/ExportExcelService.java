package com.boot.service;

import com.boot.entity.User;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 导出excel服务
 *
 * @author youzhengjie
 * @date 2022/10/22 11:18:19
 */
public interface ExportExcelService {


    /**
     * 导出所有用户
     *
     * @param response       响应
     */
    void exportAllUser(HttpServletResponse response);

    /**
     * 导出所有角色
     *
     * @param response       响应
     */
    void exportAllRole(HttpServletResponse response);

    /**
     * 导出所有菜单
     *
     * @param response       响应
     */
    void exportAllMenu(HttpServletResponse response);

    /**
     * 导出所有登录日志
     *
     * @param response 响应
     */
    void exportAllLoginLog(HttpServletResponse response);


    /**
     * 导出所有操作日志
     *
     * @param response 响应
     */
    void exportAllOperationLog(HttpServletResponse response);
}
