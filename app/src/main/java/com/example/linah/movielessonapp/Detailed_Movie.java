package com.example.linah.movielessonapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Detailed_Movie extends AppCompatActivity {

    public static List<Movie_Details> movieDetailsList = new ArrayList<>();
    private String ID;
    public String Trailer_OR_Review = "trailer";
    private boolean noConnection;
    private boolean trailersDone;
    private int trailersSize;
    private static MoviesDetailedAdapter mAdapter;

    private RecyclerView TrailerRecyclerView, ReviewsRecyclerView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed__movie);
       
	   TrailerRecyclerView = (RecyclerView) findViewById(R.id.Trailers_recycler_view);
        ReviewsRecyclerView = (RecyclerView) findViewById(R.id.reviews_recycler_view);
       
 
        new getData().execute("trailer");



        // adapter
        mAdapter = new MoviesDetailedAdapter(movieDetailsList,TrailerRecyclerView.getContext(),Trailer_OR_Review);
        mAdapter = new MoviesDetailedAdapter(movieDetailsList,ReviewsRecyclerView.getContext(),Trailer_OR_Review);

        TrailerRecyclerView.setLayoutManager(new LinearLayoutManager(TrailerRecyclerView.getContext()));
        ReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(ReviewsRecyclerView.getContext()));

        TrailerRecyclerView.setItemAnimator(new DefaultItemAnimator());
         ReviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        noConnection = false;
        if(isOnline(Detailed_Movie.this)) {
            new getData().execute("trailer");
             mAdapter.notifyDataSetChanged();
        }
        // set the adapter
//         TrailerRecyclerView.setAdapter(mAdapter);
//        ReviewsRecyclerView.setAdapter(mAdapter);
        prepareMovieData();




        Intent i = getIntent();
        // http://api.themoviedb.org/3/movie/{id}/videos

        String ImgPath   = "http://image.tmdb.org/t/p/w185/";
        String VideoPath = "http://www.youtube.com/watch?v=";


        String MovieTitle = i.getExtras().getString("title");
        Toast.makeText(getApplicationContext(),MovieTitle+" is selected!", Toast.LENGTH_SHORT).show();
        ImageView img    = (ImageView)findViewById(R.id.MovieImage);
        TextView  Title  = (TextView)findViewById(R.id.MovieTitle);
        TextView  Review = (TextView)findViewById(R.id.MovieReview);
        TextView  Date   = (TextView)findViewById(R.id.Date);
        TextView  Rate   = (TextView)findViewById(R.id.Rate);
        Button    Fav    = (Button)  findViewById(R.id.Favbutton);


        // get data from intent
        assert Title != null;
        Title. setText(i.getExtras().getString("title"));
        assert Review != null;
        Review.setText(i.getExtras().getString("review"));
        assert Rate != null;
        Rate.  setText(i.getExtras().getString("rate"));
        assert Date != null;
        Date.  setText(i.getExtras().getString("date"));


          ID     = i.getExtras().getString("id");
        String Imgurl = i.getExtras().getString("img");
        // append ImgPath
        switch (ImgPath = new StringBuilder()
                .append(ImgPath)
                .append(Imgurl)
                .toString()) {
        }
        // append VideoPath

        VideoPath = new StringBuilder()
                .append(VideoPath)
                .append("6uEMl2BtcqQ")
                .toString();

     //   VideoPath = VideoPath +  getString(R.string.API_KEY);
        final String finalVideoPath = VideoPath;

        if (Fav != null) {
            Fav.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(finalVideoPath));

                    startActivity(intent);
                }
            });
        }

        Picasso.with(this)
                .load(ImgPath)
                .placeholder(R.drawable.loading) //this is optional the image to display while the url image is downloading
                .error(R.drawable.error)         //this is also optional if some error has occurred in downloading the image
                .into(img);


        TrailerRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), TrailerRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                   Movie_Details movie = movieDetailsList.get(position);
                if (position < trailersSize) {  
                  //  String link = ((TextView) findViewById(R.id.Link)).getText().toString();
                   // String link = movie.getKey();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + movie.getKey())));
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void prepareMovieData() {


        Movie_Details movie = new Movie_Details("MovieTrailer","6uEMl2BtcqQ","Linah","verynice");
        movieDetailsList.add(movie);


        mAdapter.notifyDataSetChanged();
    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        public RecyclerTouchListener(Context applicationContext, RecyclerView trailerRecyclerView, ClickListener clickListener) {
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }



    public class getData extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = getData.class.getSimpleName();
        private String param;

        private Void putDataInArrays(String JsonStr)throws JSONException {

            final String KEY     = "key";
            final String NAME    = "name";
            final String AUTHOR  = "author";
            final String CONTENT = "content";

            JSONObject MJson = new JSONObject(JsonStr);
            JSONArray movieArray = MJson.getJSONArray("results");


//            if(param.equals("trailer")){
//                trailersSize = movieArray.length();
//               // listData.clear();
//            }else {
//                int reviewsSize = movieArray.length();
//            }

            for(int i = 0; i < movieArray.length(); i++) {

                JSONObject movie = movieArray.getJSONObject(i);
                Movie_Details gridItem = new Movie_Details();
                Log.v("jsonObj",movieArray.getJSONObject(i).toString() );
                if(param.equals("trailer")) {
                    gridItem.setName(movie.getString(NAME));
                    gridItem.setKey(movie.getString(KEY));
					Log.v("Key fetching  ", "here");

                }else{
                    gridItem.setAuthor(movie.getString(AUTHOR));
                    gridItem.setContent(movie.getString(CONTENT));
                }
                movieDetailsList.add(gridItem);
//                mAdapter.notifyDataSetChanged();

            }

            return null;
        }

        @Override
        protected String doInBackground(String... params) {

//           try{ param = params[0];}
//           catch
            {
                if ( trailersDone ) param = "review";
                else  param = "trailer";
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String JsonStr = null;

            try {

                String link;
                if(param.equals("trailer"))
                    link = "http://api.themoviedb.org/3/movie/"+ ID +"/videos?" + getString(R.string.API_KEY);
                else
                    link = "http://api.themoviedb.org/3/movie/"+ ID +"/reviews?" + getString(R.string.API_KEY);
                URL url = new URL(link);


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                JsonStr = buffer.toString();

            } catch (IOException e) {
                noConnection = true;
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }

            try {
                putDataInArrays(JsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return JsonStr;
        }

        @Override
        protected void onPostExecute(String JsonStr) {
            TrailerRecyclerView.setAdapter(mAdapter);
            ReviewsRecyclerView.setAdapter(mAdapter);

             if (movieDetailsList.size() > 0) {


                if (param.equals("trailer")) {
                    trailersDone = true;
                    noConnection = false;
                    new getData().execute("review");
                    Trailer_OR_Review = "review";

                }

            }
        }
    }

    public boolean isOnline(Context context) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            return isConnected;
        }

}
