package net.opacapp.multilinecollapsingtoolbar.demo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final CollapsingToolbarLayout ctl =
            (CollapsingToolbarLayout) findViewById(R.id.collapsible_toolbar);
        ctl.setStatusBarScrimColor(darken(Color.parseColor("#00bdf2")));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    @ColorInt int darken(@ColorInt int color) {
        final double factor = 0.75;
        return (color & 0xFF000000) |
            (crimp((int) (((color >> 16) & 0xFF) * factor)) << 16) |
            (crimp((int) (((color >> 8) & 0xFF) * factor)) << 8) |
            (crimp((int) (((color) & 0xFF) * factor)));
    }

    @ColorInt int crimp(@ColorInt int color) {
        return Math.min(Math.max(color, 0), 255);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
