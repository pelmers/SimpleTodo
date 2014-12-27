package com.pelmers.simpletodo;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TodoListFragment newInstance(int sectionNumber, List<TodoItem> todoItems) {
        TodoListFragment fragment = new TodoListFragment();
        fragment.setTodoItems(todoItems);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public TodoListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_todo, container, false);
        ListView todoListView = (ListView) rootView.findViewById(R.id.currentTodoList);
        // if this is our first time here, initialize the list
        todoListAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                todoItems);
        todoListView.setAdapter(todoListAdapter);

        return rootView;
    }

    private void addTodoItem(String itemName) {
        todoItems.add(new TodoItem(itemName));
        todoListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainTodoActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
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

    /**
     * Toggle the locked status of this list.
     */
    public void toggleLock() {
    }
}
