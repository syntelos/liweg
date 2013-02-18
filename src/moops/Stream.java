package moops;

import java.io.IOException;
import java.io.DataOutputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;

/**
 * 
 */
public class Stream {

    protected final Op operator;

    protected final Asm.Synthetic synthetic;

    protected final Object[] parameters;

    protected Stream prev, next;



    protected Stream(){
        super();
        this.operator = null;
        this.synthetic = null;
        this.parameters = null;
    }
    public Stream(Stream p, Asm.Synthetic s, Object[] params){
        super();
        this.operator = null;
        this.synthetic = s;
        this.parameters = params;
        this.prev = p;
    }
    public Stream(Stream p, Op op, Object[] params){
        super();
        this.operator = op;
        this.synthetic = null;
        this.parameters = params;
        this.prev = p;
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
    }
    public void writeVM(DataOutputStream out)
        throws IOException
    {
    }
}
