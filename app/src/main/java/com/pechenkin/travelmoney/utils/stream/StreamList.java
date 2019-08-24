package com.pechenkin.travelmoney.utils.stream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class StreamList<E> implements List<E> {
    private final List<E> list;


    public StreamList(List<E> list) {
        this.list = list;
    }


    public StreamList<E> ForEach(ForEach<E> forEach) {
        for (E e : list) {
            forEach.execute(e);
        }
        return this;
    }

    public StreamList<E> Filter(Filter<E> filter) {
        List<E> result = new ArrayList<>();

        for (E e : list) {
            if (filter.filter(e)){
                result.add(e);
            }
        }
        return new StreamList<>(result);
    }

    public interface ForEach<E> {
        void execute(E e);
    }
    public interface Filter<E> {
        boolean filter(E e);
    }


    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return list.contains(o);
    }

    @NonNull
    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @NonNull
    @Override
    @SuppressWarnings("all")
    public <T> T[] toArray(@NonNull T[] ts) {
        return list.toArray(ts);
    }

    @Override
    public boolean add(E e) {
        return list.add(e);
    }

    @Override
    public boolean remove(@Nullable Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return list.containsAll(collection);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends E> collection) {
        return list.addAll(collection);
    }

    @Override
    public boolean addAll(int i, @NonNull Collection<? extends E> collection) {
        return list.addAll(i, collection);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return list.removeAll(collection);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return list.retainAll(collection);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public E get(int i) {
        return list.get(i);
    }

    @Override
    public E set(int i, E e) {
        return list.set(i, e);
    }

    @Override
    public void add(int i, E e) {
        list.add(i, e);
    }

    @Override
    public E remove(int i) {
        return list.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @NonNull
    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<E> listIterator(int i) {
        return list.listIterator(i);
    }

    @NonNull
    @Override
    public List<E> subList(int i, int i1) {
        return list.subList(i, i1);
    }
}
