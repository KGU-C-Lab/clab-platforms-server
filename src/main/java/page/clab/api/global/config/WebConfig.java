package page.clab.api.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import page.clab.api.global.handler.ApiLoggingInterceptor;
import page.clab.api.global.util.HtmlCharacterEscapes;

import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    @Autowired
    private ApiLoggingInterceptor apiLoggingInterceptor;

    @Value("${resource.file.path}")
    private String filePath;

    @Value("${resource.file.url}")
    private String fileURL;

    @Value("${security.cors.path-pattern}")
    private String corsPathPattern;

    @Value("${security.cors.allowed-origins}")
    private String[] corsAllowedOrigins;

    @Value("${security.cors.allowed-methods}")
    private String[] corsAllowedMethods;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("Resource UploadedFile Mapped : {} -> {}", fileURL, filePath);
        registry
                .addResourceHandler(fileURL + "/**")
                .addResourceLocations("file://" + filePath + "/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource resource = location.createRelative(resourcePath);
                        if (resource.exists() && resource.isReadable()) {
                            return resource;
                        }
                        throw new FileNotFoundException("Resource not found: " + resourcePath);
                    }
                });
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(corsPathPattern)
                .allowedOrigins(corsAllowedOrigins)
                .allowedMethods(corsAllowedMethods);
    }

    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        ObjectMapper copy = objectMapper.copy();
        copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(copy);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiLoggingInterceptor);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

}
