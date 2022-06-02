package com.example.twitter.repository;

import com.example.twitter.model.ChatParticipant;
import com.example.twitter.repository.projection.chat.ChatParticipantProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant,Long> {
    @Query("select cp.id as id, cp.leftChat as leftChat, cp.chat as chat, cp.user as user " +
            "from User u left join u.chats cp where u.id = :userId")
    Set<ChatParticipantProjection> getChatParticipants(Long userId);

}
