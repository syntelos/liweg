package moops;

import java.io.File;

/**
 * Instruction set
 */
public enum Op {
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
    IFCV   (0xF0,OpArg.REF,OpArg.CDOP,OpArg.VAL),
    IFCR   (0xF1,OpArg.REF,OpArg.CDOP,OpArg.REF),
    IFRV   (0xF2,OpArg.REF,OpArg.RLOP,OpArg.VAL),
    IFRR   (0xF3,OpArg.REF,OpArg.RLOP,OpArg.REF),
    ELSE   (0xF4),
    END    (0xF5),
    FORCV  (0xF6,OpArg.REF,OpArg.CDOP,OpArg.VAL),
    FORCR  (0xF7,OpArg.REF,OpArg.CDOP,OpArg.REF),
    FORRV  (0xF8,OpArg.REF,OpArg.RLOP,OpArg.VAL),
    FORRR  (0xF9,OpArg.REF,OpArg.RLOP,OpArg.REF),
    WHILE  (0xFA,OpArg.REF),
    WHILECV(0xFB,OpArg.REF,OpArg.CDOP,OpArg.VAL),
    WHILECR(0xFC,OpArg.REF,OpArg.CDOP,OpArg.REF),
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


    public Object[] parameters(Asm asm, String[] src, Asm.Synthetic s, File file, int lno, String original){
        int ilen = src.length;
        int olen = (ilen-1);
        if (0 < olen){
            if (olen == this.argslen){
                Object[] p = new Object[olen];
                for (int cc = 0; cc < olen; cc++){

                    p[cc] = this.args[cc].parameter(asm,this,cc,src[cc+1],s,file,lno,original);
                }
                return p;
            }
            else
                throw new IllegalArgumentException(String.format("%s:%d:%s%n",file,lno,original));
        }
        else
            return MTP;
    }
}

