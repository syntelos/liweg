/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg;

public final class Register
    extends Object
    implements AS
{

    public final String label;

    public final DataType type;


    public Register(Reference ref, DataType type){
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
    public boolean equals(Reference name, DataType type){
        if (null == name || null == type)
            return false;
        else
            return (this.label.equals(name) &&
                    this.type == type);
    }
    public String toString(){

        return this.toAS();
    }
    public String toAS(){

        return this.type.toAS()+" "+this.label;
    }
}
