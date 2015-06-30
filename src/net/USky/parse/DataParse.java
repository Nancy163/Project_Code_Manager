package net.USky.parse;

import org.json.JSONException;
import org.json.JSONObject;

public class DataParse {
	public static String toJson(String other, String username,
			String userphone, String code, String type) {
		String result = "";
		try {
			JSONObject object2 = new JSONObject();
			object2.put("name", username);
			object2.put("sex", userphone);
			object2.put("code", other);
			JSONObject object = new JSONObject();
			object.put("handler", type);
			object.put("data", object2);
//			object.put("code", code);
			result = object.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
}
