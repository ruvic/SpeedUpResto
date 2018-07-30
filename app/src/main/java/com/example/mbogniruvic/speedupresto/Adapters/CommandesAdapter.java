package com.example.mbogniruvic.speedupresto.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.Tasks.DownLoadImageTask;
import com.example.mbogniruvic.speedupresto.models.Commande;

import java.util.List;

public class CommandesAdapter extends RecyclerView.Adapter<CommandesAdapter.MyViewHolder> {

    List<Commande> commandesList;
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView menu, montant, qte, heure;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);

            menu=(TextView)view.findViewById(R.id.menu_title);
            montant=(TextView)view.findViewById(R.id.montant);
            qte=(TextView)view.findViewById(R.id.qte);
            heure=(TextView)view.findViewById(R.id.heure);
            image=(ImageView)view.findViewById(R.id.cmd_img);

        }
    }

    public CommandesAdapter(List<Commande> commandesList) {
        this.commandesList = commandesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commande_list_row, parent, false);
        context=itemView.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Commande cmd= this.commandesList.get(position);
        holder.menu.setText(cmd.getMenu().getNom());
        holder.montant.setText(cmd.getMontant()+" FCFA");
        holder.qte.setText(cmd.getQte()+"");
        holder.heure.setText(cmd.getHeure());
        new DownLoadImageTask(holder.image).execute(cmd.getMenu().getImage());

    }

    @Override
    public int getItemCount() {
        return commandesList.size();
    }


}
