package com.pelmers.simpletodo;

import android.content.DialogInterface;

/**
 * Click listener for a button that takes as input the current text field's value.
 */
public interface TextInputClickListener {
    void onClick(DialogInterface dialog, String text);
}
