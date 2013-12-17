package weka;

import classification.EmailClass;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

public class WekaInstances extends Instances {
    private int dimensions;

    public static FastVector newVector(int dimensions) {
        FastVector attributes = new FastVector(dimensions);
        for (int i = 0; i < dimensions - 1; i++) {
            attributes.addElement(new Attribute("x" + Integer.toString(i)));
        }
        FastVector classes = new FastVector(2);
        classes.addElement(EmailClass.Spam.toString());
        classes.addElement(EmailClass.Ham.toString());
        attributes.addElement(new Attribute("class", classes));
        return attributes;
    }

    public WekaInstances(int dimensions) {
        super("", newVector(dimensions), dimensions);
        this.dimensions = dimensions;
    }

    public WekaInstance newInstance() {
        WekaInstance instance = new WekaInstance(dimensions);
        instance.setDataset(this);
        add(instance);
        m_Instances.addElement(instance);
        return instance;
    }
}
