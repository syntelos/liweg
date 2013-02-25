package moops;

/**
 * Assembly language references within the instruction stream and to
 * registers are label strings.
 */
public final class Reference
    extends Object
    implements AS, Comparable<Reference>
{

    public final String label;

    public final int pc;


    public Reference(String label){
        super();
        this.label = label;
        this.pc = -1;
    }
    public Reference(int pc){
        super();
        this.label = null;
        this.pc = pc;
    }


    public int hashCode(){
        if (null != this.label)
            return this.label.hashCode();
        else
            return this.pc;
    }
    public boolean equals(Object that){
        if (this == that)
            return true;
        else if (null == that)
            return false;
        else
            return this.toString().equals(that.toString());
    }
    public int compareTo(Reference that){
        if (this == that)
            return 0;
        else if (null == that)
            return +1;
        else if (null != this.label && null != that.label)
            return this.label.compareTo(that.label);
        else if (null != that.label || this.pc < that.pc)
            return -1;
        else if (this.pc > that.pc)
            return +1;
        else
            return 0;
    }
    public String toString(){
        if (null != this.label)
            return this.label;
        else
            return String.format("ref-pc:%d",this.pc);
    }
    public String toAS(){

        return this.toString();
    }
}
