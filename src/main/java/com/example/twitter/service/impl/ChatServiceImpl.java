package com.example.twitter.service.impl;

import com.example.twitter.model.ChatMessage;
import com.example.twitter.repository.*;
import com.example.twitter.repository.projection.chat.ChatMessageProjection;
import com.example.twitter.repository.projection.chat.ChatParticipantProjection;
import com.example.twitter.repository.projection.chat.ChatProjection;
import com.example.twitter.repository.projection.user.UserProjection;
import com.example.twitter.service.AuthenticationService;
import com.example.twitter.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final ChatRepository chatRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserServiceImpl userService;

    @Override
    public Set<ChatProjection> getUserChats() {
        Long userId = authenticationService.getAuthenticatedUserId();
        Set<ChatParticipantProjection> chatParticipants = chatParticipantRepository.getChatParticipants(userId);
        return chatParticipants.stream()
                .filter(participant -> !participant.getLeftChat() || !userService.isUserBlockedByMyProfile(participant.getUser().getId()))
                .map(ChatParticipantProjection::getChat)
                .collect(Collectors.toSet());
    }

    @Override
    public ChatProjection createChat(Long userId) {
        return null;
    }

    @Override
    public List<ChatMessageProjection> getChatMessages(Long chatId) {
        return null;
    }

    @Override
    public Integer readChatMessages(Long chatId) {
        return null;
    }

    @Override
    public Map<String, Object> addMessage(ChatMessage chatMessage, Long chatId) {
        return null;
    }

    @Override
    public Map<String, Object> addMessageWithTweet(String text, Long tweetId, List<Long> usersIds) {
        return null;
    }

    @Override
    public UserProjection getParticipant(Long participantId, Long chatId) {
        return null;
    }

    @Override
    public String leaveFromConversation(Long participantId, Long chatId) {
        return null;
    }
}
