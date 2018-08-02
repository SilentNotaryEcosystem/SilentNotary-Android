package com.silentnotary.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.silentnotary.R;
import com.silentnotary.ui.FirstActivity;
import com.silentnotary.ui.files.view.FilesFragment;
import com.silentnotary.api.requestmanager.FileRequestManager;
import com.silentnotary.dao.UploadedFilesConfirmationDao;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

/**
 * Created by albert on 10/4/17.
 */

public class ConfirmUploadingService extends Service {

    public static final String TAG = ConfirmUploadingService.class.getSimpleName();

    private int getNotificationIcon() {
        return R.mipmap.ic_launcher;
    }

    public void handleConfirmedSubscription(String token) {
        Intent intent = new Intent(FilesFragment.MyBroadcastReceiver.EVENT_NAME);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Intent actionIntent = new Intent(getApplicationContext(), FirstActivity.class);
        int requestID = (int) System.currentTimeMillis();
        actionIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        actionIntent.putExtra(FirstActivity.Extra.SEARCH_FILTER, token);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestID,
                actionIntent, 0);
        try {
            Uri alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSound(alertSound)
                            .setContentTitle("Confirmed!")
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setContentText(String.format("%s claim is confirmed!", token));
            mBuilder.setSmallIcon(getNotificationIcon());
            int mNotificationId = 001;
            NotificationManager mNotifyMgr =
                    (NotificationManager) this
                            .getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public void notifyFragment() {
        Intent intent = new Intent(FilesFragment.MyBroadcastReceiver.EVENT_NAME);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void handleError(Throwable throwable) {
        Log.d(TAG, throwable.getMessage());
    }

    class WorkerThread extends Thread {
        boolean interupted = false;

        void interupt() {
            interupted = true;
        }

        boolean isInterupted() {
            return interupted;
        }

        @Override
        public void run() {
            Log.d(TAG, "Starting thread");
            FileRequestManager fileRequestManager = new FileRequestManager(getApplicationContext());
            UploadedFilesConfirmationDao uploadedFilesConfirmationDao = new UploadedFilesConfirmationDao();
            while (!interupted) {
                uploadedFilesConfirmationDao
                        .getAll()
                        .flatMap(Observable::fromIterable)
                        .flatMap(s -> fileRequestManager
                                .confirmUploading(s.getConfirmationTokenHex())
                                .onErrorResumeNext((ObservableSource<? extends FileRequestManager.FileConfirmationWrapper>) err
                                        -> Observable.<FileRequestManager.FileConfirmationWrapper>empty())
                        )
                        .flatMap(s -> {
                            if (s.getResponse().isConfirmationSuccesefull())
                                return uploadedFilesConfirmationDao
                                        .removeConfirmation(s.getToken())
                                        .map(r -> s.getResponse().getHashHex());
                            else if (s.getResponse().isFileNotFound()) {
                                uploadedFilesConfirmationDao.removeConfirmation(s.getToken())
                                        .subscribe(r -> notifyFragment());
                            }
                            return Observable.empty();
                        })
                        .subscribe(ConfirmUploadingService.this::handleConfirmedSubscription,
                                ConfirmUploadingService.this::handleError);
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    WorkerThread workerThread = null;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (workerThread != null)
            workerThread.interupt();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting service...");
        if (workerThread == null || workerThread.isInterupted()) {
            Log.d(TAG, "Thread");
            workerThread = new WorkerThread();
            workerThread.start();
        } else {
            Log.d(TAG, "No need to start new thread");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
