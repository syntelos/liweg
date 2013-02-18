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
    CDOP,
    RLOP,
    REF,
    PC;

    public Object parameter(Asm asm, Op op, int p, String term, Asm.Synthetic s, File file, int lno, String src){
        switch(this){
        case NAME:
            return term;
        case TYPE:
            return new OpType(term);
        default:
            //////////////////////////////////////////////
            //////////////////////////////////////////////
            ///////////////////todo///////////////////////
            //////////////////////////////////////////////
            //////////////////////////////////////////////
            return term;
        }
    }
}
