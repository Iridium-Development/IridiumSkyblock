package com.iridium.iridiumskyblock.managers;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.NotImplementedException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Provides various cooldown features.
 *
 * @param <T> The type that can have this cooldown
 */
public class CooldownProvider<T> {

    private final Map<T, Duration> cooldownTimes = new HashMap<>();
    private final String name; // Required for database support
    private final Duration duration;
    private final boolean persistent; // Required for database support
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    /**
     * The default constructor.
     *
     * @param name          The name of this CooldownProvider in the database
     * @param duration      The duration which cooldowns from this provider have.
     * @param persistent    True if the cooldowns of this provider should be saved to the database
     */
    private CooldownProvider(String name, Duration duration, boolean persistent) {
        this.name = null;
        this.duration = duration;
        this.persistent = persistent;
    }

    /**
     * Returns if the specified entity has a valid cooldown.
     *
     * @param t The entity which should be checked
     * @return  True if the entity has a valid cooldown
     */
    public boolean isOnCooldown(T t) {
        if (cooldownTimes.containsKey(t) && cooldownTimes.get(t).toMillis() > System.currentTimeMillis()) {
            return true;
        }
        
        cooldownTimes.remove(t);
        return false;
    }

    /**
     * Returns the remaining duration of the cooldown for the provided entity.
     * ZERO if there is no cooldown.
     *
     * @param t The entity which should be checked
     * @return  The duration of the cooldown, can be ZERO
     */
    public Duration getRemainingTime(T t) {
        if (!isOnCooldown(t)) return Duration.ZERO;

        return cooldownTimes.get(t).minusMillis(System.currentTimeMillis());
    }

    /**
     * Reset the cooldown of the specified entity with the duration of this CooldownProvider.
     * {@link CooldownProvider#isOnCooldown(Object)} will return true after this.
     *
     * @param t The entity which should be checked
     */
    public void applyCooldown(T t) {
        Duration newDuration = duration.plusMillis(System.currentTimeMillis());
        cooldownTimes.put(t, newDuration);

        startTimer(t, newDuration);
    }
	
    private void startTimer(T t, Duration duration) {
        executor.schedule(() -> {
            Duration newDuration = getRemainingTime(t);
            if (!newDuration.isZero() && !newDuration.isNegative()) {
                startTimer(t, newDuration);
            }
        }, duration.getSeconds() + 1, TimeUnit.SECONDS);
    }

    /**
     * Creates a new CooldownProvider and returns it.
     * It will have the specified cooldown duration.
     *
     * @param duration The duration of the new cooldown
     * @param <T>      The type that can have this cooldown
     * @return         The new CooldownProvider
     */
    public static <T> CooldownProvider<T> newInstance(Duration duration) {
        return new CooldownProvider<>(null, duration, false);
    }

    /**
     * Creates a new CooldownProvider and returns it.
     * It will have the specified cooldown duration.
     *
     * @param name     The name of this CooldownProvider in the database
     * @param duration The duration of the new cooldown
     * @param <T>      The type that can have this cooldown
     * @return         The new CooldownProvider
     */
    public static <T> CooldownProvider<T> newPersistentInstance(String name, Duration duration) {
        throw new NotImplementedException();
        // return new CooldownManager<>(name, duration, true);
    }

}
