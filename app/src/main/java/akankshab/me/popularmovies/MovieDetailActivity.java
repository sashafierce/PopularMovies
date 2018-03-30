package akankshab.me.popularmovies;

import android.os.Bundle;
import android.os.PersistableBundle;
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
        poster = (ImageView) findViewById(R.id.detail_poster);
        title = (TextView) findViewById(R.id.detail_title);
        synopsis = (TextView) findViewById(R.id.detail_overview);
        rating = (TextView) findViewById(R.id.detail_rating);
        releaseDate = (TextView) findViewById(R.id.detail_date);
        title.setText(name);

        loadPoster(imagePath);
        releaseDate.setText("Release Date : " + date);
        rating.setText( "Rating :  " + rating1 + "/10");
        synopsis.setText(description);


    }
    private void loadPoster(String path) {
        Picasso.with(getApplicationContext())
                .load(path)
                .into(poster);
    }
}
