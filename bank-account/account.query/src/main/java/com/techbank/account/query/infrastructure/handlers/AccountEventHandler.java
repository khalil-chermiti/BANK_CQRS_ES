package com.techbank.account.query.infrastructure.handlers;

import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.account.query.domain.AccountRepository;
import com.techbank.account.query.domain.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountEventHandler implements EventHandler {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        var bankAccount = BankAccount.builder()
            .id(event.getId())
            .accountHolder(event.getAccountHolder())
            .balance(event.getOpeningBalance())
            .accountType(event.getAccountType())
            .creationDate(event.getCreatedDate())
            .build();


        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsWithdrawnEvent event) {
        var bankAccount = accountRepository.findById(event.getId());
        if (bankAccount.isEmpty()) {
            throw new RuntimeException(
                "can not withdraw funds from account with id "
                    + event.getId() + " because it does not exist"
            );
        }

        bankAccount.get().setBalance(bankAccount.get().getBalance() - event.getAmount());
        accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(FundsDepositedEvent event) {
        var bankAccount = accountRepository.findById(event.getId());
        if (bankAccount.isEmpty()) {
            throw new RuntimeException(
                "can not deposit funds to account with id "
                    + event.getId() + " because it does not exist"
            );
        }

        bankAccount.get().setBalance(bankAccount.get().getBalance() + event.getDepositAmount());
        accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(AccountClosedEvent event) {
        var bankAccount = accountRepository.findById(event.getId());
        if (bankAccount.isEmpty()) {
            throw new RuntimeException(
                "can not close account with id "
                    + event.getId() + " because it does not exist"
            );
        }

        accountRepository.delete(bankAccount.get());
    }
}
