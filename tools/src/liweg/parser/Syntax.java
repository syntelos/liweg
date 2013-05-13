/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg.parser;

import jauk.Scanner;

/**
 * Syntax error.  Failure following a related scanner match,
 * impossible to continue parsing.
 * 
 * @author jdp
 */
public final class Syntax
    extends java.lang.IllegalStateException
{

    public final Expression expression;


    public Syntax(Expression expression, Scanner scanner, String msg){
        super(msg+"\n\t"+scanner.currentLine()+": "+scanner.nextCapture()+"\n"+expression.toString());
        this.expression = expression;
    }

}
