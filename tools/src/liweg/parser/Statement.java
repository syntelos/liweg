/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg.parser;

import jauk.Pattern;

/**
 *
 */
public class Statement
    extends Expression
{
    public final static Pattern PATTERN = new jauk.Re("<Line>");

    public Statement(Expression p, int lno, String s){
        super(p,lno);
        this.setText(s);
    }
}
