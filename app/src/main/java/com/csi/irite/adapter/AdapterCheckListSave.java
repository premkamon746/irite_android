package com.csi.irite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csi.irite.R;
//import com.csi.irite.room.data.Checklist;
import com.csi.irite.utils.Tools;
import com.csi.irite.utils.ViewAnimation;

import java.util.ArrayList;
import java.util.List;

public class AdapterCheckListSave extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //private List<Checklist> items = new ArrayList<>();


    private Context ctx;
    private OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        //void onItemClick(View view, Checklist obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    /*public AdapterCheckListSave(Context context, List<Checklist> items) {
        this.items = items;
        ctx = context;
    }*/

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView head;
        public TextView listname;
        public ImageButton bt_expand;
        public View lyt_expand;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            head = (TextView) v.findViewById(R.id.head);
            listname = (TextView) v.findViewById(R.id.listname);
            bt_expand = (ImageButton) v.findViewById(R.id.bt_expand);
            lyt_expand = (View) v.findViewById(R.id.lyt_expand);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    // Replace the contents of a view (invoked by the layout manager)
    /*@Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final Checklist p = items.get(position);
            view.head.setText(p.getHead());
            view.listname.setText(p.getListname());
            //Tools.displayImageOriginal(ctx, view.image, String.valueOf(p.image));
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            view.bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //boolean show = toggleLayoutExpand(!p.getExpanded(), v, view.lyt_expand);
                    //items.get(position).setExpanded(show);
                }
            });


            // void recycling view
            /*if(p.getExpanded()){
                view.lyt_expand.setVisibility(View.VISIBLE);
            } else {
                view.lyt_expand.setVisibility(View.GONE);
            }
            Tools.toggleArrow(p.getExpanded(), view.bt_expand, false);

            int colorId = position % 2 == 0 ? R.color.blue_50 : R.color.black;
            holder.itemView.setBackgroundColor(ctx.getResources().getColor(colorId));

        }
    }*/

    private boolean toggleLayoutExpand(boolean show, View view, View lyt_expand) {
        Tools.toggleArrow(show, view);
        if (show) {
            ViewAnimation.expand(lyt_expand);
        } else {
            ViewAnimation.collapse(lyt_expand);
        }
        return show;
    }



}