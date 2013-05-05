/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg;

import java.io.File;

/**
 * {@link Stream} instances of {@link Argument}
 */
public enum Parameter {
    FB,
    PROG,
    REG,
    TYPE,
    DATA,
    BOOP,
    RLOP;

    /**
     * An instance of an operator parameter found in the instruction
     * {@link Stream}.
     */
    public final static class Value
        extends Object
    {

        public final Parameter type;

        public final Object value;

        public Value(Argument type, Object value){
            super();
            if (null != type && null != value){
                this.type = type.parameter(value);
                this.value = value;
            }
            else
                throw new IllegalArgumentException();
        }
    }
}
