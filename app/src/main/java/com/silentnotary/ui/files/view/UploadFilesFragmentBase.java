package com.silentnotary.ui.files.view;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.text.DecimalFormat;

import butterknife.BindView;

import com.silentnotary.R;
import com.silentnotary.ui.FragmentHelper;
import com.silentnotary.ui.files.list.UploadFilesRecyclerView;
import com.silentnotary.ui.files.viewmodel.UploadFileViewModel;
import com.silentnotary.api.retrofit.FileAPI;
import com.silentnotary.widget.FailSnackbar;

/**
 * Created by albert on 10/3/17.
 */

public abstract class UploadFilesFragmentBase extends RxFragment {
    @BindView(R.id.uploadFileRecyclerView)
    UploadFilesRecyclerView recyclerView;

    @BindView(R.id.bottomsheet)
    BottomSheetLayout bottomSheetLayout;

    @BindView(R.id.buttonUpload)
    View mButtonUploadFiles;

    @BindView(R.id.buttonUploadText)
    TextView mButtonUploadFileText;

    @BindView(R.id.mEditTextFilesComment)
    EditText mEditTextFilesComment;

    @BindView(R.id.statusTextViewPriceValue)
    TextView mTextViewStatusUploadPrice;

    @BindView(R.id.statusTextViewTokenValue)
    TextView mTextViewStatusUploadToken;

    @BindView(R.id.mStatusViewWrapper)
    View mStatusWrapper;

    protected static final int REQUEST_CODE_CAPTURE_PHOTO = 111;
    protected static final int REQUEST_CODE_CAPTURE_VIDEO = 112;
    protected static final int REQUEST_CODE_FILE = 113;
    protected static final int REQUEST_CODE_RECORD_AUDIO = 114;

    UploadFileViewModel viewModel;
    FailSnackbar failSnackbar = null;

    void showLoader() {
        this.mButtonUploadFiles.setEnabled(false);
        FragmentHelper.showLoader(this);
    }

    void disableLoader() {
        this.mButtonUploadFiles.setEnabled(true);
        FragmentHelper.hideLoader(this);
    }

    void showStatus(FileAPI.UploadFileResponse fileResponse) {
        mStatusWrapper.setVisibility(View.VISIBLE);
        mTextViewStatusUploadPrice.setText(
                new DecimalFormat("#.##").format(fileResponse.getInitialFee()) + " USD");
        mTextViewStatusUploadToken.setText(fileResponse.getConfirmationToken());
    }

    abstract void openPhotoCamera();

    abstract void openVideoCamera();

    abstract void openAudioRecorder();
}
