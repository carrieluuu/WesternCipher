/**
 * This class represents an array implementation of a circular queue.
 * 
 * @author Carrie Lu (251140757)
 */

public class CircularArrayQueue<T> implements QueueADT<T> {
	/**
	 * Index value of the front item
	 */
	private int front;
	/**
	 * Index value of the last item
	 */
	private int rear;
	/**
	 * Number of items in the queue
	 */
	private int count;
	/**
	 * Generic type array
	 */
	private T[] queue;
	/**
	 * Constant default capacity of the array
	 */
	private final int DEFAULT_CAPACITY = 20;

	/**
	 * Constructor creates a queue initialized to default capacity
	 * 
	 */
	public CircularArrayQueue() {
		front = 1;
		rear = DEFAULT_CAPACITY;
		count = 0;
		queue = (T[]) (new Object[DEFAULT_CAPACITY]);
	}

	/**
	 * Constructor creates an empty queue using the specified capacity.
	 * 
	 * @param initialCapacity the specified capacity
	 */
	public CircularArrayQueue(int initialCapacity) {
		front = 1;
		rear = initialCapacity;
		queue = (T[]) (new Object[initialCapacity]);
	}

	/**
	 * Method that adds the specified element to the rear of this queue.
	 * 
	 * @param element the element to be added to the rear of this queue
	 */
	public void enqueue(T element) {
		if (count == queue.length) {
			expandCapacity();
		}
		queue[rear % queue.length] = element;
		rear = (rear % queue.length + 1) % queue.length;

		count++;

	}

	/**
	 * Method that removes and returns the item at the front of this queue. Throws
	 * an EmptyCollectionException if the queue is empty.
	 *
	 * @return T item at the front of this queue
	 * @throws EmptyCollectionException if an empty collection exception occurs
	 */
	public T dequeue() throws EmptyCollectionException {
		if (isEmpty()) {
			throw new EmptyCollectionException("queue");
		}
		T item = queue[front - 1];
		queue[front - 1] = null;
		front = front % queue.length + 1;

		count--;

		return item;
	}

	/**
	 * Method that returns the item at the front of the queue without removing it.
	 * Throws an EmptyCollectionException if the queue is empty.
	 *
	 * @return T item at the front of this queue
	 * @throws EmptyCollectionException if an empty collection exception occurs
	 */
	public T first() throws EmptyCollectionException {
		if (isEmpty()) {
			throw new EmptyCollectionException("queue");
		}

		return queue[front - 1];

	}

	/**
	 * Method that returns true if this queue is empty and false otherwise.
	 *
	 * @return true if this queue is empty and false otherwise
	 */
	public boolean isEmpty() {
		if (count == 0) {
			return true;

		} else {
			return false;
		}

	}

	/**
	 * Method that returns the number of items in the queue.
	 *
	 * @return count int representation of the size of this queue
	 */
	public int size() {
		return count;
	}

	/**
	 * Accessor method that gets the front index value
	 *
	 * @return front the front index value
	 */
	public int getFront() {
		return front;
	}

	/**
	 * Accessor method that gets the rear index value
	 *
	 * @return rear the rear index value
	 */
	public int getRear() {
		return rear;
	}

	/**
	 * Accessor method that gets the current length (capacity) of the array
	 *
	 * @return queue.length length of array
	 */
	public int getLength() {
		return queue.length;

	}

	/**
	 * Method that returns a string representation of this queue with each element
	 * separated by a comma and the last element followed by a period.
	 * 
	 * @return result formatted String representation of the queue
	 */
	public String toString() {
		if (isEmpty()) {
			return "The queue is empty";
		} else {
			String result = "QUEUE: ";
			int i = front - 1;

			// When index of front is smaller than index of rear
			if (front <= rear) {
				while (i < rear) {
					if (queue[i] != null) {
						if (i == rear - 1) {
							result += queue[i].toString();
						} else {
							result += queue[i].toString() + ", ";
						}
					}
					i++;
				}
				result += ".";

				return result;

			} else {
				// When index of rear is smaller than index of front
				while (i < queue.length) {
					if (queue[i] != null) {
						result += queue[i].toString() + ", ";

					}
					i++;
				}
				i = 0;
				while (i < rear) {
					if (queue[i] != null) {
						// Last element
						if (i == rear - 1) {
							result += queue[i].toString();
						} else {
							result += queue[i].toString() + ", ";
						}
					}
					i++;

				}
				result += ".";

			}
			return result;
		}

	}

	/**
	 * Method that creates a new array to store the contents of this queue with an
	 * increased capacity of 20 spots
	 */
	public void expandCapacity() {
		T[] larger = (T[]) (new Object[queue.length + 20]);

		for (int i = 0; i < count; i++) {
			larger[i] = queue[i];
		}

		front = 1;
		rear = count;
		queue = larger;

	}

}
