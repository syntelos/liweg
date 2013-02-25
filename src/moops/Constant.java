package moops;

public class Constant
    extends Number
{

    public final Number value;
    public final OpType type;


    public Constant(Number value, OpType type){
        super();
        this.value = value;
        this.type = type;
    }


    public int intValue(){
        return this.value.intValue();
    }
    public long longValue(){
        return this.value.longValue();
    }
    public float floatValue(){
        return this.value.floatValue();
    }
    public double doubleValue(){
        return this.value.doubleValue();
    }

}
