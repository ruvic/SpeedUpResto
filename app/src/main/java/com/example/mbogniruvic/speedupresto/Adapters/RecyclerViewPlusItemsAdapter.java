package com.example.mbogniruvic.speedupresto.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.itemModels.PlusItems;

import java.util.List;

public class RecyclerViewPlusItemsAdapter extends RecyclerView.Adapter<RecyclerViewPlusItemsAdapter.MyViewHolder> {

    public final List<PlusItems> items;

    public RecyclerViewPlusItemsAdapter(List<PlusItems> items) {
        this.items = items;
    }


    @Override
    public RecyclerViewPlusItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_plus_item_model, parent, false);
        return new RecyclerViewPlusItemsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewPlusItemsAdapter.MyViewHolder holder, int position) {
        PlusItems item=items.get(position);
        holder.item_title.setText(item.title);
        holder.item_value.setText(item.value);
    }

    @Override
    public int getItemCount() {
        Log.e("erreur1", "Errrrrrr");
        return this.items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item_title, item_value;
        public MyViewHolder(View view) {
            super(view);
            item_title=(TextView)view.findViewById(R.id.plus_item_title);
            item_value=(TextView)view.findViewById(R.id.plus_item_value);
        }
    }
}
