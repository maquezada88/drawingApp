package pa2.util;

class Link<E> { // Singly linked list node class
  private E element; // Value for this node
  private Link<E> pointer; // Point to next node in list

  // Constructors
  Link(E it, Link<E> inn) {
    element = it;
    pointer = inn;
  }

  Link(Link<E> inn) {
    element = null;
    pointer = inn;
  }

  E element() {
    return element;
  } // Return the value

  E setElement(E it) {
    return element = it;
  } // Set element value

  Link<E> next() {
    return pointer;
  } // Return next link

  Link<E> setNext(Link<E> inn) {
    return pointer = inn;
  } // Set next link
}
