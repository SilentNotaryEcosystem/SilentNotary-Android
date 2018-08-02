package com.silentnotary.ui.conversation.list;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.silentnotary.R;
import com.silentnotary.api.retrofit.ConversationAPI;
import com.silentnotary.databinding.RecyclerviewConversationItemBinding;
import com.silentnotary.widget.ImageButtonEffect;
import com.silentnotary.widget.list.AbstractRecyclerView;

/**
 * Created by albert on 10/11/17.
 */

public class ConversationRecyclerView extends AbstractRecyclerView<ConversationAPI.ChatBotConversationData>{

    private OnItemClick onItemClick;

    public interface OnItemClick{
        void onClick(ConversationAPI.ChatBotConversationData item);
    }

    public ConversationRecyclerView(Context context) {
        super(context);
    }

    public ConversationRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    @Override
    public int getLayoutId() {
        return R.layout.recyclerview_conversation_item;
    }

    @Override
    public void bind(ViewDataBinding binding, ConversationAPI.ChatBotConversationData item) {
        if(binding instanceof RecyclerviewConversationItemBinding){
            ((RecyclerviewConversationItemBinding)binding)
                    .setItem(item);
            binding.getRoot().setOnTouchListener(new ImageButtonEffect(view -> {
                if(onItemClick != null)
                    onItemClick.onClick(item);
            }));
        }
    }
}