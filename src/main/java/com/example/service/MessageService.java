package com.example.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.BadRequestException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    AccountRepository accountRepository;

    public Message addMessage(Message message) {
        String message_text = message.getMessage_text();
        if (message_text.length() == 0 || message_text.length() > 255) {
            throw new BadRequestException();
        }

        Optional<Account> tempAccount = accountRepository.findById(message.getPosted_by());
        if (!tempAccount.isPresent()) {
            throw new BadRequestException();
        }

        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessage(int message_id) {
        Optional<Message> tempMessage = messageRepository.findById(message_id);

        Message m = null;

        if (tempMessage.isPresent()) {
            m = tempMessage.get();
        }

        return m;
    }

    public ResponseEntity<Object> deleteMessage(int id) {
        Optional<Message> tempMessage = messageRepository.findById(id);
        if (tempMessage.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        messageRepository.delete(tempMessage.get());
        return ResponseEntity.ok().body("1");
    }

    public ResponseEntity<Object> patch( int message_id, Map<String, Object> messagePatch) {
        if (messagePatch.containsKey("message_text")) {
            String newMessageText = (String) messagePatch.get("message_text");
            if (newMessageText.isBlank() || newMessageText.length() > 255) {
                throw new BadRequestException();
            }
        } else {
            throw new BadRequestException();
        }

        Optional<Message> message = messageRepository.findById(message_id);
      
        if(message.isPresent()) {			
            messagePatch.forEach( (key, value) -> {
                Field field = ReflectionUtils.findField(Message.class, key);
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, message.get(), value);
            });
        } else {
            throw new BadRequestException();
        }
        messageRepository.save(message.get());	
        return ResponseEntity.ok().body("1");			
    }

    public List<Message> getAllMessagesAccoundId(int account_id){
        return messageRepository.getAllMessagesAccountId(account_id);
    }
}
