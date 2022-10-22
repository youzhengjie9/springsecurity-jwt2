package com.boot.controller;

import com.boot.service.ExportExcelService;
import com.boot.utils.EasyExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 导出excel控制器
 *
 * @author youzhengjie
 * @date 2022/10/22 10:53:35
 */
@RestController
@Api("导出Excel控制器")
@RequestMapping(path = "/export/excel")
public class ExportExcelController {

    @Autowired
    private ExportExcelService exportExcelService;


    @GetMapping("/exportAllUser")
    @ApiOperation(value = "导出所有用户")
    public void exportAllUser(HttpServletResponse response){

        exportExcelService.exportAllUser(response);
    }


    @GetMapping("/exportAllRole")
    @ApiOperation(value = "导出所有角色")
    public void exportAllRole(HttpServletResponse response){

        exportExcelService.exportAllRole(response);
    }

    @GetMapping("/exportAllMenu")
    @ApiOperation(value = "导出所有菜单")
    public void exportAllMenu(HttpServletResponse response){

        exportExcelService.exportAllMenu(response);
    }


}
