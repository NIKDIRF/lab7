package studygroup;

import java.io.Serializable;

/**
 * x-y координаты.
 */
public class Coordinates implements Serializable {
    private static final long serialVersionUID = 7681916492950815465L;
    private double x;
    private double y;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return x координата.
     */
    public double getX() {
        return x;
    }

    /**
     * @return y координата.
     */
    public double getY() {
        return y;
    }
}
