package com.example.comicreader;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comicreader.Adapter.MyComicAdapter;
import com.example.comicreader.Adapter.MySliderAdapter;
import com.example.comicreader.Common.Common;
import com.example.comicreader.Interface.IBannerLoadDone;
import com.example.comicreader.Interface.IComicLoadDone;
import com.example.comicreader.Model.Comic;
import com.example.comicreader.Service.PicassoLoadingService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity implements IBannerLoadDone, IComicLoadDone {

    Slider slider;
    SwipeRefreshLayout swipeRefreshLayout;
    DatabaseReference banner,comic;
    AlertDialog alertDialog;
    RecyclerView recycler_comic;
    TextView txt_comic;
    ImageView  btn_filter_search;

    //listener
    IBannerLoadDone bannerListener;
    IComicLoadDone  comicListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init database
        banner = FirebaseDatabase.getInstance().getReference("Banners");
        comic = FirebaseDatabase.getInstance().getReference("Comic");

        //Init listener
        bannerListener = this;
        comicListener = this;

        slider = (Slider)findViewById(R.id.slider);
        Slider.init(new PicassoLoadingService());

        btn_filter_search = (ImageView)findViewById(R.id.show_search_filter);
        btn_filter_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FilterSearchActivity.class));
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBanner();
                loadComic();
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
                loadComic();
            }
        });

        recycler_comic = (RecyclerView)findViewById(R.id.recycle_comic);
        recycler_comic.setHasFixedSize(true);
        recycler_comic.setLayoutManager(new GridLayoutManager(this,2));

        txt_comic = (TextView)findViewById(R.id.txt_comic);
    }

    private void loadComic() {

        //show Dialog
        alertDialog = new SpotsDialog.Builder().setContext(this)
                .setCancelable(false)
                .setMessage("Please wait...")
                .build();

        if (!swipeRefreshLayout.isRefreshing())
            alertDialog.show();
        else
            swipeRefreshLayout.setRefreshing(false);

        comic.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Comic> comic_load = new ArrayList<>();
                for (DataSnapshot comicSnapShot:dataSnapshot.getChildren())
                {
                    Comic comic = comicSnapShot.getValue(Comic.class);
                    comic_load.add(comic);
                }

                comicListener.onComicLoadDoneListener(comic_load);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBanner() {

        banner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> bannerList = new ArrayList<>();
                for (DataSnapshot bannerSnapShot:dataSnapshot.getChildren())
                {
                    String image = bannerSnapShot.getValue(String.class);
                    bannerList.add(image);
                }

                //call listener
                bannerListener.onBannerLoadDoneListener(bannerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBannerLoadDoneListener(List<String> banners) {
        slider.setAdapter(new MySliderAdapter(banners));
    }

    @Override
    public void onComicLoadDoneListener(List<Comic> comicList) {
        Common.comicList = comicList;

        recycler_comic.setAdapter(new MyComicAdapter(getBaseContext(),comicList));

        txt_comic.setText(new StringBuilder("NEW COMIC  (")
        .append(comicList.size())
        .append(")"));

        if (!swipeRefreshLayout.isRefreshing())
            alertDialog.dismiss();
        else
            swipeRefreshLayout.setRefreshing(false);
    }
}
