package com.techbank.account.cmd.infrastructure;

import com.techbank.account.cmd.domain.EventStoreRepository;
import com.techbank.cqrs.core.event.BaseEvent;
import com.techbank.cqrs.core.event.EventModel;
import com.techbank.cqrs.core.exception.AggregateNotFoundException;
import com.techbank.cqrs.core.exception.ConcurrencyException;
import com.techbank.cqrs.core.infrastructure.EventStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountEventStore implements EventStore {
    private final AccountEventProducer accountEventProducer;

    private final EventStoreRepository eventStoreRepository;

    public AccountEventStore(AccountEventProducer accountEventProducer,
                             EventStoreRepository eventStoreRepository) {
        this.accountEventProducer = accountEventProducer;
        this.eventStoreRepository = eventStoreRepository;
    }

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        var eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);

        if (expectedVersion != -1 && eventStream.get(eventStream.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException("Concurrency exception");
        }

        var version = expectedVersion;
        for (var event : events) {
            version++;
            event.setVersion(version);
            EventModel persistedEvent = eventStoreRepository.save(
                EventModel.builder()
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(event.getClass().getName())
                    .version(version)
                    .eventType(event.getClass().getName())
                    .eventData(event)
                    .build()
            );

            if (!persistedEvent.getId().isEmpty()) {
                // TODO : produce events to kafka
                accountEventProducer.produce(event.getClass().getName(), event);
            }
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        var EventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);

        if (EventStream == null || EventStream.isEmpty()) {
            throw new AggregateNotFoundException("account with id " + aggregateId + " not found");
        }

        return EventStream.stream().map(EventModel::getEventData).toList();
    }
}
