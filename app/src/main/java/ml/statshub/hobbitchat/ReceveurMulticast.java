package ml.statshub.hobbitchat;

import android.os.AsyncTask;

import java.net.*;
/**
 * Created by 201327549 on 2016-11-18.
 */

/*public class ReceveurMulticast extends AsyncTask<Void, Void, Void> {
    static String GROUPE;
    static final int LONG_TAMPON = 1024;
    static int PORT;

    public ReceveurMulticast(String ip, int port) {
        GROUPE = ip;
        PORT = port;
    }

    @Override
    protected void onProgressUpdate(Void... progress) {
        super.onProgressUpdate();
    }

    @Override
    protected Void doInBackground(Void... params) {
        String chaine = "";

        while (true) {
            try {
                byte tampon[] = new byte[LONG_TAMPON];
                DatagramPacket paquet = new DatagramPacket(tampon, LONG_TAMPON);

                // dans le récepteur, il faut aussi créer un MulticastSocket au
                // lieu d'un DatagramSocket
                MulticastSocket socket = new MulticastSocket(PORT);

                // en plus, il faut joindre le groupe
                InetAddress adresse = InetAddress.getByName(GROUPE);
                socket.joinGroup(adresse);

                boolean fini = false;
                while (!fini) {
                    socket.receive(paquet);
                    chaine = new String(paquet.getData(), paquet.getOffset(), paquet.getLength());
                    System.out.println("Message: " + chaine);
                    System.out.println("Recu de " + paquet.getSocketAddress() + "\n");
                }

                // et éventuellement quitter le groupe
                socket.leaveGroup(adresse);
            } catch( Exception e) {
                System.err.println("Houston we have a problem:");
                e.printStackTrace();
                System.exit(1);
            }
            publishProgress();
        }
    }
}*/