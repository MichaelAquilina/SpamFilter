package text;

import org.junit.Test;

import static org.junit.Assert.*;

public class TextProcessorTest {
    
    public TextProcessorTest() {
        
    }

    @Test
    public void testExtractMailDomain() {
        // Valid Email Addresses
        assertEquals("gmail.com", TextProcessor.extractMailDomain("michaelaquilina@gmail.com"));
        assertEquals("yahoo.co.uk", TextProcessor.extractMailDomain("test@yahoo.co.uk"));

        // Invalid Email Addresses
        assertEquals(null, TextProcessor.extractMailDomain("Dog"));
        assertEquals(null, TextProcessor.extractMailDomain("@gmail.com"));
    }

    @Test
    public void testExtractUrlDomain() {
        // known urls
        assertEquals("www.google.com", TextProcessor.extractUrlDomain("http://www.google.com/dawdawdawdawdjiocjei"));
        assertEquals("click.wh5.com", TextProcessor.extractUrlDomain("http://click.wh5.com/redirect.php?c=8496&u=lxoyup..cahrnet_0bkttg"));
        assertEquals("example.bob.co.uk", TextProcessor.extractUrlDomain("https://example.bob.co.uk/?@##@#$@wdaeq2322323"));

        // no sub-domain
        assertEquals("example.com", TextProcessor.extractUrlDomain("http://example.com"));

        // Invalid Urls
        assertEquals(null, TextProcessor.extractUrlDomain("Mike Aquilina"));
        assertEquals(null, TextProcessor.extractUrlDomain("htp://something.example"));
    }

    @Test
    public void testIsEmailAddress() {
        // known email addresses
        assertTrue(TextProcessor.isEmailAddress("michaelaquilina@gmail.com"));
        assertTrue(TextProcessor.isEmailAddress("my-email-address@something.co.uk"));
        assertTrue(TextProcessor.isEmailAddress("john@mail.office.com"));

        // known non-email addresses
        assertFalse(TextProcessor.isEmailAddress("Dog"));
        assertFalse(TextProcessor.isEmailAddress("http://www.google.com/23"));

        // Corner cases
        assertFalse(TextProcessor.isEmailAddress("Hello@"));
        assertFalse(TextProcessor.isEmailAddress("@Hello"));
    }

    @Test
    public void testIsUrl() {
        assertTrue(TextProcessor.isUrl("http://www.google.com/dawdawdawdawdjiocjei"));
        assertTrue(TextProcessor.isUrl("http://click.wh5.com/redirect.php?c\u003d8496\u0026u\u003dlxoyup..cahrnet_0bkttg"));
        assertTrue(TextProcessor.isUrl("https://lists.sourceforge.net/lists/listinfo/razor-us"));

        assertFalse(TextProcessor.isUrl("Hamburger"));
        assertFalse(TextProcessor.isUrl("ht://example"));
        assertFalse(TextProcessor.isUrl("MichaelAquilina@gmail.com"));
    }

    @Test
    public void testStrip() {
        assertEquals("", TextProcessor.strip(""));

        assertEquals("hello", TextProcessor.strip("hello"));

        assertEquals("hello", TextProcessor.strip("...hello!"));
        assertEquals("goodbye", TextProcessor.strip(",goodbye...!"));

        assertEquals("ro6", TextProcessor.strip("ro6"));
    }

    @Test
    public void testRstrip() {
        assertEquals("", TextProcessor.rstrip(""));

        // No Symbols
        assertEquals("hello", TextProcessor.rstrip("hello"));

        // Words with trailing symbols
        assertEquals("hello", TextProcessor.rstrip("hello!"));
        assertEquals("because", TextProcessor.rstrip("because....!"));

        // Only Symbols
        assertEquals("", TextProcessor.rstrip(".#@@#&*^@"));

        // Words with trailing digits
        assertEquals("hello89", TextProcessor.rstrip("hello89"));
    }

    @Test
    public void testLstrip() {
        assertEquals("", TextProcessor.lstrip(""));

        // No Symbols
        assertEquals("hello", TextProcessor.lstrip("hello"));

        // Words with trailing symbols
        assertEquals("hello", TextProcessor.lstrip("...hello"));
        assertEquals("hello", TextProcessor.lstrip("@hello"));

        // Only Symbols
        assertEquals("", TextProcessor.lstrip(".#@@#&*^@"));

        // Words with trailing digits
        assertEquals("hello89", TextProcessor.lstrip("hello89"));
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
