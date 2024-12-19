
/*  This class represents a Playlist of podcast episodes, where each
/*  episode is implemented as an object of type Episode. A user navigating
/*  a Playlist should be able to move between songs using next or previous references.
/*
/*  To enable flexible navigation, the Playlist is implemented as
/*  a Doubly Linked List where each episode has a link to both the
/*  next and the prev episodes in the list.
*/

/* THIS CODE WAS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING
CODE WRITTEN BY OTHER STUDENTS OR ONLINE RESOURCES.
Leonardo Lazarevic */

import java.util.*;

public class Playlist {
	private Episode head;
	private int size;

	public Playlist() {
		head = null;
		size = 0;
	}

	public boolean isEmpty() {
		return head == null;
	}

	// Ensure that "size" is updated properly in other methods, to always
	// reflect the correct number of episodes in the current Playlist
	public int getSize() {
		return size;
	}

	// Our implementation of toString() displays the Playlist forward,
	// starting at the first episode (i.e. head) and ending at the last episode,
	// while utilizing the "next" reference in each episode
	@Override
	public String toString() {
		String output = "[HEAD] ";
		Episode current = head;
		if (!isEmpty()) {
			while (current.next != null) {
				output += current + " -> ";
				current = current.next;
			}
			output += current + " [END]\n";
		} else {
			output += " [END]\n";
		}
		return output;
	}

	// This method displays the Playlist backward, starting at
	// the last episode and ending at the first episode (i.e. head),
	// while utilizing the "prev" reference in each episode
	public String toReverseString() {
		String output = "[END] ";
		Episode current = head;
		if (!isEmpty()) {
			while (current.next != null)
				current = current.next;
			// current is now pointing to last node

			while (current.prev != null) {
				output += current + " -> ";
				current = current.prev;
			}
			output += current + " [HEAD]\n";
		} else {
			output += " [HEAD]\n";
		}
		return output;
	}

	/**************************************************************/
	// A4 Part 1 Methods (Add/Delete Operations)

	// addFirst method:
	/*
	 * Within the linked list, add the labeled title and duration episode into the
	 * first position of the linked list
	 */
	public void addFirst(String title, double duration) {
		Episode newHead = new Episode(title, duration, head, null);
		if (head != null) {
			head.prev = newHead;
		}
		head = newHead;
		size++;
	}

	// addLast method:
	/*
	 * Within the linked list, add the labeled title and duration episode into the
	 * last position of the linked list
	 */
	public void addLast(String title, double duration) {
		//Empty list special case
		if (isEmpty()) {
			addFirst(title, duration);
		} else {
			Episode current = head;
			while (current.next != null) {
				current = current.next;
			}
			size++;
			current.next = new Episode(title, duration, null, current);
		}
	}

	// deleteFirst method:
	/*
	 * Deletes the first Episode within the linked list, throws no such element
	 * exception if linked list empty
	 */
	public Episode deleteFirst() {
		//Empty list catch
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		Episode temp = head;
		head = head.next;
		head.prev = null;
		size--;
		return temp;
	}

	// deleteLast method:
	/*
	 * Deletes the last episode of the linked list, throws no such element exception
	 * if linked list empty
	 */
	public Episode deleteLast() {
		//Empty list catch
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		Episode current = head;
		while (current.next != null) {
			current = current.next;
		}
		Episode temp = current;
		if (current.prev != null) {
			current.prev.next = null;
		} else {
			head = null;
		}

		size--;
		return temp;
	}

	// deleteEpisode method:
	/*
	 * Deletes the outlined episode of the linked list, throws no such element
	 * exception if linked list empty or if outlined epsiode isn't found
	 */
	public Episode deleteEpisode(String title) {
		Episode current = head;
		Episode previous = head;
		//Empty list catch
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		if (head.title.equals(title)) {
			deleteFirst();
		} else {
			while (current != null && !current.title.equals(title)) {
				previous = current;
				current = current.next;
			}
			if (current == null) {
				throw new NoSuchElementException();
			}
			previous.next = current.next;
		}
		size--;
		return current;

	}

	/***************************************************************/
	// A4 Part 2 Methods (Sorting the Playlist using MergeSort)

	public Episode merge(Episode a, Episode b) {
		//Empty list catch
		if (a == null && b == null) {
			throw new NoSuchElementException();
		}
		// Empty auxillary list to merge and start episode to return the beginning
		// Way merge method is used is to start at beginning and use .next to get other,
		// not .prev, that's why I need a start node
		Episode start = null;
		Episode aux = null;
		// Checks if a or b is null
		if (a == null) {
			return b;
		} else if (b == null) {
			return a;
		}
		// Puts first element into aux so we can access aux.next in the future
		if (a.compareTo(b) == -1) {
			start = a;
			a = a.next;
		} else if (a.compareTo(b) == +1) {
			start = b;
			b = b.next;
		} else if (a.compareTo(b) == 0) {
			start = a;
			a = a.next;
		}
		// Setting auxlillary to head so we can start merging from the start
		aux = start;

		// Goes through both lists, until both are null, while adding the correct
		// elements to the auxillary
		while (a != null || b != null) {
			// Checks once a or b is fully empty
			if (a == null) {
				aux.next = b;
				b.prev = aux;
				b = b.next;
			} else if (b == null) {
				aux.next = a;
				a.prev = aux;
				a = a.next;
			// Simple order checks
			} else if (a.compareTo(b) == -1) {
				aux.next = a;
				a.prev = aux;
				a = a.next;
			} else if (a.compareTo(b) == +1) {
				aux.next = b;
				b.prev = aux;
				b = b.next;
			} else if (a.compareTo(b) == 0) {
				aux.next = a;
				a.prev = aux;
				a = a.next;
			}
			aux = aux.next; // I HAD TO MOVE AUX POINTER!!!!!!!!!!
		}
		return start;
	}

	// Finds the middle episode of the list that begins at the passed node reference
	private Episode getMiddleEpisode(Episode node) {
		if (node == null)
			return node;
		Episode slow = node;
		Episode fast = node;
		while (fast.next != null && fast.next.next != null) {
			slow = slow.next;
			fast = fast.next.next;
		}
		return slow;
	}

	// MergeSort starting point
	public void mergeSort() {
		if (isEmpty())
			throw new RuntimeException("Cannot sort empty list.");
		head = sort(head);
	}

	// Recursively splits the list starting at a given node reference
	public Episode sort(Episode node) {
		if (node == null || node.next == null)
			return node;
		Episode middle = getMiddleEpisode(node); // get the middle of the list
		Episode left_head = node;
		Episode right_head = middle.next;

		// split the list into two halves:
		if (right_head != null)
			right_head.prev = null;
		middle.next = null;

		Episode left = sort(left_head);
		Episode right = sort(right_head);
		return merge(left, right);
	}

} // End of Playlist class
