package top.zw.registerCenter.net;

import top.zw.registerCenter.ServiceCenter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {
    private static final int SERVER_PORT = 8888;
    private static final ServiceCenter serviceCenter = new ServiceCenter();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for(;;){
            Socket socket = serverSocket.accept();
            executorService.submit(new ListenerHandler(socket,serviceCenter));
            while (true){
                serviceCenter.showServicesInfo();
                try{
                    TimeUnit.SECONDS.sleep(5);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
