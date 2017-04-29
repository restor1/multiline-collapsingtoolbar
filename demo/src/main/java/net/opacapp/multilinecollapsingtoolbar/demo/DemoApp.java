package net.opacapp.multilinecollapsingtoolbar.demo;

import android.app.Application;
import com.codemonkeylabs.fpslibrary.TinyDancer;

public class DemoApp extends Application {

  @Override public void onCreate() {
    super.onCreate();
    if (BuildConfig.DEBUG) {
      TinyDancer.create().show(this);
    }
  }
}
