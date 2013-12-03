package classification;

public class LabelledVector {
    private EmailClass emailClass;
    private double[] vector;
    
    public EmailClass getEmailClass() {
        return emailClass;
    }

    public void setEmailClass(EmailClass emailClass) {
        this.emailClass = emailClass;
    }

    public double[] getVector() {
        return vector;
    }

    public void setVector(double[] vector) {
        this.vector = vector;
    }
    
    
}
