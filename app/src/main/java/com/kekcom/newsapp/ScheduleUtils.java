package com.kekcom.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;


public class ScheduleUtils {
    //the timer until next refresh
    private static final int SCHEDULE_INTERVAL_MINUTES = 1;
    //how long it will sync with database
    private static final int SYNC_FLEXTIME_SECONDS = 10;
    private static final String NEWS_JOB_TAG = "news_job_tag";
    //make this the one and only initializer
    private static boolean sInitialized;

    synchronized public static void scheduleRefresh(@NonNull final Context context) {
        if (sInitialized) return;
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        //set the news job
        Job constraintRefreshJob = dispatcher.newJobBuilder()
                .setService(NewsJob.class)
                .setTag(NEWS_JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SCHEDULE_INTERVAL_MINUTES,
                        SCHEDULE_INTERVAL_MINUTES + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        //now dispatch
        dispatcher.schedule(constraintRefreshJob);
        sInitialized = true;

    }
}
