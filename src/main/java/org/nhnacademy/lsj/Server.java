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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private static final int LISTENING_PORT = 32007;

    private static final String path = "src/main/java/org/nhnacademy/lsj";

    public static void main(String[] args) {


        ServerSocket server;

        Socket connection;

        try {

            server = new ServerSocket(LISTENING_PORT);
            logger.info("Listening on port " + LISTENING_PORT);

            while (true) {
                connection = server.accept();

                ConnectionHandler connectionHandler = new ConnectionHandler(connection);
                connectionHandler.start();
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
        Socket client;

        ConnectionHandler(Socket socket) {
            client = socket;
        }

        public void run() {

            sendData(client);
        }
    }


}
