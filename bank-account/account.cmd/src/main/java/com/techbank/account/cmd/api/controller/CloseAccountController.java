package com.techbank.account.cmd.api.controller;

import com.techbank.account.cmd.api.commands.CloseAccountCommand;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import com.techbank.cqrs.core.exception.AggregateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account/close")
public class CloseAccountController {
    static final Logger logger = LoggerFactory.getLogger(DepositFundsController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @DeleteMapping("/{id}")
    ResponseEntity<BaseResponse> depositFunds(
        @PathVariable String id,
        @RequestBody CloseAccountCommand closeAccountCommand
    ) {
        try {
            closeAccountCommand.setId(id);
            closeAccountCommand.setId(id);
            commandDispatcher.send(closeAccountCommand);
            return ResponseEntity.ok(new BaseResponse("account closed successfully"));
        } catch (IllegalStateException | AggregateNotFoundException e) {
            logger.error("Error while closing account due to bad client request");
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("error while processing your request please try again");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
