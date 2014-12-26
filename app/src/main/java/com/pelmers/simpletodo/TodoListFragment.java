package com.pelmers.simpletodo;

import android.app.Activity;
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
        int currentSection = getArguments().getInt(ARG_SECTION_NUMBER);
        // if this is our first time here, initialize the list
        todoListView.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                todoItems));
        return rootView;
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
}
