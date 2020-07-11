package top.zw.registerCenter.net;

import top.zw.registerCenter.ServiceCenter;
import top.zw.registerCenter.entity.ServiceProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.TimeoutException;

/**
 * @author bonsoirzw
 * @date 2020/7/11
 * 用来处理服务端建立的socket
 * 如果是初次建立连接，则向ServiceCenter注册服务，并对客户端的心跳包做相应
 * 一旦发现socket断开就通知ServiceCenter注销服务
 */
public class ListenerHandler implements Runnable{
    private Socket socket;
    private ServiceCenter serviceCenter;
    private boolean haveRegistered = false;
    private ServiceProvider provider;
    ListenerHandler(Socket socket,ServiceCenter serviceCenter){
        this.socket = socket;
        this.serviceCenter = serviceCenter;

    }
    @Override
    public void run() {
         try(BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintStream writer = new PrintStream(socket.getOutputStream());
         ){
             String serviceString = reader.readLine();
             provider = ServiceProvider.generate(serviceString);
             if(!haveRegistered){
                 serviceCenter.registerServeiceProvider(provider);
                 haveRegistered = true;
             }
             String receivedStr;
             while (!socket.isClosed() && !Thread.currentThread().isInterrupted()){
                 receivedStr = reader.readLine();
                 if(Integer.valueOf(receivedStr.trim()) == 0xff){
                     writer.println("HEART_CHECK_OK!");
                 }
             }
         }catch (IOException e){
             e.printStackTrace();
         }finally {
             serviceCenter.removeServiceProvider(provider);
             try{
                 socket.close();
             }catch (Exception e){
                 e.printStackTrace();
             }
         }
    }

}
