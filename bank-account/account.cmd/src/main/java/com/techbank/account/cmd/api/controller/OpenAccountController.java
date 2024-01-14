package com.techbank.account.cmd.api.controller;

import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.cmd.api.dto.OpenAccountResponse;
import com.techbank.cqrs.core.command.CommandDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController()
@RequestMapping("/api/v1/account/open")
public class OpenAccountController {
    private Logger logger = LoggerFactory.getLogger(OpenAccountController.class);

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<OpenAccountResponse> openAccount(@RequestBody OpenAccountCommand command) {
        logger.info("Received request to open a new account: {}", command);

        UUID id = UUID.randomUUID();
        command.setId(id.toString());

        try {
            commandDispatcher.send(command);
            return ResponseEntity.ok(new OpenAccountResponse(id.toString(), "Account created successfully"));
        } catch (IllegalStateException e) {
            logger.error("Error opening account: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error opening account: {}", e.getMessage());
            return new ResponseEntity<>(new OpenAccountResponse(null, "Error opening account"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
