package ml.statshub.hobbitchat;

import java.net.*;
/**
 * Created by 201327549 on 2016-11-18.
 */

public class EmetteurMulticast {
    static final String GROUPE = "225.0.0.0";
    static final int PORT = 10000;
    static final int DELAI = 2000;

    public void emettre(String chaine) {
        try {
            byte tampon[] = chaine.getBytes();
            InetAddress adresse = InetAddress.getByName(GROUPE);
            DatagramPacket paquet =
                    new DatagramPacket(tampon, 0, tampon.length, adresse, PORT);

            // dans l'émetteur, l'important est de créer un MulticastSocket au
            // lieu d'un DatagramSocket
            MulticastSocket socket = new MulticastSocket();

            boolean fini = false;
            while (!fini) {
                socket.send(paquet);
                try {
                    Thread.sleep(DELAI);
                } catch( InterruptedException ie ) {}
            }
        } catch (Exception e) {
            System.err.println("Houston we have a problem:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String args[]) {
        new EmetteurMulticast().emettre("Ceci est un message de multicast");
    }
}