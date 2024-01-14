package com.techbank.account.query;

import com.techbank.account.query.api.queries.QueryHandler;
import com.techbank.cqrs.core.infrastructure.QueryDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QueryApplication {

    @Autowired
    private QueryDispatcher queryDispatcher;

    public static void main(String[] args) {
        SpringApplication.run(QueryApplication.class, args);
    }

}
