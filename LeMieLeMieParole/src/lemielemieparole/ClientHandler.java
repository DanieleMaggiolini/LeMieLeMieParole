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
    String parolaCensurata;
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
        write(output, "Your name : " + name);
        parola=pickParola("src/parole.csv");
        parolaCensurata= "";
        nTentativi=0;
        for (int i = 0; i < parola.length(); i++) {
            parolaCensurata+="*";
        }
        
        write(output,"Benvenuto, la parola da indovinare è: "+parolaCensurata);
        write(output,"Per uscire dal gioco scrivi 'esci'");
        
        
        while (true) {
            received = read();
            if (received.equalsIgnoreCase(Constants.LOGOUT)) {
                System.out.println("disconnessione");
                this.isLosggedIn = false;
                closeSocket();
                closeStreams();
                break;
            }
            nTentativi++;
            if (controllo(received))
            {
                write(output,"Hai vinto! Numero di tentativi: "+nTentativi);
                System.out.println(name+" ha indovinato la parola ("+ parola +")");
            }
            else
            {
                write(output,"Parola: "+parolaCensurata+", Tentativo n°: "+nTentativi);
                System.out.println(name+": "+received+", parola da indovinare: "+parola+", livello censura: "+parolaCensurata);
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
        return parole.get(Constants.random(0, parole.size()-1));
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
        int min=x.length();
        if (parola.length()<x.length())
        {
            min=parola.length();
        }
        for (int i = 0; i < min; i++) {
            if (Character.toLowerCase(x.charAt(i))==Character.toLowerCase(parola.charAt(i)))
            {
                var temp = parolaCensurata.toCharArray();
                temp[i]=Character.toLowerCase(x.charAt(i));
                parolaCensurata="";
                for (int j = 0; j < temp.length; j++) {
                    parolaCensurata+=temp[j];
                }
            }
        }
        return false;
    }
}
