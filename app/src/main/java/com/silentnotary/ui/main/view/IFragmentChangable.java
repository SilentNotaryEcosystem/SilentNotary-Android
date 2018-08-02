package com.silentnotary.ui.main.view;

import android.os.Bundle;

/**
 * Created by albert on 10/3/17.
 */

public interface IFragmentChangable {
    enum FRAGMENT_PAGE{
        Conversation,
        Files,
        UploadFile,
        FileDetail,
        UserProfile,
        EmptyFragment,
        EmptyCompatFragment,
        ZipViewer
    }

    abstract void showFragment(FRAGMENT_PAGE page, Bundle argument);
}
