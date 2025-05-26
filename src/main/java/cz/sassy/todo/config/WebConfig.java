package cz.sassy.todo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig is a configuration class that sets up resource handling for the application.
 * It configures the locations from which static resources can be served.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * This method adds resource handlers to serve static resources.
     * It maps all requests to the classpath location "classpath:/static/".
     *
     * @param registry the ResourceHandlerRegistry to configure resource handlers
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
