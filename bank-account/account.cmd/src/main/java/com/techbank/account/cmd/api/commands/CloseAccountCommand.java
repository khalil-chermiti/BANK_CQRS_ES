package com.techbank.account.cmd.api.commands;

import com.techbank.cqrs.core.command.BaseCommand;
import lombok.Data;

@Data
public class CloseAccountCommand extends BaseCommand {
    public CloseAccountCommand(String id) {
        super(id);
    }
}
