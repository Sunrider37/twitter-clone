package com.example.twitter.repository.projection.chat;

public interface ChatParticipantProjection {
    Long getId();
    boolean getLeftChat();
    ChatProjection getChat();
    ChatUserProjection getUser();

    interface ChatUserProjection {
        Long getId();
    }
}
