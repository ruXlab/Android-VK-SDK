package com.perm.kate.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Chat entity
 */
public class Chat {
    private String type;
    private long chatId;
    private String title;
    private long adminId;
    private List<Long> users = new ArrayList<Long>();

    public static Chat parse(JSONObject json) throws JSONException {
        Chat chat = new Chat();
        chat.setAdminId(json.optLong("admin_id"));
        chat.setChatId(json.optLong("chat_id"));
        chat.setTitle(json.optString("title"));
        chat.setAdminId(json.optLong("admin_id"));

        final JSONArray users = json.getJSONArray("users");
        final int usersListLength = users.length();
        List<Long> uids = new ArrayList<Long>();
        for(int i = 0; i < usersListLength; i++) uids.add(users.getLong(i));
        chat.setUsers(uids);

        return chat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }
}
