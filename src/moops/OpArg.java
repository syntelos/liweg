package moops;

import java.io.File;

/**
 * 
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
    REF,
    PC;

    public Object synthesize(Asm asm, Op op, int p, String term, Synthetic s, File file, int lno, String src){
        switch(this){
        case NAME:
            return term;
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
            return term;            //////////////////////new Reference
        case PC:
            return term;            //////////////////////new Label
        default:
            throw new IllegalStateException(this.name());
        }
    }
}
