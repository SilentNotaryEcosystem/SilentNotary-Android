package com.silentnotary.ui.auth.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.silentnotary.ui.auth.Validator;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.silentnotary.R;
import com.silentnotary.ui.auth.IKeyboardListener;
import com.silentnotary.api.exception.InvalidLoginOrPassException;
import com.silentnotary.util.PrefUtil;
import com.silentnotary.widget.FailSnackbar;
import com.silentnotary.widget.ImageButtonEffect;

/**
 * Created by albert on 9/29/17.
 */

public class LoginFragment extends RxFragment implements IKeyboardListener {

    @BindView(R.id.mEditTextEmail)
    TextInputEditText mEditTextEmail;

    @BindView(R.id.mEditTextPassword)
    TextInputEditText mEditTextPassword;

    @BindView(R.id.mButtonSignin)
    View buttonSign;

    @BindView(R.id.loginInputLayout)
    LinearLayout loginInputLayout;

    @BindView(R.id.toolbar_loader)
    View loader;


    LoginViewModel loginViewModel;

    FailSnackbar failSnackbar = null;

    @BindView(R.id.emptyMarginBlock)
    View emptyMarginBlock;

    void sigin() {
        boolean valid = Validator.validateEmail(mEditTextEmail);
        valid &= Validator.validatePassword(mEditTextPassword);

        if (valid) {
            buttonSign.setEnabled(false);
            loader.setVisibility(View.VISIBLE);
            loginViewModel
                    .signIn(mEditTextEmail.getText().toString(),
                            mEditTextPassword.getText().toString())
                    .subscribe(e -> this.handleAuth(e,
                            mEditTextEmail.getText().toString(),
                            mEditTextPassword.getText().toString()),
                            this::handleError);
        }
    }

    @OnClick(R.id.mButtonViewForgetPassword)
    void forgetPassword() {
        loginViewModel.goToForgetPass();
    }


    @OnClick(R.id.mButtonViewSignup)
    void signup() {
        loginViewModel.goToSignup();
    }


    void handleAuth(String sessionId, String email, String pass) {
        loginViewModel.finishAuth(sessionId, email);
        buttonSign.setEnabled(true);

        PrefUtil.setLoginCredentials(email, pass);
    }

    void handleError(Throwable throwable) {
        if (throwable instanceof InvalidLoginOrPassException) {
            mEditTextEmail.setError("Invalid login or password");
        } else {
            failSnackbar.handleError(throwable);
        }

        loader.setVisibility(View.GONE);
        buttonSign.setEnabled(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        loginViewModel = new LoginViewModel(this);
        failSnackbar = new FailSnackbar(view);
        buttonSign.setOnTouchListener(new ImageButtonEffect(view1 -> sigin(), ImageButtonEffect.LIGHT_GRAY_COLOR));

        return view;
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @Override
    public void onChange(boolean isOpen) {
        emptyMarginBlock.setVisibility(isOpen ? View.GONE : View.VISIBLE);
    }
}
