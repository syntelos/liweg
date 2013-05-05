/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg;

import java.io.File;

/**
 * {@link Parameter} classes found in {@link Op} arguments.
 */
public enum Argument {

    FB(Parameter.FB),
    PROG(Parameter.PROG),
    REG(Parameter.REG_DATA,Parameter.REG_ARRAY,Parameter.REG_TABLE,Parameter.REG_STRUCT),

    TYPE(Parameter.TYPE),
    DATA(Parameter.DATA),
    CHSTR(Parameter.CHSTR),

    BOOP(Parameter.BOOP),
    RLOP(Parameter.RLOP),

    REGDATA(Parameter.REG_DATA,Parameter.REG_ARRAY,Parameter.REG_TABLE,Parameter.REG_STRUCT,Parameter.DATA);


    /**
     * {@link Parameters} available via this {@link Argument}.
     */
    public final Parameter[] params;

    private Argument(Parameter... params){
        this.params = params;
    }


    public Parameter parameter(Object value){
        switch(this){
        case FB:
            return Parameter.FB;
        case PROG:
            return Parameter.PROG;
        case REG:
            if (value instanceof Reference)
                return ((Reference)value).parameter();
            else
                throw new IllegalArgumentException(value.getClass().getName());

        case REGDATA:
            if (value instanceof Reference)
                return ((Reference)value).parameter();
            else if (value instanceof DataValue)
                return Parameter.DATA;
            else
                throw new IllegalArgumentException(value.getClass().getName());

        case TYPE:
            return Parameter.TYPE;
        case DATA:
            return Parameter.DATA;
        case BOOP:
            return Parameter.BOOP;
        case RLOP:
            return Parameter.RLOP;

        default:
            throw new IllegalStateException(this.name());
        }
    }
    /**
     * Produce parameter values for Stream instances
     * @return parameter values 
     */
    public Parameter.Value synthesize(String term){
        switch(this){
        case FB:
        case PROG:
        case REG:
            return new Parameter.Value(this, new Reference(this,term));
        case REGDATA:
            try {
                return new Parameter.Value(this, new DataValue(this,term));
            }
            catch (RuntimeException exc){

                return new Parameter.Value(this, new Reference(this,term));
            }
        case TYPE:
            return new Parameter.Value(this, DataType.For(term));
        case DATA:
            return new Parameter.Value(this, new DataValue(this,term));
        case BOOP:
            return new Parameter.Value(this, BoolOp.For(term));
        case RLOP:
            return new Parameter.Value(this, RelOp.For(term));

        default:
            throw new IllegalStateException(this.name());
        }
    }
}
