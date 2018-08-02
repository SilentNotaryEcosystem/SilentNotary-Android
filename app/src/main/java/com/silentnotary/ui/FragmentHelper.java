package com.silentnotary.ui;

import android.support.v4.app.FragmentActivity;

import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by albert on 10/14/17.
 */

public class FragmentHelper {

    public static void showLoader(RxFragment fragment) {
        FragmentActivity activity = fragment.getActivity();
        if (activity != null && activity instanceof IProgressBar) {
            ((IProgressBar) activity)
                    .showLoader();
        }
    }

    public static void hideLoader(RxFragment fragment) {
        FragmentActivity activity = fragment.getActivity();
        if (activity != null && activity instanceof IProgressBar) {
            ((IProgressBar) activity)
                    .hideLoader();
        }
    }
}
