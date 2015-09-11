package gaurav.sundim7.dias;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by gaurav on 9/8/2015.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "zvCP0m9tEm4jWGOSthyFmurfaBaGDW0tK30PUGvC", "RV2Qe08wqEJzIZhq9KjeGSa5F2XsB27OuFGs2UHu");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
