package com.silentnotary.ui.files.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.silentnotary.R;
import com.silentnotary.ui.FirstActivity;
import com.silentnotary.ui.ISearchableFragment;
import com.silentnotary.ui.IUploadableFileFragment;
import com.silentnotary.ui.files.list.FilesRecyclerView;
import com.silentnotary.ui.files.viewmodel.FilesViewModel;
import com.silentnotary.ui.main.view.IFragmentChangable;
import com.silentnotary.service.ConfirmUploadingService;
import com.silentnotary.widget.FailSnackbar;

/**
 * Created by albert on 9/27/17.
 */

public class FilesFragment extends RxFragment
        implements ISearchableFragment, IUploadableFileFragment {
    @BindView(R.id.files_recyclerview)
    FilesRecyclerView filesRecyclerView;

    @BindView(R.id.swiperefresh)
    android.support.v4.widget.SwipeRefreshLayout swipeRefreshLayout;

    FailSnackbar failSnackbar = null;
    FilesViewModel filesViewModel = null;


    @Override
    public void onQuery(String query) {
        filesViewModel
                .searchFiles(query)
                .subscribe(filesRecyclerView::dataChange,
                        failSnackbar::handleError);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void uploadFile() {
        if (getActivity() instanceof IFragmentChangable) {
            ((IFragmentChangable) getActivity())
                    .showFragment(IFragmentChangable.FRAGMENT_PAGE.UploadFile, null);
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUi();
        }

        public static final String EVENT_NAME = "event_files_list_updated";
    }

    MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);
        ButterKnife.bind(this, view);
        filesViewModel = new FilesViewModel(this);
        failSnackbar = new FailSnackbar(view);
        initUI();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(myBroadcastReceiver,
                new IntentFilter(MyBroadcastReceiver.EVENT_NAME));
        getActivity().startService(new Intent(getActivity(), ConfirmUploadingService.class));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity())
                .unregisterReceiver(myBroadcastReceiver);
    }

    void initUI() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        filesRecyclerView.setLayoutManager(llm);
        swipeRefreshLayout.setOnRefreshListener(this::updateUi);
        filesRecyclerView.setOnActionClick(file -> {
            FragmentActivity activity = getActivity();
            if (activity instanceof IFragmentChangable) {
                Bundle arguments = new Bundle();
                arguments.putSerializable(DetailFileFragment.Extra.FILE, file);
                ((IFragmentChangable) activity)
                        .showFragment(IFragmentChangable.FRAGMENT_PAGE.FileDetail, arguments);
            }

        });
    }

    void updateUi() {
        this.filesViewModel
                .getAllFiles()
                .subscribe(s -> {
                    swipeRefreshLayout.setRefreshing(false);
                    if (getActivity() instanceof FirstActivity) {
                        String query = ((FirstActivity) getActivity())
                                .tryToApplyQueryFilter();
                        onQuery(query);
                    } else
                        filesRecyclerView.dataChange(s);
                }, err -> {
                    swipeRefreshLayout.setRefreshing(false);
                    failSnackbar.handleError(err);
                });
    }
}
