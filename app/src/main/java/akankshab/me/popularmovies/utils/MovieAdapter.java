package akankshab.me.popularmovies.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import akankshab.me.popularmovies.model.Movie;
import akankshab.me.popularmovies.R;

/**
 * Created by akanksha on 28/1/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> movieList;
    public Context context;
    public ImageView imageView ;
    public CustomClickListener clickListener ;
    private LayoutInflater inflater;
    CustomItemClickListener listener;
    public MovieAdapter () {}
    // Constructer method
    public MovieAdapter(Context context, List<Movie> movies, CustomItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.movieList = movies;
        this.context = context;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    public Movie getItem(int position) {
        return movieList.get(position);
    }


    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int pos) {

        Movie selectedMovie = movieList.get(pos);
        Log.d("ADAPTER" , "" + selectedMovie);

        // bind the poster to the view holder
        if (selectedMovie != null){
            if(selectedMovie.getPath() != null ) {
                Log.d("ADAPTER" , "" + selectedMovie.getPath());
                Picasso.with(context)
                        .load(selectedMovie.getPath())
                        .into(holder.imageView);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.movie_thumbnail);
        }
    }


    // create new views when new scrolling happens
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
       final ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getLayoutPosition());
            }
        });
        return viewHolder;
    }
    public interface CustomClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
