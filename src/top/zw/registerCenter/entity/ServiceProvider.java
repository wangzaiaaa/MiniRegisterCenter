package top.zw.registerCenter.entity;

/**
 * @author bonsoirzw
 * @date 2020/7/11
 * {服务名:ip:port/路由名}
 * 此类为服务提供者的实体类，主要包含服务提供者的IP，PORT以及路由等信息
 */
public class ServiceProvider {
    private String serverName;
    private String serverAddress;
    private int serverPort;
    private String router;
    public ServiceProvider(String serverName, String serverAddress, int serverPort) {
        this.serverName = serverName;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;

    }
    public ServiceProvider(String serverName, String serverAddress, int serverPort, String router) {
        this(serverName,serverAddress,serverPort);
        this.router = router;
    }
    public static ServiceProvider generate(String serverString){
        if(serverString == null || serverString.equals("")){
            return null;
        }
        String [] arr = serverString.split(":");
        String serviceName = arr[0];
        String serviceAddress = arr[1];
        arr = arr[2].split("/");
        if(arr.length == 1){
            return new ServiceProvider(serviceName,serviceAddress,Integer.valueOf(arr[0]));
        }
        return new ServiceProvider(serviceName,serviceAddress,Integer.valueOf(arr[0]),arr[1]);
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    @Override
    public String toString() {
        return "ServiceProvider{" +
                "serverName='" + serverName + '\'' +
                ", serverAddress='" + serverAddress + '\'' +
                ", serverPort=" + serverPort +
                ", router='" + router + '\'' +
                '}';
    }
}
