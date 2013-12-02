package classification;

import java.util.List;

public class Email {

    private List<String> words;

    public Email(List<String> words) {
        this.words = words;
    }

    public List<String> getWords() {
        return words;
    }
}
