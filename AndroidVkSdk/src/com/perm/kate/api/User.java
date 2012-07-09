package com.perm.kate.api;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//Fields are optional. Should be null if not populated
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	public long uid;
	public String first_name;
	public String last_name;
	public String nickname;
	public Integer sex = null;
	public Boolean online = null;
	public String birthdate; // bdate
	public String photo;// the same as photo_rec
	public String photo_big;
	public String photo_medium;
	public Integer city = null;
	public Integer country = null;
	public Integer timezone = null;
	public String lists;
	public String domain;
	public Integer rate = null;
	public Integer university = null; // if education
	public String university_name; // if education
	public Integer faculty = null; // if education
	public String faculty_name; // if education
	public Integer graduation = null; // if education
	public Boolean has_mobile = null;
	public String home_phone;
	public String mobile_phone;
	public String status;
	public Integer relation;
	public String friends_list_ids = null;
	public long last_seen;
	public int albums_count;
	public int videos_count;
	public int audios_count;
	public int notes_count;
	public int friends_count;
	public int user_photos_count;
	public int user_videos_count;
	public boolean is_friend = false;

	// public int followers_count;
	// public int subscriptions_count;
	// public int online_friends_count;

	public static User parse(JSONObject o) throws JSONException {
		User u = new User();
		u.uid = o.getLong("uid");
		if (o.has("first_name"))
			u.first_name = Api.unescape(o.getString("first_name"));
		if (o.has("last_name"))
			u.last_name = Api.unescape(o.getString("last_name"));
		if (o.has("nickname"))
			u.nickname = Api.unescape(o.optString("nickname"));
		if (o.has("domain"))
			u.domain = o.optString("domain");
		if (o.has("online"))
			u.online = o.optInt("online") == 1;
		if (o.has("sex"))
			u.sex = Integer.parseInt(o.optString("sex"));
		if (o.has("bdate"))
			u.birthdate = o.optString("bdate");
		try {
			u.city = Integer.parseInt(o.optString("city"));
		} catch (NumberFormatException ex) {
		}
		try {
			u.country = Integer.parseInt(o.optString("country"));
		} catch (NumberFormatException ex) {
		}
		if (o.has("timezone"))
			u.timezone = o.optInt("timezone");
		if (o.has("photo"))
			u.photo = o.optString("photo");
        if (o.has("photo_medium_rec"))
            u.photo_medium = o.optString("photo_medium_rec");
        else if (o.has("photo_medium"))
			u.photo_medium = o.optString("photo_medium");
        if (o.has("photo_big_rec"))
            u.photo_big = o.optString("photo_big_rec");
        else if (o.has("photo_big"))
			u.photo_big = o.optString("photo_big");
        if (o.has("has_mobile"))
			u.has_mobile = o.optInt("has_mobile") == 1;
		if (o.has("home_phone"))
			u.home_phone = o.optString("home_phone");
		if (o.has("mobile_phone"))
			u.mobile_phone = o.optString("mobile_phone");
		if (o.has("rate"))
			u.rate = Integer.parseInt(o.optString("rate"));
		try {
			u.faculty = Integer.parseInt(o.optString("faculty"));
		} catch (NumberFormatException ex) {
		}
		if (o.has("faculty_name"))
			u.faculty_name = o.optString("faculty_name");
		try {
			u.university = Integer.parseInt(o.optString("university"));
		} catch (NumberFormatException ex) {
		}
		if (o.has("university_name"))
			u.university_name = o.optString("university_name");
		try {
			u.graduation = Integer.parseInt(o.optString("graduation"));
		} catch (NumberFormatException ex) {
		}
		if (o.has("activity"))
			u.status = Api.unescape(o.optString("activity"));
		if (o.has("relation"))
			u.relation = o.optInt("relation");
		if (o.has("lists")) {
			JSONArray array = o.optJSONArray("lists");
			if (array != null) {
				String ids = "";
				for (int i = 0; i < array.length() - 1; ++i)
					ids += array.getString(i) + ",";
				ids += array.getString(array.length() - 1);
				u.friends_list_ids = ids;
			}
		}
		if (o.has("last_seen")) {
			JSONObject object = o.optJSONObject("last_seen");
			if (object != null)
				u.last_seen = object.optLong("time");
		}
		if (o.has("counters")) {
			JSONObject object = o.optJSONObject("counters");
			if (object != null) {
				u.albums_count = object.optInt("albums");
				u.videos_count = object.optInt("videos");
				u.audios_count = object.optInt("audios");
				u.notes_count = object.optInt("notes");
				u.friends_count = object.optInt("friends");
				u.user_photos_count = object.optInt("user_photos");
				u.user_videos_count = object.optInt("user_videos");
				// u.online_friends_count = object.optInt("online_friends");
				// u.followers_count = object.optInt("followers");
				// u.subscriptions_count = object.optInt("subscriptions");
			}
		}
		return u;
	}

	public static User parseFromNews(JSONObject jprofile) throws JSONException {
		User m = new User();
		m.uid = Long.parseLong(jprofile.getString("uid"));
		m.first_name = Api.unescape(jprofile.getString("first_name"));
		m.last_name = Api.unescape(jprofile.getString("last_name"));
		m.photo = jprofile.getString("photo");
		try {
			m.sex = Integer.parseInt(jprofile.optString("sex"));
		} catch (NumberFormatException ex) {
			// если там мусор, то мы это пропускаем
			ex.printStackTrace();
		}
		return m;
	}

	public static ArrayList<User> parseUsers(JSONArray array)
			throws JSONException {
		ArrayList<User> users = new ArrayList<User>();
		// it may be null if no users returned
		// no users may be returned if we request users that are already removed
		if (array == null)
			return users;
		int category_count = array.length();
		for (int i = 0; i < category_count; ++i) {
			if (array.get(i) == null
					|| ((array.get(i) instanceof JSONObject) == false))
				continue;
			JSONObject o = (JSONObject) array.get(i);
			User u = User.parse(o);
			users.add(u);
		}
		return users;
	}
	
	/**
	 * Return concatenated string: name with surname
	 */
	public String getFullName() {
		return first_name + " " + last_name;
	}
	
	/**
	 * Returns concatenation like "#{userId}: {firstName} {lastName}"
	 */
	@Override
	public String toString() {
		return "#" + uid + ": " + first_name + " " + last_name;
	}

    /**
     * Indicate sex
     */
    public boolean isMale() {
        return sex == 1;
    }

    /**
     * Indicate sex
     */
    public boolean isFemale() {
        return sex == 2;
    }

    /**
     * Indicate unknown user's sex
     */
    public boolean isUnknownSex() {
        return sex == 0;
    }

}
