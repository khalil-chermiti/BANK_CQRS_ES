package com.techbank.account.cmd.infrastructure;

import com.techbank.cqrs.core.command.BaseCommand;
import com.techbank.cqrs.core.command.CommandDispatcher;
import com.techbank.cqrs.core.command.CommandHandlerMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {
    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(type, aClass -> new ArrayList<>());
        handlers.add(handler);
    }

    @Override
    public void send(BaseCommand command) {
        var handler = routes.get(command.getClass());
        if (handler == null || handler.isEmpty()) throw new RuntimeException("No command handler was registered");
        if (handler.size() > 1) throw new RuntimeException("we can only send commands to one handler");

        handler.get(0).handle(command);
    }
}
