package moops;

import java.io.File;

/**
 * Instruction set
 */
public enum Op
    implements AS
{
    VAR    (0x00,OpArg.NAME,OpArg.TYPE),
    STRUCT (0x01,OpArg.NAME,OpArg.STYP),
    ASSIGNV(0x02,OpArg.REF,OpArg.VAL),
    ASSIGNR(0x03,OpArg.REF,OpArg.REF),

    ADDV   (0x10,OpArg.REF,OpArg.REF,OpArg.VAL),
    ADDR   (0x11,OpArg.REF,OpArg.REF,OpArg.REF),

    SUBV   (0x12,OpArg.REF,OpArg.REF,OpArg.VAL),
    SUBR   (0x13,OpArg.REF,OpArg.REF,OpArg.REF),

    MULV   (0x14,OpArg.REF,OpArg.REF,OpArg.VAL),
    MULR   (0x15,OpArg.REF,OpArg.REF,OpArg.REF),

    DIVV   (0x16,OpArg.REF,OpArg.REF,OpArg.VAL),
    DIVR   (0x17,OpArg.REF,OpArg.REF,OpArg.REF),

    BLITV  (0xE0,OpArg.FRBX,OpArg.REF,OpArg.REF,OpArg.REF),
    BLITR  (0xE1,OpArg.REF,OpArg.REF,OpArg.REF,OpArg.REF),
    SHOWV  (0xE2,OpArg.FRBX),
    SHOWR  (0xE3,OpArg.REF),

    WAITV  (0xEA,OpArg.MS),
    WAITR  (0xEB,OpArg.REF),
    RUNV   (0xEC,OpArg.VMPX),
    RUNR   (0xED,OpArg.REF),

    GOTO   (0xEE,OpArg.REF),
    IF     (0xEF,OpArg.REF),
    IFCV   (0xF0,OpArg.REF,OpArg.BOOP,OpArg.VAL),
    IFCR   (0xF1,OpArg.REF,OpArg.BOOP,OpArg.REF),
    IFRV   (0xF2,OpArg.REF,OpArg.RLOP,OpArg.VAL),
    IFRR   (0xF3,OpArg.REF,OpArg.RLOP,OpArg.REF),
    ELSE   (0xF4),
    END    (0xF5),
    FORCV  (0xF6,OpArg.REF,OpArg.BOOP,OpArg.VAL),
    FORCR  (0xF7,OpArg.REF,OpArg.BOOP,OpArg.REF),
    FORRV  (0xF8,OpArg.REF,OpArg.RLOP,OpArg.VAL),
    FORRR  (0xF9,OpArg.REF,OpArg.RLOP,OpArg.REF),
    WHILE  (0xFA,OpArg.REF),
    WHILECV(0xFB,OpArg.REF,OpArg.BOOP,OpArg.VAL),
    WHILECR(0xFC,OpArg.REF,OpArg.BOOP,OpArg.REF),
    WHILERV(0xFD,OpArg.REF,OpArg.RLOP,OpArg.VAL),
    WHILERR(0xFE,OpArg.REF,OpArg.RLOP,OpArg.REF);


    public final static Op For(String name){
        try {
            return Op.valueOf(name.toUpperCase());
        }
        catch (RuntimeException exc){

            return null;
        }
    }

    private final static Object[] MTP = new Object[0];


    public final byte opcode;
    public final OpArg[] args;
    public final int argslen;

    private Op(int opcode, OpArg... args){
        this.opcode = (byte)opcode;
        this.args = args;
        this.argslen = args.length;
    }


    public String toAS(){
        return this.name().toLowerCase();
    }
    public Object[] synthesize(Asm asm, String[] src, Asm.Synthetic s, File file, int lno, String original){

        final int ilen = ((null != src)?(src.length):(0));

        if (ilen == this.argslen){
            if (0 < ilen){
                Object[] p = new Object[ilen];
                /*
                 * repeat first argument for (dst,src,op,src) as (src,op,src) => (src,src,op,src)
                 */
                p[0] = this.args[0].synthesize(asm,this,0,src[1],s,file,lno,original);

                for (int cc = 1; cc < ilen; cc++){

                    p[cc] = this.args[cc].synthesize(asm,this,cc,src[cc],s,file,lno,original);
                }
                return p;
            }
            else
                return MTP;
        }
        else {
            final int olen = (ilen-1);
            if (olen == this.argslen){
                if (0 < olen){
                    Object[] p = new Object[olen];
                    for (int cc = 0; cc < olen; cc++){

                        p[cc] = this.args[cc].synthesize(asm,this,cc,src[cc+1],s,file,lno,original);
                    }
                    return p;
                }
                else
                    return MTP;
            }
            else {
                throw new IllegalArgumentException(String.format("%s:%d: error i %d, a %d, in %s%n",file,lno,ilen,this.argslen,original));
            }
        }
    }
    public String toAS(Object[] src){
        int len = src.length;
        if (len == this.argslen){
            StringBuilder string = new StringBuilder();

            for (int cc = 0; cc < len; cc++){

                Object p = src[cc];
                if (0 < cc)
                    string.append(' ');

                if (p instanceof AS)
                    string.append(((AS)p).toAS());
                else
                    string.append(p);
            }
            return string.toString();
        }
        else
            throw new IllegalArgumentException();
    }
}

