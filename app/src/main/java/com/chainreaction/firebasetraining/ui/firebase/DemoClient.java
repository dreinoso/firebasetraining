package com.chainreaction.firebasetraining.ui.firebase;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class DemoClient {

    public String uid;
    public String name;
    public String lastName;
    public String age;
    public String date;

    public void setValue(String uid, String username, String email, String age, String date) {
        this.uid = uid;
        this.name = username;
        this.lastName = email;
        this.age = age;
        this.date = date;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("lastName", lastName);
        result.put("age", age);
        result.put("date", date);

        return result;
    }
}