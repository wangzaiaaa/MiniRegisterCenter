package top.zw.registerCenter.util;

import java.util.regex.Pattern;

/**
 * @author bonsoirzw
 * @date 2020/7/11
 * 用来校验客户端发送的字符串是否符合{服务名:ip:port/路由名}
 */
public class RegularCheck {
    private static final String SERVER_NAME_PATTERN = "[a-zA-Z]+";
    private static final String IP_ADDRESS_PATTERN = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
    private static final String PORT_PATTERN = "^(\\d){4}((/)[a-zA-Z]+)?";
    private static final int LENGTH_OF_SERVER_STRING = 3;
    /**
     * 检查字符串是否符合规定的格式
     * @param serverStr
     * @return
     */
    public static boolean chenckServiceString(String serverStr){
        String [] arr = serverStr.split(":");
        if(arr.length != LENGTH_OF_SERVER_STRING){
            return false;
        }
        return Pattern.matches(SERVER_NAME_PATTERN,arr[0])
                && Pattern.matches(IP_ADDRESS_PATTERN,arr[1])
                && Pattern.matches(PORT_PATTERN,arr[2]);
    }

    public static void main(String[] args) {
        System.out.println(chenckServiceString("NameService:127.0.0.1:8080/"));
        System.out.println(chenckServiceString("NameService:127.0.0.1:8080\\"));
        System.out.println(chenckServiceString("NameService:127.0.0.1:8080/name"));
    }
}
