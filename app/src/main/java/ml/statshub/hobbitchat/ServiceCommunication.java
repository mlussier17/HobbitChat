package ml.statshub.hobbitchat;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

/**
 * Created by 201327549 on 2016-11-16.
 */

public class ServiceCommunication extends IntentService {
    // Constructeur
    public ServiceCommunication() {
        // Utile seulement au débogage, mais obligatoire
        super("Service");
    }

    // Reçoit un objet Intent, lance un thread en arrière-plan et
    // arrête le service lorsque terminé
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            // Le paramètre est le nom de l'action
            Intent intentLocal = new Intent("com.leemartinez.joaquin.CHAT");

            // Traitement (ici seulement un compteur pour simuler un
            // traitement)
            for (int i = 0; i < 50; i++) {
                String message = "N = " + i;
                intentLocal.putExtra("com.leemartinez.joaquin.MESSAGE", message);

                // Diffuse l'objet Intent avec ses données
                LocalBroadcastManager.getInstance(this).sendBroadcast(intentLocal);

                // Petite pause (pour simuler un traitement pas trop
                // rapide quand même)
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {}
            }
        }
    }

    // Méthode appelée au retour de onHandleIntent()
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service is fucking dead", Toast.LENGTH_SHORT).show();
    }
}
