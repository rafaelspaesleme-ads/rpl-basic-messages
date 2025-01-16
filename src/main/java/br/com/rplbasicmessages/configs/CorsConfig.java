package br.com.rplbasicmessages.configs;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class CorsConfig {

    private static final List<String> ALLOWED_HEADERS = Arrays.asList("Access-Control-Allow-Headers","X-Requested-With", "Origin", "Content-Type", "Accept", "TokenPublic", "TokenPrivate", "*");
    private static final List<String> ALLOWED_METHODS = Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH", "OPTIONS");
    public static final String ACCESS_CONTROL = "Access-Control";

    @Bean
    public FilterRegistrationBean<CorsFilter> corsConfigurationSource() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();

        config.setAllowedHeaders(ALLOWED_HEADERS);
        config.setAllowedMethods(ALLOWED_METHODS);
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*"));

        config.addExposedHeader(ACCESS_CONTROL);

        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));

        bean.setOrder(0);

        return bean;
    }

}
