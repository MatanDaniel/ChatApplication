package com.example.springapi.repository;

import com.example.springapi.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//This allows retrieving chat history between two users regardless of who sent or received.
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(String senderId1, String receiverId1, String senderId2, String receiverId2);



    //This fetches both directions of the conversation.
    @Query("SELECT m FROM Message m WHERE " +
            "(m.senderId = :senderId AND m.receiverId = :receiverId) OR " +
            "(m.senderId = :receiverId AND m.receiverId = :senderId) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findConversation(@Param("senderId") Long senderId,
                                   @Param("receiverId") Long receiverId);

}

