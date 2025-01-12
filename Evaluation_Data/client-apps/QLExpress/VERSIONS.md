# QLExpress基本语法

# 一、背景介绍

由阿里的电商业务规则、表达式（布尔组合）、特殊数学公式计算（高精度）、语法分析、脚本二次定制等强需求而设计的一门动态脚本引擎解析工具。
在阿里集团有很强的影响力，同时为了自身不断优化、发扬开源贡献精神，于2012年开源。
先后出现了1.0版本和2.0版本，到3.0版本之后，引入了比较系统的语法树推导，使语法的功能大大增强和稳定。
之前svn的开源地址： http://code.taobao.org/p/QLExpress/src/branches/

# 二、有记录的版本迭代

## 1、3.0.7-SNAPSHOT 版本[2014-06-06 fixed]
BigDecimal.divide()函数增加默认的策略BigDecimal.ROUND_HALF_UP，防止在高精度要求的除法计算时，某些情况下出现以下异常。
ava.lang.ArithmeticException: Non-terminating decimal expansion

## 2、3.0.7版本[2015-01-08 fixed]
增加ExpressRunner.getInstructionSetFromLocalCache()方法。支持阿里的某个业务系统，支持直接获取本地指令集缓存，作为业务的判断场景需求。

## 3、3.0.8版本[2015-04-23 fixed]
（1）增加指令集错误日志打印输出的控制。支付宝解析外部脚本文件，文件信息有可能出现错误，处理的时候打印log日志导致应用压力太大。
（2）修复线上bug，该业务方不恰当的使用addFunctionMethod(class.name,methodName....)导致脚本运行期间每次去new Object（），效率变慢，影响了qps，换用了addServiceMethod（bean,methodName）之后得到解决。
对classname-object做了一层缓存保护，保证即使误用也能够获取到较好地性能。

## 4、3.0.9版本[2015-09-21 fixed] 
（1）修复多线程下的重大bug，token使用的分隔符号数组在多线程解析脚本的情况下，有可能出现数组排序异常，导致数组中的元素混乱。
重新问题的单元用例：

http://gitlab.alibaba-inc.com/alibaba-rule-platform/qlExpress2

com.ql.util.express.bugfix.CrashTest

根本解决方案：在ExpressRunner创建的时候，就排序完毕，之后不再排序，保证多线程安全，同时也提升了编译器的性能和效率。
临时解决方案：在脚本中，需要分割的地方加一些不可见字符，比如空格等。

比如："单价*数量+运费"   修改成 "单价 * 数量 + 运费" 就会不受这个bug影响

目前要求所有核心系统版本升级到3.0.9及以上版本。
```xml
<dependency>
  <groupId>com.taobao.util</groupId>
  <artifactId>taobao-express</artifactId>
  <version>3.0.9</version>
</dependency>
```
 



## 5、3.0.11版本[2016-1]
(1)解决了指令中引用ExpressRunner的问题，使用分离的ExpressRunner来解决指令集运行期间相互的影响。

## 6、3.0.12版本[2016-08-03]
（1）分支迁移到git上，字符集修改为utf-8

## 7、3.0.13版本[2016-08-15]
(1)玄难通过jprofile进行性能分析，对数组的创建、数据获取采用了swapArray方式，大量的较少了Array的创建。
(2)补上遗漏的反射缓存。
(3)对部分对象做缓存，进一步提升性能。
(4)解决null关键字导出变量列表的时候的bug
(5)注意：取消了ExpressRunner.java的以下接口，主要用在自己管理指令缓存，这个可以通过clearExpressCache()实现，所以不推荐使用，万一还是想使用这个接口，请升级到3.0.17版本。
public Object execute(InstructionSet[] instructionSets,IExpressContext<String,Object> context, List<String> errorList,
                 boolean isTrace,boolean isCatchException, Log aLog)
 



## 8、3.0.14版本[2016-09-13]
(1)支持java不定参数的调用
(2)提高数组定义的灵活性和准确性。

## 9、3.0.15版本[2016-10-26]
(1)支持Method导出，用于给菜鸟业务动态绑定函数使用

## 10、3.0.16版本[2016-10-28]
(1)支持在脚本中给任意的Object增加字段field或者方法method，比如增加string的方法，"helloworld".isNotBlank()或者"helloworld".长度  非常安全，只在脚本中生效，没有采用任何aop或者增强字节码的技术，不会影响外部的调用。

## 11、3.0.17版本[2016-11-30]
(1)考虑到老系统二方包的兼容，恢复了兼容接口ExpressRunner.execute(InstructionSet[] instructionSets....)但是不推荐使用。
(2)bugfix 3.0.16版本特性的表达式，最后不return情况下的bug。

## 12、3.0.18版本[2017-1-16]
(1)来自开源用户的反馈，bugfix 使用addFunctionOfServiceMethod指令集无法序列化的问题。

## 12、3.1.0版本[2017-3-27]
(1)增加 | & ~ << >>位操作符
(2)增加executeRule函数，打印出规则逻辑结构

## 13、3.1.1版本[2017-4-5]
(1)增加指令集的行数，出错的时候增加出错行数信息

## 14、3.1.3版本[2017-6-4]
(1)内部版本调整，避免其他的分支干扰，覆盖版本3.1.2版本

## 15、3.1.4版本[2017-9-19]
(1)增加instanceof 的操作符

## 16、3.1.5版本[2017-11-17]
(1)负号某些特殊情况下的解析bug：三元操作符，return

## 17、3.1.6版本[2017-11-17]
(1)bugfix 嵌套runner调用的时候，数据池的还原

## 18、3.1.7版本[2017-11-17]
(1)bugfix 在自定义操作符的情况下，调用 runner.getOutVarNames Api 可能引发的空指针问题

## 18、3.1.8版本[2018-1-30]
(1)增加扩展功能:ExpressRunner#setIgnoreConstChar(Boolean),设置可以忽略单字符操作，即 'a'自动变成"a"。

## 3.2.1版本[2018-2-23]
(1)增加扩展功能:ExpressRunner#setIgnoreConstChar(Boolean),设置可以忽略单字符操作，即 'a'自动变成"a"。

(2)增加接口来支持绑定自定义classloader的class的method:ExpressRunner#addFunctionOfClassMethod(String name, Class<?> aClass,...)。

## 3.2.2版本[2019-1-22]
(1)android环境的重大优化：减少编译的内存消耗，堆栈溢出问题

(2)空指针的保护策略：com.ql.util.express.config.QLExpressRunStrategy.setAvoidNullPointer(true)

## 3.2.3版本[2019-6-18]
(1)增加超时方法:TimeOutExceptionTest

(2)安全审核方法:InvokeSecurityRiskMethodsTest

(3)区分异常类型：ThrowExceptionTest

(4)**引入了不兼容的修改**, 将比较（==, >, >=, <. <=）由弱类型改成了强类型，比如在 3.2.2 中 `1=="1"` 为 true, 但是 3.3.3 及以后版本都是 false，升级时需要注意
## 3.2.4版本[2019-12-6]
(1)增加null的数字比较方案"1>null"":NullCompareTest

## 3.2.5版本[2021-8-23]
(1)支持强大的数组符号访问属性功能（List,Map,Array）：ArrayPropertyMixTest
(2)支持lambda表达式，stream方式操作集合书写更高效（List,Map）：LambdaTest
(3)解决数组类型的方法匹配bug：ArrayMisType

## 3.2.6版本[2021-11-24]
(1)彻底解决ExpressRunner重入问题，可以嵌套使用：RecursivelyRunnerTest
(2)重磅特性：通过@QLAlias对字段和方法上添加注解，实现中文字段和中文方法调用：QLAliasTest

## 3.2.7版本[2021-12-10]
(1)QLAliasTest 添加set方法

## 3.3.0版本[2022-04-09]

(1)高精度计算下的溢出问题修复 com.ql.util.express.test.NumberOperatorCalculatorTest

(2)多级别安全控制与沙箱模式 com.ql.util.express.example.MultiLevelSecurityTest

## 3.3.1版本[2023-02-03]

(1) #188 break/continue 问题修复
(2)内置脚本 cache 改成 concurrentHashMap
(3)去除 log4j 和 apache common log 的依赖， 原先部分 `execute` 参数中含有 common log ，所以部分 `execute` 签名有不兼容变更。可能会导致升级后编译不通过
(4)#233 相关安全增强
    a. 扩充默认黑名单
    b. 支持通过 com.ql.util.express.config.QLExpressRunStrategy#setMaxArrLength 限制脚本一次最多申请数组的大小