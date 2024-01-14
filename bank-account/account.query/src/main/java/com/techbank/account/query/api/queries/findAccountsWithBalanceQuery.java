package com.techbank.account.query.api.queries;

import com.techbank.account.query.api.dto.EqualityTypeDto;
import com.techbank.cqrs.core.query.BaseQuery;

public class findAccountsWithBalanceQuery extends BaseQuery {
    private EqualityTypeDto equalityType;
    private double balance;
}
