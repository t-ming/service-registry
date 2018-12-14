package me.tm.serviceregistry;

import me.tm.serviceregistry.register.ServiceRegistry;
import me.tm.serviceregistry.register.impl.ServiceRegistryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegistryConfig {
    @Value("${registry.servers}")
    private String servers;

    public void setZooServers(String servers) {
        this.servers = servers;
    }

    @Bean
    public ServiceRegistry serviceRegistry(){
        return new ServiceRegistryImpl(servers);
    }
}
