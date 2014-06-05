package com.openpeer.sample.app;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by brucexia on 2014-06-02.
 */
public class OpenPeerApplication extends Application {
    private ObjectGraph graph;

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize openpeer sdk
        graph = ObjectGraph.create(getModules().toArray());
        OPHelper.getInstance().init(this);

    }
    protected List<Object> getModules() {
        return Arrays.asList(
                new AndroidModule(this),
                new ActivitiesModule()
        );
    }
    public void inject(Object object) {
        graph.inject(object);
    }
}
