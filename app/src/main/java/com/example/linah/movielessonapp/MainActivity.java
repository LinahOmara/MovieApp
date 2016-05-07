package com.example.linah.movielessonapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

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


public class MainActivity extends AppCompatActivity {
    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;

    public String lastStatus = "Popularity";
//    public String lastStatus = "Rating";
    public boolean noConnection;
    private AlertDialog alertDialog;
    private int mPosition = 0;
    private static final String SELECTED_KEY = "selected_position";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new MoviesAdapter(movieList,recyclerView.getContext());

     //   recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
         recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

      //  recyclerView.addItemDecoration(new DividerItemDecoration(this, StaggeredGridLayoutManager.VERTICAL));
        noConnection = false;
        if(isOnline(MainActivity.this)) {
            new PrepareDataTask().execute();
            mAdapter.notifyDataSetChanged();
        }
        // set the adapter
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movieList.get(position);
                Intent i = new Intent (MainActivity.this,Detailed_Movie.class);
                i.putExtra("title" , movie.getTitle   ());
                i.putExtra("img"   , movie.getImg_Path());
                i.putExtra("review", movie.getOverview());
                i.putExtra("rate"  , movie.getAvg_Vote());
                i.putExtra("date"  , movie.getRelease_date());
                i.putExtra("id"    , movie.getID());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareMovieData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            if (item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);
            if (!(lastStatus.equals(getString(R.string.popular_tag)))) {
                mPosition = 0;
                lastStatus = getString(R.string.popular_tag);
                noConnection = false;
                if(isOnline(this)) {
                    movieList.clear();

                    new PrepareDataTask().execute();
                }else{
                    showDialogMsg();
                }
            }
            return true;
        }
      else   if (id == R.id.action_rate) {

            if (item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);
            if (!(lastStatus.equals(getString(R.string.rating_tag)))) {
                movieList.clear();
                mPosition = 0;
                lastStatus = getString(R.string.rating_tag);
                noConnection = false;
                if(isOnline(this)) {
                    new PrepareDataTask().execute();
                }else{
                    showDialogMsg();
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void prepareMovieData() {


        Movie movie = new Movie("121","Intersteller","Nice","4","2015", "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg");
        movieList.add(movie);


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

    public class PrepareDataTask extends AsyncTask<Void, Void, String > {

            private final String LOG_TAG = PrepareDataTask.class.getSimpleName();


        private Void putDataInArrays(String JsonStr)throws JSONException {

            final String ID       = "id";
            final String TITLE    = "original_title";
            final String OVERVIEW = "overview";
            final String VOTE_AVG = "vote_average";
            final String REL_DATE = "release_date";
            final String IMAGES   = "poster_path";

            JSONObject MJson = new JSONObject(JsonStr);
            JSONArray movieArray = MJson.getJSONArray("results");


            for(int i = 0; i < movieArray.length(); i++) {


                JSONObject movie = movieArray.getJSONObject(i);

                Movie gridItem = new Movie();
                gridItem.setTitle       (movie.getString(TITLE));
                gridItem.setOverview    (movie.getString(OVERVIEW));
                gridItem.setAvg_Vote    (movie.getString(VOTE_AVG));
                gridItem.setRelease_date(movie.getString(REL_DATE));
                gridItem.setImg_Path    (movie.getString(IMAGES));
                gridItem.setID          (movie.getString(ID));

               // Log.v("Data fetching ", "here");
                movieList.add(gridItem);

            }

            return null;
        }

        @Override
            protected String doInBackground(Void... params) {

            String JsonStr = null;
               HttpURLConnection urlConnection = null;
               BufferedReader reader = null;




               try {

                   String link;
                   if(lastStatus.equals(getString(R.string.popular_tag)))
                       link = getString(R.string.popular_movies) + "&" + getString(R.string.API_KEY);
                   else
                       link = getString(R.string.highest_rated_movies) + "&" + getString(R.string.API_KEY);
                   URL url = new URL(link);


                   urlConnection = (HttpURLConnection) url.openConnection();
                   urlConnection.setRequestMethod("GET");
                   urlConnection.connect();

                   InputStream inputStream = urlConnection.getInputStream();
                   StringBuffer buffer = new StringBuffer();
                   if (inputStream == null) {
                       noConnection = true;
                       return null;
                   }
                   reader = new BufferedReader(new InputStreamReader(inputStream));

                   String line;
                   while ((line = reader.readLine()) != null) {
                       buffer.append(line + "\n");
                   }

                   if (buffer.length() == 0) {
                       noConnection = true;
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


            Log.v("finish background", "here");
            return JsonStr;
            }

        protected void onPostExecute(String JsonStr) {
            try {
                Log.v("Post Execute", "here");
                putDataInArrays(JsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        }

    private void showDialogMsg() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("No Internet Connection, Please connect to internet firstly");

        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                alertDialog.dismiss();
                MainActivity.this.finish();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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