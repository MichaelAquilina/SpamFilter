package invertedindex;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashedIndexTest {
    
    public HashedIndexTest() {
    }

    @Test
    public void testGetTermFrequency() {
        HashedIndex invertedIndex = new HashedIndex();
        
        assertEquals(invertedIndex.getTermFrequency("Imaginary"), 0);
        
        invertedIndex.add("hello", "hello.txt");
        invertedIndex.add("hello", "world.txt");
        invertedIndex.add("world", "world.txt");
        
        assertEquals(invertedIndex.getTermFrequency("hello"), 2);
        assertEquals(invertedIndex.getTermFrequency("hello", "hello.txt"), 1);
        
        assertEquals(invertedIndex.getTermFrequency("world"), 1);
        assertEquals(invertedIndex.getTermFrequency("world", "hello.txt"), 0);
        assertEquals(invertedIndex.getTermFrequency("world", "world.txt"), 1);
    }

    /**
     * Test of getDocuments method, of class HashedIndex.
     */
    @Test
    public void testGetDocuments() {
        HashedIndex invertedIndex = new HashedIndex();
        
        assertEquals(invertedIndex.getDocuments(), new ArrayList<String>());
        
        ArrayList<String> myDocuments = new ArrayList<>();
        myDocuments.add("hello.txt");
        myDocuments.add("world.txt");
        myDocuments.add("GladOs.txt");
        
        for(String document : myDocuments)
            invertedIndex.add("random", document);
        
        assertEquals(invertedIndex.getDocuments(), myDocuments);
    }
    
}
