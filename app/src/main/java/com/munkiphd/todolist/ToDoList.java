package com.munkiphd.todolist;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ToDoList extends Activity {

    static final private int ADD_NEW_TODO = Menu.FIRST;
    static final private int REMOVE_TODO = Menu.FIRST + 1;

    private boolean addingNew = false;
    private ArrayList<String> todoItems;
    private ListView myListView;
    private EditText myEditText;
    private ArrayAdapter<String> arrayAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        // controls
        myListView = (ListView) findViewById(R.id.myListView);
        myEditText = (EditText) findViewById(R.id.myEditText);

        // array with values to add to the listview
        todoItems = new ArrayList<String>();
        int resourceID = R.layout.todo_list_item_view;
        arrayAdapter = new ArrayAdapter<String>(this, resourceID, todoItems);

        myListView.setAdapter(arrayAdapter);


        final Button addButton = (Button) findViewById(R.id.btnAddItem);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoItems.add(0, myEditText.getText().toString());
                arrayAdapter.notifyDataSetChanged();
                myEditText.setText("");
                cancelAdd();
            }
        });


        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        myListView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                String itemRemoved = "";
                                for (int position : reverseSortedPositions) {
                                    itemRemoved = arrayAdapter.getItem(position);
                                    arrayAdapter.remove(itemRemoved);
                                }
                                arrayAdapter.notifyDataSetChanged();
                                Toast.makeText(listView.getContext(), "Removed Item " + itemRemoved,  Toast.LENGTH_SHORT).show();
                            }
                        });
        myListView.setOnTouchListener(touchListener);

        registerForContextMenu(myListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Selected todo item");
        menu.add(0, REMOVE_TODO, Menu.NONE, R.string.remove);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        int idx = myListView.getSelectedItemPosition();
        String removeTitle = getString(addingNew ? R.string.cancel : R.string.remove);

        MenuItem removeItem = menu.findItem(REMOVE_TODO);
        removeItem.setTitle(removeTitle);
        removeItem.setVisible(addingNew || idx > -1);

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //create and add new menu items
        MenuItem addItem = menu.add(0, ADD_NEW_TODO, Menu.NONE, R.string.add_new);
        MenuItem removeItem = menu.add(0, REMOVE_TODO, Menu.NONE, R.string.remove);

        //Assign icons
        addItem.setIcon(R.drawable.addicon);
        removeItem.setIcon(R.drawable.removeicon);

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.to_do_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        super.onOptionsItemSelected(item);

        int index = myListView.getSelectedItemPosition();

        switch (item.getItemId()){
            case (REMOVE_TODO):
                if(addingNew){
                    cancelAdd();
                } else {
                    removeItem(index);
                }
                return true;

            case(ADD_NEW_TODO): {
                addNewItem();
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item){
        super.onContextItemSelected(item);

        switch(item.getItemId()){
            case (REMOVE_TODO):{
                AdapterView.AdapterContextMenuInfo menuInfo;
                menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

                int index = menuInfo.position;
                removeItem(index);
                return true;
            }
        }
        return false;
    }

    private void cancelAdd(){
        addingNew = false;
        //myEditText.setVisibility(View.GONE);
    }

    private void addNewItem(){
        addingNew = true;
        //myEditText.setVisibility(View.VISIBLE);
        myEditText.requestFocus();
    }

    private void removeItem(int _index){
        todoItems.remove(_index);
        arrayAdapter.notifyDataSetChanged();
    }


}
