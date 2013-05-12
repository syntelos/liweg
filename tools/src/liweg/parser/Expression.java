/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg.parser;

import jauk.Pattern;
import jauk.Scanner;

import java.io.IOException;
import java.io.LineNumberReader;

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
    public Expression(SourceFile src, Scanner in)
        throws IOException
    {
        super();
        this.p = null;
        this.linenumber = in.currentLine();

        String expr;
        int lno;

        while (true){

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

                    throw new Syntax(this,in,"Unrecognized input");
                }
            }
        }
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
    public final String trace(){

        Expression child = this;
        Expression parent = this.p;
        if (null != parent){
            StringBuilder string = new StringBuilder();

            while (null != parent && (!parent.contains(child))){
                final int d = child.depth();

                string.append(String.format("trace depth %02d ------------------ %02d ------------------ %02d ------------------%n",d,d,d));

                string.append(child.toString(d));

                child = parent;
                parent = parent.getParent();
            }
            return string.toString();
        }
        else
            return this.toString();
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

        return this.toString(1);
    }
    public String toString(int indent){
        StringBuilder string = new StringBuilder();

        Indent(indent,string);

        if (null != this.text){

            string.append(String.format("%s:%d:\t%s%n",this.getName(),this.linenumber,this.getText()));
        }
        else {

            string.append(String.format("%s:%d:%n",this.getName(),this.linenumber));
        }


        for (Expression child: this){

            string.append(child.toString(indent+1));
        }
        return string.toString();
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
    public final static void Indent(int indent, StringBuilder string){
        for (int cc = 0; cc < indent; cc++){
            string.append('\t');
        }
    }

}
