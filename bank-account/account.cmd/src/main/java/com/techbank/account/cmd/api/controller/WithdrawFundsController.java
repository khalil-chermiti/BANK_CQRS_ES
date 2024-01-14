package com.techbank.account.cmd.api.controller;

import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.cmd.api.commands.WithdrawFundsCommand;
import com.techbank.account.cmd.api.dto.OpenAccountResponse;
import com.techbank.cqrs.core.command.CommandDispatcher;
import com.techbank.cqrs.core.exception.AggregateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/depositFunds")
public class WithdrawFundsController {
    private Logger logger = LoggerFactory.getLogger(OpenAccountController.class);

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<OpenAccountResponse> withdrawFunds(@RequestBody WithdrawFundsCommand command) {
        logger.info("Received request to withdraw funds: {}", command);

        try {
            commandDispatcher.send(command);
            return ResponseEntity.ok(new OpenAccountResponse(command.getId(), "withdrawing funds success"));
        } catch (IllegalStateException | AggregateNotFoundException e) {
            logger.error("Error withdrawing funds , bad request : {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error opening account: {}", e.getMessage());
            return new ResponseEntity<>(new OpenAccountResponse(null, "Error withdrawing funds"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
