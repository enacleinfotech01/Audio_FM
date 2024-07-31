package com.my.audio_video_fm.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.my.audio_video_fm.model.MediaItem;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<MediaItem> selectedItem = new MutableLiveData<>();

    public void selectItem(MediaItem item) {
        selectedItem.setValue(item);
    }

    public LiveData<MediaItem> getSelectedItem() {
        return selectedItem;
    }
}
