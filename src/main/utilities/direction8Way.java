package utilities;

public class direction8Way {
    private int value;

    public direction8Way(){
        this.value = (int) Math.floor(8 * Math.random());
    }
    public direction8Way(int value){ this.value = value; }

    public int get() {          // returning a copy, as to disable any sort of changes
        return this.value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }

    public int turn(int amount){
        this.value = ( this.value + amount ) % 8;
        return this.value;          // returns a direction after the turn
    }

    public Vector2d toUnitVector(){
        switch(this.value){
            case 0: return new Vector2d(0,1);
            case 1: return new Vector2d(1,1);
            case 2: return new Vector2d(1,0);
            case 3: return new Vector2d(1,-1);
            case 4: return new Vector2d(0,-1);
            case 5: return new Vector2d(-1,-1);
            case 6: return new Vector2d(-1,0);
            case 7: return new Vector2d(-1,1);
            default: throw new IllegalArgumentException(this.value + " is not a proper direction. Error in 'direction.java'");
        }
    }
}
