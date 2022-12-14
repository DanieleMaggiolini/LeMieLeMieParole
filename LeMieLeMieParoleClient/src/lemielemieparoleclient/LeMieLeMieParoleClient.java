/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lemielemieparoleclient;

import static java.lang.Thread.sleep;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

/**
 *
 * @author occhiato_andrea
 */
public class LeMieLeMieParoleClient {
    
    public static String IP="";
    public static int PORTA=-1;
    public static boolean CONNESSO=false;
    
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        
        FinestraGioco finestragioco = new FinestraGioco();
        Client client=new Client("",0,finestragioco);
        String controlloIP="";
        while(CONNESSO==false)
        {
            FinestraServer finestraserver = new FinestraServer();
            finestraserver.setVisible(true);
            while(IP==controlloIP||PORTA==-1)
            {
                sleep(500);   
            }
            client = new Client(IP, PORTA, finestragioco);
            if (!CONNESSO)
            {
                JOptionPane.showMessageDialog(null, "Impossibile connettersi a " + IP, "", JOptionPane.ERROR_MESSAGE);
                controlloIP=IP;
            }
        }
        finestragioco.setClient(client);
        client.readMessageThread();
        while(!client.chiudendo)
        {
            finestragioco.setVisible(true);
        }
        finestragioco.dispose();
    }
}
