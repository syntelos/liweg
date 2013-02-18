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
    public enum Synthetic {
        Comment, Label, Break, Expr;
    }


    public Asm(File file, LineNumberReader src)
        throws IOException
    {
        super();
        if (null != src){

            Stream current = this;
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

                                current = this.synthesize(current,input,Synthetic.Expr,file,lno,line);

                                current = this.synthesize(current,comment,Synthetic.Comment,file,lno,line);
                            }
                        }
                        else {
                            current = this.synthesize(current,line,Synthetic.Expr,file,lno,line);
                        }
                        break;

                    default:
                        cmx = line.indexOf(';');
                        if (-1 < cmx){
                            if (0 < cmx){
                                String input = line.substring(0,cmx).trim();
                                String comment = line.substring(cmx).trim();
                                if (0 < input.length()){

                                    current = this.synthesize(current,input,Synthetic.Label,file,lno,line);

                                    current = this.synthesize(current,comment,Synthetic.Comment,file,lno,line);
                                }
                            }
                            else
                                current = this.synthesize(current,line,Synthetic.Comment,file,lno,line);
                        }
                        else {
                            current = this.synthesize(current,line,Synthetic.Label,file,lno,line);
                        }
                        break;
                    }
                }
            }
        }
        else
            throw new IllegalArgumentException("Missing operator");
    }


    private final Stream synthesize(Stream p, String expr, Asm.Synthetic s, File file, int lno, String src){
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
                    return new Stream(p,Asm.Synthetic.Break,(new Object[]{label}));
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
                //////////////////////////////////////////////////
                //////////////////////////////////////////////////
                //////////////////////////////////////////////////
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
