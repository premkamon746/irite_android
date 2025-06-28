package com.csi.irite.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csi.irite.R;
import com.csi.irite.utils.Tools;
import com.csi.irite.utils.ViewAnimation;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class AdapterListExpand extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

    //private final List<Checklist> items;


    //private final Context ctx;
    //private OnItemClickListener mOnItemClickListener;

    /*public interface OnItemClickListener {
        void onItemClick(View view, Checklist obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListExpand(Context context, List<Checklist> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ToggleButton button;
        public ImageView image;
        public TextView head;
        public Button fix;
        public Button remark;
        public TextView listname;
        public ImageButton bt_expand;
        public View lyt_expand;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            button = (ToggleButton) v.findViewById(R.id.toggle);
            image = (ImageView) v.findViewById(R.id.image);
            head = (TextView) v.findViewById(R.id.head);
            listname = (TextView) v.findViewById(R.id.listname);
            bt_expand = (ImageButton) v.findViewById(R.id.bt_expand);
            lyt_expand = (View) v.findViewById(R.id.lyt_expand);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            fix = (Button) v.findViewById(R.id.fix);
            remark = (Button) v.findViewById(R.id.remark);
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //Log.d("ddddxxx","onbild "+(holder instanceof OriginalViewHolder));
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
                        //mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            view.button.setOnClickListener(new View.OnClickListener(

            ) {
                @Override
                public void onClick(View v) {

                }
            });

            view.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        //mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            view.bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*boolean show = toggleLayoutExpand(!p.getExpanded(), v, view.lyt_expand);
                    items.get(position).setExpanded(show);
                }
            });

            view.fix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Item  clicked", Snackbar.LENGTH_SHORT).show();
                    if (mOnItemClickListener != null) {
                        Snackbar.make((View) view.getParent(), "Item  clicked", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

            view.remark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        //mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });



            int colorId = position % 2 == 0 ? R.color.blue_50 : R.color.green_50;
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