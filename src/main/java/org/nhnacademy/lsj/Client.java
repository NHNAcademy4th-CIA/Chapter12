package org.nhnacademy.lsj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private static final int LISTENING_PORT = 32007;

    public static void main(String[] args) {


        try (Socket connection = new Socket("127.0.0.1", LISTENING_PORT);
             BufferedReader incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()), true);
        ) {

            Scanner sc = new Scanner(System.in);

            pw.println(sc.nextLine());

            incoming.lines().forEach(x -> logger.info("{}", x));

            logger.info("프로그램 종료");

        } catch (Exception e) {

            logger.warn("{}", e.getMessage());
        }


    }


}
