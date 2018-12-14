package me.tm.serviceregistry.register;

public interface ServiceRegistry {
    void register(String serviceName, String serviceAddress);
}