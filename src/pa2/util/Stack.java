package pa2.util;


/**
 *
 * @author John C. Bowers <bowersjc@jmu.edu>
 */
public interface Stack<E> {

  // Reinitialize stack
  public void clear();

  // Put "it" on stack
  public boolean push(E it);

  // Remove "it" from stack
  public E pop();

  public E topValue();

  // Return stack length
  public int length();
}
