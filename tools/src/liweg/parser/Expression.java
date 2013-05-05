/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg.parser;

/**
 *
 */
public abstract class Expression
    extends Object
{

    public final Expression p;
    public final String expression;

    public Expression(Expression p, String expression){
        super();
        this.p = p;
        this.expression = expression;
    }
}
