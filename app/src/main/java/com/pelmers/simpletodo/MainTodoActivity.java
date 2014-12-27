package com.pelmers.simpletodo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainTodoActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = "MainTodoActivity";
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    // map of index in the nav bar to the list of items in the list
    private Map<Integer, List<TodoItem>> todoLists;
    // the currently selected to do list fragment
    private TodoListFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load up the todoLists from storage
        loadTodoLists();
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
     * Load up lists from the device memory
     */
    private void loadTodoLists() {
        todoLists = new HashMap<>();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (todoLists.get(position) == null)
            todoLists.put(position, new ArrayList<TodoItem>());
        List<TodoItem> selectedList = todoLists.get(position);
        currentFragment = TodoListFragment.newInstance(position, selectedList);
        fragmentManager.beginTransaction()
                .replace(R.id.container, currentFragment)
                .commit();
    }

    public void onSectionAttached(int number) {
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
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main_todo, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_add) {
            currentFragment.openItemAddDialog();
            return true;
        } else if (item.getItemId() == R.id.action_lock) {
            Toast.makeText(this, "List locked.", Toast.LENGTH_SHORT).show();
            currentFragment.toggleLock();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
