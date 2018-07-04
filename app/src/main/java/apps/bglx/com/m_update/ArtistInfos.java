package apps.bglx.com.m_update;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ArtistInfos extends AppCompatActivity {

    public static Activity infoAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_infos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle b = getIntent().getExtras();
        final int id = b.getInt("id");
        System.out.println(id);

        TextView testText = (TextView) findViewById(R.id.info_test_text);
        testText.append(String.valueOf(id));

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

}
