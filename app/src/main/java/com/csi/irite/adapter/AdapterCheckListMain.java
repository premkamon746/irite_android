package com.csi.irite.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csi.irite.R;
import com.csi.irite.utils.Tools;
import com.csi.irite.utils.ViewAnimation;

import java.util.ArrayList;
import java.util.List;

public class AdapterCheckListMain extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //private List<ChecklistMain> items = new ArrayList<>();


    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public interface OnItemClickListener {
        //void onItemClick(View view, ChecklistMain obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    /*public AdapterCheckListMain(Context context, List<ChecklistMain> items) {
        Log.d("ddddddt", String.valueOf(items.size()));
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView date;

        public OriginalViewHolder(View v) {
            super(v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d("ddddddt", String.valueOf(items.size()));
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final ChecklistMain p = items.get(position);
            view.date.setText(p.getDate());

            int colorId = position % 2 == 0 ? R.color.blue_50 : R.color.black;
            holder.itemView.setBackgroundColor(ctx.getResources().getColor(colorId));

        }
    }

    private boolean toggleLayoutExpand(boolean show, View view, View lyt_expand) {
        Tools.toggleArrow(show, view);
        if (show) {
            ViewAnimation.expand(lyt_expand);
        } else {
            ViewAnimation.collapse(lyt_expand);
        }
        return show;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }*/

}