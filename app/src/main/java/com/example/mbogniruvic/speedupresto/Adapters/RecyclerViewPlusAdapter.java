package com.example.mbogniruvic.speedupresto.Adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.itemModels.ContactItemModel;
import com.example.mbogniruvic.speedupresto.itemModels.PlusItemModels;
import com.example.mbogniruvic.speedupresto.itemModels.PlusItems;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewPlusAdapter extends RecyclerView.Adapter<RecyclerViewPlusAdapter.ViewHolder> {

    private final List<PlusItemModels> data;
    private Context context;
    private SparseBooleanArray expandState = new SparseBooleanArray();

    public RecyclerViewPlusAdapter(List<PlusItemModels> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            expandState.append(i, false);
        }
    }

    @Override
    public RecyclerViewPlusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new RecyclerViewPlusAdapter.ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_profile_plus_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewPlusAdapter.ViewHolder holder, final int position) {
        final PlusItemModels plusItemModels = data.get(position);

        holder.setIsRecyclable(false);

        List<PlusItems> items=new ArrayList<>();
        PlusItems item=new PlusItems("Note", plusItemModels.restaurant.getNote()+"");
        items.add(item);

        item=new PlusItems("Nombre de notes", plusItemModels.restaurant.getNber_note()+"");
        items.add(item);

        item=new PlusItems("Minimum de livraison", plusItemModels.restaurant.getMin_order()+"");
        items.add(item);

        item=new PlusItems("Frais de livraison", plusItemModels.restaurant.getFee_delivery()+"");
        items.add(item);

        item=new PlusItems("Durée de livraison", plusItemModels.restaurant.getTime_delivery()+"");
        items.add(item);

        String etat=(plusItemModels.restaurant.isState())?"Compte activé":"Compte désactivé";
        item=new PlusItems("Etat", etat);
        items.add(item);

        RecyclerViewPlusItemsAdapter mAdapter = new RecyclerViewPlusItemsAdapter(items);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.context);
        holder.recyclerView.setLayoutManager(mLayoutManager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(mAdapter);


        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, plusItemModels.colorId1));
        holder.expandableLayout.setInRecyclerView(true);
        holder.expandableLayout.setBackgroundColor(ContextCompat.getColor(context, plusItemModels.colorId2));
        holder.expandableLayout.setInterpolator(plusItemModels.interpolator);
        holder.expandableLayout.setExpanded(expandState.get(position));
        holder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotateAnimator(holder.buttonLayout, 0f, 180f).start();
                expandState.put(position, true);
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(holder.buttonLayout, 180f, 0f).start();
                expandState.put(position, false);
            }
        });

        holder.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.expandableLayout);
            }
        });
    }

    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * You must use the ExpandableLinearLayout in the recycler view.
         * The ExpandableRelativeLayout doesn't work.
         */
        public ExpandableLinearLayout expandableLayout;
        public RecyclerView recyclerView;
        public RelativeLayout buttonLayout;

        public ViewHolder(View v) {
            super(v);
            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerV_profile_plus_items);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.plus_button);
            expandableLayout = (ExpandableLinearLayout) v.findViewById(R.id.plus_expandableLayout);
        }
    }


    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}
