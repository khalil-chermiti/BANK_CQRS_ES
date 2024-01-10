package com.techbank.cqrs.core.domain;

import com.techbank.cqrs.core.event.BaseEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AggregateRoot {
    private String id;
    private int version = -1;

    private final List<BaseEvent> changes = new ArrayList<>();
    private Logger logger = Logger.getLogger(AggregateRoot.class.getName());


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<BaseEvent> getCommittedChanges() {
        return this.changes;
    }

    public void markChangesAsCommitted() {
        this.changes.clear();
    }

    protected void applyChange(BaseEvent event, boolean isNewEvent) {
        try {
            var method = getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (NoSuchMethodException nme) {
            logger.log(Level.WARNING, "method apply does not exist in class " + event.getClass().getName());
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.log(Level.SEVERE, "Invocation or Illegal Access exception when invoking apply method from class " + event.getClass().getName());
        } finally {
            if (isNewEvent) changes.add(event);
        }
    }

    public void raiseEvent(BaseEvent event) {
        applyChange(event, true);
    }

    public void replayEvents(Iterable<BaseEvent> events) {
        events.forEach(event -> applyChange(event, false));
    }
}

