package com.example.mbogniruvic.speedupresto.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.MenuActivity;
import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.VoirPusMenusActivity;
import com.example.mbogniruvic.speedupresto.models.CategoryMenu;
import com.example.mbogniruvic.speedupresto.models.MenuItem;

import java.util.List;

public class CategoriesAdapters extends RecyclerView.Adapter<CategoriesAdapters.MyViewHolder> {

    private List<CategoryMenu> categoryMenuList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView categorie, voirPlus;
        RecyclerView categorieItems;

        public MyViewHolder(View view) {
            super(view);

            categorie=(TextView)view.findViewById(R.id.nom_categorie);
            categorieItems=(RecyclerView)view.findViewById(R.id.recyclerV_categorieItems);
            voirPlus=(TextView)view.findViewById(R.id.voir_plus);

        }
    }

    public CategoriesAdapters(List<CategoryMenu> categoryMenuList) {
        this.categoryMenuList = categoryMenuList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categorie_list_row, parent, false);
        context=parent.getContext();
        return new CategoriesAdapters.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CategoryMenu cat=categoryMenuList.get(position);
        holder.categorie.setText(cat.getCategorie());

        //Toast.makeText(context, "passage 1 : "+holder.categorie.getText(), Toast.LENGTH_SHORT).show();

        CategorieItemsAdapter mAdapter = new CategorieItemsAdapter(cat.getMenus(), cat);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL, false);
        holder.categorieItems.setLayoutManager(mLayoutManager);
        holder.categorieItems.setItemAnimator(new DefaultItemAnimator());
        holder.categorieItems.setAdapter(mAdapter);

        holder.voirPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(context, VoirPusMenusActivity.class);
            intent.putExtra(MenuActivity.CAT_OBJECT_TAG, cat);
            context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryMenuList.size();
    }


}
