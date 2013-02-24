package moops;

import java.io.File;

/**
 * Op parameters synthesize parameter values for Stream instances
 *
 * @see Op.java
 * @see OpType.java
 */
public enum OpArg {
    NAME,
    TYPE,
    STYP,
    VAL,
    FRBX, 
    MS,
    VMPX,
    BOOP,
    RLOP,
    REF;

    /**
     * Produce parameter values for Stream instances
     * @return parameter values 
     */
    public Object synthesize(Asm asm, Op op, int p, String term, Synthetic s, File file, int lno, String src){
        switch(this){
        case NAME:
            return new Reference(term);
        case TYPE:
            return OpType.For(term);
        case STYP:
            return null;            //////////////////////new Structure
        case VAL:
            return asm.synthesize(op,p,term,s,file,lno,src);
        case FRBX:
        case MS:
        case VMPX:
            return Byte.decode(term);
        case BOOP:
            return BoolOp.For(term);
        case RLOP:
            return RelOp.For(term);
        case REF:
            return new Reference(term);
        default:
            throw new IllegalStateException(this.name());
        }
    }
}
