package text;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextProcessorTest {
    
    public TextProcessorTest() {
        
    }
    
    @Test
    public void testWeakStem() {
        assertEquals("book", TextProcessor.weakStem("books"));
        assertEquals("s", TextProcessor.weakStem("s"));
    }
    
    @Test
    public void testPorterStem() {
        assertEquals("book", TextProcessor.porterStem("books"));

        assertEquals("connect", TextProcessor.porterStem("connected"));
        assertEquals("connect", TextProcessor.porterStem("connection"));
        
        // Stemmer sometimes cuts off endings but they still conflate well
        assertEquals("relat", TextProcessor.porterStem("relational"));
        
        assertEquals("allow", TextProcessor.porterStem("allowance"));
        
        assertEquals("adjust", TextProcessor.porterStem("adjustable"));
    }
}
