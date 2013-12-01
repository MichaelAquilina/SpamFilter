package invertedindex;

import java.util.HashSet;
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
        
        assertEquals(invertedIndex.getDocuments(), new HashSet<>());
        
        HashSet<String> myDocuments = new HashSet<>();
        myDocuments.add("hello.txt");
        myDocuments.add("world.txt");
        myDocuments.add("GladOs.txt");
        
        for(String document : myDocuments)
            invertedIndex.add("random", document);
        
        assertEquals(invertedIndex.getDocuments(), myDocuments);
        
        // Ensure no duplicates occur
        invertedIndex.add("random2", "hello.txt");
        assertEquals(invertedIndex.getDocuments(), myDocuments);
    }
    
    @Test
    public void testTrimIndex() {
        HashedIndex invertedIndex = new HashedIndex();
        
        // Words with just 1 occureance
        invertedIndex.add("steel", "peal.txt");
        
        // Words with 4 occurances
        for(int i=0; i<4; i++)
            invertedIndex.add("hello", "hello.txt");
        
        // Words with 5 occurances
        for(int i=0; i<5; i++)
            invertedIndex.add("world", "world.txt");
        
        invertedIndex.trimIndex(2, 4);
        
        assertFalse(invertedIndex.containsTerm("world"));
        assertFalse(invertedIndex.containsTerm("steel"));
        
        assertTrue(invertedIndex.containsTerm("hello"));
    }
    
    @Test
    public void testMinMaxTermFrequency() {
        HashedIndex invertedIndex = new HashedIndex();
        
        // min and max should be 0 at the start
        assertEquals(invertedIndex.getMaxTermFrequency(), -1);
        assertEquals(invertedIndex.getMinTermFrequency(), -1);
        
        for(int i=0; i<4; i++)
            invertedIndex.add("hello", "hello.txt");
        
        assertEquals(invertedIndex.getMaxTermFrequency(), 4);
        assertEquals(invertedIndex.getMinTermFrequency(), 4);
        
        for(int i=0; i<6; i++)
            invertedIndex.add("world", "world.txt");
        
        assertEquals(invertedIndex.getMaxTermFrequency(), 6);
        assertEquals(invertedIndex.getMinTermFrequency(), 4);
    }
}
