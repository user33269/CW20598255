package com.comp2042;

/**
 * This is an immutable class that allows input-handling system to distinguish whether action comes from USER or THREAD.
 * Helps game interpret actions correctly and apply different behaviours depending on the event source.
 */
public final class MoveEvent {

    private final EventSource eventSource;

    /**
     * Constructs a new MoveEvent with given event source
     * @param eventSource origin of Event
     */
    public MoveEvent( EventSource eventSource) {
        this.eventSource = eventSource;
    }

    /**
     * Returns source that triggered the event
     * @return the event source.
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}
