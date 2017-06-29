package com.kekcom.newsapp;

/**
 * Created by lujac on 6/28/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;


/**
 * {@link NewsAdapter} exposes a list of weather forecasts to a
 * {@link android.support.v7.widget.RecyclerView}
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemHolder>{

    private RecyclerView rv;
    private ArrayList<NewsItem> data;
    ItemClickListener listener;

    public NewsAdapter(ItemClickListener listener){
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(int clickedItemIndex);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.item, parent, shouldAttachToParentImmediately);
        ItemHolder holder = new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView description;
        TextView time;

        ItemHolder(View view){
            super(view);
            title = (TextView)view.findViewById(R.id.title);
            description = (TextView)view.findViewById(R.id.description);
            time = (TextView)view.findViewById(R.id.time);
            view.setOnClickListener(this);
        }

        public void bind(int pos){
            NewsItem item = data.get(pos);
            title.setText(item.getTitle());
            description.setText(item.getDescription());
            time.setText(item.getPublishedAt());
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        }
    }

    public void setNewsData(ArrayList<NewsItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}