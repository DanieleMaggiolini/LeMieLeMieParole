package lemielemieparole;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

public class LeMieLeMieParole {

    public static void main(String[] args) throws UnknownHostException {
        Server server = new Server();
        JOptionPane.showMessageDialog(null,"L'indirizzo IP del server è: "+InetAddress.getLocalHost()+"\nLa porta è: "+Constants.PORT,"Indirizzo server", JOptionPane.INFORMATION_MESSAGE);
        server.waitConnection();
    }
    
}
