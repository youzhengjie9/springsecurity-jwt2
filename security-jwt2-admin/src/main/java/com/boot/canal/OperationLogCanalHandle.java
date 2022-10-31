package com.boot.canal;

import cn.hutool.core.bean.BeanUtil;
import com.boot.entity.OperationLog;
import com.boot.entity.OperationLogCanal;
import com.boot.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

/**
 * 操作日志canal处理器
 *
 * @author youzhengjie
 * @date 2022/10/30 15:19:09
 */
@CanalTable("sys_oper_log") //@CanalTable("sys_oper_log")：指定canal监听的表名为sys_oper_log
@Component
@Slf4j
public class OperationLogCanalHandle implements EntryHandler<OperationLogCanal> {

    @Autowired
    private OperationLogService operationLogService;

    @Override
    public void insert(OperationLogCanal operationLogCanal) {

        //编写mysql和缓存同步的逻辑（例如JVM本地缓存、Redis分布式缓存、es等）
        OperationLog operationLog = new OperationLog();
        //bean拷贝
        BeanUtil.copyProperties(operationLogCanal,operationLog);
        //同步到es中
        operationLogService.addOperationLogToEs(operationLog);
        log.warn("OperationLogCanalHandle->insert->开始同步->"+operationLog);

    }

    /**
     * 更新
     *
     * @param before 之前
     * @param after  之后
     */
    @Override
    public void update(OperationLogCanal before, OperationLogCanal after) {

        //编写mysql和缓存同步的逻辑（例如JVM本地缓存、Redis分布式缓存、es等）
        OperationLog operationLog = new OperationLog();
        //注意：要拷贝after对象，这个对象是修改之后的对象
        BeanUtil.copyProperties(after,operationLog);
        //同步es
        operationLogService.updateOperationLogToEs(operationLog);
        log.warn("OperationLogCanalHandle->update->开始同步->"+operationLog);

    }

    @Override
    public void delete(OperationLogCanal operationLogCanal) {
        //编写mysql和缓存同步的逻辑（例如JVM本地缓存、Redis分布式缓存、es等）
        Long id = operationLogCanal.getId();
        //同步es
        operationLogService.deleteOperationLogToEs(id);
        log.warn("OperationLogCanalHandle->delete->开始同步->"+id);
    }
}
