package akankshab.me.popularmovies;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import akankshab.me.popularmovies.model.MovieResponse;

import akankshab.me.popularmovies.model.Movie;
import akankshab.me.popularmovies.utils.CustomItemClickListener;
import akankshab.me.popularmovies.utils.MovieAdapter;


/**
 * Created by akanksha on 28/1/18.
 */

public class MainFragment extends Fragment {
    private RecyclerView recyclerView;

    private final String SORT_KEY = "sort";
    private final String RATING ="top_rated";
    private final String POPULARITY = "popular";

    private String prefOrder = "popular";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private static List<Movie> list;
    private static MovieAdapter adapter ;
    private static final int GRID_COLUMNS  = 2;
    private TextView errorMessage;

    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        View rootView = layoutInflater.inflate(R.layout.activity_fragment,container,false);
        setHasOptionsMenu(true);
        list = new ArrayList<Movie>();
        adapter = new MovieAdapter(getActivity(),list, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
            }
        });
        recyclerView = rootView.findViewById(R.id.movie_recycler_view);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        editor.apply();

        errorMessage = rootView.findViewById(R.id.message);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), GRID_COLUMNS));
        recyclerView.setAdapter(adapter);
        if (isOnline()) {
            populateList();
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            errorMessage.setVisibility(View.VISIBLE);

        }
        return rootView;
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void  onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.rating:
                menuItem.setChecked(true);
                editor.putString(SORT_KEY,RATING);
                prefOrder = RATING;
                editor.commit();
                break;
            case R.id.popularity:
                menuItem.setChecked(true);
                editor.putString(SORT_KEY,POPULARITY);
                prefOrder = POPULARITY;
                editor.commit();
                break;
        }
        if(isOnline()) {
            Log.e("MAIN", "LOOKS LIKE YOUR ONLINE :p");
            populateList();
        }
        else {
            recyclerView.setVisibility(View.INVISIBLE);
            errorMessage.setVisibility(View.VISIBLE);
            Log.e("MAIN", "INTERNET PROBLEM");
        }
        return super.onOptionsItemSelected(menuItem);
    }
    private void populateList() {
        //https://api.themoviedb.org/3/movie/popular?api_key=
        final String MOVIEDB_BASE_URL =  "https://api.themoviedb.org/3/movie/";
        final String QUERY_APPKEY = "api_key";
        String sortOrder = preferences.getString(SORT_KEY, prefOrder);

        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(QUERY_APPKEY, BuildConfig.MOVIE_API_KEY)
                .build();

        Log.e("MAINFRAGMENT", "URL " + builtUri);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e("", "MalformedURLException", e);
        }

        FetchMoviesTask task = new FetchMoviesTask();

        try {
            list = task.execute(url).get();

            Log.d("MAINFRAGMENT: ", "Received list" + list);

            adapter = new MovieAdapter(getActivity(), list, new CustomItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    //long id = movie.get(position).getID();
                    Movie movie = adapter.getItem(position);
                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                    //intent.putExtra("Movie", movie);
                    intent.putExtra("title", movie.getTitle());
                    intent.putExtra("description", movie.getSynopsis());
                    intent.putExtra("rating", movie.getRating());
                    intent.putExtra("date", movie.getReleaseDate());
                    intent.putExtra("imagePath", movie.getPosterPath());
                    Log.d("FRAGMENT2", "onItemClick: movie" + movie.getPosterPath());
                    startActivity(intent);

                }
            });
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            errorMessage.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        String sortBy = preferences.getString(SORT_KEY, POPULARITY);
        if (sortBy.equals(POPULARITY)) {
            menu.findItem(R.id.popularity).setChecked(true);
        } else {
            menu.findItem(R.id.rating).setChecked(true);        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static class FetchMoviesTask extends AsyncTask<URL , Void , List<Movie> > {
        //private final String LOG_TAG = getActivity().getString(R.string.fetch);

        @Override
        protected List<Movie> doInBackground(URL... params) {
            return fetchMovies(params[0]);
        }

        @Override
        protected void onPostExecute( List<Movie> result) {
            super.onPostExecute(result);
            list = result;
            adapter.notifyDataSetChanged();
        }

        // return a list of Movies
        public  List<Movie> fetchMovies(URL requestUrl){
            URL url = requestUrl ;

            String jsonResponse = null;
            try {
                jsonResponse = httpRequest(url);
                if (jsonResponse == null ) {
                    // Log.e(LOG_TAG, "error in JSON response");
                }

            } catch (IOException e) {
                //Log.e(LOG_TAG, "IO error HTTP request", e);
            }

            return MovieResponse.parseJSON(jsonResponse);
        }

        private  String httpRequest(URL url) throws IOException {
            String json= "";
            InputStream inputStream = null;

            HttpURLConnection urlConnection = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(3000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                if (urlConnection.getResponseCode() == 200){
                    inputStream = urlConnection.getInputStream();
                    json = inputStreamToString(inputStream);
//                    Log.d(LOG_TAG, "This is json recieved"+json);

                } else {
                   // Log.e(LOG_TAG, " response code error: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                // Log.d(LOG_TAG, ""+url);
                //Log.e(LOG_TAG, "error while fetching JSON", e);
            } finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }

                if (inputStream != null){
                    inputStream.close();
                }
            }

            return json;
        }

        //  reference : https://stackoverflow.com/questions/10809731/httpresponse-and-bufferedreader
        private  String inputStreamToString(InputStream is) throws IOException
        {
            String line = "";
            StringBuilder result = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is), 1024 * 4);
            try
            {
                while ((line = rd.readLine()) != null)
                {
                    result.append(line);
                }
            } catch (IOException e)
            {
                Log.e("", ""+ e.getMessage());
            }
            return result.toString();
        }
    }

}