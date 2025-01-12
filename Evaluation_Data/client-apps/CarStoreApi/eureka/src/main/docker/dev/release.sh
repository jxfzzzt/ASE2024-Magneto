#由于云服务器带宽的问题，使用scp方式
export MAVEN_HOME=/var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/maven3.5.3
export PATH=${MAVEN_HOME}/bin:$PATH
cd /var/jenkins_home/workspace/car-service-api
mvn clean install -am -pl eureka
cd /var/jenkins_home/workspace/car-service-api/eureka/target
docker rmi car-eureka-dev
docker build -t car-eureka-dev -f ../src/main/docker/dev/Dockerfile .

ssh root@58.33.180.67 << remotessh
rm -rf /var/local/docker-images-tar/car-eureka-dev.tar
docker save car-eureka-dev -o /var/local/docker-images-tar/car-eureka-dev.tar
scp /var/local/docker-images-tar/car-eureka-dev.tar root@8.136.105.20:/var/local/docker-images-tar/car-eureka-dev.tar
exit
remotessh
ssh root@8.136.105.20 << remotessh
docker stop car-eureka-dev
docker rm car-eureka-dev
docker rmi car-eureka-dev
docker load < /var/local/docker-images-tar/car-eureka-dev.tar
docker run -d -v /etc/localtime:/etc/localtime:ro -v /var/local/java-log/eureka:/var/local/java-log/eureka --memory 700m --restart=always --name car-eureka-dev --network host car-eureka-dev
exit
remotessh



#-pl 	--projects 	Build specified reactor projects instead of all projects 选项后可跟随{groupId}:{artifactId}或者所选模块的相对路径(多个模块以逗号分隔)
#-am 	--also-make 	If project list is specified, also build projects required by the list 表示同时处理选定模块所依赖的模块
#-amd 	--also-make-dependents 	If project list is specified, also build projects that depend on projects on the list 表示同时处理依赖选定模块的模块
#-N 	--Non-recursive 	Build projects without recursive 表示不递归子模块
#-rf 	--resume-from 	Resume reactor from specified project 表示从指定模块开始继续处理