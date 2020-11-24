package com.app.api29;

import android.widget.RadioGroup;

import com.app.api29.base.BaseActivity;
import com.app.api29.ui.fragment.HomeFragment;
import com.app.api29.ui.fragment.MineFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.main_group)
    RadioGroup mainGroup;
    private HomeFragment homeFragment;
    private MineFragment mineFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mainGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.main_home) {
                changeTab(0);
            } else if (checkedId == R.id.main_mine) {
                changeTab(1);
            }
        });
    }

    @Override
    public void initData() {
        changeTab(0);
    }

    private void changeTab(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case 0:
                showFragment((homeFragment == null) ? homeFragment = new HomeFragment() : homeFragment, transaction);
                break;
            case 1:
                showFragment((mineFragment == null) ? mineFragment = new MineFragment() : mineFragment, transaction);
                break;
        }
    }

    private void showFragment(Fragment fragment, FragmentTransaction transaction) {
        if (!fragment.isAdded()) {
            transaction.add(R.id.main_content, fragment);
        }
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }

}
