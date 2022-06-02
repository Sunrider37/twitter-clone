package com.example.twitter.repository;

import com.example.twitter.model.Notification;
import com.example.twitter.repository.projection.NotificationInfoProjection;
import com.example.twitter.repository.projection.notification.NotificationProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query("select n as notification from User u left join u.notifications n " +
            "where u.id = :userId and n.notificationType != 'TWEET' order by n.date desc")
    List<NotificationProjection> getNotificationsByUserId(Long userId);

    @Query("select n.id as id, n.date as date, n.notificationType as notificationType, " +
            "n.user as user, n.tweet as tweet from User u left join u.notifications n " +
            "where u.id = :userId and n.id = :notificationId")
    Optional<NotificationInfoProjection> getUserNotificationById(Long userId, Long notificationId);
}
