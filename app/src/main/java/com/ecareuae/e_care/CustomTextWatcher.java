package com.ecareuae.e_care;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

import java.lang.ref.WeakReference;

public class CustomTextWatcher implements TextWatcher {

    private WeakReference<TextInputLayout> mEditTextLayout;

    public CustomTextWatcher(TextInputLayout et) {
        mEditTextLayout = new WeakReference<>(et);
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
