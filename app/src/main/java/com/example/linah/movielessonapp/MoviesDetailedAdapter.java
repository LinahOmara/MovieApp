package com.example.linah.movielessonapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Linah on 5/1/2016.
 */
public class MoviesDetailedAdapter extends RecyclerView.Adapter<MoviesDetailedAdapter.MyViewHolder> {


    private List<Movie_Details> moviesList;
    private Context context;
    public String Trailer_OR_Review = "trailer";
    public TextView TrailerName , Author , Content , TrailerLink ;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {



        public MyViewHolder(View view) {
            super(view);
            Log.v("here","MyViewHolder");
            TrailerName = (TextView) view.findViewById(R.id.Name);
            Author      = (TextView) view.findViewById(R.id.Author);
            TrailerLink = (TextView) view.findViewById(R.id.Link);
            Content     = (TextView) view.findViewById(R.id.Content);

            view.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }

    public MoviesDetailedAdapter(List<Movie_Details> moviesList,Context context, String trailerORReview) {
        this.moviesList = moviesList;
        this.context = context;
        Trailer_OR_Review = trailerORReview;
        Log.v("here","madapter");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        Log.v("here","onCreateViewHolder");

        itemView  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailers_layout, parent, false);
        /*
        if (Trailer_OR_Review.equals("trailers")){


            itemView  = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.trailers_layout, parent, false);
        }
        else{
              itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reviews_layout, parent, false);
        }*/
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Log.v("here","onBindViewHolder");
        Movie_Details movie_details = moviesList.get(position);
        Log.v("here",movie_details.getContent());
        Log.v("here",movie_details.getName());

        TrailerName.setText(movie_details.getName());
        TrailerLink.setText(movie_details.getKey());

        /*
        if (Trailer_OR_Review.equals("trailers")){
            TrailerName.setText(movie_details.getName());
            TrailerLink.setText(movie_details.getKey());


        }
        else{
            Author.setText(movie_details.getAuthor());
            Content.setText(movie_details.getContent());

        }*/

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
