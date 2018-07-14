package com.example.mbogniruvic.speedupresto.Adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.itemModels.ContactItemModel;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import java.util.List;

public class RecyclerViewContactsAdapter extends RecyclerView.Adapter<RecyclerViewContactsAdapter.ViewHolder> {

    private final List<ContactItemModel> data;
    private Context context;
    private SparseBooleanArray expandState = new SparseBooleanArray();

    public RecyclerViewContactsAdapter(List<ContactItemModel> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            expandState.append(i, true);
        }
    }

    @Override
    public RecyclerViewContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new RecyclerViewContactsAdapter.ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_profile_contact_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewContactsAdapter.ViewHolder holder, final int position) {
        final ContactItemModel item = data.get(position);
        holder.setIsRecyclable(false);
        holder.phoneTextView.setText(item.phone);
        holder.emailTextView.setText(item.email);
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, item.colorId1));
        holder.expandableLayout.setInRecyclerView(true);
        holder.expandableLayout.setBackgroundColor(ContextCompat.getColor(context, item.colorId2));
        holder.expandableLayout.setInterpolator(item.interpolator);
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
        public TextView phoneTextView;
        public TextView emailTextView;
        public RelativeLayout buttonLayout;

        public ViewHolder(View v) {
            super(v);
            phoneTextView = (TextView) v.findViewById(R.id.phone_text);
            emailTextView = (TextView) v.findViewById(R.id.email_text);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.contact_button);
            expandableLayout = (ExpandableLinearLayout) v.findViewById(R.id.contact_expandableLayout);
        }
    }


    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

}
