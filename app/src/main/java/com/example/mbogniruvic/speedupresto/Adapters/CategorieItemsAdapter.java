package com.example.mbogniruvic.speedupresto.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.MenuActivity;
import com.example.mbogniruvic.speedupresto.MenuDetailsActivity;
import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.Tasks.DownLoadImageTask;
import com.example.mbogniruvic.speedupresto.Utils.ConnectionStatus;
import com.example.mbogniruvic.speedupresto.models.CategoryMenu;
import com.example.mbogniruvic.speedupresto.models.MenuItem;

import org.w3c.dom.Text;

import java.util.List;

public class CategorieItemsAdapter extends RecyclerView.Adapter<CategorieItemsAdapter.MyViewHolder> {

    CategoryMenu currentCat;
    List<MenuItem> categorieItemsList;
    Context context;
    boolean isVoirPlus=false;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView prix, nom;
        CardView cardView;
        ImageView image_menu;
        public MyViewHolder(View view) {

            super(view);

            if(isVoirPlus) {
                nom=(TextView)view.findViewById(R.id.voir_plus_menu_cat);
                prix=(TextView)view.findViewById(R.id.voir_plus_prix_cat);
                cardView=(CardView) view.findViewById(R.id.voir_plus_card);
                image_menu=(ImageView)view.findViewById(R.id.voir_plus_image_menu);
            }else{
                nom=(TextView)view.findViewById(R.id.menu_cat);
                prix=(TextView)view.findViewById(R.id.prix_cat);
                cardView=(CardView) view.findViewById(R.id.cat_menu_card);
                image_menu=(ImageView)view.findViewById(R.id.image_menu);
            }

        }
    }

    public CategorieItemsAdapter(List<MenuItem> categorieItemsList, CategoryMenu currentCat) {
        this.categorieItemsList = categorieItemsList;
        this.currentCat=currentCat;
    }

    public CategorieItemsAdapter(List<MenuItem> categorieItemsList, CategoryMenu cat, boolean isVoirPlus) {
        this.categorieItemsList = categorieItemsList;
        this.isVoirPlus=isVoirPlus;
        this.currentCat=cat;
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

        if (ConnectionStatus.getInstance(context).isOnline()) {
            new DownLoadImageTask(holder.image_menu).execute(menu.getImage());
        } else {
            new DownLoadImageTask(holder.image_menu, menu.getId()).execute(menu.getImage());
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context.getApplicationContext(), MenuDetailsActivity.class);
                intent.putExtra(MenuActivity.MENU_ITEM_TAG, menu);
                intent.putExtra(MenuActivity.MENU_ITEM_VOIR_PLUS_TAG, isVoirPlus);
                intent.putExtra(MenuActivity.CAT_CURRENT_TAG, currentCat);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorieItemsList.size();
    }
}
