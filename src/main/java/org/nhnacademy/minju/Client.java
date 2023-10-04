package org.nhnacademy.minju;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    public static final int LISTENING_PORT = 32007;

    public static void main(String[] args) {
        try (Socket connection = new Socket("127.0.0.1", LISTENING_PORT);
             BufferedReader incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()), true)) {
            Scanner scanner = new Scanner(System.in);
            // GET, INDEX
            printWriter.println(scanner.nextLine());
            // 출력
            incoming.lines().forEach(x -> logger.info("{}", x));
            logger.info("종료");
        } catch (IOException e) {
            logger.warn("{}", e.getMessage());
        }
    }
}
