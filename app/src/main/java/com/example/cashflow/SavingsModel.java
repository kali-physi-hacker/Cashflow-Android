package com.example.cashflow;

public class SavingsModel {
    private String date;
    private int amount;

    public SavingsModel(String mDate, int mAmount) {
        date = mDate;
        amount = mAmount;
    }

    public String getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }
}
