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
    /**
     * Register map created during assembly
     */
    private final Map<Reference,Register> registers = new Map();
    /**
     * Instruction stream created during assembly
     */
    private final Map<Reference,Stream> assembly = new Map();


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
            Reference label = new Reference(Label(expr));
            return new Stream(p,s,(new Object[]{label}));
        }
        case Expr:{
            String[] tokens = Asm.Split(expr," \t");

            if (null != tokens && 0 < tokens.length){
                final String tokop = tokens[0];
                final Op operator = Op.For(tokop);

                if (null != operator){

                    Object[] parameters = operator.synthesize(this,tokens,s,file,lno,src);

                    return new Stream(p,operator,parameters);
                }
                else {

                    if ("break".equalsIgnoreCase(tokop)){

                        if (1 < tokens.length){
                            Reference label = new Reference(Label(tokens[1]));

                            return new Stream(p,Synthetic.Break,(new Object[]{label}));
                        }
                        else {
                            return new Stream(p,Synthetic.Break,(new Object[0]));
                        }
                    }
                    else
                        throw new IllegalStateException(String.format("%s:%d: unknown op '%s' in: %s",file,lno,tokop,src));
                }
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
    public Register registerFor(Reference reference){

        return this.registers.get(reference);
    }
    public OpType typeOf(Reference reference){

        Register dstr = this.registerFor(reference);

        if (null != dstr)
            return dstr.type;
        else
            throw new IllegalStateException(reference.label);
    }
    public Register var(Reference name, OpType type){

        Register r = this.registerFor(name);

        if (null == r ||
            (!r.equals(name,type)))
            {
                r = new Register(name,type);
                this.registers.put(name,r);
            }
        return r;
    }
    public Reference assemble(int pc, Stream op){

        Reference r = new Reference(pc);

        this.assembly.put(r,op);

        return r;
    }
    /**
     * Allocate variable table by name, resolve lables, collapse
     * values and types, validate code blocks.
     */
    public void assemble(){

        this.registers.clear();
        this.assembly.clear(); 

        int pc = 0;

        for (Stream s = this.next; null != s; s = s.next, pc += 1){

            if (s.isSynthetic()){
                switch(s.synthetic){
                case Comment:
                    /////////////////////////////////////
                    /////////////////////////////////////
                    break;
                case Label:
                    /////////////////////////////////////
                    /////////////////////////////////////
                    break;
                case Break:
                    /////////////////////////////////////
                    /////////////////////////////////////
                    break;
                default:
                    throw new IllegalStateException(s.synthetic.name());
                }
            }
            else {
                switch(s.operator){
                case VAR:{
                    Reference name = s.parameter(0); // type coersion
                    OpType type = s.parameter(1);
                    this.var(name,type);
                    this.assemble(pc,s);
                    break;
                }
                case STRUCT:{
                    Reference name = s.parameter(0);
                    //Structure type = s.parameter(1);
                    //this.var(name,type);
                    //this.assemble(pc,s);
                    break;
                }
                case ASSIGNV:{
                    Reference dst = s.parameter(0);
                    Constant value = s.constant(1,this.typeOf(dst));

                    this.assemble(pc,s);
                    break;
                }
                case ASSIGNR:{
                    Reference dst = s.parameter(0);
                    Reference src = s.parameter(1);

                    this.assemble(pc,s);
                    break;
                }
                case ADDV:{
                    Reference dst = s.parameter(0);
                    Reference a = s.parameter(1);
                    Constant value = s.constant(2,this.typeOf(dst));

                    this.assemble(pc,s);
                    break;
                }
                case ADDR:{
                    Reference dst = s.parameter(0);
                    Reference a = s.parameter(1);
                    Reference b = s.parameter(2);

                    this.assemble(pc,s);
                    break;
                }
                case SUBV:{
                    Reference dst = s.parameter(0);
                    Reference a = s.parameter(1);
                    Constant value = s.constant(2,this.typeOf(dst));

                    this.assemble(pc,s);
                    break;
                }
                case SUBR:{
                    Reference dst = s.parameter(0);
                    Reference a = s.parameter(1);
                    Reference b = s.parameter(2);

                    this.assemble(pc,s);
                    break;
                }
                case MULV:{
                    Reference dst = s.parameter(0);
                    Reference a = s.parameter(1);
                    Constant value = s.constant(2,this.typeOf(dst));

                    this.assemble(pc,s);
                    break;
                }
                case MULR:{
                    Reference dst = s.parameter(0);
                    Reference a = s.parameter(1);
                    Reference b = s.parameter(2);

                    this.assemble(pc,s);
                    break;
                }
                case DIVV:{
                    Reference dst = s.parameter(0);
                    Reference a = s.parameter(1);
                    Constant value = s.constant(2,this.typeOf(dst));

                    this.assemble(pc,s);
                    break;
                }
                case DIVR:{
                    Reference dst = s.parameter(0);
                    Reference a = s.parameter(1);
                    Reference b = s.parameter(2);

                    this.assemble(pc,s);
                    break;
                }
                case BLITV:{
                    Number dst = s.parameter(0);
                    Reference a = s.parameter(1);
                    Reference b = s.parameter(2);
                    Reference c = s.parameter(3);

                    this.assemble(pc,s);
                    break;
                }
                case BLITR:{
                    Reference dst = s.parameter(0);
                    Reference a = s.parameter(1);
                    Reference b = s.parameter(2);
                    Reference c = s.parameter(3);

                    this.assemble(pc,s);
                    break;
                }
                case SHOWV:{
                    Constant dst = s.constant(0,OpType.FRBX);

                    this.assemble(pc,s);
                    break;
                }
                case SHOWR:{
                    Reference dst = s.parameter(0);

                    this.assemble(pc,s);
                    break;
                }
                case WAITV:{
                    Constant dst = s.constant(0,OpType.MS);

                    this.assemble(pc,s);
                    break;
                }
                case WAITR:{
                    Reference dst = s.parameter(0);

                    this.assemble(pc,s);
                    break;
                }
                case RUNV:{
                    Constant dst = s.constant(0,OpType.VMPX);

                    this.assemble(pc,s);
                    break;
                }
                case RUNR:{
                    Reference dst = s.parameter(0);

                    this.assemble(pc,s);
                    break;
                }
                case GOTO:{
                    Reference label = s.parameter(0);

                    this.assemble(pc,s);
                    break;
                }
                case IF:{
                    Reference cond = s.parameter(0);

                    this.assemble(pc,s);
                    break;
                }
                case IFCV:{
                    Reference ref = s.parameter(0);
                    BoolOp op = s.parameter(1);
                    Constant value = s.constant(2,this.typeOf(ref));

                    Register reg = this.registerFor(ref);

                    this.assemble(pc,s);
                    break;
                }
                case IFCR:{
                    Reference a = s.parameter(0);
                    BoolOp op = s.parameter(1);
                    Reference b = s.parameter(2);

                    Register ra = this.registerFor(a);
                    Register rb = this.registerFor(b);

                    this.assemble(pc,s);
                    break;
                }
                case IFRV:{
                    Reference ref = s.parameter(0);
                    RelOp op = s.parameter(1);
                    Constant value = s.constant(2,this.typeOf(ref));

                    Register reg = this.registerFor(ref);

                    this.assemble(pc,s);
                    break;
                }
                case IFRR:{
                    Reference a = s.parameter(0);
                    RelOp op = s.parameter(1);
                    Reference b = s.parameter(2);

                    Register ra = this.registerFor(a);
                    Register rb = this.registerFor(b);

                    this.assemble(pc,s);
                    break;
                }
                case ELSE:{

                    this.assemble(pc,s);
                    break;
                }
                case END:{

                    this.assemble(pc,s);
                    break;
                }
                case FORCV:{
                    Reference ref = s.parameter(0);
                    BoolOp op = s.parameter(1);
                    Constant value = s.constant(2,this.typeOf(ref));

                    Register reg = this.registerFor(ref);

                    this.assemble(pc,s);
                    break;
                }
                case FORCR:{
                    Reference a = s.parameter(0);
                    BoolOp op = s.parameter(1);
                    Reference b = s.parameter(2);

                    Register ra = this.registerFor(a);
                    Register rb = this.registerFor(b);

                    this.assemble(pc,s);
                    break;
                }
                case FORRV:{
                    Reference ref = s.parameter(0);
                    RelOp op = s.parameter(1);
                    Constant value = s.constant(2,this.typeOf(ref));

                    Register reg = this.registerFor(ref);

                    

                    this.assemble(pc,s);
                    break;
                }
                case FORRR:{
                    Reference a = s.parameter(0);
                    RelOp op = s.parameter(1);
                    Reference b = s.parameter(2);

                    Register ra = this.registerFor(a);
                    Register rb = this.registerFor(b);

                    

                    this.assemble(pc,s);
                    break;
                }
                case WHILE:{
                    Reference cond = s.parameter(0);

                    OpType tgt = this.typeOf(cond);

                    this.assemble(pc,s);
                    break;
                }
                case WHILECV:{
                    Reference ref = s.parameter(0);
                    BoolOp op = s.parameter(1);
                    Constant value = s.constant(2,this.typeOf(ref));

                    Register reg = this.registerFor(ref);

                    this.assemble(pc,s);
                    break;
                }
                case WHILECR:{
                    Reference a = s.parameter(0);
                    BoolOp op = s.parameter(1);
                    Reference b = s.parameter(2);

                    Register ra = this.registerFor(a);
                    Register rb = this.registerFor(b);

                    this.assemble(pc,s);
                    break;
                }
                case WHILERV:{
                    Reference ref = s.parameter(0);
                    RelOp op = s.parameter(1);
                    Constant value = s.constant(2,this.typeOf(ref));

                    Register reg = this.registerFor(ref);

                    this.assemble(pc,s);
                    break;
                }
                case WHILERR:{
                    Reference a = s.parameter(0);
                    RelOp op = s.parameter(1);
                    Reference b = s.parameter(2);

                    Register ra = this.registerFor(a);
                    Register rb = this.registerFor(b);

                    this.assemble(pc,s);
                    break;
                }
                default:
                    throw new IllegalStateException(s.operator.name());
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
