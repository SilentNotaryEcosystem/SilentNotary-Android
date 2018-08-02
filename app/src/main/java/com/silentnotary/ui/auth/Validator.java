package com.silentnotary.ui.auth;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;

import com.silentnotary.R;

/**
 * Created by albert on 9/29/17.
 */

public class Validator {

    public static boolean validatePassword(EditText editText) {
        String password = editText.getText().toString();
        Context context = editText.getContext();
        boolean valid = true;
        if (password.isEmpty()) {
            valid = false;
            editText.setError(context.getString(R.string.empty_pass));
        } else {
            editText.setError(null);
        }
        return valid;
    }

    public static boolean validateEmail(EditText editText) {
        String email = editText.getText().toString();

        boolean valid = true;
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            editText.setError(editText.getContext().getString(R.string.email_not_valid));
        } else {
            editText.setError(null);
        }
        return valid;
    }

    public static boolean validatePasswordInputs(EditText passwordEditText1,
                                                 EditText passwordEditText2) {
        boolean valid = true;
        if (!passwordEditText1.getText().toString().equals(passwordEditText2.getText().toString())) {
            passwordEditText2.setError(passwordEditText1.getContext().getString(R.string.passwords_not_equal));
            valid = false;
        } else {
            passwordEditText2.setError(null);
        }
        return valid;
    }
}
