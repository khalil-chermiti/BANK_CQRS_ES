package com.techbank.account.cmd.api.commands;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.handler.EventSourcingHandler;
import org.springframework.stereotype.Service;

@Service
public class AccountCommandHandler implements CommandHandler {
    private final EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    public AccountCommandHandler(EventSourcingHandler<AccountAggregate> eventSourcingHandler) {
        this.eventSourcingHandler = eventSourcingHandler;
    }

    @Override
    public void handle(OpenAccountCommand command) {
        var account = new AccountAggregate(command);
        eventSourcingHandler.save(account);
    }

    @Override
    public void handle(DepositFundsCommand command) {
        var account = eventSourcingHandler.getById(command.getId());
        account.depositFunds(command.getAmount());
        eventSourcingHandler.save(account);
    }

    @Override
    public void handle(WithdrawFundsCommand command) {
        var account = eventSourcingHandler.getById(command.getId());
        account.withdrawFunds(command.getAmount());
        eventSourcingHandler.save(account);
    }

    @Override
    public void handle(CloseAccountCommand command) {
        var account = eventSourcingHandler.getById(command.getId());
        account.closeAccount();
        eventSourcingHandler.save(account);
    }
}
