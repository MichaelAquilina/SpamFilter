package invertedindex;

import org.junit.Test;
import static org.junit.Assert.*;
import text.TextProcessor;

public class TextProcessorTest {
    
    public TextProcessorTest() {
        
    }
    
    @Test
    public void testWeakStem() {
        TextProcessor textProcessor = new TextProcessor();
        
        assertEquals(textProcessor.weakStem("books"), "book");
        assertEquals(textProcessor.weakStem("s"), "s");
    }
}
