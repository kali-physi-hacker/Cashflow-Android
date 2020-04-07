package com.example.cashflow;

public class SavingsAccountModel {
    private String name, uuid;

    public SavingsAccountModel(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }
}
