package com.pelmers.simpletodo;

/**
 * Encapsulate info about each todo item
 */
public class TodoItem {
    // the text of the item
    private String text;
    private boolean completed;
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
