package com.example.comicreader.Interface;

import com.example.comicreader.Model.Comic;

import java.util.List;

public interface IComicLoadDone {
    void onComicLoadDoneListener(List<Comic> comicList);
}
