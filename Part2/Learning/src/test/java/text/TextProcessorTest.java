package text;

import org.junit.Test;
import static org.junit.Assert.*;
import text.TextProcessor;

public class TextProcessorTest {
    
    public TextProcessorTest() {
        
    }
    
    @Test
    public void testWeakStem() {
        TextProcessor textProcessor = new TextProcessor();
        
        assertEquals("book", textProcessor.weakStem("books"));
        assertEquals("s", textProcessor.weakStem("s"));
    }
    
    @Test
    public void testPorterStem() {
        TextProcessor textProcessor = new TextProcessor();
        
        assertEquals("book", textProcessor.porterStem("books"));
        
        assertEquals("connect", textProcessor.porterStem("connected"));
        assertEquals("connect", textProcessor.porterStem("connection"));
        
        // Stemmer sometimes cuts off endings but they still conflate well
        assertEquals("relat", textProcessor.porterStem("relational"));
        
        assertEquals("allow", textProcessor.porterStem("allowance"));
        
        assertEquals("adjust", textProcessor.porterStem("adjustable"));
    }
}
