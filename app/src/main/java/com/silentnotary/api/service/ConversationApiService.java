package com.silentnotary.api.service;

import java.util.List;

import com.silentnotary.api.retrofit.ConversationAPI;
import com.silentnotary.api.retrofit.FileAPI;
import com.silentnotary.util.TimeUtil;
import io.reactivex.Observable;

/**
 * Created by albert on 10/11/17.
 */

public class ConversationApiService {

    public Observable<List<ConversationAPI.ChatBotConversationData>>
        getConversations(Long beginDate, Long endDate){

        FileAPI.DatesRequest datesRequest = new FileAPI.DatesRequest(TimeUtil.timestampToJsonDate(beginDate),
                TimeUtil.timestampToJsonDate(endDate));

        return ApiBuilder.buildWithRequiredAuth()
                .create(ConversationAPI.class)
                .getChatBotConversations(datesRequest);
    }


}
