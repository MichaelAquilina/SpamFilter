package invertedindex;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class InvertedIndexTest {
    
    public InvertedIndexTest() {
    }

    @Test
    public void testGetTermFrequency() {
        InvertedIndex invertedIndex = new InvertedIndex();
        
        assertEquals(0, invertedIndex.getTermFrequency("Imaginary"));
        
        invertedIndex.add("hello", "hello.txt");
        invertedIndex.add("hello", "world.txt");
        invertedIndex.add("world", "world.txt");
        
        assertEquals(2, invertedIndex.getTermFrequency("hello"));
        assertEquals(1, invertedIndex.getTermFrequency("hello", "hello.txt"));
        
        assertEquals(1, invertedIndex.getTermFrequency("world"));
        assertEquals(0, invertedIndex.getTermFrequency("world", "hello.txt"));
        assertEquals(1, invertedIndex.getTermFrequency("world", "world.txt"));
    }

    @Test
    public void testGetDocuments() {
        InvertedIndex invertedIndex = new InvertedIndex();
        
        assertTrue(invertedIndex.getDocuments().isEmpty());
        
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
        InvertedIndex invertedIndex = new InvertedIndex();
        
        // Words with just 1 occurrence
        invertedIndex.add("steel", "peal.txt");

        // Words with 3 occurrence
        invertedIndex.add("hello", "world.txt");
        invertedIndex.add("hello", "hello.txt");
        invertedIndex.add("hello", "peal.txt");

        // Words with 5 occurrence
        invertedIndex.add("world", "world.txt");
        invertedIndex.add("world", "hello.txt");
        invertedIndex.add("world", "peal.txt");
        invertedIndex.add("world", "scream.txt");
        invertedIndex.add("world", "team.txt");

        invertedIndex.trimIndex(2, 4);
        
        assertFalse(invertedIndex.containsTerm("world"));
        assertFalse(invertedIndex.containsTerm("steel"));
        
        assertTrue(invertedIndex.containsTerm("hello"));
    }
    
    @Test
    public void testMinMaxTermFrequency() {
        InvertedIndex invertedIndex = new InvertedIndex();
        
        // min and max should be 0 at the start
        assertEquals(-1, invertedIndex.getMaxTermFrequency());
        assertEquals(-1, invertedIndex.getMinTermFrequency());
        
        for(int i=0; i<4; i++)
            invertedIndex.add("hello", "hello.txt");
        
        assertEquals(4, invertedIndex.getMaxTermFrequency());
        assertEquals(4, invertedIndex.getMinTermFrequency());
        
        for(int i=0; i<6; i++)
            invertedIndex.add("world", "world.txt");
        
        assertEquals(6, invertedIndex.getMaxTermFrequency());
        assertEquals(4, invertedIndex.getMinTermFrequency());
    }
    
    @Test
    public void testRemove() {
        InvertedIndex invertedIndex = new InvertedIndex();
        
        invertedIndex.add("hello", "hello.txt");
        invertedIndex.add("nyan", "cat.txt");
        invertedIndex.add("hello", "hello2.txt");
        invertedIndex.add("oxy", "moron.txt");
        
        assertTrue(invertedIndex.remove("hello"));
        
        assertFalse(invertedIndex.containsTerm("hello"));
        assertTrue(invertedIndex.containsTerm("nyan"));
        assertTrue(invertedIndex.containsTerm("oxy"));
    }
    
    @Test
    public void testGetTerms() {
        InvertedIndex invertedIndex = new InvertedIndex();
        String[] expected;
        
        invertedIndex.add("hello", "hello.txt");
        invertedIndex.add("hello", "hello2.txt");
        
        expected = new String[]{"hello"};
        assertTrue(CollectionUtils.isEqualCollection(Arrays.asList(expected), invertedIndex.getTerms()));
        
        invertedIndex.add("nyan", "cat.txt");
        
        expected = new String[]{"hello", "nyan"};
        assertTrue(CollectionUtils.isEqualCollection(Arrays.asList(expected), invertedIndex.getTerms()));
    }
}
