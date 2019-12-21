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

    public Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public boolean equals(Object other){
        if (other == null) return false;
        if ( this.getClass() != other.getClass() ) return false;

        Vector2d otherVector = (Vector2d) other;

        return ( this.x == otherVector.x && this.y == otherVector.y );
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash += this.x * 29;
        hash += this.y * 17;
        return hash;
    }
}
