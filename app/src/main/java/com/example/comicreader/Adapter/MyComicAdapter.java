package com.example.comicreader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.comicreader.ChapterActivity;
import com.example.comicreader.Common.Common;
import com.example.comicreader.Interface.IRecyclerItemClickListener;
import com.example.comicreader.Model.Comic;
import com.example.comicreader.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyComicAdapter extends RecyclerView.Adapter<MyComicAdapter.MyViewHolder> {

    Context context;
    List<Comic> comicList;
    LayoutInflater layoutInflater;

    public MyComicAdapter(Context context, List<Comic> comicList) {
        this.context = context;
        this.comicList = comicList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = layoutInflater.inflate(R.layout.comic_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Picasso.get()
                .load(comicList.get(i).Image)
                .into(myViewHolder.comic_image);

         myViewHolder.comic_name.setText(comicList.get(i).Name);

         //event
        myViewHolder.setiRecyclerItemClickListener(new IRecyclerItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                //save comic selected
                Common.comicSelected = comicList.get(position);
                Intent intent = new Intent(context, ChapterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView comic_name;
        ImageView comic_image;
        IRecyclerItemClickListener iRecyclerItemClickListener;

        public void setiRecyclerItemClickListener(IRecyclerItemClickListener iRecyclerItemClickListener) {
            this.iRecyclerItemClickListener = iRecyclerItemClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            comic_image = (ImageView)itemView.findViewById(R.id.image_comic);
            comic_name = (TextView)itemView.findViewById(R.id.comic_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemClickListener.onClick(view, getAdapterPosition());
        }
    }
}
