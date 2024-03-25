package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    // Method to update a message.
    @Modifying
    @Query("update Message m set m.message_text = :message_text where m.message_id = :message_id")
    void updateMessage(@Param("message_id") int message_id, @Param("message_text") String message_text);

    // Method to find message that belong to a particular account.
    @Query("select m from Message m where m.posted_by = :posted_by")
    List<Message> getAllMessagesAccountId(@Param("posted_by") int posted_by);
}
