package org.nhnacademy.lsj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 서버와 연결해 request 던지는 클래스 , INDEX , GET 명령어 사용 가능함.
 */
public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private static final int LISTENING_PORT = 32007;

    /**
     * Socket  ip address = host , PrintWriter 로 입력보냄 -> BufferReader로 한번에 입력 받음 -> 출력.
     *
     * @param args comand Line.
     */
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
