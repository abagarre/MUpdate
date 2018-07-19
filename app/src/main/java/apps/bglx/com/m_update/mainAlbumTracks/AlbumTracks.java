package apps.bglx.com.m_update.mainAlbumTracks;


import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import apps.bglx.com.m_update.ArtistInfos;
import apps.bglx.com.m_update.GetInfo;
import apps.bglx.com.m_update.MainActivity;
import apps.bglx.com.m_update.R;
import apps.bglx.com.m_update.imageTransformations.ImageTarget;

import static apps.bglx.com.m_update.MainActivity.mainAct;

public class AlbumTracks extends DialogFragment {

    private int id;

    private ImageView cover, backCover;
    private ViewGroup background;
    private TextView title;
    private TextView trackName;
    private TextView trackDuration;
    private Button artistPage;

    public static Dialog dialog;

    String coverURL;

    AlbumTracksAdapter adapter;
    RecyclerView recyclerView;

    ArrayList<String> trackList = new ArrayList<>();

    private GetInfo getInfo = new GetInfo();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_album_tracks, container,
                false);
        View trackView = inflater.inflate(R.layout.album_tracks,container,false);
        getDialog().setTitle("DialogFragment Tutorial");

        dialog = getDialog();


        id = getArguments().getInt("artistID");
        System.out.println(id);
        displayTracks(id);

        cover = (ImageView) rootView.findViewById(R.id.dialog_album_cover);
        backCover = (ImageView) rootView.findViewById(R.id.dialog_album_cover_bg);
        background = (ViewGroup) rootView.findViewById(R.id.dialog_background);
        title = (TextView) rootView.findViewById(R.id.dialog_album_name);
        trackName = (TextView) trackView.findViewById(R.id.track_name);
        trackDuration = (TextView) trackView.findViewById(R.id.track_duration);
        artistPage = (Button) rootView.findViewById(R.id.dialog_button);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.list_tracks_recycle);

        recyclerView.setLayoutManager(new LinearLayoutManager(mainAct));

        // if button is clicked, close the custom dialog
                artistPage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mainAct, ArtistInfos.class);
                        intent.putExtra("id", id);

                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(mainAct, cover, "last_cover");

                        startActivity(intent, options.toBundle());

                    }
                });

        // Do something else
        return rootView;
    }

    public void displayTracks(int artistID) {

        new albumInfos().execute(Integer.toString(artistID));

    }

    private class albumInfos extends AsyncTask<String, Void, String> {

        List<List> albumDatas = new ArrayList<>();
        Target target;

        @Override
        protected String doInBackground(String... args) {
            String artistID = args[0];
            try {
                List<String> lastAlbumInfos = getInfo.getLastAlbum(artistID);
                String albumID = lastAlbumInfos.get(3);
                albumDatas = getInfo.getAlbumInfo(albumID);
                int nbTracks = Integer.parseInt(albumDatas.get(0).get(3).toString());

                for (int i = 0; i < nbTracks*2; i += 2) {
                    trackList.add(
                                    Integer.toString(i/2 + 1)
                                    + ". "
                                    + albumDatas.get(1).get(i).toString()
                                    + "::"
                                    + albumDatas.get(1).get(i+1).toString());
                }

            } catch (Exception e) {
                System.out.println("Error retrieving album info");
            }

            target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    assert cover != null;
                    backCover.setImageBitmap(bitmap);
                    cover.setImageBitmap(bitmap);
                    Palette.from(bitmap)
                            .generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                    if (textSwatch == null) {
                                        textSwatch = palette.getLightMutedSwatch();
                                    }
                                    if (textSwatch == null) {
                                        trackName.setTextColor(Color.WHITE);
                                        trackDuration.setTextColor(Color.WHITE);
                                        return;
                                    }
                                    Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();

                                    if (lightVibrant != null) {
                                        GradientDrawable gd = new GradientDrawable();
                                        gd.setColors(new int[]{
                                                textSwatch.getRgb(),
                                                lightVibrant.getRgb()});
                                        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                                        background.setBackground(gd);
                                    } else {
                                        background.setBackgroundColor(textSwatch.getRgb());
                                    }
                                    title.setTextColor(textSwatch.getTitleTextColor());
                                    trackName.setTextColor(textSwatch.getBodyTextColor());
                                    trackDuration.setTextColor(textSwatch.getBodyTextColor());
                                    Palette.Swatch darkSwatch = palette.getDarkMutedSwatch();
                                    if (darkSwatch == null) {
                                        darkSwatch = palette.getDarkVibrantSwatch();
                                        assert darkSwatch != null;
                                        artistPage.setBackgroundColor(darkSwatch.getRgb());
                                    } else {
                                        artistPage.setBackgroundColor(darkSwatch.getRgb());
                                    }
                                    artistPage.setTextColor(Color.WHITE);
                                }
                            });
                }
                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                }
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            coverURL = albumDatas.get(0).get(0).toString();
            Picasso.get().load(coverURL).noFade().into(target);
            title.setText(albumDatas.get(0).get(1).toString());
            adapter = new AlbumTracksAdapter(mainAct, trackList);
            recyclerView.setAdapter(adapter);
        }

    }

}
