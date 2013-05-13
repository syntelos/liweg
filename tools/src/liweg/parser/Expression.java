/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg.parser;

import jauk.Pattern;
import jauk.Scanner;

import java.io.IOException;
import java.io.PrintStream;

/**
 *
 */
public class Expression
    extends lxl.ArrayList<Expression>
{

    public final Expression p;

    public final int linenumber;

    protected String name, capture, text;


    public Expression(Expression p, int lno, Expression... subexpr){
        super();
        if (null != p){
            this.p = p;
            this.linenumber = lno;

            if (null != subexpr){
                for (Expression sx : subexpr){
                    this.add(sx);
                }
            }
        }
        else
            throw new IllegalArgumentException();
    }
    /*
     */
    public Expression(SourceFile src, Scanner in)
        throws IOException
    {
        super();
        this.p = null;
        this.linenumber = in.currentLine();
        /*
         */
        this.parse(src,in);
    }


    /*
     */
    public Expression parse(SourceFile src, Scanner in)
        throws IOException
    {
        String expr;
        int lno;

        while (in.isNotEmpty()){

            expr = in.next(Comment.PATTERN);
            if (null != expr){
                lno = in.currentLine();

                this.add(new Comment(this,lno,expr));
            }
            else {
                expr = in.next(Define.PATTERN);
                if (null != expr){
                    lno = in.currentLine();

                    this.add(new Define(this,lno,expr));
                }
                else {
                    expr = in.next(Block.PATTERN);
                    if (null != expr){
                        lno = in.currentLine();

                        this.add(new Block(this,lno,expr));
                    }
                    else {

                        throw new Syntax(this,in,"Unrecognized input");
                    }
                }
            }
        }
        return this;
    }
    public final int getLinenumber(){

        return this.linenumber;
    }
    /**
     * Set once capture, set many trimmed text.
     */
    protected void setText(String text){
        if (null == this.capture)
            this.capture = text;

        this.text = text.trim();
    }
    public final boolean hasCapture(){
        return (null != this.capture);
    }
    public final String getCapture(){
        return this.capture;
    }
    public final boolean hasText(){
        return (null != this.text);
    }
    public final String getText(){
        return this.text;
    }
    public final String getName(){
        String name = this.name;
        if (null == name){
            name = NameOf(this.getClass());
            this.name = name;
        }
        return name;
    }
    public final boolean hasParent(){
        return (null != this.p);
    }
    public final Expression getParent(){
        return this.p;
    }
    public final void trace(PrintStream out){

        Trace(this,out);

    }
    public final int depth(){
        int d = 0;
        Expression p = this.p;
        while (null != p){
            d += 1;
            p = p.p;
        }
        return d;
    }
    public final String toString(){
        if (null != this.text){

            return String.format("%s:%d:\t%s",this.getName(),this.linenumber,this.getText());
        }
        else {

            return String.format("%s:%d:",this.getName(),this.linenumber);
        }
    }
    @Override
    public final int indexOf(Expression child){
        for (int cc = 0, count = this.size(); cc < count; cc++){
            if (child == this.get(cc))
                return cc;
        }
        return -1;
    }


    public final static String NameOf(Class<? extends Expression> expressionclass){

        return expressionclass.getSimpleName();
    }
    public final static void Indent(int indent, PrintStream out){

        for (int cc = 0; cc < indent; cc++){
            out.print('\t');
        }
    }
    public final static void Trace(Expression child, PrintStream out){

        final int d = child.depth();

        out.printf("trace depth %02d ------------------ %02d ------------------ %02d ------------------%n",d,d,d);

        Indent(d,out);

        out.println(child.toString());

        for (Expression gchild: child){

            Trace(gchild,out);
        }
    }
}
