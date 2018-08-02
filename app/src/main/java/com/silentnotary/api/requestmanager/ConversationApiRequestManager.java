package com.silentnotary.api.requestmanager;



import java.util.List;

import com.silentnotary.api.retrofit.ConversationAPI;
import com.silentnotary.api.service.ConversationApiService;

/**
 * Created by albert on 10/11/17.
 */

public class ConversationApiRequestManager {

    ConversationApiService conversationApiService = new ConversationApiService();

    public io.reactivex.Observable<List<ConversationAPI.ChatBotConversationData>> getFacebookConversations(){
        return conversationApiService.getConversations(0L, 1598059696000L);
    }
}
