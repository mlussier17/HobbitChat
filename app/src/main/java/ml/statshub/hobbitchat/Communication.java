package ml.statshub.hobbitchat;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
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
    private ReceveurMulticast receiver;

    public InetAddress inetAdresseMulticast;
    public CheckBox choice;

    public String _autreUser;
    public String _chaine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
        choice = (CheckBox)findViewById(R.id.choice);

        _edtMessage = (EditText) findViewById(R.id.editText);
        _tvMessages = (TextView) findViewById(R.id.textView);
        _tvMessages.setMovementMethod(new ScrollingMovementMethod());
        // GET IP, port, username
        getExtrasFromConfiguration();

        // TODO
        // Receive other shits
        String mess = null;
        receiver = new ReceveurMulticast(_ip, _port);
        receiver.execute();

        _tvMessages.append("\n");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receiver.cancel();
    }

    public void sendMessage(View view) {
        if (_edtMessage.getText().length() > 0) {
            //_tvMessages.append(_user + DEUX_POINT + _edtMessage.getText().toString() + "\n");
            // Send to other users
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String chaine = _user+ ":" + _edtMessage.getText().toString();
                    byte tampon[] = chaine.getBytes();
                    DatagramPacket paquet = new DatagramPacket(tampon, 0, tampon.length, inetAdresseMulticast, _port);
                    try {
                        MulticastSocket socket = new MulticastSocket();
                        socket.send(paquet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

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



    public class ReceveurMulticast extends AsyncTask<Void, String, Void> {
        byte tampon[] = new byte[LONG_TAMPON];
        MulticastSocket multicastSocketUDP;
        private Boolean _listening = false;
        String GROUPE;
        int PORT;
        DatagramPacket datagramPacket;

        public ReceveurMulticast(String ip, int port) {
            GROUPE = ip;
            PORT = port;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                inetAdresseMulticast = InetAddress.getByName(GROUPE);
                datagramPacket = new DatagramPacket(tampon, 0, LONG_TAMPON);
                multicastSocketUDP = new MulticastSocket(_port);
                multicastSocketUDP.joinGroup(inetAdresseMulticast);
                _listening = true;
            }
            catch (Exception ex) {
                ex.getMessage();
            }
        }

        @Override
        protected Void doInBackground(Void... args) {
            try
            {
                while (_listening) {
                    multicastSocketUDP.receive((datagramPacket));
                    _chaine = new String(datagramPacket.getData(),datagramPacket.getOffset(), datagramPacket.getLength());
                    _autreUser = datagramPacket.getSocketAddress().toString();
                    _autreUser = _autreUser.substring(1,_autreUser.indexOf(":"));
                    publishProgress(_chaine);
                }
            }
            catch (Exception ex) {
                ex.getMessage();
                ex.printStackTrace();
                System.exit(0);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... valeurs) {
            super.onProgressUpdate();

            if (choice.isChecked()) {
                choice.setText(R.string.username);
                int deleteUser = _chaine.indexOf(":");
                String message = _chaine.substring(deleteUser, _chaine.length());

                _tvMessages.append(_autreUser + message + "\n");
            }
            else {
                choice.setText(R.string.IP);
                _chaine = valeurs[0];
                _tvMessages.append(_chaine + "\n");
            }
        }

        @Override
        protected void onPostExecute(Void resultat) {
            try {
                multicastSocketUDP.leaveGroup(inetAdresseMulticast);
            }
            catch (Exception ex) {
                ex.getMessage();
            }

        }
        public void cancel(){
            _listening = false;
        }
    }
}
