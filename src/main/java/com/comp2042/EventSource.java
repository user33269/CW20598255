package com.comp2042;

/**
 * Represents the origin of an event within the game. Events may be triggered by user or the game thread itself.
 */
public enum EventSource {
    /** event triggered by user interaction with computer */
    USER,
    /** event triggered by game thread or loop itself.*/
    THREAD
}
