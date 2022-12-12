package lemielemieparoleclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    Scanner scan;
    Socket socket = null;
    DataInputStream input = null;
    DataOutputStream output = null;
    InetAddress ip;
    FinestraGioco gioco;
    boolean chiudendo=false;

    public Client(String ServerIP, int ServerPort, FinestraGioco gioco) {
        try {
            ip = InetAddress.getByName(ServerIP);
            socket = new Socket(ip, ServerPort);

            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            scan = new Scanner(System.in);

            LeMieLeMieParoleClient.CONNESSO = true;

            this.gioco = gioco;
        } catch (UnknownHostException ex) {
            log("Client : " + ex.getMessage());
        } catch (IOException ex) {
            log("Client : " + ex.getMessage());
        }

    }

    public void readMessageThread() {
        Thread readMessage = new Thread(new Runnable() {

            @Override
            public void run() {
                while (!chiudendo) {
                    try {
                        String msg = input.readUTF();
                        log(msg);
                        gioco.scrivi(msg);
                    } catch (IOException ex) {
                        log("readMessageThread : " + ex.getMessage());
                    }

                }
            }
        });
        readMessage.start();
    }

    public void writeMessageThread(String x) {

        try {
            output.writeUTF(x);
        } catch (IOException ex) {
            log("writeMessageThread : " + ex.getMessage());
        }
    }

    private void log(String msg) {
        System.out.println(msg);
    }
}
