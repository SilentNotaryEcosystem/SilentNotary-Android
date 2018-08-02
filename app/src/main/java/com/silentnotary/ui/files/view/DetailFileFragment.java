package com.silentnotary.ui.files.view;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.silentnotary.R;
import com.silentnotary.ui.FragmentHelper;
import com.silentnotary.ui.files.viewmodel.DetailFileViewModel;
import com.silentnotary.ui.main.view.IFragmentChangable;
import com.silentnotary.ui.zip.ZipFragment;
import com.silentnotary.databinding.FileDetailFragmentBinding;
import com.silentnotary.model.File;
import com.silentnotary.util.PermissionChecker;
import com.silentnotary.util.WebUtil;
import com.silentnotary.widget.ConfirmDeleteFileDialogBuilder;
import com.silentnotary.widget.FailSnackbar;
import com.silentnotary.widget.ImageButtonEffect;

/**
 * Created by albert on 10/12/17.
 */

public class DetailFileFragment extends RxFragment {

    @BindView(R.id.showTransactionButton)
    View showTransactionButton;

    @BindView(R.id.deleteButton)
    View mDeleteButton;

    @BindView(R.id.downloadButton)
    View mDownloadButton;

    DetailFileViewModel viewModel;

    FailSnackbar failSnackbar;

    public static File latestDetailFile = null;

    public interface Extra {
        public static final String FILE = "file";
    }

    File file;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.file_detail_fragment, container, false);

        this.file = (File) getArguments().getSerializable(Extra.FILE);

        latestDetailFile = this.file;

        FileDetailFragmentBinding binding = DataBindingUtil.bind(view);
        binding.setItem(file);

        ButterKnife.bind(this, view);

        if (file.getEtherTx() != null && !file.getEtherTx().isEmpty())
            showTransactionButton
                    .setOnClickListener(view1 -> WebUtil.openWebpage(String.format("https://kovan.etherscan.io/tx/%s", file.getEtherTx()), (Activity) getContext()));
        else
            showTransactionButton
                    .setVisibility(View.GONE);


        mDeleteButton.setOnTouchListener(new ImageButtonEffect(v -> {
            deleteFile();
        }, ImageButtonEffect.LIGHT_GRAY_COLOR));
        mDownloadButton.setOnTouchListener(new ImageButtonEffect(v -> {
            downloadFile();
        }, ImageButtonEffect.LIGHT_GRAY_COLOR));

        viewModel = new DetailFileViewModel(this);
        failSnackbar = new FailSnackbar(view);
        return view;
    }

    private void downloadFile() {
        FragmentHelper.showLoader(this);
        PermissionChecker.checkStoragePermission(getActivity())
                .flatMap(s -> viewModel.downloadFile(this.file))
                .doOnNext(s -> FragmentHelper.hideLoader(this))
                .doOnError(err -> FragmentHelper.hideLoader(this))
                .subscribe(this::handleFileDownloaded, failSnackbar::handleError);
    }

    private void handleFileDownloaded(java.io.File file) {
        Activity activity = getActivity();
        if (activity != null && activity instanceof IFragmentChangable) {
            Bundle bundle = new Bundle();
            bundle.putString(ZipFragment.Extra.ZIP_LOCATION, file.getAbsolutePath());

            ((IFragmentChangable) activity)
                    .showFragment(IFragmentChangable.FRAGMENT_PAGE.ZipViewer, bundle);
        }
    }

    private void handleFileDeleted(File file) {
        Toast.makeText(getActivity(), String.format("File with hash %s deleted!", file.getHash()),
                Toast.LENGTH_LONG)
                .show();
        getActivity().onBackPressed();
    }

    private void deleteFile() {
        ConfirmDeleteFileDialogBuilder.buildAndShow(getActivity(), (dialogInterface, i) -> {
            FragmentHelper.showLoader(this);
            viewModel.deleteFile(file)
                    .doOnNext(s -> FragmentHelper.hideLoader(this))
                    .doOnError(err -> FragmentHelper.hideLoader(this))
                    .subscribe(DetailFileFragment.this::handleFileDeleted, failSnackbar::handleError);
        });
    }
}
