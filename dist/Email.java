package classification;

import java.io.File;
import java.util.List;

public class Email {

    private File emailFile;
    private EmailClass _emailClass = EmailClass.Unknown;
    private final List<String> words;

    public Email(File emailFile, List<String> words) {
        this.emailFile = emailFile;
        this.words = words;
    }

    public File getEmailFile() {
        return emailFile;
    }

    /**
     * Get the list of all words contained in the content of the mail.
     * @return
     */
    public List<String> getWords() {
        return words;
    }

    /**
     * Is this email Ham or Spam or does it need to be classified?
     *
     * EmailClass = classification class
     * @return
     */
    public EmailClass getEmailClass() {
        return _emailClass;
    }
    
    /**
     * Set if the mail is Spam or Ham
     * @param emailClass
     */
    public void setEmailClass(EmailClass emailClass) {
        this._emailClass = emailClass;
    }

}
