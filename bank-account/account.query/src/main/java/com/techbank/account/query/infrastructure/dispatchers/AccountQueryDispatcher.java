package com.techbank.account.query.infrastructure.dispatchers;

import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.infrastructure.QueryDispatcher;
import com.techbank.cqrs.core.query.BaseQuery;
import com.techbank.cqrs.core.query.QueryHandlerMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountQueryDispatcher implements QueryDispatcher {
    private final Map<Class<? extends BaseQuery>, List<QueryHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(type, c -> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public <T extends BaseEntity> List<T> send(BaseQuery query) {
        var handlers = routes.get(query.getClass());

        if (handlers == null || handlers.isEmpty())
            throw new RuntimeException("Can't find handler for " + query.getClass().getName() + " query");

        if (handlers.size() > 1)
            throw new RuntimeException("can not send query to more than one handler");

        return handlers.get(0).handle(query);
    }
}
