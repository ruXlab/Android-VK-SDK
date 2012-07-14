package com.perm.kate.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Message implements Serializable {
	public long date;
	public long uid;
	public long mid;
	public String title;
	public String body;
	public boolean read_state;
	public boolean is_out;
	public List<Attachment> attachments = null;
	public List<Message> fwdMessages = null;
	public long chat_id = 0;
    public List<Long> chat_active = emptyList;
    public int users_count = 0;
    public long admin_id = 0;



    private String attachmentsJSON = "";
	private String geoJSON = "";
	private String fwdMessagesJSON = "";

	@SuppressWarnings("rawtypes")
	private static final List emptyList = new ArrayList(0);

	
	public static int UNREAD = 1; // сообщение не прочитано
	public static int OUTBOX = 2; // исходящее сообщение
	public static int REPLIED = 4; // на сообщение был создан ответ
	public static int IMPORTANT = 8; // помеченное сообщение
	public static int CHAT = 16; // сообщение отправлено через диалог
	public static int FRIENDS = 32; // сообщение отправлено другом
	public static int SPAM = 64; // сообщение помечено как "Спам"
	public static int DELETED = 128; // сообщение удалено (в корзине)
	public static int FIXED = 256; // сообщение проверено пользователем на спам
	public static int MEDIA = 512; // сообщение содержит медиаконтент
	public static int BESEDA = 8192; // беседа

	public static Message parse(JSONObject o) {
		final Message msg = new Message();
		msg.setUserId(o.optLong("uid"));
		msg.setId(o.optLong("mid"));
		msg.setChatId(o.optLong("chat_id"));
		msg.setBody(o.optString("body"));
		msg.setTitle(o.optString("title"));
        msg.setDate(o.optLong("date") * 1000L);
        if (msg.isChat()) {
            msg.setChatAdminId(o.optLong("admin_id"));
            msg.setChatUsersCount(o.optInt("users_count"));
            String uidsStr[] = o.optString("chat_active").split(",");
            List<Long> uids = new ArrayList<Long>(uidsStr.length);
            for(String strVal: uidsStr) uids.add(Long.parseLong(strVal));
            msg.setChatLastUsers(uids);
        }
		msg.attachmentsJSON = o.optString("attachments");
		msg.fwdMessagesJSON = o.optString("fwd_messages");
		msg.geoJSON = o.optString("geo");
		
		return msg;
	}
	
	public static Message parse(JSONObject o, boolean from_history,
			long history_uid, boolean from_chat, long me)
			throws NumberFormatException, JSONException {
		Message m = new Message();
		if (from_chat) {
			long from_id = o.getLong("from_id");
			m.uid = from_id;
			m.is_out = (from_id == me);
		} else if (from_history) {
			m.uid = history_uid;
			Long from_id = o.getLong("from_id");
			m.is_out = !(from_id == history_uid);
		} else {
			m.uid = o.getLong("uid");
			m.is_out = o.optInt("out") == 1;
		}
		m.mid = o.getLong("mid");
		m.date = o.getLong("date") * 1000;
		if (!from_history && !from_chat)
			m.title = Api.unescape(o.getString("title"));
		m.body = Api.unescape(o.getString("body"));
		m.read_state = o.getInt("read_state") != 0;
		m.chat_id = o.optLong("chat_id", 0);
        if (m.isChat()) {
            m.setChatAdminId(o.optLong("admin_id"));
            m.setChatUsersCount(o.optInt("users_count"));
            String uidsStr[] = o.optString("chat_active").split(",");
            List<Long> uids = new ArrayList<Long>(uidsStr.length);
            for(String strVal: uidsStr) uids.add(Long.parseLong(strVal));
            m.setChatLastUsers(uids);
        }
		m.attachmentsJSON = o.optString("attachments");
		m.geoJSON = o.optString("geo");
		m.fwdMessagesJSON = o.optString("fwd_messages");
		return m;
	}


	public static Message parse(JSONArray a) throws JSONException {
		Message m = new Message();
		m.mid = a.getLong(1);
		m.uid = a.getLong(3);
		m.date = a.getLong(4) * 1000;
		m.title = Api.unescape(a.getString(5));
		m.body = Api.unescape(a.getString(6));
		int flag = a.getInt(2);
		m.read_state = ((flag & UNREAD) != 0) ? false : true;
		m.is_out = (flag & OUTBOX) != 0;
		if ((flag & BESEDA) != 0) {
			m.chat_id = a.getLong(3) & 63;// cut 6 last digits
			JSONObject o = a.getJSONObject(7);
			m.uid = o.getLong("from");
		}
		// m.attachment = a.getJSONArray(7); TODO
		return m;
	}

	/**
	 * Get timestamp in microseconds
	 */
	public long getDate() {
		return date;
	}

	/**
	 * Set date in microseconds
	 */
	public void setDate(long date) {
		this.date = date;
	}

	/**
	 * Get interlocutor's userId or member in chat 
	 */
	public long getUserId() {
		return uid;
	}

	public void setUserId(long uid) {
		this.uid = uid;
	}

	public long getId() {
		return mid;
	}

	public void setId(long mid) {
		this.mid = mid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isRead() {
		return read_state;
	}

	public void setRead(boolean read_state) {
		this.read_state = read_state;
	}

	public boolean isOut() {
		return is_out;
	}

	public void setOut(boolean is_out) {
		this.is_out = is_out;
	}

	/**
	 * Return attachments list (may be empty)
	 */
	@SuppressWarnings("unchecked")
	public List<Attachment> getAttachments() {
		if (attachments == null) {
			try {
				attachments = Attachment.parseAttachments(attachmentsJSON, 0,
						0, geoJSON);
			} catch (JSONException e) {
				attachments = emptyList;
			}
		}
		return attachments;
	}

	/**
	 * Get json-serialized representation of forwarded messages.
	 * May be empty string
	 */
	public String getAttachmentsJSON() {
		return attachmentsJSON;
	}

	public void setAttachments(String attachmentsJSONString) {
		this.attachmentsJSON = attachmentsJSONString;
	}

	public Long getChatId() {
		return chat_id;
	}

	public void setChatId(Long chat_id) {
		this.chat_id = chat_id;
	}

	/**
	 * Check if this message is a group message
	 */
	public boolean isChat() {
		return chat_id != 0;
	}

	/**
	 * Set string-serialized forwarded messages
	 */
	public void setFwdMessages(String fwdMessagesJson) {
		fwdMessagesJSON = fwdMessagesJson;
	}
	
	/**
	 * Return array of fwdMessages (if exists).
	 * Lazy parsing
	 */
	@SuppressWarnings("unchecked")
	public List<Message> getFwdMessages() {
		if (fwdMessages == null) {
			try {
				final JSONArray jsonArray = new JSONArray(fwdMessagesJSON);
				final int cnt = jsonArray.length();
				if (cnt == 0) return emptyList;

				fwdMessages = new ArrayList<Message>(cnt);
				for (int i = 0; i < cnt; i++) {
					final Message m = Message.parse(jsonArray.getJSONObject(i)); 
					fwdMessages.add(m);
				}
			} catch (JSONException e) {
				fwdMessages = emptyList;
			}
		}
		return fwdMessages;
	}
	
	/**
	 * Get json-serialized representation of forwarded messages
	 */
	public String getFwdMessagesJSON() {
		return fwdMessagesJSON;
	}

    public List<Long> getChatLastUsers() {
        return chat_active;
    }

    public void setChatLastUsers(List<Long> chat_active) {
        this.chat_active = chat_active;
    }

    public int getChatUsersCount() {
        return users_count;
    }

    public void setChatUsersCount(int users_count) {
        this.users_count = users_count;
    }

    public long getChatAdminId() {
        return admin_id;
    }

    public void setChatAdminId(long admin_id) {
        this.admin_id = admin_id;
    }
}
