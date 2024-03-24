package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {
    @Autowired
    AccountService accountService;

    @Autowired
    MessageService messageService;

    @PostMapping("/register")
	public Account addAccount(@RequestBody Account account) {
        account.setAccount_id(0);
		return accountService.addAccount(account);
    }

    @PostMapping("/login")
    public Account login(@RequestBody Account account) {
        return accountService.login(account);
    }

    @PostMapping("/messages")
    public Message addMessage(@RequestBody Message message) {
        return messageService.addMessage(message);
    }

    @GetMapping("/messages")
    public List<Message> allMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{message_id}")
    public Message getMessage(@PathVariable int message_id) {
        return messageService.getMessage(message_id);
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Object> deleteMessage(@PathVariable int message_id) {
        return messageService.deleteMessage(message_id);
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Object> updateMessage(@PathVariable int message_id, @RequestBody Map<String, Object> messagePatch) {
        return messageService.patch(message_id, messagePatch);          
    }

    @GetMapping("/accounts/{account_id}/messages")
    public List<Message> getAllMessagesAccountId(@PathVariable int account_id) {
        return messageService.getAllMessagesAccoundId(account_id);
    }
}
