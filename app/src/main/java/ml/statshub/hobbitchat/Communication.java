package ml.statshub.hobbitchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Communication extends AppCompatActivity {
    // Constantes
    private final String DEUX_POINT = " : ";

    //Variables
    private String _user;
    private EditText _edtMessage;
    private TextView _tvMessages;
    private Intent _intent;
    private RecepteurUpdates _getUpdates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        _edtMessage = (EditText) findViewById(R.id.editText);
        _tvMessages = (TextView) findViewById(R.id.textView);

        if (savedInstanceState == null) {
            strtService();
        }

        strtRecepteur();
    }

    public void sendMessage(View view) {
        // TODO
        // Send to other users

        if (_edtMessage.getText().length() > 0) {
            if (_user == null) _user = "bob";

            _tvMessages.setText(_getUpdates.toString());
            _tvMessages.append("\n" + _user + DEUX_POINT + _edtMessage.getText());
            _edtMessage.setText("");
            _edtMessage.setError(null);
        }
        else {
            _edtMessage.setError("Veuillez inscrire un message");
        }
    }

    private void strtService() {
        _intent = new Intent(this, ServiceCommunication.class);
        startService(_intent);

        Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();
    }
    private void strtRecepteur() {
        String filterName = "com.leemartinez.joaquin.CHAT";
        IntentFilter filter = new IntentFilter(filterName);

        _getUpdates = new RecepteurUpdates();
    }

    public class RecepteurUpdates extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("com.leemartinez.joaquin.MESSAGE");

            _tvMessages.setText("\n" + message);
        }
    }
}
