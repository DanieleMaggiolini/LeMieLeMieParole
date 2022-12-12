package lemielemieparole;

import java.util.GregorianCalendar;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {

    final Socket socket;
    final Scanner scan;
    String name;
    boolean isLosggedIn;
    String parola;
    char[] parolaCensurata;
    int nTentativi;

    private DataInputStream input;
    private DataOutputStream output;

    public ClientHandler(Socket socket, String name) {
        this.socket = socket;
        scan = new Scanner(System.in);
        this.name = name;
        isLosggedIn = true;
        

        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

        } catch (IOException ex) {
            log("ClientHander : " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        String received;
        write(output, "Your name : " + name + "\nUtenti connessi:");
        for (int i = 0; i < Server.numOfUsers; i++) {
            write(output, Server.getClients().get(i).name);
        }
        parola=pickParola("src/parole.csv");
        parolaCensurata= new char[parola.length()];
        nTentativi=0;
        for (int i = 0; i < parola.length(); i++) {
            parolaCensurata[i]='*';
        }
        
        write(output,"Benvenuto, la parola da indovinare è: "+stampaParolaCensurata());
        write(output,"Per uscire dal gioco scrivi 'esci'");
        
        
        while (true) {
            received = read();
            if (received.equalsIgnoreCase(Constants.LOGOUT)) {
                this.isLosggedIn = false;
                closeSocket();
                closeStreams();
                break;
            }
            if (controllo(received))
            {
                write(output,"hai vinto");
                System.out.println(name+" ha indovinato la parola ("+ parola +")");
            }
            else
            {
                write(output,stampaParolaCensurata());
                System.out.println(name+": "+received+", parola da indovinare: "+parola+", livello censura: "+stampaParolaCensurata());
            }
        }
        closeStreams();
    }

    public static final String delimiter = ";";
    
    private String pickParola(String csvFile) {
        
        ArrayList<String> parole = new ArrayList<String>();
            try {
                File file = new File(csvFile);
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line = "";
                String[] tempArr;
                while ((line = br.readLine()) != null) {
                    tempArr = line.split(delimiter);
                    for (String tempStr : tempArr) {
                        parole.add(tempStr);
                    }
                }
                br.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        return parole.get(Constants.random(0, parole.size()));
    }

  

    private String read() {
        String line = "";
        try {
            line = input.readUTF();
        } catch (IOException ex) {
            log("read : " + ex.getMessage());
        }
        return line;
    }

    private void write(DataOutputStream output, String message) {
        try {
            output.writeUTF(message);
        } catch (IOException ex) {
            log("write : " + ex.getMessage());
        }
    }

    private void closeStreams() {
        try {
            this.input.close();
            this.output.close();
        } catch (IOException ex) {
            log("closeStreams : " + ex.getMessage());
        }
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException ex) {
            log("closeSocket : " + ex.getMessage());
        }
    }

    private void log(String msg) {
        System.out.println(msg);
    }

    
    private boolean controllo(String x)
    {
        if (x.equalsIgnoreCase(parola))
        {
            return true;
        }
        char[] xTemp = new char[x.length()];
        char[] parolaTemp = new char[parola.length()];
        for (int i = 0; i < x.length(); i++) {
            if (Character.toLowerCase(xTemp[i])==Character.toLowerCase(parolaTemp[i]))
            {
                parolaCensurata[i]=Character.toLowerCase(xTemp[i]);
            }
        }
        return false;
    }
    
    private String stampaParolaCensurata()
    {
        String temp="";
        for (int i = 0; i < parola.length(); i++) {
            temp+=parolaCensurata[i];
        }
        return temp;
    }
}
