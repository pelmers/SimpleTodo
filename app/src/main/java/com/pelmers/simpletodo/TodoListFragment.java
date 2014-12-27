package com.pelmers.simpletodo;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * A fragment containing a the todolist
 */
public class TodoListFragment extends Fragment {
    // section number of a fragment
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final String TAG = "TodoListFragment";

    private ArrayAdapter<TodoItem> todoListAdapter;

    // the items on this list fragment
    private List<TodoItem> todoItems;

    // is the list locked?
    private boolean isLocked;

    // use to detect gestures
    private GestureDetectorCompat detector;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TodoListFragment newInstance(int sectionNumber) {
        TodoListFragment fragment = new TodoListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public TodoListFragment() {
    }

    /**
     * Create the views for the UI. This happens after onAttach
     * @return view for this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_todo, container, false);
        ListView todoListView = (ListView) rootView.findViewById(R.id.currentTodoList);
        todoListAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.todo_item,
                android.R.id.text1,
                todoItems);
        todoListView.setAdapter(todoListAdapter);
        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // don't do anything if the list is locked.
                if (isLocked())
                    return;
                TodoItem item = todoItems.get(position);
                item.setCompleted(!item.isCompleted());
                TextView textView = (TextView) view;
                if (item.isCompleted()) {
                    markComplete(textView);
                } else {
                    markIncomplete(textView);
                }
            }
        });
        todoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (isLocked())
                    return false;
                openItemModifyDialog(position);
                return true;
            }
        });
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });
        return rootView;
    }

    /**
     * Indicate completion of a text view
     * @param textView to mark completed
     */
    private void markComplete(TextView textView) {
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
    private void markIncomplete(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        textView.setTextColor(Color.BLACK);
        textView.setText(textView.getText().toString());
    }

    private void addTodoItem(String itemName) {
        todoItems.add(new TodoItem(itemName));
        todoListAdapter.notifyDataSetChanged();
    }

    /**
     * The first stage in the fragment creation process.
     * In this method we let the activity know we're created and ask for the correct list.
     * @param activity the activity we attach to
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        detector = new GestureDetectorCompat(activity, new GestureListener(this));
        MainTodoActivity mainActivity = (MainTodoActivity) activity;
        int number = getArguments().getInt(ARG_SECTION_NUMBER);
        setTodoItems(mainActivity.getTodoList(number));
        mainActivity.onListAttached(number);
    }

    public void setTodoItems(List<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }

    /**
     * Open the dialog for adding an item, and add it to the to do list.
     */
    public void openItemAddDialog() {
        // pop up a text input dialog and get input from an EditText view
        TextInputAlertDialog.showInputAlertDialog(getActivity(), "Add item", "Add", new TextInputClickListener() {
            @Override
            public void onClick(DialogInterface dialog, String text) {
                addTodoItem(text);
            }
        });
    }

    public void openItemModifyDialog(final int position) {
        final boolean wasCompleted = todoItems.get(position).isCompleted();
        TextInputAlertDialog.showInputAlertDialog(getActivity(), "Modify item",
                todoItems.get(position).getText(), "Cancel", "Modify",
                new TextInputClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, String text) {
                    }
                }, new TextInputClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, String text) {
                        todoItems.set(position, new TodoItem(text));
                        todoItems.get(position).setCompleted(wasCompleted);
                        todoListAdapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * Toggle the locked status of this list.
     */
    public void toggleLock() {
        isLocked = !isLocked;
    }

    /**
     * @return whether fragment is locked (cannot be edited)
     */
    public boolean isLocked() {
        return isLocked;
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private TodoListFragment that;
        GestureListener(TodoListFragment that) {
            this.that = that;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            that.openItemAddDialog();
            return true;
        }
    }
}
