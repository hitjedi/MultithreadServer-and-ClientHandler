package task.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: Goldberg
 * Date: 9/17/14
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Client {

    private Socket socket;

    public Client(InetAddress remoteHost, int remotePort) throws IOException{

        socket = new Socket(remoteHost, remotePort);

    }

    public Client(){
    }

    private FileOutputStream fileOutputStream;

    private long serverFileSize;

   // private long clientFileSize;

    //private long l;

   // private long clientFileSizeExists;

    private InputStream sin;

    private OutputStream sout;

    private DataInputStream in;

    private DataOutputStream out;

    private File clientFile;


    public void Start() {

        File clientFile = null;

        FileOutputStream fileOutputStream = null;

        InputStream inputStream = null;

        OutputStream outputStream = null;

        DataInputStream dataInputStream = null;

        DataOutputStream dataOutputStream = null;

        BufferedInputStream bufferedInputStream = null;

        BufferedReader keyboard = null;

        String line = null;

        Path path= null;

        long serverFileSize = 0l;

        try {

            System.out.println("The socket with IP address " + socket.getInetAddress() + " and port " + socket.getPort() + " is ready.");

            inputStream = socket.getInputStream();

            outputStream = socket.getOutputStream();

            dataInputStream = new DataInputStream(inputStream);

            dataOutputStream = new DataOutputStream(outputStream);

            keyboard = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Type path");

            line = keyboard.readLine();

            dataOutputStream.writeUTF(line);//запись пути

            dataOutputStream.flush();

            line = dataInputStream.readUTF();// считывание директории с сервера

            if(line.equals("0")){

                System.out.println("There is no such dir");

                System.exit(0);

            }

            System.out.println("Files in dir:");

            System.out.println(line);

            System.out.println("Choose file:");

            line = keyboard.readLine();

            dataOutputStream.writeUTF(line);

            dataOutputStream.flush();

            serverFileSize = dataInputStream.readLong();

            if(serverFileSize == -1){

                System.out.println("File is not found");

                System.exit(0);

            }

            //System.out.println(serverFileSize);
            String file = dataInputStream.readUTF();

            System.out.println("Input dir for the saving");

            line = keyboard.readLine();

            FileDir(Paths.get(line).normalize().toString());

            path = Paths.get(line + "\\" + file).normalize();

            clientFile = path.toFile();

            //System.out.println(clientFile);

            fileOutputStream = new FileOutputStream(clientFile);

            FileWrite(fileOutputStream, dataInputStream, serverFileSize);

        } catch (Exception x) {

            x.printStackTrace();

        }

    }

    public void FileWrite(FileOutputStream fileOutputStream, DataInputStream dataInputStream,long serverFileSize) throws IOException{

        byte[] byteArray = new byte[8*1024];

        long l = 0l;

        BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

        try {

            int bytesRead;

            while ((bytesRead = dataInputStream.read(byteArray, 0, byteArray.length)) >= 0) {

                bos.write(byteArray, 0, bytesRead);

                l += bytesRead;

            }

            System.out.println("The file has been successfully downloaded");

        } catch (SocketException e) {

            System.out.println("The server is offline, downloaded " + l + " of " +serverFileSize);

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            bos.close();

        }

    }

    public void close() throws IOException {

        socket.close();
    }

    public void FileDir(String path) {

        File folderSave = new File(path);

        if (folderSave.isDirectory()) {

        } else {

            System.out.println("Such dir doesn't exist");

            System.exit(0);

        }

    }

}