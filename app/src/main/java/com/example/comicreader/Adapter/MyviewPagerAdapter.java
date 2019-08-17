package com.example.comicreader.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.comicreader.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyviewPagerAdapter extends PagerAdapter {

    Context context;
    List<String> imageLinks;
    LayoutInflater layoutInflater;

    public MyviewPagerAdapter(Context context, List<String> imageLinks) {
        this.context = context;
        this.imageLinks = imageLinks;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageLinks.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View image_layout = layoutInflater.inflate(R.layout.view_pager_item, container, false);

        PhotoView photoView = (PhotoView)image_layout.findViewById(R.id.page_image);
        Picasso.get()
                .load(imageLinks.get(position))
                .into(photoView);

        container.addView(image_layout);
        return image_layout;
    }
}
