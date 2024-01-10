package com.techbank.cqrs.core.command;

import com.techbank.cqrs.core.message.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class BaseCommand extends Message {
    public BaseCommand(String id) {
        super(id);
    }
}
