package akankshab.me.popularmovies;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by akanksha on 1/3/18.
 */

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView poster;
    private TextView title , synopsis , rating , releaseDate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetail);
       // akankshab.me.popularmovies.Movie movie = getIntent().getParcelableExtra("Movie");
        String name = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        Double rating1 = getIntent().getDoubleExtra("rating",10);
        String date = getIntent().getStringExtra("date");
        String imagePath = getIntent().getStringExtra("imagePath");

       // Log.d("DETAIL", "onCreate: "+ title);
        poster = findViewById(R.id.detail_poster);
        title = findViewById(R.id.detail_title);
        synopsis = findViewById(R.id.detail_overview);
        rating = findViewById(R.id.detail_rating);
        releaseDate = findViewById(R.id.detail_date);
        title.setText(name);

        loadPoster(imagePath);
        releaseDate.setText(getResources().getString(R.string.date) + date);
        rating.setText( getResources().getString(R.string.ratingtext1) + rating1 + getResources().getString(R.string.ratingtext2));
        synopsis.setText(description);


    }
    private void loadPoster(String path) {
        Picasso.with(getApplicationContext())
                .load(path)
                .into(poster);
    }
}
