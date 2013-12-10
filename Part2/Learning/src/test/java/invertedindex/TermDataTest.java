package invertedindex;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TermDataTest {

    private TermData termData;

    @Before
    public void setup() {
        termData = new TermData("someterm");
        termData.addTermOccurrence("hello.txt");
        termData.addTermOccurrence("hello.txt");
        termData.addTermOccurrence("world.txt");
    }

    @Test
    public void testGetTermFrequency() {
        assertEquals(2, termData.getTermFrequency("hello.txt"));
        assertEquals(1, termData.getTermFrequency("world.txt"));
        assertEquals(0, termData.getTermFrequency("imaginary.txt"));
    }

    @Test
    public void testGetDocumentFrequency() {
        assertEquals(2, termData.getDocumentFrequency());

        // Adding existing document should not increase value
        termData.addTermOccurrence("hello.txt");
        assertEquals(2, termData.getDocumentFrequency());

        // Adding new document should increase value
        termData.addTermOccurrence("new.txt");
        assertEquals(3, termData.getDocumentFrequency());
    }

    @Test
    public void testGetTotalTermFrequency() {
        assertEquals(3, termData.getTotalTermFrequency());

        // Adding to an existing document should still increase total term freq.
        termData.addTermOccurrence("hello.txt");
        assertEquals(4, termData.getTotalTermFrequency());

        // Adding a new document should also increase total term freq.
        termData.addTermOccurrence("new.txt");
        assertEquals(5, termData.getTotalTermFrequency());
    }
}
