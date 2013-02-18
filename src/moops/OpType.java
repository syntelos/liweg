package moops;

/**
 * 
 */
public class OpType {

    public final static OpType BIT = new OpType(1);

    public final static OpType UBYTE = new OpType(false,8);

    public final static OpType UINT = new OpType(false,16);

    public final static OpType ULONG = new OpType(false,32);

    public final static OpType SBYTE = new OpType(true,8);

    public final static OpType SINT = new OpType(true,16);

    public final static OpType SLONG = new OpType(true,32);

    public final static OpType FLOAT = new OpType(true,32,true);

    public enum builtins {
        BIT(OpType.BIT),
        UBYTE(OpType.UBYTE),
        UINT(OpType.UINT),
        ULONG(OpType.ULONG),
        SBYTE(OpType.SBYTE),
        SINT(OpType.SINT),
        SLONG(OpType.SLONG),
        FLOAT(OpType.FLOAT);

        public final OpType identity;

        private builtins(OpType id){
            this.identity = id;
        }


        public final static OpType For(String token){
            if (null == token)
                return null;
            else if (-1 < token.indexOf(':'))
                return new OpType(token);
            else {
                try {
                    return builtins.valueOf(token.toUpperCase()).identity;
                }
                catch (RuntimeException exc){
                    return null;
                }
            }
        }
    }


    public final boolean signed;

    public final int length;

    public final boolean fp;


    public OpType(String longspec){
        this(longspec.split(":"));
    }
    public OpType(String... spec){
        super();
        switch (spec.length){
        case 0:
            throw new IllegalArgumentException();
        case 1:
            this.signed = false;
            this.length = Integer.parseInt(spec[0]);
            this.fp = false;
            break;
        case 2:
            if ("type".equals(spec[0])){
                this.signed = false;
                this.length = Integer.parseInt(spec[1]);
                this.fp = false;
            }
            else {
                this.signed = new Boolean(spec[0]);
                this.length = Integer.parseInt(spec[1]);
                this.fp = false;
            }
            break;
        case 3:
            if ("type".equals(spec[0])){
                this.signed = new Boolean(spec[1]);
                this.length = Integer.parseInt(spec[2]);
                this.fp = false;
            }
            else {
                this.signed = new Boolean(spec[0]);
                this.length = Integer.parseInt(spec[1]);
                this.fp = new Boolean(spec[2]);
            }
            break;
        case 4:
            if ("type".equals(spec[0])){
                this.signed = new Boolean(spec[1]);
                this.length = Integer.parseInt(spec[2]);
                this.fp = new Boolean(spec[3]);
            }
            else {
                throw new IllegalArgumentException(Join(spec,":"));
            }
            break;
        default:
            throw new IllegalArgumentException(Join(spec,":"));
        }

        if (this.signed){
            if (this.fp){
                if (4 != this.length){
                    throw new IllegalArgumentException(Join(spec,":"));
                }
            }
            else if (2 > this.length){
                throw new IllegalArgumentException(Join(spec,":"));
            }
        }
        else if (this.fp){
            throw new IllegalArgumentException(Join(spec,":"));
        }
        else if (1 > this.length){
            throw new IllegalArgumentException(Join(spec,":"));
        }
    }
    public OpType(int length){

        this(true,length,false);
    }
    public OpType(boolean s, int length){

        this(s,length,false);
    }
    public OpType(boolean s, int length, boolean fp){
        super();
        if (0 < length){
            this.signed = (1 < length && s);
            this.length = length;
            this.fp = (4 == length && fp && s);
        }
        else
            throw new IllegalArgumentException();
    }


    public int hashCode(){
        int h;
        if (this.signed)
            h = 0x80;
        else
            h = 0;

        h |= (this.length << 1);

        if (this.fp)
            h |= 1;

        return h;
    }
    public boolean equals(Object that){
        if (this == that)
            return true;
        else if (that instanceof OpType)
            return this.equals( (OpType)that);
        else
            return false;
    }
    public boolean equals(OpType that){
        if (this == that)
            return true;
        else if (this.signed == that.signed){

            if (this.length == that.length){

                return (this.fp == that.fp);
            }
        }
        return false;
    }
    public String toString(){
        return ("type:"+this.signed+":"+this.length+":"+this.fp);
    }


    public final static String Join(String[] tokens, String sep){
        if (null == tokens)
            return "";
        else {
            switch(tokens.length){
            case 0:
                return "";
            case 1:
                return tokens[0];
            case 2:
                return tokens[0]+sep+tokens[1];
            default:
                return tokens[0]+sep+tokens[1]+sep+tokens[2];
            }
        }
    }
}