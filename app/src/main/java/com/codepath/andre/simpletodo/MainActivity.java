package com.codepath.andre.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView lvItems;
    EditText etEditText;
    private final int EDIT_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText)findViewById(R.id.etEditText);

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> perent, View view, int position, long id) {
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> perent, View view, int position, long id) {
                launchEditView(position);

                /*todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                writeItems();*/
            }
        });
    }

    public void launchEditView(int index) {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(this, EditItemActivity.class);
        String temp = todoItems.get(index);
        i.putExtra("data", temp);
        i.putExtra("index", index);
        // brings up the second activity
        startActivityForResult(i, EDIT_CODE);
    }

    public void populateArrayItems(){

        readItems();
        aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,todoItems);
    }

    private void readItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try{
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        }catch (IOException e){
            todoItems = new ArrayList<String>(); // this fixes an error in the video
                                                 // he already had a file in place but I did not
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(file, todoItems);
        }catch (IOException e){

        }
    }

    public void onAddItem(View view){
        aToDoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == EDIT_CODE) {
            int index = data.getExtras().getInt("index");

            if(index >= 0)
            {
                String newData = data.getExtras().getString("data");
                todoItems.set(index, newData);
                aToDoAdapter.notifyDataSetChanged();
                writeItems();
            }
        }
    }
}
