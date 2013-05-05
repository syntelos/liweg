package moops;

import lxl.Map;

import java.io.File;
import java.io.IOException;
import java.io.DataOutputStream;
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


    public Asm(File file, LineNumberReader src)
        throws IOException
    {
        super();
        if (null != src){

            for (String line = src.readLine(); null != line; line = src.readLine()){

                int lno = src.getLineNumber();

                if (0 < line.length()){

                    String[] tokens = Asm.Split(line," \t");

                    if (null != tokens && 0 < tokens.length){

                        final String tokop = tokens[0];

                        final Op operator = Op.For(tokop);

                        if (null != operator){

                            Parameter.Value[] parameters = operator.synthesize(this,tokens,file,lno,line);

                            this.synthesis = new Stream(this.synthesis,operator,parameters);
                        }
                        else {

                            throw new IllegalStateException(String.format("%s:%d: unknown op '%s' in: %s",file,lno,tokop,line));
                        }
                    }
                }
            }
        }
        else
            throw new IllegalArgumentException("Missing operator");
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
    public void assemble(){

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
    }
    public void writeAS(PrintWriter out)
        throws IOException
    {
        if (null != this.next && this.next != this){
            this.next.writeAS(out);
        }
    }
    public void writeVM(DataOutputStream out)
        throws IOException
    {
        if (null != this.next && this.next != this){
            this.next.writeVM(out);
        }
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
