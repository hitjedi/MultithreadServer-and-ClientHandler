package task.main;

import java.io.IOException;
import java.net.InetAddress;
import task.client.Client;

/**
 * Created with IntelliJ IDEA.
 * User: Goldberg
 * Date: 9/23/14
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientMain {

    public static void main(String args[]) {

        try {

            Client client = new Client(InetAddress.getByName("127.0.0.1"), 9999);

            client.Start();

            client.close();

        } catch (IOException e) {

            e.printStackTrace();

        }



    }

}