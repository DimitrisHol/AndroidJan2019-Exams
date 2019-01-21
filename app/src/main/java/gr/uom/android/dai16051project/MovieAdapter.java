package gr.uom.android.dai16051project;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.squareup.picasso.Picasso;


public class MovieAdapter extends ArrayAdapter {

    private List<Movie> movies;
    private final LayoutInflater inflater;
    private final int layoutResource;

    public MovieAdapter (@NonNull Context context, int resource, @NonNull List<Movie> objects) {
        super(context, resource, objects);

        movies = objects;

        inflater = LayoutInflater.from(context);
        layoutResource = resource;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }


    // Buffer
    static class MovieHolder{

        public TextView titleView;
        public TextView originaltitleView;
        public TextView descriptionView;
        public TextView releaseDateView;
        public TextView ratingView;
        public ImageView urlToImageView;

        public MovieHolder(View Item){

            titleView = Item.findViewById(R.id.titleText);
            originaltitleView = Item.findViewById(R.id.originaltitleText);
            descriptionView = Item.findViewById(R.id.descriptionText);
            releaseDateView= Item.findViewById(R.id.dateText);
            ratingView = Item.findViewById(R.id.ratingText);


            urlToImageView = Item.findViewById(R.id.imageView);

        }

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MovieHolder movieHolder;

        //Inflate only the first time

        if (convertView == null){
            convertView = inflater.inflate(layoutResource , parent, false);
            movieHolder = new MovieHolder(convertView);

            convertView.setTag(movieHolder);
        }

        else {  // Get the data from the MovieHolder

            movieHolder = (MovieHolder) convertView.getTag();

        }

        Movie movie = movies.get(position);

        movieHolder.titleView.setText(movie.getTitle());
        movieHolder.originaltitleView.setText("Πρωτότυπος Τίτλος : " + movie.getOriginalTitle());
        movieHolder.descriptionView.setText(movie.getDescription());
        movieHolder.releaseDateView.setText("Πρώτη Προβολή : " + movie.getReleaseDate());
        movieHolder.ratingView.setText("Μέσος όρος : " + movie.getRating());


        //https://image.tmdb.org/t/p/w185/aYXsFCsjxBZdNlUdhPCaDvU4eQp.jpg



        Picasso.get().load("https://image.tmdb.org/t/p/w185/" + movie.getPosterImage()).into(movieHolder.urlToImageView);

        return convertView;

    }
}
