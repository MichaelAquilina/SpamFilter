package text;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TextProcessorTest {
    
    public TextProcessorTest() {
        
    }

    @Test
    public void testRstrip() {
        assertEquals("", TextProcessor.rstrip(""));

        // No Symbols
        assertEquals("hello", TextProcessor.rstrip("hello"));

        // Words with trailing symbols
        assertEquals("hello", TextProcessor.rstrip("hello:"));
        assertEquals("hello", TextProcessor.rstrip("hello!"));
        assertEquals("because", TextProcessor.rstrip("because....!"));
        assertEquals("users", TextProcessor.rstrip("users'"));

        // Only Symbols
        assertEquals("", TextProcessor.rstrip(".#@@#&*^@"));

        // Words with trailing digits
        assertEquals("hello89", TextProcessor.rstrip("hello89"));
    }

    @Test
    public void testIsCurrency() {
        assertTrue(TextProcessor.isCurrency("$499"));
        assertTrue(TextProcessor.isCurrency("$4"));

        assertFalse(TextProcessor.isCurrency("$"));
        assertFalse(TextProcessor.isCurrency("hello"));
        assertFalse(TextProcessor.isCurrency("423"));
    }

    @Test
    public void testIsNumber() {
        assertTrue(TextProcessor.isNumber("499"));
        assertTrue(TextProcessor.isNumber("123456789"));

        assertFalse(TextProcessor.isNumber("$500"));
        assertFalse(TextProcessor.isNumber("hello"));
        assertFalse(TextProcessor.isNumber("%$#"));
    }

    @Test
    public void testIsSymbol() {
        assertTrue(TextProcessor.isSymbol("$"));
        assertTrue(TextProcessor.isSymbol("@"));
        assertTrue(TextProcessor.isSymbol("!"));
        assertTrue(TextProcessor.isSymbol("$@#@#@()!"));

        assertFalse(TextProcessor.isSymbol("hello"));
        assertFalse(TextProcessor.isSymbol("1234"));
        assertFalse(TextProcessor.isSymbol("@#$@#@H"));
        assertFalse(TextProcessor.isSymbol("Michael: "));
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
