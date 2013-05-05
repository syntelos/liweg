package moops;

import java.io.IOException;
import java.io.DataOutputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;

/**
 * <h3>Instruction stream format</h3>
 * 
 * Each component of the instruction stream is a byte, with the
 * exception of the data-value, which is one to eight bytes as defined
 * by the data-type.
 * 
 * <pre>
 * op-code ( stream-parameter data-type data-value )*
 * </pre>
 */
public class Stream
    extends Object
{
    /*
     * One of operator or synthetic is required
     */
    protected final Op operator;

    protected final Parameter.Value[] parameters;

    protected Stream prev, next;
    /*
     * Numbered from zero
     */
    private int pc;



    protected Stream(){
        super();
        this.operator = null;
        this.parameters = null;
    }
    public Stream(Stream p, Op op, Parameter.Value[] params){
        super();
        this.operator = op;
        this.parameters = params;
        this.prev = p;
        p.next = this;
    }


    public int getPC(){

        return this.pc;
    }
    public boolean setPC(int pc){
        if (0 > this.pc){
            this.pc = pc;
            return true;
        }
        else
            return false;
    }
    /**
     * The target of an operation is the type of the destination
     * operand, i.e. the semantic write-store target.
     */
    public Reference target(){
        return this.parameter(0);
    }
    public boolean hasParameter(int idx){
        return (-1 < idx && idx < this.parameters.length);
    }
    public <T> T parameter(int idx){
        return (T)this.parameters[idx];
    }
    public void destroy(){
        this.prev = null;
        Stream next = this.next;
        if (null != next){
            this.next = null;
            if (next != this)
                next.destroy();
        }
    }
    public boolean isHead(){
        return (null == this.operator && null == this.parameters);
    }
    public void writeAS(PrintWriter out)
        throws IOException
    {
        if (null != this.operator){

            out.printf(" %s %s%n",this.operator.toAS(),this.operator.toAS(this.parameters));
        }

        if (null != this.next && this.next != this){

            this.next.writeAS(out);
        }
    }
    public void writeVM(DataOutputStream out)
        throws IOException
    {
        if (null != this.operator){

            out.write(this.operator.opcode);
        }

        if (null != this.next && this.next != this){

            this.next.writeVM(out);
        }
    }
}
