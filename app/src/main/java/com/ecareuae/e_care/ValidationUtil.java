package com.ecareuae.e_care;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

import java.lang.ref.WeakReference;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

public class ValidationUtil implements TextWatcher {

    private WeakReference<TextInputLayout> mEditTextLayout;

    public ValidationUtil(TextInputLayout et) {
        mEditTextLayout = new WeakReference<>(et);
    }

    public static final  Pattern SPECIAL_CHARACTERS_REGEX;

    static {
        SPECIAL_CHARACTERS_REGEX = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");
    }

    public static boolean stringIsEmpty(String stringToCheck){
        if (isEmpty(stringToCheck) || stringToCheck.trim().isEmpty())
            return true;
        return false;
    }

    public static boolean hasSpecialCharacters(String stringToCheck){
        if (SPECIAL_CHARACTERS_REGEX.matcher(stringToCheck).find())
            return true;
        return false;
    }

    public static boolean isEmail(String email){
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean passwordLengthNotGreaterThanSixCharacters(String password){
        if (password.length() < 6)
            return true;
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String name = String.valueOf(editable);
        final TextInputLayout textInputLayout = mEditTextLayout.get();
        if(ValidationUtil.hasSpecialCharacters(name)){
            textInputLayout.setError("Invalid Input");
        }else {
            textInputLayout.setError(null);
        }
    }
}
