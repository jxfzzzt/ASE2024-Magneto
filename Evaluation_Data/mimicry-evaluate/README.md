## 实验说明

#### 文件准备
* **additionalClasses.evosuite_target**: 留空即可
* **calleeMethod.evosuite_target**: 存在漏洞的方法
* **calleeMethod.test**: 测试漏洞的测试样例的方法名称
* **callerMethod.evosuite_target**: 应用程序中调用漏洞方法的方法

#### 运行Mimicry工具
启动前将所有的 *.log* 文件 和 *disable_other_goals.evosuite_target* 配置文件删除
然后运行run.sh脚本启动**3**遍即可成功启动工具。