package gr.uom.android.dai16051project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView listView = findViewById(R.id.listView);

        adapter = new MovieAdapter(MainActivity.this, R.layout.movie_item , new ArrayList<Movie>());
        listView.setAdapter(adapter);

        FetchMovieTask task = new FetchMovieTask(adapter);
        task.execute();

    }
}
