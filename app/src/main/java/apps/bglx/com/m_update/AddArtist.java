package apps.bglx.com.m_update;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class AddArtist extends AppCompatActivity implements View.OnClickListener {

    public static Activity addAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artist);

        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);

        Button add = (Button) findViewById(R.id.add_button);
        add.setOnClickListener(this);

    }

    public void onClick(View view) {
        // detect the view that was "clicked"
        switch (view.getId()) {
            case R.id.search_button:
                new LongOperation().execute("");
                break;
            case R.id.add_button:
                Button add = (Button) findViewById(R.id.add_button);
                add.setEnabled(false);
                add.setText("En Cours");
                EditText artistFinalName = (EditText) findViewById(R.id.final_artist_name);
                EditText artistDeezerID = (EditText) findViewById(R.id.final_artist_idDeezer);
                DatabaseManager databaseManager = new DatabaseManager(this);
                databaseManager.insertScore(
                        artistFinalName.getText().toString(),
                        Integer.parseInt(artistDeezerID.getText().toString()),
                        0,
                        0);
                databaseManager.close();
                Context context = getApplicationContext();
                CharSequence text = artistFinalName.getText().toString()+" ajouté !";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                MainActivity.mainAct.finish();
                Intent i = new Intent(AddArtist.this,MainActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> implements View.OnClickListener   {

        private String artistSearchedName,
                artistName1, artistName2, artistName3,
                artistId1, artistId2,artistId3,
                artistImage1, artistImage2, artistImage3;
        private GetInfo get = new GetInfo();

        ImageView artistImageView1 = (ImageView) findViewById(R.id.search_artist_image_1);
        ImageView artistImageView2 = (ImageView) findViewById(R.id.search_artist_image_2);
        ImageView artistImageView3 = (ImageView) findViewById(R.id.search_artist_image_3);

        TextView artistTextName1 = (TextView) findViewById(R.id.search_artist_text_1);
        TextView artistTextName2 = (TextView) findViewById(R.id.search_artist_text_2);
        TextView artistTextName3 = (TextView) findViewById(R.id.search_artist_text_3);

        Button artistButton1 = (Button) findViewById(R.id.search_artist_button_1);
        Button artistButton2 = (Button) findViewById(R.id.search_artist_button_2);
        Button artistButton3 = (Button) findViewById(R.id.search_artist_button_3);

        EditText artistFinalName = (EditText) findViewById(R.id.final_artist_name);
        EditText artistDeezerID = (EditText) findViewById(R.id.final_artist_idDeezer);

        @Override
        protected String doInBackground(String... params) {
            try {
                EditText searchText = (EditText) findViewById(R.id.search_artist_text);
                artistSearchedName = searchText.getText().toString();
                artistId1 = get.artistSearch(artistSearchedName).get(0).get(0);
                artistId2 = get.artistSearch(artistSearchedName).get(0).get(1);
                artistId3 = get.artistSearch(artistSearchedName).get(0).get(2);
                artistName1 = get.artistSearch(artistSearchedName).get(1).get(0);
                artistName2 = get.artistSearch(artistSearchedName).get(1).get(1);
                artistName3 = get.artistSearch(artistSearchedName).get(1).get(2);
                artistImage1 = get.artistSearch(artistSearchedName).get(2).get(0);
                artistImage2 = get.artistSearch(artistSearchedName).get(2).get(1);
                artistImage3 = get.artistSearch(artistSearchedName).get(2).get(2);
            } catch (Exception e) {
                System.out.println("Error");
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            Picasso.get().load(
                    "https://e-cdns-images.dzcdn.net/images/artist/"+artistImage1+"/264x264-000000-80-0-0.jpg"
            ).into(artistImageView1);
            Picasso.get().load(
                    "https://e-cdns-images.dzcdn.net/images/artist/"+artistImage2+"/264x264-000000-80-0-0.jpg"
            ).into(artistImageView2);
            Picasso.get().load(
                    "https://e-cdns-images.dzcdn.net/images/artist/"+artistImage3+"/264x264-000000-80-0-0.jpg"
            ).into(artistImageView3);
            artistTextName1.setText(artistName1);
            artistButton1.setVisibility(View.VISIBLE);
            artistTextName2.setText(artistName2);
            artistButton2.setVisibility(View.VISIBLE);
            artistTextName3.setText(artistName3);
            artistButton3.setVisibility(View.VISIBLE);

            artistButton1.setOnClickListener(this);
            artistButton2.setOnClickListener(this);
            artistButton3.setOnClickListener(this);

        }

        public void onClick(View view) {
            // detect the view that was "clicked"
            switch (view.getId()) {
                case R.id.search_artist_button_1:
                    artistFinalName.setText(artistName1);
                    artistDeezerID.setText(artistId1);
                    break;
                case R.id.search_artist_button_2:
                    artistFinalName.setText(artistName2);
                    artistDeezerID.setText(artistId2);
                    break;
                case R.id.search_artist_button_3:
                    artistFinalName.setText(artistName3);
                    artistDeezerID.setText(artistId3);
                    break;
            }
        }

    }

}
