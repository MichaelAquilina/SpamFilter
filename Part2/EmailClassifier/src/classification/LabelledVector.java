package classification;

public class LabelledVector {
    private EmailClass emailClass;
    private int[] vector;
    
    public EmailClass getEmailClass() {
        return emailClass;
    }

    public void setEmailClass(EmailClass emailClass) {
        this.emailClass = emailClass;
    }

    public int[] getVector() {
        return vector;
    }

    public void setVector(int[] vector) {
        this.vector = vector;
    }
    
    
}
