package com.example.cloudmusic.other;


import java.util.List;

public interface ILrcBulider {
    List<LrcRow> getLrcRows(String rawLrc);
}
