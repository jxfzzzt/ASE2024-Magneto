package com.car.common.annotation;



import com.car.common.enums.OperEnum;

import java.lang.annotation.*;

/**
 * 系统操作日志切面配置
 * @author linjiang.xie
 * @date 2019/12/12 14:54
 */
//注解放置的目标位置,METHOD是可注解在方法级别上
@Target(ElementType.METHOD)
//注解在哪个阶段执行
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysOperLog {
    // 操作模块
    String operModul() default "";
    // 操作类型
    OperEnum operType() default OperEnum.DEFAULT;
    // 操作说明
    String operDesc() default "";
}
