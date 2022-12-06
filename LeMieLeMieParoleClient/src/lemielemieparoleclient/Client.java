package lemielemieparoleclient;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    
    Scanner scan ;
    Socket socket = null;
    DataInputStream input = null;
    DataOutputStream output = null;
    InetAddress ip;
    
    public Client(String ServerIP, int ServerPort){
        try{
            ip = InetAddress.getByName(ServerIP);
            socket = new Socket(ip, ServerPort);
            
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            
            scan = new Scanner(System.in);
            
            LeMieLeMieParoleClient.CONNESSO=true;
        }catch(UnknownHostException ex){
            log("Client : " + ex.getMessage());
        }catch(IOException ex){
            log("Client : " + ex.getMessage());
        }
        
    }
    
    public void readMessageThread(){
        Thread readMessage = new Thread(new Runnable() {

            @Override
            public void run() {
                while(true){
                    try{
                        String msg = input.readUTF();
                        log(msg);
                    }catch(IOException ex){
                        log("readMessageThread : " + ex.getMessage());
                    }
                    
                }
            }
        });
        readMessage.start();
    }
    
    public void writeMessageThread(){
        Thread sendMessage = new Thread(new Runnable() {

            @Override
            public void run() {
                while(true){
                    String msg = scan.nextLine();
                    
                    try{
                        output.writeUTF(msg);
                    }catch(IOException ex){
                        log("writeMessageThread : " + ex.getMessage());
                    }
                }
            }
        });
        sendMessage.start();
    }
    private void log(String msg){
        System.out.println(msg);
    }
}