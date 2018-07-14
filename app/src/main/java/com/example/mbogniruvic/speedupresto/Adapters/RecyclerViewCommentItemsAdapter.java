package com.example.mbogniruvic.speedupresto.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.itemModels.CommentItem;

import java.util.List;

public class RecyclerViewCommentItemsAdapter extends RecyclerView.Adapter<RecyclerViewCommentItemsAdapter.MyViewHolder> {

    public final List<CommentItem> items;

    public RecyclerViewCommentItemsAdapter(List<CommentItem> items) {
        this.items = items;
    }


    @Override
    public RecyclerViewCommentItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_comment_item_model, parent, false);
        return new RecyclerViewCommentItemsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewCommentItemsAdapter.MyViewHolder holder, int position) {
        CommentItem item=items.get(position);
        holder.item_user.setText(item.user);
        holder.item_comment.setText(item.comment);
        holder.item_date.setText(item.date);
        holder.item_note.setText(item.note);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item_user, item_comment, item_date, item_note;
        public MyViewHolder(View view) {
            super(view);
            item_user=(TextView)view.findViewById(R.id.comment_item_user);
            item_comment=(TextView)view.findViewById(R.id.comment_item_value);
            item_date=(TextView)view.findViewById(R.id.comment_item_date);
            item_note=(TextView)view.findViewById(R.id.comment_item_note);
        }
    }

}
