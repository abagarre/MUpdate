package apps.bglx.com.m_update;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private List<Movie> albumList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAdapter mAdapter;
    private int incr = 1;

    public static Activity mainAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // To finish MainActivity in another activity :
        mainAct = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddArtist.class);
                startActivity(i);
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new RecyclerAdapter(albumList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        new LongOperation().execute();

        mAdapter.notifyDataSetChanged();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = albumList.get(position);
                Intent intent = new Intent(MainActivity.this, ArtistInfos.class);
                intent.putExtra("id", movie.getID());
                startActivity(intent);
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        GetInfo getAlbum = new GetInfo();
        String artistName, albumName, albumDate, albumPicture;
        String ID;
        int id;
        ProgressBar loadingBar = (ProgressBar) findViewById(R.id.loading_bar);

        private DatabaseManager databaseManager;

        int len;
        long emptyLong = 0;

        List<List> finalData = new ArrayList<>();

        TreeMap<Long,List<String>> data = new TreeMap<Long,List<String>>(Collections.reverseOrder());

        @Override
        protected String doInBackground(String... args) {

            databaseManager = new DatabaseManager(mainAct);
            List<ArtistsData> ids = databaseManager.readTop10();
            len = 1000/databaseManager.readTop10().size();

            for ( ArtistsData artistsData : ids ) {


                List<String> data0 = new ArrayList<>();

                final String infos = artistsData.toString();
                try {
                    ID = infos.substring(infos.indexOf(",") + 1);
                    id = Integer.parseInt(ID);
                    artistName = infos.substring(0,infos.indexOf(","));
                    List<String> lastAlbum = getAlbum.getLastAlbum(ID);
                    albumName = lastAlbum.get(0);

                    albumDate = lastAlbum.get(1);
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = dateFormat.parse(albumDate);
                    long unixTime = date.getTime()/1000;

                    albumPicture = lastAlbum.get(2);
                    if (albumName.length() > 28) {
                        albumName = albumName.substring(0,26) + "...";
                    }
                    data0.add(albumName);
                    data0.add(artistName);
                    data0.add(albumDate);
                    data0.add(albumPicture);
                    data0.add(ID);


                    data.put(unixTime,data0);


                } catch (Exception e) {
                    System.out.println(e.toString());
                    System.out.println("Background Error");
                    data0.add("Empty");
                    data.put(emptyLong,data0);
                }
            }

            SortedSet<Long> keys = new TreeSet<>(data.keySet());
            for (long key : keys) {
                List<String> value = data.get(key);
                finalData.add(0,value);
            }

            databaseManager.close();

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            for (List<String> infosArtist : finalData) {
                Movie movie = new Movie(infosArtist.get(0), infosArtist.get(1), infosArtist.get(2), infosArtist.get(3),Integer.parseInt(infosArtist.get(4)));
                albumList.add(movie);
                mAdapter.notifyDataSetChanged();

                loadingBar.setIndeterminate(false);
                loadingBar.setProgress(100);

//                ObjectAnimator animation = ObjectAnimator.ofInt(loadingBar, "progress", len * incr);
//                animation.setDuration(500); // 0.5 second
//                animation.setInterpolator(new DecelerateInterpolator());
//                animation.start();
//                incr += 1;
            }

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
