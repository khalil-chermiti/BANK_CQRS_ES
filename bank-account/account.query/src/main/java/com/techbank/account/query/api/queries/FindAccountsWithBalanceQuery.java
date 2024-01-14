package com.techbank.account.query.api.queries;

import com.techbank.account.query.api.dto.EqualityTypeDto;
import com.techbank.cqrs.core.query.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAccountsWithBalanceQuery extends BaseQuery {
    private EqualityTypeDto equalityType;
    private double balance;
}
