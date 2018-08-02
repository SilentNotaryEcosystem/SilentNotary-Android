package com.silentnotary.ui.auth.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.silentnotary.ui.auth.Validator;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.silentnotary.R;
import com.silentnotary.ui.auth.IKeyboardListener;
import com.silentnotary.api.exception.CannotRegisterUserException;
import com.silentnotary.widget.FailSnackbar;
import com.silentnotary.widget.ImageButtonEffect;

/**
 * Created by albert on 9/29/17.
 */

public class RegisterFrament extends RxFragment implements IKeyboardListener {

    RegisterViewModel registerViewModel = null;

    @BindView(R.id.mEditTextEmail)
    TextInputEditText mEditTextEmail;

    @BindView(R.id.mEditTextPassword)
    TextInputEditText mEditTextPassword;

    @BindView(R.id.mEditTextPasswordConfirm)
    TextInputEditText mEditTextPasswordConfirm;

    @BindView(R.id.mButtonSignup)
    View buttonSignup;

    @BindView(R.id.toolbar_loader)
    View loader;

    @BindView(R.id.emptyMarginBlock)
    View emptyMarginBlock;

    @BindView(R.id.logoImg)
    View logoImg;

    FailSnackbar failSnackbar = null;


    void signup() {
        boolean valid = Validator.validateEmail(mEditTextEmail);
        valid &= Validator.validatePassword(mEditTextPassword);
        valid &= Validator.validatePasswordInputs(mEditTextPassword, mEditTextPasswordConfirm);

        if (valid) {
            loader.setVisibility(View.VISIBLE);
            buttonSignup.setEnabled(false);
            registerViewModel
                    .signUp(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString())
                    .subscribe(e -> handleRegister(),
                            this::handleError);
        }
    }

    @OnClick(R.id.mButtonViewForgetPassword)
    void forgetPassword() {
        registerViewModel.goToForgetPass();
    }

    @OnClick(R.id.mButtonViewSignIn)
    void goToSigIn() {
        registerViewModel.goToSignIn();
    }

    private void handleError(Throwable throwable) {
        if (throwable instanceof CannotRegisterUserException) {
            mEditTextEmail.setError(getString(R.string.username_already_exist));
        } else {
            failSnackbar.handleError(throwable);
        }
        buttonSignup.setEnabled(true);
        loader.setVisibility(View.GONE);
    }

    void handleRegister() {
        registerViewModel.finishAuth();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        registerViewModel = new RegisterViewModel(this);
        failSnackbar = new FailSnackbar(view);
        buttonSignup.setOnTouchListener(new ImageButtonEffect(view1 -> signup(), ImageButtonEffect.LIGHT_GRAY_COLOR));
        return view;
    }

    @Override
    public void onChange(boolean isOpen) {
        emptyMarginBlock.setVisibility(isOpen ? View.GONE : View.VISIBLE);
    }
}
