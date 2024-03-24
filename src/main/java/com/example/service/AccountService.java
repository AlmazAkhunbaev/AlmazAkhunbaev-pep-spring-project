package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.BadRequestException;
import com.example.exception.ConflictException;
import com.example.exception.UnauthorizedException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    public Account addAccount(Account account) {
        String username = account.getUsername();
        if (username.length() == 0) {
            throw new BadRequestException();
        }
        String password = account.getPassword();
        if (password.length() < 4) {
            throw new BadRequestException();
        }

        Account account_exist = accountRepository.findAccountByUsername(username);
        if (account_exist != null) {
            throw new ConflictException();
        }
        return accountRepository.save(account);
    }

    public Account login(Account account) {
        Account account_verified =  accountRepository.findAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
        if (account_verified == null) {
            throw new UnauthorizedException();
        }
        return account_verified;
    }
}
