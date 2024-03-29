package org.nhnacademy.lsj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 클라이언트로 부터 request 받는 Server . Multi Thread 지원해서 , 여러개의 request 처리 가능.
 */
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private static final int LISTENING_PORT = 32007;

    private static final String path = "src/main/java/org/nhnacademy/lsj";

    private static ArrayBlockingQueue<Socket> connectionQueue;


    /**
     * ArrayBlockingQueue 사용 -> 대기열 크기 정함 -> thread pool 에 thread는 5개.
     * 5개 클라이언트 한번에 받기 가능 -> 그 이후 부터는 Queue에 쌓임 .
     *
     * @param args Comand Line.
     */
    public static void main(String[] args) {

        connectionQueue = new ArrayBlockingQueue<>(10);
        // 10보다 큰 수의 클라이언트가 대기하면  , 접근 차단함 , 대기열의 크기가 10인 것

        for (int i = 0; i < 5; i++) { // 쓰레드 풀의 쓰레드 수 가 5개 , 이걸 가지고 돌려서 써먹을꺼임
            ConnectionHandler connectionHandler = new ConnectionHandler();
            connectionHandler.start();
        } // 즉 한번에 5개 클라이언트 받기 가능 함 , 그 이후부터는 대기열에 쌓일 꺼임

        ServerSocket server;

        Socket connection;

        try {

            server = new ServerSocket(LISTENING_PORT);
            logger.info("Listening on port " + LISTENING_PORT);

            while (true) {
                connection = server.accept();
                connectionQueue.add(connection); // 대기열에 작업 추가
            }
        } catch (Exception e) {
            logger.warn("서버 다운");
        }


    }

    private static void isValidate(BufferedWriter bw, File file) throws IOException {

        if (file.isFile() || file.isDirectory()) {
            bw.write("OK");
            bw.newLine();
            return;
        }

        logger.warn("잘못된 입력입니다.");
        throw new IllegalArgumentException();


    }


    private static void sendData(Socket server) {


        try (BufferedReader bf = new BufferedReader(new InputStreamReader(server.getInputStream()));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()))) {

            logger.info("Connection from " + server.getInetAddress());


            String str = bf.readLine();

            str.replace(" ", ""); // 공백제거


            File file;

            if (str.equals("INDEX")) {


                file = new File(path);
                isValidate(bw, file);


                String[] files = file.list();

                for (int i = 0; i < files.length; i++) {
                    logger.info("{}", files[i]);
                    bw.write(files[i]);
                    bw.newLine();
                }

            } else if (str.substring(0, 3).equals("GET")) {

                file = new File(path + "/" + str.substring(4));

                isValidate(bw, file);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

                String temp;

                while ((temp = bufferedReader.readLine()) != null) {
                    logger.info(temp); // 서버 로그
                    bw.write(temp);
                    bw.newLine();

                }

            } else {
                logger.warn("잘못된 명령어입니다");
                throw new IllegalArgumentException();
            }

            bw.write("Socket Close");
            bw.flush();
        } catch (IOException e) {
            logger.info("Error: " + e.getMessage());
        }

    }

    private static class ConnectionHandler extends Thread {

        ConnectionHandler() {
            setDaemon(true);
        }

        public void run() {

            while (true) {

                Socket socket;
                try {
                    socket = connectionQueue.take();
                    sendData(socket);
                } catch (InterruptedException e) {
                    logger.warn("{}", e.getMessage());
                }

            }

        }
    }


}
