package com.example.mbogniruvic.speedupresto.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Utils.NotificationGenerator;
import com.example.mbogniruvic.speedupresto.models.Commande;
import com.example.mbogniruvic.speedupresto.models.Restaurant;

import java.util.List;

public class NotificationTask extends AsyncTask<Void, Void, Void> {

    Context context;
    private Restaurant restaurant;
    private List<Commande> commandeList;

    public NotificationTask(Context context, Restaurant resto, List<Commande> commandeList) {
        this.context = context;
        this.restaurant=resto;
        this.commandeList=commandeList;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        while (true){
            try {
                Thread.sleep(30000);
                getCommandes();
            } catch (InterruptedException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private List<Commande>  getCommandes(){
        NotificationGenerator.openActivityNotification(context);
        return commandeList;
    }
}
