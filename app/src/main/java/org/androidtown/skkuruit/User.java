package org.androidtown.skkuruit;

/**
 * Created by genie on 2017. 6. 10..
 */
public class User {
    private String fcmToken;
    private String nickname;

    public User(){}

    void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
    String getFcmToken() {
        return fcmToken;
    }

    void setNickname(String nickname) {
        this.nickname = nickname;
    }
    String getNickname() {
        return nickname;
    }
}
