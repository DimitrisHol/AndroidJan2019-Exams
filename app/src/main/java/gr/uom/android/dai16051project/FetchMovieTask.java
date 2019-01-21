package gr.uom.android.dai16051project;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchMovieTask extends AsyncTask<String ,Void , List<Movie>> {

    private static final String TAG = "FetchMovieTask";

    private MovieAdapter adapter;

    public FetchMovieTask(MovieAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {


        String movieJSONString = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        //End result : "https://api.themoviedb.org/3/discover/movie?with_genres=28&primary_release_year=2017&language=el&api_key=b9beb26d3970656faaee10e7173889c7"
        // Building the URI
        try {

            final String baseUrl = "https://api.themoviedb.org/3/discover/movie";

            final String genresParameter = "with_genres";
            final String primaryreleaseParameter = "primary_release_year";
            final String languageParameter = "language";
            final String apiKeyParameter = "api_key";


            Uri builtURi = Uri.parse(baseUrl).buildUpon()

                    .appendQueryParameter(genresParameter, "28")
                    .appendQueryParameter(primaryreleaseParameter , "2017")
                    .appendQueryParameter(languageParameter , "el")
                    .appendQueryParameter(apiKeyParameter , "b9beb26d3970656faaee10e7173889c7")

                    .build();
            URL url = new URL(builtURi.toString());

            Log.v(TAG, "Built URI : " + builtURi.toString());


            // Sending the request to the server

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read the response

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null){
                //Your princess is in another castle , nothing came through
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            //Debugging
            String line;
            while ((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0){
                return null;
            }

            //Final string of data
            movieJSONString = buffer.toString();

            Log.v(TAG, "Movie JSON String: " + movieJSONString);

        }
        catch (IOException e){
            Log.e(TAG, "Error in getting the data from API, check return code", e);
            // If you can't get the data, no point in parsing.
            return null;
        }

        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (final IOException e) {
                Log.e(TAG, "Error closing stream", e);
            }
        }

        //Parse the JSON format to string/class objects , and return the final list of items!
        try {
            return JSONparser(movieJSONString);
        }catch (JSONException e){
            Log.e(TAG, "Error, could not parse the data from JSON" + e.getMessage());
            return null;
        }
    }


    private List<Movie> JSONparser(String jsonString) throws JSONException{


        final String resultsKey = "results";

        final String titleKey = "title";
        final String originalTitleKey = "original_title";

        final String descriptionKey = "overview";

        final String releaseKey = "release_date";
        final String ratingKey = "vote_average";

        final String posterKey = "poster_path";


        List<Movie> movies = new ArrayList<>();

        JSONObject movieJSON = new JSONObject(jsonString);


        JSONArray movieArray = movieJSON.getJSONArray(resultsKey);

        for (int i = 0; i< movieArray.length();i++){

            JSONObject movieObject = movieArray.getJSONObject(i);

            Movie movie = new Movie();

            movie.setTitle(movieObject.getString(titleKey));
            movie.setOriginalTitle(movieObject.getString(originalTitleKey));

            movie.setDescription(movieObject.getString(descriptionKey));

            movie.setReleaseDate(movieObject.getString(releaseKey));
            movie.setRating(movieObject.getString(ratingKey));

            movie.setPosterImage(movieObject.getString(posterKey));


            movies.add(movie);

            Log.d(TAG, "JSONparser: " + movie.toString());
        }
        return movies;

    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);

        adapter.setMovies(movies);


    }
}
