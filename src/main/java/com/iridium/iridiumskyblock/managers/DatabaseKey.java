package com.iridium.iridiumskyblock.managers;

public interface DatabaseKey<Key, Value> {
    Key getKey(Value input);
}