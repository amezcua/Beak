package net.byteabyte.beak;

import android.app.Application;
import com.facebook.stetho.Stetho;

public class BeakApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();

    Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);
    initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));

    Stetho.initialize(initializerBuilder.build());
  }
}
