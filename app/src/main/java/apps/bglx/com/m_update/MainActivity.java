package apps.bglx.com.m_update;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddArtist.class);
                startActivity(i);
            }
        });

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
        int len = 100/ids.size();

        for ( ArtistsData artistsData : ids ) {

            String infos = artistsData.toString();
            new LongOperation().execute(infos);

        }

        databaseManager.close();

        mAdapter.notifyDataSetChanged();

    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        GetInfo getAlbum = new GetInfo();
        String artistURL;
        String artistName, albumName, albumDate, albumPicture;
        ProgressBar loadingBar = (ProgressBar) findViewById(R.id.loading_bar);
        int len = 100/databaseManager.readTop10().size();

        @Override
        protected String doInBackground(String... info) {
            try {
                String infos = info[0];
                artistURL = "https://www.deezer.com/fr/artist/" + infos.substring(infos.indexOf(",") + 1);
                artistName = infos.substring(0,infos.indexOf(","));
                albumName = getAlbum.albumName(artistURL);
                albumDate = getAlbum.albumDate(artistURL);
                albumPicture = getAlbum.albumPicture(artistURL);
                if (albumName.length() > 25) {
                    albumName = albumName.substring(0,25);
                    albumName += "...";
                }
            } catch (Exception e) {
                System.out.println("Error");
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            Movie movie = new Movie(albumName, artistName, albumDate, albumPicture);
            movieList.add(movie);
            mAdapter.notifyDataSetChanged();

            loadingBar.incrementProgressBy(len);
        }

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
