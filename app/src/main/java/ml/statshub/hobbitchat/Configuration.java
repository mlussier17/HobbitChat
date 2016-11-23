package ml.statshub.hobbitchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Configuration extends AppCompatActivity {
    //IP Const
    final String COMTE = "230.0.0.1";
    final String MORDOR = "230.0.0.2";
    final String ISENGARD = "230.0.0.3";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void nextPage(View view) {
        Intent intent = new Intent(this, Communication.class);
        startActivity(intent);
    }
}
