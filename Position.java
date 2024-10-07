public class Position implements Comparable<Position> {
    private int i;
    private int j;

    public Position(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    @Override
    public int compareTo(Position o) {
        int retVal = Double.compare(getI(), o.getI());
        if (retVal != 0) {
            return retVal;
        }
        return Double.compare(getI(), o.getJ());
    }
}
