package com.pelmers.simpletodo;

import java.io.Serializable;

/**
 * Encapsulate info about each item
 */
public class TodoItem implements Serializable {
    // the text of the item
    private String text;
    private boolean completed;
    // default constructor for serialization purposes
    public TodoItem() {
        text = "";
        completed = false;
    }
    public TodoItem(String text) {
        this.text = text;
        completed = false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String toString() {
        return text;
    }
}
