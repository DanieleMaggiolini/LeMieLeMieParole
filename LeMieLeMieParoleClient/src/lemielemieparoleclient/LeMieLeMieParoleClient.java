/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lemielemieparoleclient;

import java.net.UnknownHostException;

/**
 *
 * @author occhiato_andrea
 */
public class LeMieLeMieParoleClient {

    public static void main(String[] args) throws UnknownHostException {
        FinestraServer finestra = new FinestraServer();
        finestra.setVisible(true);
        Client client = new Client("", 1);
        client.readMessageThread();
        client.writeMessageThread();
    }
}
