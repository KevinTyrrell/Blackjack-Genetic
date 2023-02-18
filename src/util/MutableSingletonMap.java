/*
 *     Genetic algorithm which teaches agents how to play Blackjack.
 *     Copyright (C) 2019-2023  Kevin Tyrrell
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package util;


import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;


/**
 * Re-implementation of Collections.singletonMap
 *
 * This collection supports mutable key/value pairs
 */
public class MutableSingletonMap<K, V> implements Map<K, V>
{
    private K key;
    private V value;

    public MutableSingletonMap() { this(null, null); }
    public MutableSingletonMap(final K key, final V value)
    {
        this.key = key;
        this.value = value;
    }

    private final Entry entry = new Entry(); // Facade

    /* Facade entry -- defers operations to outer class */
    class Entry implements Map.Entry<K, V>
    {
        @Override public K getKey() { return key; }
        @Override public V getValue() { return value; }
        @Override public V setValue(V value)
        {
            final V old = MutableSingletonMap.this.value;
            MutableSingletonMap.this.value = value;
            return old;
        }
    }

    @Override public int size() { return key == null ? 0 : 1; }
    @Override public boolean isEmpty() { return key == null; }
    @Override public boolean containsKey(Object key) { return this.key == key; }
    @Override public boolean containsValue(Object value) { return this.value == value; }
    @Override public V get(Object key) { return this.key == key ? value : null; }
    @Override public V put(K key, V value)
    {
        final V old = this.value;
        this.key = key;
        this.value = value;
        return old;
    }
    @Override public V remove(Object key)
    {
        if (this.key == key)
        {
            final V old = value;
            this.key = null;
            this.value = null;
            return old;
        }
        return null;
    }

    @Override public void putAll(Map<? extends K, ? extends V> m) { throw new UnsupportedOperationException(); }
    @Override public void clear() { key = null; value = null; }
    @Override public Set<K> keySet() { return Collections.singleton(key); }
    @Override public Collection<V> values() { return Collections.singleton(value); }
    @Override public Set<Map.Entry<K, V>> entrySet() { return Collections.singleton(entry); }
}
