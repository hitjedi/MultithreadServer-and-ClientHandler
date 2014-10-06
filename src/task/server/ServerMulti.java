package task.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Goldberg
 * Date: 9/18/14
 * Time: 3:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerMulti {

    final int maxConnection = 50;

    private ServerSocket socket;

    public ServerMulti(InetAddress host, int port) throws IOException {

        socket = new ServerSocket(port, maxConnection, host);

    }


    public void Start() throws IOException{

        while(true){


            Socket clientSocket = socket.accept();


            new Thread(new ClientHandler(clientSocket)).start();


        }

    }

}