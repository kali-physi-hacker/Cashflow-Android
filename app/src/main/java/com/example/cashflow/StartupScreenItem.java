package com.example.cashflow;

public class StartupScreenItem {
    String Title, Description;
    int ScreenImg;

    public StartupScreenItem(String title, String description, int ScreenImg) {
        Title = title;
        Description = description;
        this.ScreenImg = ScreenImg;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public int getImg() {
        return ScreenImg;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setImg(int ScreenImg) {
        this.ScreenImg = ScreenImg;
    }
}
