package top.zw.registerCenter.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.server.ExportException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 8888;
    //每个5000毫秒检测一次心跳
    private static final int HEART_CHECK_RATE = 5000;
    //心跳检测包
    private static final String HEART_CHECK_PACKAGE = "HEART CKECK010203";
    //心跳检测规定返回字符串
    private static final String HEART_CHECK_RESPONSE_STRING = "HEART_CHECK_OK!";

    /**
     * 心跳检测线程
     */
    private static class HeartCheck extends Thread{
        private Socket socket;
        HeartCheck(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream writer = new PrintStream(socket.getOutputStream());
            ){

                long beginTime;
                String receivedStr;
                while (!Thread.currentThread().isInterrupted() && !socket.isClosed()){
                    writer.println(HEART_CHECK_PACKAGE);
                    beginTime = System.currentTimeMillis();
                    receivedStr = reader.readLine();
//                    try{
//                        TimeUnit.SECONDS.sleep(6);
//                    }catch (InterruptedException e){
//                        e.printStackTrace();
//                    }
                    if(System.currentTimeMillis() - beginTime > HEART_CHECK_RATE){
                        System.out.println("超出心跳检测规定的时间，连接断开......");
                        throw new TimeoutException("心跳检测超时...");
                    }
                    if(receivedStr == null || !receivedStr.equals(HEART_CHECK_RESPONSE_STRING)) {
                        System.out.println("不符合心跳检测规定返回，连接断开......");
                        throw new IllegalArgumentException("未按照规定相应心跳包");
                    }
                }
                System.out.println("end");
            }catch (IOException e){
                e.printStackTrace();
            }catch (TimeoutException e){
                e.printStackTrace();
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }finally {
                try{
                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 像socket中写入一行信息
     * @param out
     */
    private static void writeLine(PrintStream out,String info){
        out.println(info);
    }


    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_ADDRESS,SERVER_PORT);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream writer = new PrintStream(socket.getOutputStream());
        ){
            writeLine(writer,"UserService:127.0.0.1:8888/getUserById");
            Thread heartThread = new HeartCheck(socket);
            heartThread.start();
            heartThread.join();

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
