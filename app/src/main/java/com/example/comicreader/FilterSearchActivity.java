package com.example.comicreader;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comicreader.Adapter.MyComicAdapter;
import com.example.comicreader.Common.Common;
import com.example.comicreader.Model.Comic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterSearchActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView recycler_filter_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);

        recycler_filter_search = (RecyclerView)findViewById(R.id.recycler_filter_search);
        recycler_filter_search.setHasFixedSize(true);
        recycler_filter_search.setLayoutManager(new GridLayoutManager(this, 2));

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_nav);
        bottomNavigationView.inflateMenu(R.menu.main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.action_filter:
                        showFilterDialog();
                        break;
                    case R.id.action_search:
                        showSearchDialog();
                        break;

                        default:
                            break;
                }
                return true;
            }
        });
    }

    private void showFilterDialog(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterSearchActivity.this);
        alertDialog.setTitle("Select Category");

        final LayoutInflater layoutInflater = this.getLayoutInflater();
        View filter_layout = layoutInflater.inflate(R.layout.dialog_options,null);

        final AutoCompleteTextView txt_category = (AutoCompleteTextView)filter_layout.findViewById(R.id.txt_category);
        final ChipGroup chipGroup = (ChipGroup)filter_layout.findViewById(R.id.chip_group);
        alertDialog.setView(filter_layout);

        //create auto-complete
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this,android.R.layout.select_dialog_item, Common.categories);
        txt_category.setAdapter(adapter);
        txt_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //clear
                txt_category.setText("");

                //create tag
                Chip chip = (Chip)layoutInflater.inflate(R.layout.chip_item,null,false);
                chip.setText(((TextView)view).getText());
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         chipGroup.removeView(view);
                    }
                });
                chipGroup.addView(chip);
            }
        });

        alertDialog.setView(filter_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("FILTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                List<String> filter_key = new ArrayList<>();
                StringBuilder filter_query = new StringBuilder("");
                for (int j=0 ; j<chipGroup.getChildCount(); j++)
                {
                    Chip chip = (Chip)chipGroup.getChildAt(j);
                    filter_key.add(chip.getText().toString());
                }
                //sort out filter key
                Collections.sort(filter_key);
                //convert list to string
                for (String key:filter_key)
                {
                    filter_query.append(key).append(",");
                }
                //remove last ","
                filter_query.setLength(filter_query.length()-1);

                //filter by this query
                fetchFilterCategory(filter_query.toString());
            }
        });
        alertDialog.show();
    }

    private void showSearchDialog(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterSearchActivity.this);
        alertDialog.setTitle("Search");

        final LayoutInflater layoutInflater = this.getLayoutInflater();
        View search_layout = layoutInflater.inflate(R.layout.dialog_search,null);

        final EditText editText = (EditText)search_layout.findViewById(R.id.search);
        alertDialog.setView(search_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fetchSearchComic(editText.getText().toString());
            }
        });

        alertDialog.show();
    }

    private void fetchSearchComic(String query) {

        List<Comic> comic_search = new ArrayList<>();
        for (Comic comic:Common.comicList)
        {
            if (comic.Name.contains(query))
                comic_search.add(comic);
        }
        if (comic_search.size() > 0)
            recycler_filter_search.setAdapter(new MyComicAdapter(getBaseContext(), comic_search));
        else
            Toast.makeText(this, R.string.no_result, Toast.LENGTH_SHORT).show();
    }

    private void fetchFilterCategory(String query) {

        List<Comic> comic_filtered = new ArrayList<>();
        for (Comic comic:Common.comicList) {
            if (comic.Category != null) {
                if (comic.Category.contains(query))
                    comic_filtered.add(comic);
            }
        }
        if (comic_filtered.size() > 0)
            recycler_filter_search.setAdapter(new MyComicAdapter(getBaseContext(), comic_filtered));
        else
            Toast.makeText(this, R.string.no_result, Toast.LENGTH_SHORT).show();
    }
}
