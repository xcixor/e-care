package com.ecareuae.e_care.helpers;

import android.text.Editable;
import android.text.TextWatcher;

import com.ecareuae.e_care.utils.ValidationUtil;
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
