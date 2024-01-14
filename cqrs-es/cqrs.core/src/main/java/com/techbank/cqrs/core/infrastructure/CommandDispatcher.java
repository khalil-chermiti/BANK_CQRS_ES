package com.techbank.cqrs.core.infrastructure;

import com.techbank.cqrs.core.command.BaseCommand;
import com.techbank.cqrs.core.command.CommandHandlerMethod;

public interface CommandDispatcher {
    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler);

    void send(BaseCommand command);
}
