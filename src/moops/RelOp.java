package moops;

public enum RelOp
    implements AS
{

    LT("<"),
    LE("<="),
    EQ("=="),
    NE("!="),
    GT(">"),
    GE(">=");


    public final static RelOp For(String asm){
        final int len = asm.length();
        if (null != asm && 0 < len){
            switch(asm.charAt(0)){
            case '<':
                if (1 == len)
                    return RelOp.LT;
                else if (LE.asm.equals(asm))
                    return RelOp.LE;
                else
                    return null;
            case '=':
                if (EQ.asm.equals(asm))
                    return RelOp.EQ;
                else
                    return null;
            case '!':
                if (NE.asm.equals(asm))
                    return RelOp.NE;
                else
                    return null;
            case '>':
                if (1 == len)
                    return RelOp.GT;
                else if (GE.asm.equals(asm))
                    return RelOp.GE;
                else
                    return null;
            default:
                break;
            }
        }
        return null;
    }


    public final String asm;

    private RelOp(String asm){
        this.asm = asm;
    }

    public String toAS(){
        return this.asm;
    }
}
