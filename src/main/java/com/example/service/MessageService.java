package com.example.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * This method adds a new message record to the database.
     * @param message object to add to the database.
     * @return added message object.
     */
    public Message addMessage(Message message) {
        // Check if message is not blank and it not greater than 255 characters.
        String message_text = message.getMessage_text();

        if (message_text.length() == 0 || message_text.length() > 255) {
            throw new BadRequestException();
        }

        // Check if account id exists in the database.
        Optional<Account> tempAccount = accountRepository.findById(message.getPosted_by());

        if (!tempAccount.isPresent()) {
            throw new BadRequestException();
        }

        return messageRepository.save(message);
    }

    /**
     * This method returns all messages in a list.
     * @return List of messages.
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * This method finds a message object in the database.
     * @param message_id used to find the message in the database.
     * @return message object.
     */
    public Message getMessage(int message_id) {
        Optional<Message> tempMessage = messageRepository.findById(message_id);

        Message m = null;

        if (tempMessage.isPresent()) {
            m = tempMessage.get();
        }

        return m;
    }

    /**
     * This method deletes a message record in the database.
     * @param id used to find and delete a message.
     * @return ResponseEntity.
     */
    public ResponseEntity<Object> deleteMessage(int id) {
        // Find the message to delete.
        Optional<Message> tempMessage = messageRepository.findById(id);

        if (tempMessage.isEmpty()) {
            return ResponseEntity.ok().build();
        }

        messageRepository.delete(tempMessage.get());
        return ResponseEntity.ok().body("1");
    }

    /**
     * This method partially updates a message record in the database.
     * @param message_id integer to find the message to update in the database.
     * @param Map object passed to the request body.
     * @return ResponseEntity.
     */
    @Transactional
    public ResponseEntity<Object> patch( int message_id, Map<String, Object> messagePatch) {
        String newMessageText;
        // Check if the parameters meet the business rules.
        if (messagePatch.containsKey("message_text")) {
            newMessageText = (String) messagePatch.get("message_text");
            if (newMessageText.isBlank() || newMessageText.length() > 255) {
                throw new BadRequestException();
            }
        } else {
            throw new BadRequestException();
        }

        Optional<Message> message = messageRepository.findById(message_id);

        if(!message.isPresent()) {
            throw new BadRequestException();
        }

        messageRepository.updateMessage(message_id, newMessageText);

        return ResponseEntity.ok().body("1");			
    }

    /**
     * This method gets all messages that belong to an account.
     * @param account_id integer to find the messages that belong to this account id.
     * @return List<Message> that contains all messages found.
     */
    public List<Message> getAllMessagesAccoundId(int account_id){
        return messageRepository.getAllMessagesAccountId(account_id);
    }
}
