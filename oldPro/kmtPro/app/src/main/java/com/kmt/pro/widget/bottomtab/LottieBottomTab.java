package com.kmt.pro.widget.bottomtab;

/**
 * Create by JFZ
 * date: 2020-05-27 14:06
 **/
class LottieBottomTab {

    private String folder;
    private boolean isSelect;
    private String enter, exit;

    public LottieBottomTab(String folder) {
        this.folder = folder;
        this.enter = "navi_enter.json";
        this.exit = "kmt_nav_exit.json";
    }

    public String getFolder() {
        return folder;
    }

    public String getEnter() {
        return enter;
    }

    public String getExit() {
        return exit;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
