package moops;

public final class Register
    extends Object
    implements AS
{

    public final String label;

    public final OpType type;


    public Register(Reference ref, OpType type){
        super();
        this.label = ref.label;
        this.type = type;
    }


    public int hashCode(){
        return this.label.hashCode()^(type.code<<24);
    }
    public boolean equals(Object that){
        if (this == that)
            return true;
        else if (that instanceof Register)
            return this.equals( (Register)that);
        else
            return false;
    }
    public boolean equals(Register that){
        if (this == that)
            return true;
        else if (null == that)
            return false;
        else
            return (this.label.equals(that.label) &&
                    this.type == that.type);
    }
    public String toString(){

        return this.toAS();
    }
    public String toAS(){

        return this.type.toAS()+" "+this.label;
    }
}
