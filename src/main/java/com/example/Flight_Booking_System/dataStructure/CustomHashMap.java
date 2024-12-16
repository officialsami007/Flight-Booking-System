package com.example.Flight_Booking_System.dataStructure;

import java.util.function.Function;

public class CustomHashMap<K, V> {
    private static final int SIZE = 16;
    private Entry<K, V>[] table;

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    public CustomHashMap() {
        table = new Entry[SIZE];
    }

    private int hash(K key) {
        return Math.abs(key.hashCode() % SIZE);
    }

    public void put(K key, V value) {
        int index = hash(key);
        Entry<K, V> newEntry = new Entry<>(key, value);
        if (table[index] == null) {
            table[index] = newEntry;
        } else {
            Entry<K, V> current = table[index];
            while (current.next != null && !current.key.equals(key)) {
                current = current.next;
            }
            if (current.key.equals(key)) {
                current.value = value;
            } else {
                current.next = newEntry;
            }
        }
    }

    public V get(K key) {
        int index = hash(key);
        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        V value = get(key);
        if (value == null) {
            value = mappingFunction.apply(key);
            put(key, value);
        }
        return value;
    }

}