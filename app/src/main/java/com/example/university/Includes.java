package com.example.university;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

public class Includes {

    boolean valid = true;

    public boolean checkField(EditText textField) {
        if(textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        }
        else {
            valid = true;
        }

        return valid;
    }

    public Includes navigateTo(Context context, Class<?> destination) {
        Intent intent = new Intent(context, destination);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
        return null;
    }
}
