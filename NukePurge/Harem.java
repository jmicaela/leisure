import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.lang3.RandomStringUtils;

/*
 * CLASS PIG
 * 
 * */
/* public */ class Pig {
	String codeName;
	Character i;
	Float pow;
	Boolean isShit;   // default value true/false?
	Boolean isUseful;
	Kind type;	
	
	Pig(String codeName) throws Exception {
		if (codeName.isEmpty()) {
			throw new Exception("Code name must be nonempty.");
		}
		this.codeName = codeName.toLowerCase();
	}
}
/* public */ enum Kind {WANTED, DESIRABLES, THROWABLE, SLAUGHTER_PENDING}

/*
 * CLASS PIG KEY
 * 
 * */
/* private */ class PigKey {
	String Key;
	PigKey() {
		this.Key = RandomStringUtils.randomAlphabetic(4).toLowerCase();
	}
	
	String getKey() {
		return this.Key;
	}
}

/*
 * CLASS HAREM
 * 
 * */
/* private */ class Harem {
	
	public static void main(String[] args) {
		Harem myHarem = new Harem();
		/*
		 * TO DO: add test cases 
		 * */

		try {

			ArrayList<Pig> pen = new ArrayList<Pig>(Arrays.asList(
				new Pig("Rob"),
				new Pig("Igor")
			));

			// CASE: WANTED
			
			// CASE: DESIRABLES
			
			// CASE: THROWABLES
			//myHarem.add(pen.get(0));
			
			// CASE: SLAUGHTER_PENDING
			//myHarem.add(pen.get(0));
		

			for (Pig p : pen) {
				System.out.println("Pig added to class harem? " + myHarem.add(p));
				System.out.println("\tCodename: " + p.codeName);
				System.out.println("\t" + p.codeName + " isShit? " + p.isShit);
				System.out.println("\t" + p.codeName + " isUseful? " + p.isUseful);
				System.out.println("\t" + p.codeName + " has been set status of: " + p.type);
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}		
	}

	// where key is 4-length character
	HashMap<PigKey, Pig> Harem;

	Harem() {
		this.Harem =  new HashMap<PigKey, Pig>();
	}
	
	/*
	 * Adds the specified element p to this set. 
	 * Returns true if added successfully, false otherwise. 
	 */
	  boolean add(Pig p) {
		  switch (p.codeName) { //case sensitive
		  	case "rob":
		  		p.isShit = true;
		  		p.isUseful = false;
		  		p.type = Kind.SLAUGHTER_PENDING;
		  		return false;
		  	case "igor":
		  		p.isShit = true;
		  		p.isUseful = true;
		  		p.type = Kind.THROWABLE;		  		
		  		return false;
		  	default:
				System.out.println("TO DO\n");
		  }			
		  return true;
	  }
	
	  
	 /* boolean addAll(Collection<? extends E> c) Adds
	 * all of the elements in the specified collection to this set if they're not
	 * already present (optional operation). 
	 * 
	 * void clear() Removes all of the
	 * elements from this set (optional operation). boolean contains(Object o)
	 * Returns true if this set contains the specified element. 
	 * 
	 * boolean containsAll(Collection<?> c) 
	 * Returns true if this set contains all of the
	 * elements of the specified collection. 
	 * 
	 * boolean equals(Object o) Compares the
	 * specified object with this set for equality. int hashCode() Returns the hash
	 * code value for this set. 
	 * 
	 * boolean isEmpty() Returns true if this set contains
	 * no elements. 
	 * 
	 * Iterator<E> iterator() Returns an iterator over the elements in
	 * this set. boolean remove(Object o) Removes the specified element from this
	 * set if it is present (optional operation). 
	 * 
	 * boolean removeAll(Collection<?> c)
	 * Removes from this set all of its elements that are contained in the specified
	 * collection (optional operation). 
	 * 
	 * boolean retainAll(Collection<?> c) Retains
	 * only the elements in this set that are contained in the specified collection
	 * (optional operation). 
	 * 
	 * int size() Returns the number of elements in this set
	 * (its cardinality). 
	 * 
	 * Object[] toArray() Returns an array containing all of the
	 * elements in this set. 
	 * 
	 * <T> T[] toArray(T[] a) Returns an array containing all
	 * of the elements in this set; the runtime type of the returned array is that
	 * of the specified array.
	 */
}
