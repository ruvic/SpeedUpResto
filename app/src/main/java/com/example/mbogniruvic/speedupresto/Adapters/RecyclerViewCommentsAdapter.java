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

import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.itemModels.CommentItem;
import com.example.mbogniruvic.speedupresto.itemModels.CommentItemsModel;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewCommentsAdapter extends RecyclerView.Adapter<RecyclerViewCommentsAdapter.ViewHolder> {

    private final List<CommentItemsModel> data;
    private Context context;
    private SparseBooleanArray expandState = new SparseBooleanArray();

    public RecyclerViewCommentsAdapter(List<CommentItemsModel> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            expandState.append(i, false);
        }
    }

    @Override
    public RecyclerViewCommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new RecyclerViewCommentsAdapter.ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_profile_comment_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewCommentsAdapter.ViewHolder holder, final int position) {
        final CommentItemsModel commentModel = data.get(position);

        holder.setIsRecyclable(false);
        List<CommentItem> items=new ArrayList<>();
        CommentItem cmItem=null;
        for (int i=0; i<commentModel.comments.size(); i++){
            cmItem=new CommentItem("Paul Xavier",
                    commentModel.comments.get(i).getComment(),
                    commentModel.comments.get(i).getDate(),
                    commentModel.comments.get(i).getNote()+"/10");
            items.add(cmItem);
        }

        RecyclerViewCommentItemsAdapter mAdapter = new RecyclerViewCommentItemsAdapter(items);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.context);
        holder.recyclerView.setLayoutManager(mLayoutManager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(mAdapter);

        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, commentModel.colorId1));
        holder.expandableLayout.setInRecyclerView(true);
        holder.expandableLayout.setBackgroundColor(ContextCompat.getColor(context, commentModel.colorId2));
        holder.expandableLayout.setInterpolator(commentModel.interpolator);
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
            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerV_profile_comment_items);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.comment_button);
            expandableLayout = (ExpandableLinearLayout) v.findViewById(R.id.comment_expandableLayout);
        }
    }


    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}
