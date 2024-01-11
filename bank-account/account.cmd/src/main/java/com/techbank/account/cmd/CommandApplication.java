package com.techbank.account.cmd;

import com.techbank.account.cmd.api.commands.*;
import com.techbank.cqrs.core.command.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CommandApplication {

    @Autowired
    private CommandDispatcher accountCommandDispatcher;

    @Autowired
    private CommandHandler accountCommandHandler;

    public static void main(String[] args) {
        SpringApplication.run(CommandApplication.class, args);
    }

    @PostConstruct
    public void registerCommandHandlers() {
        accountCommandDispatcher.registerHandler(OpenAccountCommand.class, accountCommandHandler::handle);
        accountCommandDispatcher.registerHandler(DepositFundsCommand.class, accountCommandHandler::handle);
        accountCommandDispatcher.registerHandler(WithdrawFundsCommand.class, accountCommandHandler::handle);
        accountCommandDispatcher.registerHandler(CloseAccountCommand.class, accountCommandHandler::handle);
    }
}
