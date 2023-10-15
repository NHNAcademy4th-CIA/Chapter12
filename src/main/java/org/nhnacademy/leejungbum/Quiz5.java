package org.nhnacademy.leejungbum;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.ArrayBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 파일검색 및 다운로드 서버 멀티쓰레드로 구현
 */
public class Quiz5 {
    private static final Logger logger = LoggerFactory.getLogger(Quiz5.class);
    public static final int LISTENING_PORT = 32007;

    private static final int THREAD_POOL_SIZE = 10;

    private static final int CONNECTION_QUEUE_SIZE = 5;

    private static ArrayBlockingQueue<Socket> connectionQueue;

    public static void main(String[] args) {

        ServerSocket listener;
        Socket connection;
        File directory = new File("Disk");


        connectionQueue = new ArrayBlockingQueue<Socket>(CONNECTION_QUEUE_SIZE);

        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            ConnectionHandler worker = new ConnectionHandler(directory);
            worker.start();
        }


        try {
            listener = new ServerSocket(LISTENING_PORT);
            logger.info("Listening on port {}", LISTENING_PORT);
            while (true) {
                connection = listener.accept();
                connectionQueue.add(connection);
            }
        } catch (Exception e) {
            logger.info("Server shut down unexpectedly.");
            logger.info("Error:  {}", e);
        }

    }  // end main()

    /**
     * 연결을 쓰레드단위로 해주는 클래스
     */
    private static class ConnectionHandler extends Thread {
        File directory;  // The directory that contains the files

        // that are made available on this server.
        ConnectionHandler(File directory) {
            this.directory = directory;
            setDaemon(true);
        }

        public void run() {
            while (true) {
                try {
                    Socket connection = connectionQueue.take();
                    handleConnection(directory, connection);
                } catch (Exception e) {
                    logger.error(e.toString());
                }
            }
        }
    }

    /***
     * 연결
     * @param directory 디렉토리
     * @param connection 연결
     */
    private static void handleConnection(File directory, Socket connection) {
        Scanner incoming;
        PrintWriter outgoing;
        String command = "Command not read";
        try {
            incoming = new Scanner(connection.getInputStream());
            outgoing = new PrintWriter(connection.getOutputStream());
            command = incoming.nextLine();
            if (command.equalsIgnoreCase("index")) {
                sendIndex(directory, outgoing);
            } else if (command.toLowerCase().startsWith("get")) {
                String fileName = command.substring(3).trim();
                sendFile(fileName, directory, outgoing);
            } else {
                outgoing.println("unsupported command");
                outgoing.flush();
            }
            System.out.println("OK    " + connection.getInetAddress()
                    + " " + command);
        } catch (Exception e) {
            System.out.println("ERROR " + connection.getInetAddress()
                    + " " + command + " " + e);
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
            }
        }
    }

    /***
     * 파일 리스트 전송
     * @param directory 디렉토리
     * @param outgoing 보내는쪽
     * @throws Exception 파일 에러
     */
    private static void sendIndex(File directory, PrintWriter outgoing) throws Exception {
        String[] fileList = directory.list();
        for (int i = 0; i < fileList.length; i++)
            outgoing.println(fileList[i]);
        outgoing.flush();
        outgoing.close();
        if (outgoing.checkError())
            throw new Exception("Error");
    }

    /***
     * 파일 전송
     * @param fileName 파일이름
     * @param directory 폴더
     * @param outgoing 보내는쪽
     * @throws Exception 파일에러
     */
    private static void sendFile(String fileName, File directory, PrintWriter outgoing) throws Exception {
        File file = new File(directory, fileName);
        if ((!file.exists()) || file.isDirectory()) {
            outgoing.println("error");
        } else {
            outgoing.println("ok");
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            while (true) {

                String line = fileIn.readLine();
                if (line == null)
                    break;
                outgoing.println(line);
            }
        }
        outgoing.flush();
        outgoing.close();
        if (outgoing.checkError())
            throw new Exception("Erro");
    }
}
