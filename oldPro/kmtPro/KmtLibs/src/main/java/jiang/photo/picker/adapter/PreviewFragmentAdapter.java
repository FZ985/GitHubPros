package jiang.photo.picker.adapter;


import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import jiang.photo.picker.activity.SelectPreviewFragment;

/**
 * Create by JFZ
 * date: 2020-06-10 14:09
 **/
public class PreviewFragmentAdapter extends FragmentStatePagerAdapter {
    private List<SelectPreviewFragment> frags;

    public PreviewFragmentAdapter(FragmentManager fm, List<SelectPreviewFragment> frags) {
        super(fm);
        this.frags = frags;
    }

    @Override
    public SelectPreviewFragment getItem(int i) {
        return frags.get(i);
    }

    @Override
    public int getCount() {
        return frags == null ? 0 : frags.size();
    }
}
