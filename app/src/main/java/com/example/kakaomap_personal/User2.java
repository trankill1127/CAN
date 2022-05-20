package com.example.kakaomap_personal;

/**
 * Created by kch on 2018. 2. 17..
 */

public class User2 {

    String userTotal;
    String userName;


    public String getUserTotal() {
        return userTotal;
    }



    public void setUserTotal(String userTotal) {
        this.userTotal = userTotal;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User2(String userName, String userTotal ) {

        this.userTotal = userTotal;
        this.userName = userName;

    }
}


