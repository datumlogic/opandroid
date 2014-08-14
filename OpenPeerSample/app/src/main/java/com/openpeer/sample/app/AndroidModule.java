package com.openpeer.sample.app;

/**
 * Created by brucexia on 2014-06-03.
 */

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 */
@Module(library = true)
public class AndroidModule {
    private final OpenPeerApplication application;

    public AndroidModule(OpenPeerApplication application) {
        this.application = application;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides @Singleton
    @ForApplication
    Context provideApplicationContext() {
        return application;
    }

//    @Provides
//    @Singleton LocationManager provideLocationManager() {
//        return (LocationManager) application.getSystemService(LOCATION_SERVICE);
//    }
}
