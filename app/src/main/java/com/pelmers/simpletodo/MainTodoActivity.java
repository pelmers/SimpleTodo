package com.pelmers.simpletodo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainTodoActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = "MainTodoActivity";
    private static final String FILENAME = "TodoLists";
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    // map of index in the nav bar to the list of items in the list
    private List<List<TodoItem>> todoLists;
    // the currently selected to do list fragment
    private TodoListFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load up the todoLists from storage
        setContentView(R.layout.activity_main_todo);
        mTitle = getTitle();
        initializeDrawer();
    }

    /**
     * Initialize the navigation drawer.
     */
    private void initializeDrawer() {
        if (mNavigationDrawerFragment != null)
            return;
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    /**
     * Load up lists from the device memory if they haven't already been loaded
     */
    private void loadTodoLists() {
        if (todoLists != null)
            return;
        try {
            FileInputStream inputStream = openFileInput(FILENAME);
            ObjectInputStream objectStream = new ObjectInputStream(inputStream);
            //noinspection unchecked
            todoLists = (List<List<TodoItem>>) objectStream.readObject();
            objectStream.close(); inputStream.close();
        } catch (IOException e) {
            // assume that if we can't load that is because we haven't saved yet
            todoLists = new ArrayList<>();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error lists", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Save current state of lists to device memory.
     */
    private void saveTodoLists() {
        try {
            FileOutputStream outputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
            objectStream.writeObject(todoLists);
            objectStream.close(); outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving lists", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        currentFragment = TodoListFragment.newInstance(position);
        fragmentManager.beginTransaction()
                .replace(R.id.container, currentFragment)
                .commit();
    }

    @Override
    public void onListDeleted(int position) {
        todoLists.remove(position);
    }

    public void onListAttached(int number) {
        if (mNavigationDrawerFragment != null)
            mTitle = mNavigationDrawerFragment.getListTitle(number);
        setTitle(mTitle);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // if the drawer is open, let it do the action bar
        // otherwise the activity will handle it
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main_todo, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            currentFragment.openItemAddDialog();
            return true;
        } else if (item.getItemId() == R.id.action_toggle_lock) {
            currentFragment.toggleLock();
            if (currentFragment.isLocked()) {
                Toast.makeText(this, "List locked.", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_action_not_secure);
                item.setTitle(getString(R.string.action_unlock));
            }
            else {
                Toast.makeText(this, "List unlocked.", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_action_secure);
                item.setTitle(getString(R.string.action_lock));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveTodoLists();
    }

    public List<TodoItem> getTodoList(int number) {
        loadTodoLists();
        while (todoLists.size() <= number)
            todoLists.add(new ArrayList<TodoItem>());
        return todoLists.get(number);
    }
}
