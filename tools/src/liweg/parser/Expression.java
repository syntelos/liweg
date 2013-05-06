/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg.parser;

import java.io.IOException;
import java.io.LineNumberReader;

/**
 *
 */
public class Expression
    extends lxl.ArrayList
{

    public final Expression p;


    public Expression(Expression p, String expression){
        super();
        this.p = p;
        this.add(expression);
    }
    public Expression(SourceFile src, LineNumberReader in)
        throws IOException
    {
        super();
        this.p = null;
        String line;
        while (null != (line = in.readLine())){
            ///////////////////////////////////////
            /*
             * use regular expressions to sort input into subclass
             * constructors
             */
            ///////////////////////////////////////
            this.add(line);
        }
    }
}
