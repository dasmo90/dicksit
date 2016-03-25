package de.marmor.dicksit;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashSet;

import de.marmor.dicksit.game.ChangeCallback;
import de.marmor.dicksit.game.Game;
import de.marmor.dicksit.game.GameException;
import de.marmor.dicksit.game.GameState;
import de.marmor.dicksit.game.config.GameConfig;
import de.marmor.dicksit.game.remote.GameRemoteProvider;

public class MainActivity extends AppCompatActivity {

    private Typeface TYPEFACE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TYPEFACE = Typeface.createFromAsset(getAssets(), "fonts/Drift___.ttf");

        Log.d(MainActivity.class.getSimpleName(), "create");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView headline = (TextView) findViewById(R.id.text_headline);
        if(headline != null) {
            headline.setTypeface(TYPEFACE);
        }

        TextView newGame = (TextView) findViewById(R.id.btn_new_game);
        if(newGame != null) {
            newGame.setTypeface(TYPEFACE);
        }

        TextView joinGame = (TextView) findViewById(R.id.btn_join_game);
        if(joinGame != null) {
            joinGame.setTypeface(TYPEFACE);
        }

        TextView password = (TextView) findViewById(R.id.input_pw);
        if(password != null) {
            password.setTypeface(null);
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
