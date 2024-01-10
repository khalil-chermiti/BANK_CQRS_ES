package com.techbank.cqrs.core.event;

import com.techbank.cqrs.core.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseEvent extends Message {
    private int version;
}
