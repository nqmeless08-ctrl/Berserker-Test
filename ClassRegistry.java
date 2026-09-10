package com.yourserver.classsystem.classes;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ClassRegistry {

    // LinkedHashMap keeps registration order, which is the order the GUI displays them in.
    private final Map<String, PlayerClass> classes = new LinkedHashMap<>();

    public void register(PlayerClass playerClass) {
        classes.put(playerClass.getId().toLowerCase(), playerClass);
    }

    public Optional<PlayerClass> get(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(classes.get(id.toLowerCase()));
    }

    public Collection<PlayerClass> getAll() {
        return classes.values();
    }
}
