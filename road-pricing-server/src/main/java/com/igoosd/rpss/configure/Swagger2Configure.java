package com.igoosd.rpss.configure;

import com.igoosd.common.util.Const;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * 2018/3/6.
 */
@Configuration
@EnableSwagger2
public class Swagger2Configure {

    @Bean
    public Docket createRestApi() {

        ParameterBuilder sessionParam = new ParameterBuilder();
        sessionParam.name(Const.HEADER_SESSION_ID).description("会话").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        ParameterBuilder signatureParam = new ParameterBuilder();
        signatureParam.name(Const.HEADER_SIGNATURE).description("签名").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        List<Parameter> pars = new ArrayList<>(2);
        pars.add(sessionParam.build());
        pars.add(signatureParam.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .globalOperationParameters(pars)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.igoosd.rpss"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("占道收费APP 服务端接口文档")
                .description("APP服务端节接口")
                .version("1.0")
                .build();
    }
}
