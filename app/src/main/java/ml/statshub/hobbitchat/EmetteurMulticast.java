package ml.statshub.hobbitchat;

import android.os.AsyncTask;

import java.net.*;
/**
 * Created by 201327549 on 2016-11-18.
 */

public class EmetteurMulticast extends AsyncTask<Void, Void, String> {
    static String GROUPE;
    static int PORT;
    static final int DELAI = 2000;
    static String MESSAGE;

    public EmetteurMulticast(String ip, int port, String message) {
        GROUPE = ip;
        PORT = port;
        MESSAGE = message;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            byte tampon[] = MESSAGE.getBytes();
            InetAddress adresse = InetAddress.getByName(GROUPE);
            DatagramPacket paquet = new DatagramPacket(tampon, 0, tampon.length, adresse, PORT);

            // dans l'émetteur, l'important est de créer un MulticastSocket au
            // lieu d'un DatagramSocket
            MulticastSocket socket = new MulticastSocket();

            boolean fini = false;
            while (!fini) {
                socket.send(paquet);
                tampon = new byte[0];
                fini = true;
                try {
                    Thread.sleep(DELAI);
                } catch( InterruptedException ie ) {}
            }
        } catch (Exception e) {
            System.err.println("Houston we have a problem!!");
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}