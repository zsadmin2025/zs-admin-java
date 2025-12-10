package com.zs.common.core.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zsadmin
 */
@Configuration
public class Knife4jOpenApiConfig {



    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(getInfo());
    }

    public Info getInfo() {
        return new Info()
                .title("ZS-Admin API")
                .version("v1.0.0")
                .description("ZS-Admin API")
                .contact(getContact())
                .license(getLicense())
                ;
    }

    /**
     * 开发者信息
     **/
    public Contact getContact() {
        return new Contact()
                .name("zs")
                .email("704347074@qq.com");
    }

    /**
     * 授权信息
     **/
    public License getLicense() {
        return new License().name("MIT");
    }


//    /**
//     * 安全模式，这里指定token通过Authorization头请求头传递
//     */
//    private List<SecurityScheme> securitySchemes() {
//        // 设置请求头信息
//        List<SecurityScheme> apiKeyList = new ArrayList<SecurityScheme>();
//        apiKeyList.add(new ApiKey("Authorization", "Authorization", In.HEADER.toValue()));
//        return apiKeyList;
//    }
}
