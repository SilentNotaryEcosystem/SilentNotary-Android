package com.silentnotary.ui.files.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flipboard.bottomsheet.commons.MenuSheetView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

import com.silentnotary.R;
import com.silentnotary.ui.files.exception.CannotOpenCameraException;
import com.silentnotary.ui.files.list.UploadFilesRecyclerView;
import com.silentnotary.ui.files.viewmodel.FilesViewModel;
import com.silentnotary.ui.files.viewmodel.UploadFileViewModel;
import com.silentnotary.ui.main.view.IFragmentChangable;
import com.silentnotary.api.exception.CannotConfirmUploading;
import com.silentnotary.api.exception.CannotOpenAudioRecoderException;
import com.silentnotary.api.exception.CannotUploadFilesException;
import com.silentnotary.api.retrofit.FileAPI;
import com.silentnotary.util.CacheFileUtil;
import com.silentnotary.util.CameraUtil;
import com.silentnotary.util.PermissionChecker;
import com.silentnotary.util.PrefUtil;
import com.silentnotary.widget.FailSnackbar;
import com.silentnotary.widget.ImageButtonEffect;
import com.silentnotary.widget.alert.SnackbarNeedPermission;

import static android.app.Activity.RESULT_OK;

/**
 * Created by albert on 10/3/17.
 */

public class UploadFilesFragment extends UploadFilesFragmentBase {
    FileAPI.UploadFileResponse lastUploadFileResponse = null;
    State currentState = State.STATE_NONE;
    boolean saveInstance = true;

    enum State {
        STATE_NONE,
        STATE_READY_TO_UPLOAD,
        STATE_READY_TO_CONFIRM
    }

    public void setState(State state) {
        this.currentState = state;
        String textStatus = "";
        if (state == State.STATE_NONE) {
            mButtonUploadFiles.setEnabled(true);
            textStatus = "Upload";
        } else if (state == State.STATE_READY_TO_UPLOAD) {
            textStatus = "Upload";
            mButtonUploadFiles.setEnabled(true);
        } else {
            textStatus = "Confirm";
            mButtonUploadFiles.setEnabled(true);
        }

        mButtonUploadFileText.setText(textStatus);
    }

    private void openChooser() {
        MenuSheetView menuSheetView =
                new MenuSheetView(getActivity(), MenuSheetView.MenuType.GRID,
                        getString(R.string.upload_waiting), item -> {
                    if (bottomSheetLayout.isSheetShowing()) {
                        bottomSheetLayout.dismissSheet();
                    }

                    if (item.getItemId() == R.id.menu_photo) {
                        PermissionChecker.checkPermission(getActivity())
                                .subscribe(s -> openPhotoCamera());

                    } else if (item.getItemId() == R.id.menu_video) {
                        PermissionChecker.checkPermission(getActivity())
                                .subscribe(s -> openVideoCamera());

                    } else if (item.getItemId() == R.id.menu_file) {
                        PermissionChecker.checkPermission(getActivity())
                                .flatMap(s -> viewModel
                                        .getFileFromStorage())
                                .subscribe(this::handleAddFile,
                                        failSnackbar::handleError);
                    } else if (item.getItemId() == R.id.menu_mic) {
                        PermissionChecker.checkPermission(getActivity())
                                .subscribe(s -> openAudioRecorder());

                    }
                    return true;
                });

        menuSheetView.inflateMenu(R.menu.bottom_menu);
        bottomSheetLayout.showWithSheetView(menuSheetView);
    }

    public void addFile() {
        showBottomSheet();
    }

    private void showBottomSheet() {
        openChooser();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CAPTURE_PHOTO:
                    try {
                        CameraUtil.copyFromMediaStoreIntoCache(data == null ? null : data.getData(), getActivity(),
                                UploadFilesFragment.this, "jpg")
                                .subscribe(this::handleAddFile, this::handleError);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case REQUEST_CODE_CAPTURE_VIDEO:
                    try {
                        CameraUtil.copyFromMediaStoreIntoCache(data.getData(), getActivity(), UploadFilesFragment.this, "mp4")
                                .subscribe(this::handleAddFile, this::handleError);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case REQUEST_CODE_RECORD_AUDIO:
                    handleAddFile(CacheFileUtil.getFileByKey("audio"));
                    break;
            }
        }
    }

    public void handleAddFile(File file) {
        if (file == null || !file.exists() || file.length() == 0) return;
        UploadFilesRecyclerView.FilesAdapter adapter
                = (UploadFilesRecyclerView.FilesAdapter) recyclerView.getAdapter();
        adapter.addItem(file);
    }

    void handleUploadFiles(List<File> files) {
        showLoader();
        viewModel
                .upload(files, mEditTextFilesComment.getText().toString())
                .subscribe(this::handleUploadFile, err -> handleError(new CannotUploadFilesException()),
                        this::disableLoader);
    }

    public void handleUploadFile(FileAPI.UploadFileResponse response) {
        this.lastUploadFileResponse = response;
        showStatus(response);
        setState(State.STATE_READY_TO_CONFIRM);
    }

    public void handleError(Throwable throwable) {
        if (throwable instanceof FilesViewModel.RationalMessageNeedToBeShownException) {
            new SnackbarNeedPermission(getContext(),
                    getContext().getString(R.string.storage))
                    .showAlert();
        } else if (throwable instanceof CannotUploadFilesException) {
            Toast.makeText(getActivity(), "Cannot upload file", Toast.LENGTH_LONG).show();
            this.disableLoader();
        } else if (throwable instanceof CannotConfirmUploading) {
            Toast.makeText(getActivity(), "Cannot confirm uploading", Toast.LENGTH_LONG).show();
        }
        updateUploadButton();
    }

    @Override
    public void onDestroyView() {
        if (saveInstance) {
            try {
                UploadFilesRecyclerView.FilesAdapter adapter = (UploadFilesRecyclerView.FilesAdapter) recyclerView.getAdapter();
                List<File> files = adapter.getFiles();
                PrefUtil.saveLatestUploadedFiles(files);
            } catch (Exception e) {

            }
        }

        super.onDestroyView();
    }

    public void updateUploadButton() {
        UploadFilesRecyclerView.FilesAdapter adapter = (UploadFilesRecyclerView.FilesAdapter) recyclerView.getAdapter();
        if (adapter.getItemCount() == 0) {
            setState(State.STATE_NONE);
        } else {
            setState(State.STATE_READY_TO_UPLOAD);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_files, container, false);
        ButterKnife.bind(this, view);
        failSnackbar = new FailSnackbar(view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setOnDataChanged(this::updateUploadButton);
        recyclerView.dataChange(PrefUtil.getLatestUploadedFiles());
        mButtonUploadFiles.setOnTouchListener(new ImageButtonEffect(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentState == State.STATE_NONE) {
                    showBottomSheet();
                } else if (currentState == State.STATE_READY_TO_UPLOAD) {

                    UploadFilesRecyclerView.FilesAdapter adapter = (UploadFilesRecyclerView.FilesAdapter) recyclerView.getAdapter();
                    List<File> files = adapter.getFiles();
                    if (!files.isEmpty()) {
                        handleUploadFiles(files);
                    }
                } else {
                    if (lastUploadFileResponse == null
                            || recyclerView.getFiles().isEmpty()) {
                        return;
                    }
                    viewModel.saveTransactionToDb(lastUploadFileResponse.getConfirmationToken(),
                            mEditTextFilesComment.getText().toString(), recyclerView.getFiles())
                            .subscribe(UploadFilesFragment.this::handleSaveConfirmationToDb,
                                    UploadFilesFragment.this::handleError);
                }
            }
        }, ImageButtonEffect.LIGHT_GRAY_COLOR));
        return view;
    }

    private void handleSaveConfirmationToDb(String token) {
        try {
            PrefUtil.saveLatestUploadedFiles(new ArrayList<>());
        } catch (Exception e) {
        }

        Toast.makeText(getActivity(), "Confirmation is started...You will receive notification when confirmation will finish.", Toast.LENGTH_LONG)
                .show();
        saveInstance = false;
        if (getActivity() instanceof IFragmentChangable) {
            ((IFragmentChangable) getActivity())
                    .showFragment(IFragmentChangable.FRAGMENT_PAGE.Files, null);
        }
    }

    private void handleConfirmUploading(FileAPI.ConfirmFileResponse confirmFileResponse) {
        if (getActivity() instanceof IFragmentChangable) {
            ((IFragmentChangable) getActivity())
                    .showFragment(IFragmentChangable.FRAGMENT_PAGE.Files, null);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new UploadFileViewModel(this);
        showBottomSheet();
    }

    @Override
    void openPhotoCamera() {
        Intent capturePhotoIntent = CameraUtil.getCapturePhotoIntent(getContext());
        if (capturePhotoIntent != null)
            startActivityForResult(capturePhotoIntent,
                    REQUEST_CODE_CAPTURE_PHOTO);
        else
            failSnackbar.handleError(new CannotOpenCameraException(getContext()));
    }

    @Override
    void openVideoCamera() {
        Intent captureVideoIntent = null;
        try {
            captureVideoIntent = CameraUtil.getCaptureVideoIntent(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (captureVideoIntent != null)
            startActivityForResult(captureVideoIntent,
                    REQUEST_CODE_CAPTURE_VIDEO);
        else
            failSnackbar.handleError(new CannotOpenCameraException(getContext()));
    }

    @Override
    void openAudioRecorder() {
        String filePath = null;
        try {
            filePath = CacheFileUtil.createRandomFileByKey("audio", "wav", getActivity()).getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (filePath == null) {
            this.handleError(new CannotOpenAudioRecoderException());
            return;
        }
        int color = getResources().getColor(R.color.colorPrimaryDark);
        AndroidAudioRecorder.with(getActivity())
                .setFilePath(filePath)
                .setColor(color)
                .setRequestCode(REQUEST_CODE_RECORD_AUDIO)
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(false)
                .setKeepDisplayOn(true)
                .record();
    }
}
