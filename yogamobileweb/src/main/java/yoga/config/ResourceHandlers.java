package yoga.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
public class ResourceHandlers extends WebMvcConfigurerAdapter {

    @Value("${image.MappingPath}")
    private String imageMappingPath;

    @Value("${image.MappingUrl}")
    private String imageMappingUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String[] staticImageMappingPath = { "file:///"+imageMappingPath };
        String[] staticWebMappingPath = { "/"};
        registry.addResourceHandler(imageMappingUrl+"**").addResourceLocations(staticImageMappingPath);
        registry.addResourceHandler("/**").addResourceLocations(staticWebMappingPath);
        super.addResourceHandlers(registry);
    }
}

