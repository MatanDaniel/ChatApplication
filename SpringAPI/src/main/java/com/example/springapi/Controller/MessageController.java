package com.example.springapi.Controller;

import com.example.springapi.model.Message;
import com.example.springapi.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//This is a REST controller in your backend, just like our
// LoginController or RegisterController.
//It receives messages from the Android app via @PostMapping,
// then saves them into the H2 database using a MessageRepository.

@RestController
@RequestMapping("/api/messages") // all endpoints will now start with /api/messages
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    // POST /api/messages/send
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        message.setTimestamp(System.currentTimeMillis());
        Message saved = messageRepository.save(message);
        return ResponseEntity.ok(saved);
    }

    // GET /api/messages/history?user1=1&user2=2
    @GetMapping("/history")
    public ResponseEntity<List<Message>> getMessages(
            @RequestParam String user1,
            @RequestParam String user2) {

        List<Message> messages = messageRepository
                .findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(user1, user2, user1, user2);

        return ResponseEntity.ok(messages);
    }

    // GET /api/messages?senderId=1&receiverId=2
    @GetMapping("")
    public List<Message> getConversation(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {

        return messageRepository.findConversation(senderId, receiverId);
    }
}

