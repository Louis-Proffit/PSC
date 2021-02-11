public class Vector {

    private double x;
    private double y;

    public Vector(){
        this.x = Math.random();
        this.y = Math.random();
    }

    public double distance(Vector vector){
        return Math.pow(Math.pow(vector.x - this.x, 2) + Math.pow(vector.y - this.y, 2), 0.5);
    }
    
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
}
