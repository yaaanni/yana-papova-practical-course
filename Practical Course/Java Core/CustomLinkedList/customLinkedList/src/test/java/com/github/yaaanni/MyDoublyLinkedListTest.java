package com.github.yaaanni;

import com.github.yaaanni.doublyLinkedList.MyDoublyLinkedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

public class MyDoublyLinkedListTest {
    private MyDoublyLinkedList<Integer> myDoublyLinkedList;

    @BeforeEach
    void setUp() {
        myDoublyLinkedList = new MyDoublyLinkedList<Integer>();
    }

    @Test
    void sizeTest() {
        myDoublyLinkedList.addFirst(2);
        myDoublyLinkedList.addLast(3);
        Assertions.assertEquals(2, myDoublyLinkedList.size());
    }

    @Test
    void addFirstTest() {
        myDoublyLinkedList.addFirst(4);
        myDoublyLinkedList.addFirst(5);
        Assertions.assertEquals(5, myDoublyLinkedList.getFirst());
    }

    @Test
    void addLastTest() {
        myDoublyLinkedList.addLast(5);
        myDoublyLinkedList.addLast(7);
        Assertions.assertEquals(7, myDoublyLinkedList.getLast());
    }

    @Test
    void addTest() {
        myDoublyLinkedList.add(0, 2);
        myDoublyLinkedList.add(0, 4);
        myDoublyLinkedList.add(2, 7);
        myDoublyLinkedList.add(1, 8);
        Assertions.assertEquals(4, myDoublyLinkedList.getFirst());
        Assertions.assertEquals(7, myDoublyLinkedList.getLast());
        Assertions.assertEquals(8, myDoublyLinkedList.get(1));
        Assertions.assertEquals(8, myDoublyLinkedList.get(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> myDoublyLinkedList.add(6, 7));
    }

    @Test
    void getFirstEmptyTest() {
        Assertions.assertThrows(NoSuchElementException.class, () -> myDoublyLinkedList.getFirst());
    }

    @Test
    void getFirstTest() {
        myDoublyLinkedList.addFirst(2);
        myDoublyLinkedList.addFirst(5);
        Assertions.assertEquals(5, myDoublyLinkedList.getFirst());
    }

    @Test
    void getLastEmptyTest() {
        Assertions.assertThrows(NoSuchElementException.class, () -> myDoublyLinkedList.getLast());
    }

    @Test
    void getLastTest() {
        myDoublyLinkedList.add(0, 6);
        myDoublyLinkedList.addLast(4);
        Assertions.assertEquals(4, myDoublyLinkedList.getLast());
    }

    @Test
    void getEmptyTest() {
        Assertions.assertThrows(NoSuchElementException.class, () -> myDoublyLinkedList.get(0));
    }

    @Test
    void getTest() {
        myDoublyLinkedList.add(0, 6);
        myDoublyLinkedList.addLast(4);
        myDoublyLinkedList.addLast(9);
        Assertions.assertEquals(4, myDoublyLinkedList.get(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> myDoublyLinkedList.get(5));
    }

    @Test
    void removeFirstEmptyTest(){
        Assertions.assertThrows(NoSuchElementException.class, ()->myDoublyLinkedList.removeFirst());
    }

    @Test
    void removeFirstTest(){
        myDoublyLinkedList.addFirst(7);
        myDoublyLinkedList.addLast(4);
        myDoublyLinkedList.addLast(9);
        Assertions.assertEquals(7, myDoublyLinkedList.removeFirst());
    }

    @Test
    void removeLastEmptyTest(){
        Assertions.assertThrows(NoSuchElementException.class, ()->myDoublyLinkedList.removeLast());
    }

    @Test
    void removeLastTest(){
        myDoublyLinkedList.addFirst(7);
        myDoublyLinkedList.addLast(4);
        myDoublyLinkedList.addLast(9);
        Assertions.assertEquals(9, myDoublyLinkedList.removeLast());
    }

    @Test
    void removeEmptyTest(){
        Assertions.assertThrows(NoSuchElementException.class, ()->myDoublyLinkedList.remove(0));
    }

    @Test
    void removeTest(){
        myDoublyLinkedList.addFirst(7);
        myDoublyLinkedList.addLast(4);
        myDoublyLinkedList.addLast(9);
        Assertions.assertEquals(4, myDoublyLinkedList.remove(1));
    }
}

