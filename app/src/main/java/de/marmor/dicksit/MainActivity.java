package de.marmor.dicksit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.HashSet;
import java.util.InputMismatchException;

import de.marmor.dicksit.game.ChangeCallback;
import de.marmor.dicksit.game.Game;
import de.marmor.dicksit.game.GameException;
import de.marmor.dicksit.game.GameState;
import de.marmor.dicksit.game.config.GameConfig;
import de.marmor.dicksit.game.remote.GameRemoteProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(MainActivity.class.getSimpleName(), "create");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        GameRemoteProvider<Integer> gameRemoteProvider = new GameRemoteProvider<>();
        try {

            HashSet<Integer> hashSet = new HashSet<>();
            for (int i = 0; i < 84; i++) {
                hashSet.add(i + 1);
            }

            Game<Integer> game = gameRemoteProvider.server(new GameConfig<>(hashSet));
            game.register(new ChangeCallback<Integer>() {
                @Override
                public void changed(String uuid, GameState<Integer> gameState) {
                    Log.d(MainActivity.class.getSimpleName(), "change");
                    Log.d(MainActivity.class.getSimpleName(), uuid);
                    Log.d(MainActivity.class.getSimpleName(), gameState.toString());
                }
            });
        } catch (GameException | IOException e) {
            e.printStackTrace();
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
