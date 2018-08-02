package com.silentnotary.ui.auth.restore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.silentnotary.R;
import com.silentnotary.ui.auth.IKeyboardListener;
import com.silentnotary.widget.FailSnackbar;
import com.silentnotary.widget.ImageButtonEffect;

/**
 * Created by albert on 10/10/17.
 */

public class RestorePasswordFragment
        extends com.trello.rxlifecycle2.components.support.RxFragment
        implements IKeyboardListener {

    @BindView(R.id.mEditTextEmail)
    TextInputEditText mEditTextEmail;

    @BindView(R.id.mButtonRestorePass)
    View mButtonRestorePass;

    @BindView(R.id.toolbar_loader)
    View loader;

    @BindView(R.id.emptyMarginBlock)
    View emptyMarginBlock;

    RestorePasswordViewModel restorePasswordViewModel;
    FailSnackbar failSnackbar;


    void restore() {
        loader.setVisibility(View.VISIBLE);
        mButtonRestorePass.setEnabled(false);
        restorePasswordViewModel.restorePassword(mEditTextEmail.getText().toString())
                .subscribe(this::handlePasswordRestored, this::handleError);
    }

    private void handleError(Throwable throwable) {
        mButtonRestorePass.setEnabled(true);
        failSnackbar.handleError(throwable);
         loader.setVisibility(View.GONE);
    }

    @OnClick(R.id.mButtonLogin)
    void backToLogin() {
        restorePasswordViewModel.backToLogin();
    }

    void handlePasswordRestored() {
        Toast.makeText(getActivity(), "Restore password link was sent to your email.", Toast.LENGTH_LONG)
                .show();
        restorePasswordViewModel.backToLogin();
     }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        ButterKnife.bind(this, view);
        restorePasswordViewModel = new RestorePasswordViewModel(this);
        failSnackbar = new FailSnackbar(view);
        mButtonRestorePass.setOnTouchListener(new ImageButtonEffect(view1 -> restore(), ImageButtonEffect.LIGHT_GRAY_COLOR));
        return view;
    }

    @Override
    public void onChange(boolean isOpen) {
        emptyMarginBlock.setVisibility(isOpen ? View.GONE : View.VISIBLE);
    }
}
