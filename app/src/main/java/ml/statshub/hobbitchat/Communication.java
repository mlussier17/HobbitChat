package ml.statshub.hobbitchat;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Communication extends AppCompatActivity {
    // Constantes
    public final int LONG_TAMPON = 1024;
    public final String DEUX_POINT = " : ";

    //Variables
    private String _user;
    private int _port;
    private String _ip;
    private EditText _edtMessage;
    private TextView _tvMessages;
    public Boolean _listening = false;
    public InetAddress inetAdresseMulticast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        _edtMessage = (EditText) findViewById(R.id.editText);
        _tvMessages = (TextView) findViewById(R.id.textView);

        // GET IP, port, username
        getExtrasFromConfiguration();

        // TODO
        // Receive other shits
        String mess = null;
        new ReceveurMulticast(_ip, _port).execute();

        _tvMessages.append("\n" + _user + DEUX_POINT + mess);
    }

    public void sendMessage(View view) {
        if (_edtMessage.getText().length() > 0) {
            _tvMessages.append("\n" + _user + DEUX_POINT + _edtMessage.getText().toString());
            // Send to other users
            new EmetteurMulticast(_ip, _port, _user + " : " + _edtMessage.getText().toString()).execute();

            // Clear EditText
            _edtMessage.setText("");
            _edtMessage.setError(null);
        }
        else {
            _edtMessage.setError("Veuillez inscrire un message");
        }
    }

    private void getExtrasFromConfiguration() {
        // Get extras
        Bundle bundle = getIntent().getExtras();
        _ip = bundle.getString("IP");
        _port = bundle.getInt("port");
        _user = bundle.getString("username");
    }



    public class ReceveurMulticast extends AsyncTask<Void, Void, Void> {
        byte tampon[] = new byte[LONG_TAMPON];
        MulticastSocket multicastSocketUDP;
        String GROUPE;
        int PORT;
        DatagramPacket datagramPacket;

        public ReceveurMulticast(String ip, int port) {
            GROUPE = ip;
            PORT = port;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            try
            {
                datagramPacket = new DatagramPacket(tampon, 0, LONG_TAMPON);
                multicastSocketUDP = new MulticastSocket(_port);
                multicastSocketUDP.joinGroup(inetAdresseMulticast);
                _listening = true;
            }
            catch (Exception ex)
            {
                ex.getMessage();
            }
        }

        @Override
        protected Void doInBackground(Void... args)
        {
            try
            {
                while (_listening)
                {
                    multicastSocketUDP.receive((datagramPacket));
                    String chaine = new String(datagramPacket.getData(),datagramPacket.getOffset(), datagramPacket.getLength());
                    publishProgress(chaine);
                }
            }
            catch (Exception ex)
            {
                ex.getMessage();
                ex.printStackTrace();
                System.exit(0);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... valeurs)
        {
            super.onProgressUpdate();
            String message;
            if (true/*checkBoxIp.isChecked()*/)
            {
                message = valeurs[0].substring(0, valeurs[0].indexOf(":")) + datagramPacket.getSocketAddress()+ ":"+valeurs[0].substring(valeurs[0].lastIndexOf(":") + 1);
            }
            else
            {
                message = valeurs[0];
            }
            _tvMessages.append(message + "\n");
        }

        @Override
        protected void onPostExecute(Void resultat)
        {
            try
            {
                multicastSocketUDP.leaveGroup(inetAdresseMulticast);
            }
            catch (Exception ex)
            {
                ex.getMessage();
            }
            _listening = false;
        }
    }
}
