/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg;

import liweg.parser.Expression;

import lxl.Map;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Simple asm list: no labels or comments; accepts empty lines.
 */
public class Asm
    extends Stream
{
    /**
     * Current operation
     * @see Stream
     * @see Op
     * @see OpArg
     */
    private Stream synthesis = this;
    /**
     * Register map created during assembly
     */
    private final Map<Reference,Register> registers = new Map();


    public Asm(SourceFileLiweg file)
        throws IOException
    {
        super(Op.REGISTERS,new Parameter.Value[1]);

        Expression src = file.read();
        if (null != src){
            for (Object sexpr: src){

                if (sexpr instanceof Expression){

                }
            }
        }
    }


    /**
     * Called by synthesize called from OpArg synthesize to determine
     * the type of the target of this operation.
     * 
     * The type of an operator value operand ({@link OpArg#VAL}) is
     * the type of the destination operand.  See {@link
     * Stream#target()}.
     */
    public DataType typeOfTargetOf(){

        return this.typeOf(this.targetOf());
    }
    public Reference targetOf(){

        return this.synthesis.target();
    }
    public Register registerFor(Reference reference){

        return this.registers.get(reference);
    }
    public DataType typeOf(Reference reference){

        Register dstr = this.registerFor(reference);

        if (null != dstr)
            return dstr.type;
        else
            throw new IllegalStateException(reference.label);
    }
    public Register reg(Reference name, DataType type){

        Register r = this.registerFor(name);

        if (null == r ||
            (!r.equals(name,type)))
            {
                r = new Register(name,type);
                this.registers.put(name,r);
            }
        return r;
    }
    /**
     * Allocate variable table by name, resolve lables, collapse
     * values and types, validate code blocks.
     */
    public Asm assemble(){

        this.registers.clear();


        int pc = 0;

        for (this.synthesis = this; null != this.synthesis; this.synthesis = this.synthesis.next, pc += 1){

            if (this.synthesis.setPC(pc)){

                ////////////////////////////////////////////
                ////////////////////////////////////////////
                ////////////////////////////////////////////
                ////////////////////////////////////////////
            }
        }
        return this;
    }


    private final static String Label(String label){
        int x = label.indexOf(':'); 
        if (0 < x)
            label = label.substring(0,x);
        label = label.trim();
        if (0 < label.length())
            return label;
        else
            return null;
    }
    protected final static String[] Split(String src, String sep){
        final StringTokenizer strtok = new StringTokenizer(src,sep);
        final int len = strtok.countTokens();
        if (0 < len){
            String[] re = new String[len];
            for (int cc = 0; cc < len; cc++){
                re[cc] = strtok.nextToken();
            }
            return re;
        }
        else
            return null;
    }
}
