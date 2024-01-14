package com.techbank.account.cmd.domain;

import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private boolean active;
    private double balance;

    public boolean isActive() {
        return active;
    }

    public AccountAggregate(OpenAccountCommand openAccountCommand) {
        raiseEvent(AccountOpenedEvent
            .builder()
            .id(openAccountCommand.getId())
            .version(1)
            .accountHolder(openAccountCommand.getAccountHolder())
            .accountType(openAccountCommand.getAccountType())
            .openingBalance(openAccountCommand.getOpeningBalance())
            .createdDate(new Date())
            .build());
    }

    public void apply(AccountOpenedEvent event) {
        setId(event.getId());
        this.active = true;
        this.balance = event.getOpeningBalance();
    }

    public void depositFunds(double amount) {
        if (!this.active)
            throw new IllegalStateException("Funds can not be deposited when account is not active");

        if (amount <= 0)
            throw new IllegalStateException("The deposit amount must be greater than 0");

        raiseEvent(FundsDepositedEvent
            .builder()
            .id(this.getId())
            .depositAmount(amount)
            .build()
        );
    }

    public void apply(FundsDepositedEvent event) {
        this.balance += event.getDepositAmount();
    }

    public void withdrawFunds(double amount) {
        if (!this.active)
            throw new IllegalStateException("Funds can not be withdrawn when account is not active");

        if (amount < this.balance)
            throw new IllegalStateException("the balance must be greater or equal to the withdrawn amount");

        raiseEvent(FundsWithdrawnEvent
            .builder()
            .id(this.getId())
            .amount(amount)
            .build());
    }

    public void apply(FundsWithdrawnEvent event) {
        this.balance -= event.getAmount();
    }

    public void closeAccount() {
        if (!this.active)
            throw new IllegalStateException("account is already closed");

        raiseEvent(AccountClosedEvent
            .builder()
            .id(getId())
            .build());
    }

    public void apply(AccountClosedEvent event) {
        this.active = false;
    }
}
