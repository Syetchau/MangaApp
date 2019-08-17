package com.example.comicreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.comicreader.Adapter.MyChapterAdapter;
import com.example.comicreader.Common.Common;
import com.example.comicreader.Model.Comic;

public class ChapterActivity extends AppCompatActivity {

    RecyclerView recycler_chapter;
    TextView txt_chapter_name;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        recycler_chapter = (RecyclerView)findViewById(R.id.recycle_chapter);
        recycler_chapter.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recycler_chapter.setLayoutManager(linearLayoutManager);
        recycler_chapter.addItemDecoration(new DividerItemDecoration(this,
                linearLayoutManager.getOrientation()));

        txt_chapter_name = (TextView)findViewById(R.id.txt_chapter_name);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(Common.comicSelected.Name);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fetchChapter(Common.comicSelected);
    }

    private void fetchChapter(Comic comicSelected) {
        Common.chapterList = comicSelected.Chapters;
        if (Common.chapterList != null) {
            recycler_chapter.setAdapter(new MyChapterAdapter(this, comicSelected.Chapters));
            txt_chapter_name.setText(new StringBuilder("CHAPTERS (").append(comicSelected.Chapters.size())
                    .append(")"));
        }
    }
}
