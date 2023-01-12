package com.example.umfs;

public class Category {

    private String category;
    private boolean isTrendingNow;

    public Category(String category) {
        this.category = category;
    }

    public Category(String category, boolean isTrendingNow) {
        this.category = category;
        this.isTrendingNow = isTrendingNow;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isTrendingNow() {
        return isTrendingNow;
    }

    public void setTrendingNow(boolean trendingNow) {
        isTrendingNow = trendingNow;
    }

    @Override
    public String toString() {
        return this.category;
    }

}
