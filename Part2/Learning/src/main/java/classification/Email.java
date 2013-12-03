package classification;

import java.util.List;

public class Email {

    public enum Class {
        Spam, Ham, Unknown
    }

    private Class _class = Class.Unknown;
    private List<String> words;

    public Email(List<String> words) {
        this.words = words;
    }

    /**
     * Get the list of all words contained in the content of the mail.
     */
    public List<String> getWords() {
        return words;
    }

    /**
     * Is this email Ham or Spam or does it need to be classified?
     *
     * CClass = classification class
     */
    public Class getCClass() {
        return _class;
    }
    
    /**
     * Set if the mail is Spam or Ham
     */
    public void setCClass(Class _class) {
        this._class = _class;
    }

}
