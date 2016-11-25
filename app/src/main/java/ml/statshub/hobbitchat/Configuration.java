package ml.statshub.hobbitchat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Configuration extends AppCompatActivity {
    //IP Const
    final String COMTE = "230.0.0.1";
    final String MORDOR = "230.0.0.2";
    final String ISENGARD = "230.0.0.3";
    private EditText port;
    private EditText username;
    private Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        port = (EditText)findViewById(R.id.port);
        username = (EditText)findViewById(R.id.username);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    public void nextPage(View view) {
        String city;
        if (checkPort() && checkUsername()) {
            Bundle b = new Bundle();
            city = spinner.getSelectedItem().toString();
            switch (city){
                case "Comté":
                    b.putString("IP",COMTE);
                    break;
                case "Mordor":
                    b.putString("IP",MORDOR);
                    break;
                case "Isengard":
                    b.putString("IP",ISENGARD);
                    break;
            }
            b.putInt("port", Integer.parseInt(port.getText().toString()));
            b.putString("username",username.getText().toString());
            Intent intent = new Intent(this, Communication.class);
            intent.putExtras(b);
            startActivity(intent);
        }
    }

    public Boolean checkPort(){
        int validate = Integer.parseInt(port.getText().toString());
        if(validate > 1023 && validate < 65536) return true;
        else {
            Toast.makeText(Configuration.this, R.string.error_port,Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public Boolean checkUsername(){
        String validate = username.getText().toString();
        if(validate.length() >= 2 && validate.length() <= 8) return true;
        else {
            Toast.makeText(Configuration.this,R.string.error_username,Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
