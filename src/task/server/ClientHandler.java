package task.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: Goldberg
 * Date: 9/20/14
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientHandler implements Runnable{

    private Socket clientSocket;

    public ClientHandler(Socket socket){

        clientSocket = socket;

    }

    public void run(){

        InputStream inputStream = null;

        OutputStream outputStream = null;

        DataInputStream dataInputStream = null;

        DataOutputStream dataOutputStream = null;

        BufferedInputStream bufferedInputStream = null;

        long serverFileSize = 0l;

        long clientFileSize = 0l;

        int n = 0;

        byte[] byteArray = new byte[8*1024];

        long clientFileSizeExists = 0l;

        String line = null;

        String filesInDir = null;

        String dirName = null;

        String path = null;

        File serverFile = null;

        try {

            String name = clientSocket.getInetAddress().getHostName();

            int port = clientSocket.getPort();

            System.out.println("Client " + name + ":" + port + " connected");

            inputStream = clientSocket.getInputStream();

            outputStream = clientSocket.getOutputStream();

            dataInputStream = new DataInputStream(inputStream);

            dataOutputStream = new DataOutputStream(outputStream);

            line = dataInputStream.readUTF(); //введеная директория

            dirName = line;

            filesInDir = getFileNameFromFolder(dirName);

            dataOutputStream.writeUTF(filesInDir); // запись списка файлов в директории

            line = dataInputStream.readUTF(); // считанное имя файла

            if (filesInDir.indexOf(line) != -1) {

                path = dirName + "\\" + line;

                Path file2 = Paths.get(path).normalize();

                serverFile = file2.toFile();

                serverFileSize = serverFile.length();

                dataOutputStream.writeLong(serverFileSize); // запись размера файла на сервере

                dataOutputStream.writeUTF(line);

                dataOutputStream.flush();

            }

            else {

                System.out.println("There is no such file");

                dataOutputStream.writeLong(-1);

            }

            try {

                bufferedInputStream = new BufferedInputStream(new FileInputStream(serverFile));


                while ((n = bufferedInputStream.read(byteArray)) > 0) {

                    outputStream.write(byteArray, (int) clientFileSize , n);

                }

            } catch(NullPointerException npe){

                System.out.println("NullPointerException has been thrown");

            } catch (SocketException se) {

                System.out.println("Client is disconnected");

            }

            outputStream.flush();

        } catch (SocketException se) {

            System.out.println("Connection reset");

        } catch (FileNotFoundException fnfe){

            System.out.println("File not found");

        } catch (IOException ioe){

            ioe.printStackTrace();

        } finally {

            try {

               clientSocket.close();

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

    public String getFileNameFromFolder(String folderName) {

        File folder = new File(folderName);

        File[] listOfFiles = folder.listFiles();

        StringBuffer sb = new StringBuffer();

        if(listOfFiles == null){

            System.out.println("Such dir doesn't exist");

            return "0";

        }

        for(int i=0; i<listOfFiles.length; i++) {

            if(listOfFiles[i].isFile()){

                sb.append(listOfFiles[i].getName() + " ");

            }

        }

        return sb.toString();

    }

}

