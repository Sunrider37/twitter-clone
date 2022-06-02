package com.example.twitter.service;

import com.example.twitter.model.ChatMessage;
import com.example.twitter.repository.projection.chat.ChatMessageProjection;
import com.example.twitter.repository.projection.chat.ChatProjection;
import com.example.twitter.repository.projection.user.UserProjection;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ChatService {


    Set<ChatProjection> getUserChats();

    ChatProjection createChat(Long userId);

    List<ChatMessageProjection> getChatMessages(Long chatId);

    Integer readChatMessages(Long chatId);

    Map<String, Object> addMessage(ChatMessage chatMessage, Long chatId);

    Map<String, Object> addMessageWithTweet(String text, Long tweetId, List<Long> usersIds);

    UserProjection getParticipant(Long participantId, Long chatId);

    String leaveFromConversation(Long participantId, Long chatId);
}
