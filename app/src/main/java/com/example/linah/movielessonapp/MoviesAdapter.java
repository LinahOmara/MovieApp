package com.example.linah.movielessonapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder>   {

    private List<Movie> moviesList;
    private Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView title ;
        public ImageView poster;

        public MyViewHolder(View view) {
            super(view);

             poster = (ImageView) view.findViewById(R.id.poster);

            view.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }


    public MoviesAdapter(List<Movie> moviesList,Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        String ImgPath = "http://image.tmdb.org/t/p/w185/";

        ImgPath = new StringBuilder()
        .append(ImgPath)
        .append(movie.getImg_Path())
        .toString();

        Picasso.with(context).load(ImgPath)
                .placeholder(R.drawable.loading).error(R.drawable.error)  .into(holder.poster);
        Log.v("msg path", ImgPath);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }



}