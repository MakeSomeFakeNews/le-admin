package me.dqq.leadmin.aspect;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.dqq.leadmin.annotation.Log;
import me.dqq.leadmin.entity.SysLogOperate;
import me.dqq.leadmin.service.SysLogOperateService;
import me.dqq.leadmin.util.IpUtil;
import me.dqq.leadmin.util.SecurityUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final SysLogOperateService logOperateService;

    @Around("@annotation(logAnnotation)")
    public Object around(ProceedingJoinPoint pjp, Log logAnnotation) throws Throwable {
        long startTime = System.currentTimeMillis();
        SysLogOperate logOperate = new SysLogOperate();
        logOperate.setModule(logAnnotation.module());
        logOperate.setAction(logAnnotation.action());

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        logOperate.setMethod(pjp.getTarget().getClass().getName() + "." + signature.getName());

        HttpServletRequest request = null;
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            request = attrs.getRequest();
            logOperate.setRequestUrl(request.getRequestURI());
            logOperate.setRequestMethod(request.getMethod());
            String ip = IpUtil.getIp(request);
            logOperate.setIp(ip);
            logOperate.setIpLocation(IpUtil.getIpLocation(ip));
        }

        try {
            String params = JSONUtil.toJsonStr(pjp.getArgs());
            logOperate.setRequestParams(params.length() > 2000 ? params.substring(0, 2000) : params);
        } catch (Exception ignored) {}

        logOperate.setOperatorId(SecurityUtil.getUserId());
        logOperate.setCreateTime(LocalDateTime.now());

        Object result = null;
        try {
            result = pjp.proceed();
            logOperate.setStatus(1);
            try {
                String resultStr = JSONUtil.toJsonStr(result);
                logOperate.setResponseResult(resultStr.length() > 2000 ? resultStr.substring(0, 2000) : resultStr);
            } catch (Exception ignored) {}
        } catch (Throwable e) {
            logOperate.setStatus(0);
            logOperate.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            logOperate.setDuration(System.currentTimeMillis() - startTime);
            try {
                logOperateService.save(logOperate);
            } catch (Exception e) {
                log.error("保存操作日志失败", e);
            }
        }
        return result;
    }
}
