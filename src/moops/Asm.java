package moops;

import lxl.Map;

import java.io.File;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * 
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

    private Map<Reference,Register> registers = new Map();


    public Asm(File file, LineNumberReader src)
        throws IOException
    {
        super();
        if (null != src){

            int cmx;

            for (String line = src.readLine(); null != line; line = src.readLine()){
                int lno = src.getLineNumber();
                if (0 < line.length()){

                    switch(line.charAt(0)){
                    case ' ':
                    case '\t':
                        cmx = line.indexOf(';');
                        if (0 < cmx){
                            String input = line.substring(0,cmx).trim();
                            String comment = line.substring(cmx).trim();
                            if (0 < input.length()){

                                synthesis = this.synthesize(synthesis,input,Synthetic.Expr,file,lno,line);

                                synthesis = this.synthesize(synthesis,comment,Synthetic.Comment,file,lno,line);
                            }
                        }
                        else {
                            synthesis = this.synthesize(synthesis,line,Synthetic.Expr,file,lno,line);
                        }
                        break;

                    default:
                        cmx = line.indexOf(';');
                        if (-1 < cmx){
                            if (0 < cmx){
                                String input = line.substring(0,cmx).trim();
                                String comment = line.substring(cmx).trim();
                                if (0 < input.length()){

                                    synthesis = this.synthesize(synthesis,input,Synthetic.Label,file,lno,line);

                                    synthesis = this.synthesize(synthesis,comment,Synthetic.Comment,file,lno,line);
                                }
                            }
                            else
                                synthesis = this.synthesize(synthesis,line,Synthetic.Comment,file,lno,line);
                        }
                        else {
                            synthesis = this.synthesize(synthesis,line,Synthetic.Label,file,lno,line);
                        }
                        break;
                    }
                }
            }
        }
        else
            throw new IllegalArgumentException("Missing operator");
    }


    private final Stream synthesize(Stream p, String expr, Synthetic s, File file, int lno, String src){
        switch(s){
        case Comment:{
            return new Stream(p,s,(new Object[]{expr}));
        }
        case Label:{
            String label = Label(expr);
            return new Stream(p,s,(new Object[]{label}));
        }
        case Expr:{
            String[] tokens = Asm.Split(expr," \t");
            Op operator = Op.For(tokens[0]);
            if (null == operator){

                if ("break".equalsIgnoreCase(tokens[0])){

                    String label = Label(tokens[1]);
                    return new Stream(p,Synthetic.Break,(new Object[]{label}));
                }
                else
                    throw new IllegalStateException(String.format("%s:%d: unknown op '%s' in: %s",file,lno,tokens[0],src));
            }
            else {
                Object[] parameters = operator.synthesize(this,tokens,s,file,lno,src);
                return new Stream(p,operator,parameters);
            }
        }
        default:
            throw new IllegalArgumentException(s.name());
        }
    }
    /**
     * Called from OpArg synthesize in case VAL to use the type of the
     * target of the current operation.
     */
    public Object synthesize(Op op, int p, String term, Synthetic s, File file, int lno, String src){

        return this.typeOfTargetOf().valueOf(term);
    }
    /**
     * Called by synthesize called from OpArg synthesize to determine
     * the type of the target of this operation.
     * 
     * The type of an operator value operand ({@link OpArg#VAL}) is
     * the type of the destination operand.  See {@link
     * Stream#target()}.
     */
    public OpType typeOfTargetOf(){

        return this.typeOf(this.targetOf());
    }
    public Reference targetOf(){

        return this.synthesis.target();
    }
    public OpType typeOf(Reference reference){

        Register dstr = this.get(reference);

        if (null != dstr)
            return dstr.type;
        else
            throw new IllegalStateException(reference.label);
    }
    public Register get(Reference reference){

        return this.registers.get(reference);
    }
    public Register create(Reference name, OpType type){

        Register r = this.registers.get(name);
        if (null == r){
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

        int pc = 0;

        for (Stream s = this.next; null != s; s = s.next, pc += 1){

            if (s.isSynthetic()){
                //////////////////////////////////////////////////
                //////////////////////////////////////////////////
                //////////////////////////////////////////////////
            }
            else {
                switch(s.operator){
                case VAR:{
                    Reference name = s.parameter(0); // type coersion
                    OpType type = s.parameter(1);
                    this.create(name,type);
                    break;
                }
                case STRUCT:
                    break;
                case ASSIGNV:
                case ASSIGNR:
                    break;
                case ADDV:
                case ADDR:
                    break;
                case SUBV:
                case SUBR:
                    break;
                case MULV:
                case MULR:
                    break;
                case DIVV:
                case DIVR:
                    break;
                case BLITV:
                case BLITR:
                case SHOWV:
                case SHOWR:
                    break;
                case WAITV:
                case WAITR:
                case RUNV:
                case RUNR:
                    break;
                case GOTO:
                case IF:
                case IFCV:
                case IFCR:
                case IFRV:
                case IFRR:
                case ELSE:
                case END:
                case FORCV:
                case FORCR:
                case FORRV:
                case FORRR:
                case WHILE:
                case WHILECV:
                case WHILECR:
                case WHILERV:
                case WHILERR:
                    break;
                }
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
