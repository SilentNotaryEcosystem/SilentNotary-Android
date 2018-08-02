package com.silentnotary.ui.conversation.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxFragment;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.silentnotary.R;
import com.silentnotary.ui.FragmentHelper;
import com.silentnotary.ui.conversation.list.ConversationRecyclerView;
import com.silentnotary.ui.conversation.viewmodel.ConversationViewModel;
import com.silentnotary.ui.exception.CannotDownloadConverstationException;
import com.silentnotary.api.retrofit.ConversationAPI;
import com.silentnotary.util.PermissionChecker;
import com.silentnotary.widget.FailSnackbar;

/**
 * Created by albert on 9/27/17.
 */

public class ConversationFragment extends RxFragment {

    ConversationViewModel viewModel;
    FailSnackbar snackbar;

    @BindView(R.id.recyclerView)
    ConversationRecyclerView recyclerView;

    @BindView(R.id.swiperefresh)
    android.support.v4.widget.SwipeRefreshLayout swipeRefreshLayout;

    void downloadFile(ConversationAPI.ChatBotConversationData item){
        if(item.getHashHex() == null || item.getNonceHex() == null
                || item.getHashHex().isEmpty() || item.getNonceHex().isEmpty()){
            snackbar.handleError(new CannotDownloadConverstationException());
            return;
        }

        FragmentHelper.showLoader(this);
        PermissionChecker.checkStoragePermission(getActivity())
                .flatMap(s -> viewModel.downloadFile(item))
                .doOnNext(s -> FragmentHelper.hideLoader(this))
                .doOnError(err -> FragmentHelper.hideLoader(this))
                .subscribe(this::handleFileDownloaded, snackbar::handleError);
    }

    private void handleFileDownloaded(File file) {
        Toast.makeText(getActivity(), String.format("File %s downloaded inside the 'Downloads/SilentNotary/conversations' folder.", file.getName()), Toast.LENGTH_LONG)
                .show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);

        ButterKnife.bind(this, view);
        viewModel = new ConversationViewModel(this);
        snackbar = new FailSnackbar(view);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.setOnItemClick(this::downloadFile);

        swipeRefreshLayout.setOnRefreshListener(this::refreshUI);

        refreshUI();

        return view;
    }

    void disableLoader(){
        FragmentHelper.hideLoader(this);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void refreshUI(){
        FragmentHelper.showLoader(this);
        viewModel.getConversations()
                .doOnComplete(this::disableLoader)
                .doOnError(err -> disableLoader())
                .subscribe(recyclerView::newList,
                        snackbar::handleError);
    }
}
