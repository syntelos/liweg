package moops;

import java.io.IOException;
import java.io.DataOutputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;

/**
 * Lined list of assembly language 
 */
public class Stream {

    protected final Op operator;

    protected final Synthetic synthetic;

    protected final Object[] parameters;

    protected Stream prev, next;



    protected Stream(){
        super();
        this.operator = null;
        this.synthetic = null;
        this.parameters = null;
    }
    public Stream(Stream p, Synthetic s, Object[] params){
        super();
        this.operator = null;
        this.synthetic = s;
        this.parameters = params;
        this.prev = p;
        p.next = this;
    }
    public Stream(Stream p, Op op, Object[] params){
        super();
        this.operator = op;
        this.synthetic = null;
        this.parameters = params;
        this.prev = p;
        p.next = this;
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
    public Constant constant(int idx, OpType type){
        return (Constant)(this.parameters[idx] = new Constant( (Number)this.parameters[idx],type));
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
    public boolean isSynthetic(){
        return (null == this.operator && null != this.synthetic);
    }
    public boolean isHead(){
        return (null == this.operator && null == this.parameters && null == this.synthetic);
    }
    public void writeAS(PrintWriter out)
        throws IOException
    {
        if (null != this.operator){
            out.printf(" %s %s%n",this.operator.toAS(),this.operator.toAS(this.parameters));
        }
        else if (null != this.synthetic){
            switch(this.synthetic){
            case Comment:
                out.printf("%s%n",this.parameter(0));
                break;
            case Label:
                out.printf("%s%n",this.parameter(0));
                break;
            case Break:
                if (this.hasParameter(0))
                    out.printf(" break %s%n",this.parameter(0));
                else
                    out.printf(" break%n");
                break;
            }
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
