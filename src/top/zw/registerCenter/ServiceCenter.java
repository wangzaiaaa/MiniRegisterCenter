package top.zw.registerCenter;

import top.zw.registerCenter.entity.ServiceProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author bonsoirzw
 * @date 2020/7/11
 * 此类用来存储服务信息
 */
public class ServiceCenter {
    private static final ConcurrentHashMap<String, Set<ServiceProvider>> map = new ConcurrentHashMap<>();

    /**
     * 注册服务提供者，需要考虑并发安全问题
     * @param provider
     * @return
     */
    public boolean registerServeiceProvider(ServiceProvider provider){
        String serverName = provider.getServerName();
        if(!map.containsKey(serverName)){
            map.put(serverName,new HashSet<>());
        }
        Set<ServiceProvider> serviceProviders = map.get(serverName);
        boolean result;
        synchronized (serviceProviders){
            result = serviceProviders.add(provider);
        }
        return result;
    }

    /**
     * 注销服务提供者，同样需要考虑并发安全问题
     * @param provider
     * @return
     */
    public boolean removeServiceProvider(ServiceProvider provider){
        String serverName = provider.getServerName();
        if(!map.containsKey(serverName)){
            return false;
        }
        Set<ServiceProvider> serviceProviders = map.get(serverName);
        boolean result;
        synchronized (serviceProviders){
            result = serviceProviders.remove(provider);
        }
        map.remove(serverName);
        return result;
    }

    /**
     * 显示服务注册信息
     */
    public void showServicesInfo(){
        Set<String> servicesName = map.keySet();
        if(servicesName.isEmpty()){
            System.out.println("当前没有服务注册......");
            return;
        }
        servicesName.stream()
                .map(s -> map.get(s))
                .forEach(System.out::println);
    }

    /**
     * 每隔5秒显示一次服务注册信息
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException{
        while (!Thread.currentThread().isInterrupted()){
            TimeUnit.SECONDS.sleep(5);

        }
    }
}
