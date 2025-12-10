#基础镜像
#FROM openjdk:8 
# 镜像加速
FROM  registry.cn-beijing.aliyuncs.com/my-admin/zs-openjdk-17:17
# 设置时区
ENV TZ=Asia/Shanghai
#RUN mvn clean package
WORKDIR /app
COPY ./zs-admin/target/zs-admin.jar /app/

CMD ["java", "-jar", "zs-admin.jar","--spring.profiles.active=prod"]

#指定暴露端口
EXPOSE 8080