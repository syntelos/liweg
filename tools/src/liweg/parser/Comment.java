/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg.parser;

import jauk.Pattern;

/**
 *
 */
public class Comment
    extends Expression
{
    public final static Pattern PATTERN = new jauk.Re("<CComment>");

    public Comment(Expression p, int lno, String s){
        super(p,lno);
        this.setText(s);
    }
}
