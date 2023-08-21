package pa2.util;

// Linked stack implementation
public class LStack<E> implements Stack<E> {
  private boolean flag = false; // boolean in case constructor with capacity got invoked
  private Link<E> top; // Pointer to first element
  private int size; // Number of elements
  private int maxSize; // capacity for stack

  // Constructors
  public LStack() {
    top = null;
    size = 0;
    flag = false;
  }

  public LStack(int capacity) { // constructor with capacity
    flag = true;
    top = null;
    size = 0;
    maxSize = capacity;
  }

  // Reinitialize stack
  public void clear() {
    top = null;
    size = 0;
  }

  // Put "it" on stack
  public boolean push(E it) {
    if (flag == true && maxSize == 0) {
      return false;
    }

    if (size >= maxSize && flag == true) {
      Link<E> temp = top;
      for (int i = maxSize; i > 0; i--) {
        if (i == 1) {
          temp.setNext(null);
          size--;
        } else {
          temp = temp.next();
        }
      }
    }

    top = new Link<E>(it, top);
    size++;
    return true;
  }

  // Remove "it" from stack
  public E pop() {
    if (top == null) {
      return null;
    }

    if (size == 0) {
      return null;
    } else {
      E it = top.element();
      top = top.next();
      size--;
      return it;
    }
  }

  public E topValue() { // Return top value
    if (top == null) {
      return null;
    }
    if (maxSize == 0) {
      return null;
    }
    return top.element();
  }

  // Return stack length
  public int length() {
    return size;
  }
}
