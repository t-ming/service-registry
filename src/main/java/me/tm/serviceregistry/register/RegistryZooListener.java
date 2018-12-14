package me.tm.serviceregistry.register;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;

@Component
public class RegistryZooListener implements ServletContextListener {

    @Value("${service.ip}")
    private String serverAddress;
    @Value("${service.port}")
    private String serverPort;

    @Resource
    private ServiceRegistry serviceRegistry;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> infoMap = mapping.getHandlerMethods();
        for(RequestMappingInfo info : infoMap.keySet()) {
            String serviceName = info.getName();
            if (StringUtils.hasText(serviceName)) {
                serviceRegistry.register(serviceName, String.format("%s:%s", serverAddress, serverPort));
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}