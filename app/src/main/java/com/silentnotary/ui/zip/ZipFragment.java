package com.silentnotary.ui.zip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.silentnotary.R;
import com.silentnotary.ui.FragmentHelper;
import com.silentnotary.util.FileOpener;
import com.silentnotary.widget.FailSnackbar;

/**
 * Created by albert on 10/27/17.
 */

public class ZipFragment extends RxFragment {
    @BindView(R.id.recyclerView)
    ZipViewerRecyclerView recyclerView;

    ZipFileViewModel viewModel;

    FailSnackbar snackbar;

    public interface Extra{
        String ZIP_LOCATION = "zip_location";
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zip_viewer, container, false);

        ButterKnife.bind(this, view);

        viewModel = new ZipFileViewModel(this);
        snackbar = new FailSnackbar(view);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);



        recyclerView.setOnItemClick(item -> {
            try{
                FileOpener.openFile(getActivity(), item.getFile());
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        FragmentHelper.showLoader(this);
        viewModel.unzip( getArguments().getString(Extra.ZIP_LOCATION))
                .doOnComplete(this::disableLoader)
                .doOnError(err -> disableLoader())
                .subscribe(recyclerView::newList, snackbar::handleError);


        return view;
    }

    private void disableLoader() {
        FragmentHelper.hideLoader(this);
    }
}
