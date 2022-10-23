package com.boot.aop;

import com.boot.annotation.OperationLog;
import com.boot.security.LoginUser;
import com.boot.service.OperationLogService;
import com.boot.utils.BrowserUtils;
import com.boot.utils.IpToAddressUtil;
import com.boot.utils.IpUtils;
import com.boot.utils.SnowId;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 操作日志切面类，说白了就是为了触发注解
 *
 * @author youzhengjie
 * @date 2022/10/22 22:44:19
 */
//OperationLog注解的aop切面
@Aspect
@Component
@Slf4j
public class OperationLogAspect {

    @Autowired
    private OperationLogService operationLogService;


    //OperationLog注解的aop切面切入点
    @Pointcut("@annotation(com.boot.annotation.OperationLog)")
    public void operationLogPointCut(){

    }


    /**
     * 触发操作日志注解
     *
     * @param proceedingJoinPoint 进行连接点
     * @return {@link Object}
     */
    //环绕通知
    @Around("operationLogPointCut()")
    public Object operationLog(ProceedingJoinPoint proceedingJoinPoint){

        Object proceed=null;
        try {
            //获取调用方法前的毫秒值
            long startMs = System.currentTimeMillis();

            //正式执行被注解标记的方法
            proceed= proceedingJoinPoint.proceed();


            //等调用完proceedingJoinPoint.proceed()之后，说明接口方法已经执行了，就可以把OperationLog存到数据库中。
            ServletRequestAttributes servletRequestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            //获取HttpServletRequest
            HttpServletRequest request = servletRequestAttributes.getRequest();
            //获取正在访问的并且有OperationLog注解的接口方法的MethodSignature对象
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            //获取正在访问的并且有OperationLog注解的接口方法的OperationLog注解
            OperationLog operationLogAnnotation = signature.getMethod().getAnnotation(OperationLog.class);
            //拿到注解值
            String annotationValue = operationLogAnnotation.value();
            //从springsecurity中获取当前访问的用户名（从这里就可以看出，只有被springsecurity拦截的接口才能获取到用户名）
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            String userName = loginUser.getUser().getUserName();
            //获取正在访问的并且有OperationLog注解的接口的uri
            String uri = request.getRequestURI();
            //获取ip
            String ipAddr = IpUtils.getIpAddr(request);
            //获取ip所在的地址
            String address = IpToAddressUtil.getCityInfo(ipAddr);
            //获取用户使用的浏览器
            String browserName = BrowserUtils.getBrowserName(request);
            //获取用户使用的操作系统
            String osName = BrowserUtils.getOsName(request);

            //获取调用方法后+注解的一些操作的毫秒值
            long endMs = System.currentTimeMillis();

            //访问接口的耗时
            String timeMs=(endMs-startMs)+"ms";

            //插入OperationLog数据到数据库
            com.boot.entity.OperationLog operationLog = com.boot.entity.OperationLog.builder()
                    .id(SnowId.nextId())
                    .username(userName)
                    .type(annotationValue)
                    .uri(uri)
                    .time(timeMs)
                    .ip(ipAddr)
                    .address(address)
                    .browser(browserName)
                    .os(osName)
                    .operTime(LocalDateTime.now())
                    .build();
            operationLogService.save(operationLog);


            return proceed;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

    }








}
