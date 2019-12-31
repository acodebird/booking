package com.booking.config;

import com.booking.component.interceptor.LoginInterceptor;
import com.booking.component.interceptor.TestInterceptor;
import com.booking.component.interceptor.TypeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> loginPathPatterns= Arrays.asList(new String[]{"/user/**", "/login/update/**", "/orderManage/**"});
        List<String> typePathPatterns= Arrays.asList(new String[]{"/user/**"});
        //registry.addInterceptor(new TestInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns(loginPathPatterns);
        registry.addInterceptor(new TypeInterceptor()).addPathPatterns(typePathPatterns);
    }
}