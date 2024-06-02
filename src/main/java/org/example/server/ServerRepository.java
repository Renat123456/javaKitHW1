package org.example.server;

public interface ServerRepository<T> {
    void save(T text);
    T load();
}
