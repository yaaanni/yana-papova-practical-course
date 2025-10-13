package com.github.yaaanni.doublyLinkedList;

import java.util.NoSuchElementException;

public class MyDoublyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    private static class Node<T> {
        Node<T> next;
        Node<T> previous;
        T data;

        Node(T data, Node<T> next, Node<T> previous) {
            this.data = data;
            this.next = next;
            this.previous = previous;
        }
    }

    public MyDoublyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(T el) {
        if (head == null) {
            head = new Node<T>(el, null, null);
            tail = head;
        } else {
            Node<T> current = new Node<T>(el, null, null);
            current.next = head;
            head.previous = current;
            head = current;
        }
        size++;
    }

    public void addLast(T el) {
        if (head == null) {
            head = new Node<T>(el, null, null);
            tail = head;
        } else {
            Node<T> current = new Node<T>(el, null, null);
            current.previous = tail;
            tail.next = current;
            tail = current;
        }
        size++;
    }

    public void add(int index, T el) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        if (index == 0) {
            addFirst(el);
        } else if (index == size) {
            addLast(el);
        } else {
            Node<T> newNode = new Node<T>(el, null, null);
            if (index < size / 2) {
                int counter = 0;
                Node<T> current = head;
                while (counter != index) {
                    current = current.next;
                    counter++;
                }
                newNode.previous = current.previous;
                newNode.next = current;
                current.previous.next = newNode;
                current.previous = newNode;
                size++;
            } else {
                Node<T> current = tail;
                int counter = size - 1;
                while (counter != index) {
                    current = current.previous;
                    counter--;
                }
                newNode.previous = current.previous;
                newNode.next = current;
                current.previous.next = newNode;
                current.previous = newNode;
                size++;
            }
        }
    }

    public T getFirst() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        return head.data;
    }

    public T getLast() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        return tail.data;
    }

    public T get(int index) {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        if (index == 0) {
            return getFirst();
        } else if (index == size - 1) {
            return getLast();
        } else if (index < size / 2) {
            int counter = 0;
            Node<T> current = head;
            while (counter != index) {
                current = current.next;
                counter++;
            }
            return current.data;
        } else {
            int counter = size - 1;
            Node<T> current = tail;
            while (counter != index) {
                current = current.previous;
                counter--;
            }
            return current.data;
        }
    }

    public T removeFirst() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        T data = head.data;
        head = head.next;
        head.previous = null;
        size--;
        return data;
    }

    public T removeLast() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        T data = tail.data;
        tail = tail.previous;
        tail.next = null;
        size--;
        return data;
    }

    public T remove(int index) {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        if (index == 0) {
            return removeFirst();

        } else if (index == size - 1) {
            return removeLast();
        } else if (index < size / 2) {
            int counter = 0;
            Node<T> current = head;
            while (counter != index) {
                current = current.next;
                counter++;
            }
            T data = current.data;
            current.previous.next = current.next;
            current.next.previous = current.previous;
            size--;
            return data;
        } else {
            int counter = size - 1;
            Node<T> current = tail;
            while (counter != index) {
                current = current.previous;
                counter--;
            }
            T data = current.data;
            current.previous.next = current.next;
            current.next.previous = current.previous;
            size--;
            return data;
        }

    }

}
