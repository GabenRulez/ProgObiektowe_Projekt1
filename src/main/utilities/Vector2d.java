package utilities;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "(" + x + "," + y + ")";
    }

    public boolean precedes(Vector2d other){
        return (this.x <= other.x  &&  this.y <= other.y);
    }

    public boolean follows(Vector2d other){
        return (this.x >= other.x  &&  this.y >= other.y);
    }

    public Vector2d upperRight(Vector2d other){
        int temp_x = this.x;
        int temp_y = this.y;

        if (other.x > temp_x) temp_x = other.x;
        if (other.y > temp_y) temp_y = other.y;

        return new Vector2d(temp_x, temp_y);
    }

    public Vector2d lowerLeft(Vector2d other){
        int temp_x = this.x;
        int temp_y = this.y;

        if (other.x < temp_x) temp_x = other.x;
        if (other.y < temp_y) temp_y = other.y;

        return new Vector2d(temp_x, temp_y);
    }

    public Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other){
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public boolean equals(Object other){
        if (other == null) return false;
        if ( this.getClass() != other.getClass() ) return false;

        Vector2d otherVector = (Vector2d) other;

        return ( this.x == otherVector.x && this.y == otherVector.y );
    }

    public Vector2d opposite(){
        return new Vector2d(this.x*(-1), this.y*(-1));
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash += this.x * 29;
        hash += this.y * 17;
        return hash;
    }
}
