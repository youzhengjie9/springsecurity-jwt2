package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.entity.OperationLog;
import com.boot.mapper.OperationLogMapper;
import com.boot.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志服务impl
 *
 * @author youzhengjie
 * @date 2022/10/21 23:42:49
 */
@Service
@Slf4j
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public List<OperationLog> selectAllOperationLogByLimit(int page, int size) {
        return operationLogMapper.selectAllOperationLogByLimit(page, size);
    }
}
