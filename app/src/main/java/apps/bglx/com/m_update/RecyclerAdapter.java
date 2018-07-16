package apps.bglx.com.m_update;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import apps.bglx.com.m_update.imageTransformations.BlurTransform;
import apps.bglx.com.m_update.imageTransformations.ImageCornerRadius;

class RecycleViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {

    public TextView title;
    public TextView genre;
    public TextView year;
    public ImageView cover;
    public ImageView backgroundCover;

    private ItemClickListener itemClickListener;


    public RecycleViewHolder(@NonNull View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        genre = (TextView) itemView.findViewById(R.id.genre);
        year = (TextView) itemView.findViewById(R.id.year);
        cover = (ImageView) itemView.findViewById(R.id.cover);
        backgroundCover = itemView.findViewById(R.id.background_cover);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View v) {
        itemClickListener.OnClick(v, getAdapterPosition(),false);
    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.OnClick(v,getAdapterPosition(),true);
        return true;
    }
}

public class RecyclerAdapter extends RecyclerView.Adapter<RecycleViewHolder> {

    private List<Movie> albumList;

    ImageCornerRadius cornerRadius = new ImageCornerRadius();


    public RecyclerAdapter(List<Movie> albumList) {
        this.albumList = albumList;
    }


    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.movie_list_row,parent,false);

        return new RecycleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        final Movie movie = albumList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());
        final ImageView backgroundBlurred = (ImageView) holder.backgroundCover;
        final ImageView coverFront = (ImageView) holder.cover;
        Picasso.get().load(movie.getCover()).transform(new BlurTransform()).into(backgroundBlurred);
        Picasso.get().load(movie.getCover()).into(coverFront);

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

}
