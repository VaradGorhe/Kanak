package com.example.eyickanak.util;

import android.util.Patterns;
import android.widget.EditText;

public class Validator {
    public static boolean validateEditText(EditText editText, String errMessage) {
        if (editText.getText().toString().equals("")) {
            editText.setError(errMessage);
            return false;
        }
        return true;
    }

    public static boolean validateEmail(EditText editText, String errMessage){
        if(Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString().trim()).matches() && !editText.getText().toString().trim().equals(""))
            return true;
        else
            editText.setError(errMessage);
        return false;
    }
}
