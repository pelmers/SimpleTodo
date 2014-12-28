package com.pelmers.simpletodo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Custom adapter based on ArrayAdapter
 */
public class TodoAdapter extends ArrayAdapter<TodoItem> {
    public TodoAdapter(Context activity, int layout_id, int text_id, List<TodoItem> todoItems) {
        super(activity, layout_id, text_id, todoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view =  (TextView) super.getView(position, convertView, parent);
        if (getItem(position).isCompleted())
            markComplete(view);
        else
            markIncomplete(view);
        return view;
    }

    /**
     * Modify the style of a text view to indicate completion
     * @param textView to mark completed
     */
    public void markComplete(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        SpannableString spannableString = new SpannableString(textView.getText());
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
        textView.setTextColor(Color.LTGRAY);
        textView.setText(spannableString);
    }

    /**
     * Mark a text view as incomplete (should undo what markComplete does)
     * @param textView to mark incomplete
     */
    public void markIncomplete(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        textView.setTextColor(Color.BLACK);
        textView.setText(textView.getText().toString());
    }
}
