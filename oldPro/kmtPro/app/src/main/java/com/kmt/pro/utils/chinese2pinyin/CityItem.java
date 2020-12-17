package com.kmt.pro.utils.chinese2pinyin;


public class CityItem implements ContactItemInterface {
    private String nickName;
    private String fullName;
    private String code;
    private String letter;

    public CityItem(String nickName, String fullName, String code) {
        super();
        this.nickName = nickName;
        this.code = code;
        this.setFullName(fullName);
        this.setLetter(fullName);
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        String sortString = letter.substring(0, 1).toUpperCase();
        if (sortString.matches("[A-Z]")) {
            this.letter = sortString.toUpperCase();
        } else {
            this.letter = "#";
        }
    }

    @Override
    public String getItemForIndex() {
        return fullName;
    }

    @Override
    public String getDisplayInfo() {
        return nickName;
    }

    public String getCode() {
        return code;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
