package com.silentnotary.ui.conversation.viewmodel;

import com.trello.rxlifecycle2.components.support.RxFragment;

import java.io.File;
import java.util.List;

import com.silentnotary.api.requestmanager.ConversationApiRequestManager;
import com.silentnotary.api.requestmanager.FileRequestManager;
import com.silentnotary.api.retrofit.ConversationAPI;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by albert on 10/11/17.
 */

public class ConversationViewModel {

    private ConversationApiRequestManager requestManager;
    private RxFragment fragment;

    private FileRequestManager fileRequestManager;

    public ConversationViewModel(RxFragment fragment) {
        this.fragment = fragment;
        requestManager = new ConversationApiRequestManager();
        fileRequestManager = new FileRequestManager(fragment.getActivity());
    }

    public Observable<List<ConversationAPI.ChatBotConversationData>> getConversations() {
        return requestManager.getFacebookConversations()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(fragment.bindToLifecycle());
    }

    public Observable<File> downloadFile(ConversationAPI.ChatBotConversationData data) {
        return fileRequestManager
                .downloadFile(data.getHashHex(), data.getNonceHex(), "conversations")
                .compose(fragment.bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
