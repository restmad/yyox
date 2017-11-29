package com.kf5.sdk.im.utils;

import android.text.TextUtils;

import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.utils.SafeJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * author:chosen
 * date:2017/3/29 15:31
 * email:812219713@qq.com
 */

public class MessageUtils {


    private MessageUtils() {

    }


    public static String dealAIMessage(String message) {

        try {

            StringBuilder stringBuilder = new StringBuilder();
            JSONObject jsonObject = SafeJson.parseObj(message);

            String content = SafeJson.safeGet(jsonObject, Field.CONTENT);
            if (content.contains("{{") && content.contains("}}")) {
                content = content.replaceAll("\\{\\{", "<a href=\"" + Field.GET_AGENT + "\">");
                content = content.replaceAll("\\}\\}", "</a>");
            }
            stringBuilder.append(content);

            String type = SafeJson.safeGet(jsonObject, Field.TYPE);
            if (TextUtils.equals(Field.QUESTION, type)) {
                dealQuestions(jsonObject, stringBuilder);
            } else if (TextUtils.equals(Field.DOCUMENT, type)) {
                dealDocuments(jsonObject, stringBuilder);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return message;
        }
    }

    /**
     * 解析机器人消息中的文档内容
     *
     * @param jsonObject
     * @param stringBuilder
     * @throws JSONException
     */
    private static void dealDocuments(JSONObject jsonObject, StringBuilder stringBuilder) throws JSONException {

        JSONArray jsonArray = SafeJson.safeArray(jsonObject, Field.DOCUMENTS);
        if (jsonArray != null) {
            int size = jsonArray.length();
            if (size > 0) {
                stringBuilder.append("<br/>");
            }
            for (int i = 0; i < size; i++) {
                stringBuilder.append("<a href=\"").append(SafeJson.safeGet(jsonArray.getJSONObject(i), Field.POST_ID)).append("\">").append(SafeJson.safeGet(jsonArray.getJSONObject(i), Field.TITLE_TAG)).append("</a>");
                if (i != size - 1) {
                    stringBuilder.append("<br/>");
                }
            }
        }
    }

    /**
     * 解析机器人消息的分词内容
     *
     * @param jsonObject
     * @param stringBuilder
     * @throws JSONException
     */
    private static void dealQuestions(JSONObject jsonObject, StringBuilder stringBuilder) throws JSONException {

        JSONArray jsonArray = SafeJson.safeArray(jsonObject, Field.QUESTIONS);
        if (jsonArray != null) {
            int size = jsonArray.length();
            if (size > 0) {
                stringBuilder.append("<br/>");
            }
            for (int i = 0; i < size; i++) {
                stringBuilder.append("<a href=\"").append(SafeJson.safeGet(jsonArray.getJSONObject(i), Field.ID)).append("\">").append(SafeJson.safeGet(jsonArray.getJSONObject(i), Field.TITLE_TAG)).append("</a>");
                if (i != size - 1) {
                    stringBuilder.append("<br/>");
                }
            }
        }
    }

}
