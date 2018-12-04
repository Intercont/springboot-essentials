package br.com.devdojo.springbootessentials.adapter;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Sobrescrevendo o comportamento padrão de paginação do Spring Data
 */
/*@Configuration
public class SpringBootEssentialsAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver phmar = new PageableHandlerMethodArgumentResolver();
        phmar.setFallbackPageable(new PageRequest(0,5));
        argumentResolvers.add(phmar);
    }
}*/

//PAGINAÇÃO
//Configuração com as classes atuais sem estarem depreciadas
    //No lugar de extender WebMvcConfigurerAdapter apenas implemento WebMvcConfigurer
    //https://stackoverflow.com/questions/44848653/pagerequest-constructors-have-been-deprecated
@Configuration
public class SpringBootEssentialsAdapter implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver phmar = new PageableHandlerMethodArgumentResolver();
        phmar.setFallbackPageable(PageRequest.of(0,50));
        argumentResolvers.add(phmar);
    }
}
