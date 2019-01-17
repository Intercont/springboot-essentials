package br.com.devdojo.springbootessentials.docs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket apiDoc(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("br.com.devdojo.springbootessentials.endpoint"))
                    .paths(PathSelectors.regex("/v1.*"))
                    .build()
                .globalOperationParameters(Collections.singletonList(new ParameterBuilder() //Config Global
                    .name("Authorization") //para o Swagger solicitar o Token obrigatóriamente para realizar as requisições
                    .description("Bearer token") //desta forma colocamos apenas em um lugar a solicitação para autenticação
                    .modelRef(new ModelRef("string")) //por Token de todos os endpoints
                    .parameterType("header")
                    .required(true)
                    .build()))
                .apiInfo(metaData());
    }

    private ApiInfo metaData(){
        return new ApiInfoBuilder()
                .title("Spring Boot Essentials DevDojo versão desenvolvida por Igor Fraga")
                .description("Estudos de Desenvolvimento de APIs com Spring Boot")
                .version("1.0")
                .contact(new Contact("Igor Fraga", "http://igorfraga.com", "igoravancinifraga@gmail.com"))
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }
}
