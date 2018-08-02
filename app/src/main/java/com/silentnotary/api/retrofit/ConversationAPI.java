package com.silentnotary.api.retrofit;


import java.util.List;

import com.silentnotary.api.NetworkingConstantsApi;
import com.silentnotary.util.TimeUtil;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by albert on 10/11/17.
 */

public interface ConversationAPI {

    @POST(NetworkingConstantsApi.FACEBOOK_GET_CHATBOT_CONVERSATIONS)
    Observable<List<ChatBotConversationData>> getChatBotConversations(@Body FileAPI.DatesRequest datesRequest);

    class ChatBotConversationData {
        String Subject;

        String Created;

        String Fixed;

        String HashHex;

        String NonceHex;

        String ChatBotType;

        public ChatBotConversationData() {
        }

        public String getCreatedDateFormated() {
            try {
                return TimeUtil.formatTimestamp(
                        TimeUtil.parseJsonDate(getCreated())
                );
            } catch (Exception e) {
            }
            return "";
        }

        public String getSubject() {
            return Subject;
        }

        public void setSubject(String subject) {
            Subject = subject;
        }

        public String getCreated() {
            return Created;
        }

        public void setCreated(String created) {
            Created = created;
        }

        public String getFixed() {
            return Fixed;
        }

        public void setFixed(String fixed) {
            Fixed = fixed;
        }

        public String getHashHex() {
            return HashHex;
        }

        public void setHashHex(String hashHex) {
            HashHex = hashHex;
        }

        public String getNonceHex() {
            return NonceHex;
        }

        public void setNonceHex(String nonceHex) {
            NonceHex = nonceHex;
        }

        public String getChatBotType() {
            return ChatBotType;
        }

        public void setChatBotType(String chatBotType) {
            ChatBotType = chatBotType;
        }
    }
}
