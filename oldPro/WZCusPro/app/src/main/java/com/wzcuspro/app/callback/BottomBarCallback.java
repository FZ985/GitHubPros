package com.wzcuspro.app.callback;

public interface BottomBarCallback {
    void bottomClick(int index, int oldIndex);

    void bottomReleaseClick(int index, int oldIndex);
}
