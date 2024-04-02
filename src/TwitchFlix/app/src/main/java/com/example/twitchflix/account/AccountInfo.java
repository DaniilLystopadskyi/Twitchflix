package com.example.twitchflix.account;

import com.example.twitchflix.network.UserData;

public class AccountInfo {
    UserData data = null;

    private static final AccountInfo instance = new AccountInfo();

    private AccountInfo() {
    }

    public static AccountInfo getInstance() {
        return instance;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    public UserData getData(){
        return data;
    }
}
