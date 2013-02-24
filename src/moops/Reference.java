package moops;

public final class Reference
    extends Object
    implements AS, Comparable<Reference>
{

    public final String label;


    public Reference(String label){
        super();
        this.label = label;
    }


    public int hashCode(){
        return this.label.hashCode();
    }
    public boolean equals(Object that){
        if (this == that)
            return true;
        else if (null == that)
            return false;
        else
            return this.label.equals(that.toString());
    }
    public int compareTo(Reference that){
        if (this == that)
            return 0;
        else if (null == that)
            return +1;
        else
            return this.label.compareTo(that.label);
    }
    public String toString(){
        return this.label;
    }
    public String toAS(){
        return this.label;
    }
}
