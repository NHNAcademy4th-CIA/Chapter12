package org.nhnacademy.minju;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final String path = "src/main/java/org/nhnacademy/minju/";
    private static final int QUEUE_SIZE = 10;
    public static final int LISTENING_PORT = 32007;

    private static ArrayBlockingQueue<Socket> queue;

    /**
     * queue의 크기 10, 스레드풀에 스레드 크기 5 -> 클라이언트가 5명 초과하면 queue에 쌓인다
     *
     * @param args args
     */
    public static void main(String[] args) {
        Socket connection;
        ServerSocket listener;
        queue = new ArrayBlockingQueue<>(QUEUE_SIZE); // 대기열 크기

        for (int i = 0; i < 5; i++) {
            ConnectionHandler connectionHandler = new ConnectionHandler();
            connectionHandler.start();
        }
        try {
            listener = new ServerSocket(LISTENING_PORT);
            logger.info("port : {}", LISTENING_PORT);
            while (true) {
                connection = listener.accept();
                queue.add(connection);
            }
        } catch (IOException e) {
            logger.warn("{}", e.getMessage());
        }
    }

    /**
     * 대기열에서 소켓을 꺼내온다
     * GET/INDEX 작업을 시작
     */
    private static class ConnectionHandler extends Thread {
        ConnectionHandler() {
            setDaemon(true);  // 스레드 시작 전
        }

        @Override
        public void run() {
            while (true) {
                Socket socket;
                try {
                    socket = queue.take();
                    clientCommand(socket);
                } catch (InterruptedException e) {
                    logger.warn("{}", e.getMessage());
                }
            }
        }

        private static void clientCommand(Socket client) {

            try (BufferedReader incoming = new BufferedReader(new InputStreamReader(
                    client.getInputStream()))) {


                String messageIn = incoming.readLine();

                if (messageIn.equals("INDEX")) {
                    File directory = new File(System.getProperty("user.home"));
                    String[] files = directory.list();
                    sendMsg(client, Arrays.toString(files));
                } else if (messageIn.startsWith("GET")) {
                    String fileName = messageIn.substring(3).trim();
                    File file = new File(path + fileName);
                    logger.info("{}", file);
                    if (file.isFile()) {
                        // send "OK"
                        // send content of file and close connection
                        sendMsg(client, "OK");

                        try (FileInputStream fileInputStream = new FileInputStream(file)) {
                            OutputStream outputStream = client.getOutputStream(); // 전송
                            outputStream.write(fileInputStream.read());

                            int temp;
                            while ((temp = fileInputStream.read()) != -1) { // 파일로부터 바이트로 입력 받아 바이트 단위로 출력
                                outputStream.write(temp);
                            }
                        } // 파일

                    } else {
                        // // send "ERROR, error msg" and close connection
                        throw new IllegalArgumentException("ERROR : " + fileName + " is not file name");
                    }
                } else {
                    throw new IllegalArgumentException("입력은 INDEX거나 GET <fileName>이어야 합니다.");
                }
                client.close();
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }

        private static void sendMsg(Socket client, String response) {
            try {
                logger.info("Connection from {}", client.getInetAddress());
                PrintWriter outgoing;   // Stream for sending data.
                outgoing = new PrintWriter(client.getOutputStream());
                outgoing.println(response);
                outgoing.flush();  // Make sure the data is actually sent!
            } catch (Exception e) {
                logger.warn("Error: {}", e.getMessage());
            }
        } // end sendDate()
    }
}
