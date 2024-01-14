package com.techbank.account.query.api.controllers;

import com.techbank.account.query.api.dto.AccountLookupResponse;
import com.techbank.account.query.api.dto.EqualityTypeDto;
import com.techbank.account.query.api.queries.FindAccountByHolderQuery;
import com.techbank.account.query.api.queries.FindAccountByIdQuery;
import com.techbank.account.query.api.queries.FindAccountsWithBalanceQuery;
import com.techbank.account.query.api.queries.FindAllAccountsQuery;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.cqrs.core.infrastructure.QueryDispatcher;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bankAccountLookup")
public class AccountLookupController {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(AccountLookupController.class);

    @Autowired
    private QueryDispatcher queryDispatcher;


    @GetMapping("/")
    public ResponseEntity<AccountLookupResponse> getAccounts() {
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAllAccountsQuery());

            if (accounts.isEmpty())
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new AccountLookupResponse("No accounts were found"));

            return ResponseEntity.status(HttpStatus.OK).body(new AccountLookupResponse(accounts));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            String errorMessage = "Error while processing request to get all bank accounts";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AccountLookupResponse(errorMessage));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountLookupResponse> getAccountById(@PathVariable String id) {
        try {
            List<BankAccount> account = queryDispatcher.send(new FindAccountByIdQuery(id));

            if (account.isEmpty())
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new AccountLookupResponse("No accounts were found"));

            return ResponseEntity.status(HttpStatus.OK).body(new AccountLookupResponse(account));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            String errorMessage = "Error while processing request to get bank account by id: " + id;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AccountLookupResponse(errorMessage));
        }
    }


    @GetMapping("/holder/{holder}")
    public ResponseEntity<AccountLookupResponse> getAccountByHolder(@PathVariable String holder) {
        try {
            List<BankAccount> account = queryDispatcher.send(new FindAccountByHolderQuery(holder));

            if (account.isEmpty())
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new AccountLookupResponse("No accounts were found"));

            return ResponseEntity.status(HttpStatus.OK).body(new AccountLookupResponse(account));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            String errorMessage = "Error while processing request to get bank account by holder: " + holder;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AccountLookupResponse(errorMessage));
        }
    }


    @GetMapping("/balance/{equalityType}/{balance}")
    public ResponseEntity<AccountLookupResponse> getAccountsWithBalance(
        @PathVariable EqualityTypeDto equalityType,
        @PathVariable double balance
    ) {
        try {
            List<BankAccount> account = queryDispatcher.send(new FindAccountsWithBalanceQuery(equalityType, balance));

            if (account.isEmpty())
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new AccountLookupResponse("No accounts were found"));

            return ResponseEntity.status(HttpStatus.OK).body(new AccountLookupResponse(account));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            String errorMessage = "Error while processing request to get bank account with balance: " + balance;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AccountLookupResponse(errorMessage));
        }
    }
}
