package apps.bglx.com.m_update;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;

    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new MoviesAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // set the adapter
        recyclerView.setAdapter(mAdapter);

        prepareMovieData();
    }

    private void prepareMovieData() {

        databaseManager = new DatabaseManager(this);

        List<ArtistsData> ids = databaseManager.readTop10();

        for ( ArtistsData artistsData : ids ) {

            String infos = artistsData.toString();

            System.out.println(infos);

            new LongOperation().execute(infos);

        }

        databaseManager.close();

        mAdapter.notifyDataSetChanged();

    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        GetInfo getAlbum = new GetInfo();
        String artistURL;
        String artistName, albumName, albumDate, albumPicture;

        @Override
        protected String doInBackground(String... info) {

            try {
                String infos = info[0];
                artistURL = "https://www.deezer.com/fr/artist/" + infos.substring(infos.indexOf(",") + 1);
                System.out.println(artistURL);
                artistName = infos.substring(0,infos.indexOf(","));
                albumName = getAlbum.albumName(artistURL).replace("\\u00e9","é");
                albumDate = getAlbum.albumDate(artistURL);
                albumPicture = getAlbum.albumPicture(artistURL);
            } catch (Exception e) {
                System.out.println("Error");
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(albumName);
            System.out.println(albumDate);

            Movie movie = new Movie(albumName, artistName, albumDate, albumPicture);
            movieList.add(movie);

            mAdapter.notifyDataSetChanged();

        }

    }
}
