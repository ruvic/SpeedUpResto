package com.example.mbogniruvic.speedupresto.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mbogniruvic.speedupresto.MenuDetailsActivity;
import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.models.MenuItem;

import org.w3c.dom.Text;

import java.util.List;

public class CategorieItemsAdapter extends RecyclerView.Adapter<CategorieItemsAdapter.MyViewHolder> {

    List<MenuItem> categorieItemsList;
    Context context;
    boolean isVoirPlus=false;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView prix, nom;
        CardView cardView;
        public MyViewHolder(View view) {

            super(view);

            if(isVoirPlus) {
                nom=(TextView)view.findViewById(R.id.voir_plus_menu_cat);
                prix=(TextView)view.findViewById(R.id.voir_plus_prix_cat);
                cardView=(CardView) view.findViewById(R.id.voir_plus_card);
            }else{
                nom=(TextView)view.findViewById(R.id.menu_cat);
                prix=(TextView)view.findViewById(R.id.prix_cat);
                cardView=(CardView) view.findViewById(R.id.card);
            }

        }
    }

    public CategorieItemsAdapter(List<MenuItem> categorieItemsList) {
        this.categorieItemsList = categorieItemsList;
    }

    public CategorieItemsAdapter(List<MenuItem> categorieItemsList, boolean isVoirPlus) {
        this.categorieItemsList = categorieItemsList;
        this.isVoirPlus=isVoirPlus;
    }

    @Override
    public CategorieItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        View itemView = null;
        if(isVoirPlus) itemView=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.voir_plus_menu_list_row, parent, false);
        else itemView=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_list_row, parent, false);
        return new CategorieItemsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategorieItemsAdapter.MyViewHolder holder, int position) {
        final MenuItem menu=categorieItemsList.get(position);
        holder.nom.setText(menu.getNom());
        holder.prix.setText(menu.getPrice()+" FCFA");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context.getApplicationContext(), MenuDetailsActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorieItemsList.size();
    }
}
