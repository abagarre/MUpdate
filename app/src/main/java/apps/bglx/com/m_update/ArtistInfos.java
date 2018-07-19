package apps.bglx.com.m_update;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import apps.bglx.com.m_update.imageTransformations.BlurTransform;
import apps.bglx.com.m_update.imageTransformations.CircleTransform;
import apps.bglx.com.m_update.imageTransformations.DeepBlurTransform;
import apps.bglx.com.m_update.mainAlbumTracks.AlbumTracks;

public class ArtistInfos extends AppCompatActivity {

    GetInfo get = new GetInfo();
    private ImageView artistImageView, backgroundImageView, albumCover;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_infos);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        artistImageView = (ImageView) findViewById(R.id.info_artist_image);
        backgroundImageView = (ImageView) findViewById(R.id.info_background);
        albumCover = (ImageView) findViewById(R.id.artist_last_album_cover);

        Bundle b = getIntent().getExtras();
        id = b.getInt("id");

        supportPostponeEnterTransition();

        new LongOperation().execute(String.valueOf(id));

        Button delete = (Button) findViewById(R.id.info_delete_button);

        final DatabaseManager databaseManager = new DatabaseManager(this);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseManager.deleteArtist(String.valueOf(id));
                MainActivity.mainAct.finish();
                Intent i = new Intent(ArtistInfos.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        databaseManager.close();

    }

    @Override
    public void onBackPressed() {
        albumCover.setVisibility(View.GONE);
        super.onBackPressed();
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        String artistPicture, artistName, nbAlbum, albumPicture;

        @Override
        protected String doInBackground(String... info) {
            try {
                String id = info[0];
                List<String> artistInfos = get.getArtistPage(String.valueOf(id));
                List<String> lastAlbum = get.getLastAlbum(String.valueOf(id));
                artistPicture = artistInfos.get(0);
                artistName = artistInfos.get(1);
                nbAlbum = artistInfos.get(2);
                albumPicture = lastAlbum.get(2);
                } catch (Exception e) {
                System.out.println("Artist Infos Error");
                }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            Picasso.get().load(albumPicture).noFade().into(albumCover, new Callback() {
                @Override
                public void onSuccess() {
                    supportStartPostponedEnterTransition();
                    Dialog dialog = AlbumTracks.dialog;
                    dialog.dismiss();
                }
                @Override
                public void onError(Exception e) {
                    supportStartPostponedEnterTransition();
                }
            });

            Picasso.get().load(artistPicture).transform(new CircleTransform()).into(artistImageView);
            Picasso.get().load(artistPicture).transform(new DeepBlurTransform()).into(backgroundImageView);
            TextView artistText = (TextView) findViewById(R.id.info_artist_text);
            artistText.setText(artistName);
            TextView nbAlbumText = (TextView) findViewById(R.id.info_nb_album);
            nbAlbum = nbAlbum + " albums";
            nbAlbumText.setText(nbAlbum);
        }

    }

}

