package task.main;

import task.server.ServerMulti;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created with IntelliJ IDEA.
 * User: Goldberg
 * Date: 9/23/14
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainServer {

    public static void main(String[] args) {
        try {

            ServerMulti server = new ServerMulti(InetAddress.getByName("127.0.0.1"),9999);

            server.Start();


        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }

    }

}
