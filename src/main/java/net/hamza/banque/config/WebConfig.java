package net.hamza.banque.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@Configuration
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfigClass.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{ ServletConfigClass.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}


