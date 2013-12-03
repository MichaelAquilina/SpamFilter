package classification;

public class LabelledVector {
    private EmailClass emailClass;
    private float[] vector;
    
    public EmailClass getEmailClass() {
        return emailClass;
    }

    public void setEmailClass(EmailClass emailClass) {
        this.emailClass = emailClass;
    }

    public float[] getVector() {
        return vector;
    }

    public void setVector(float[] vector) {
        this.vector = vector;
    }
    
    
}
