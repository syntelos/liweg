/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg;

public enum BoolOp 
    implements AS
{

    And("&"),
    Nor("|"),
    Xor("^");

    public final static BoolOp For(String asm){
        if (null != asm && 0 < asm.length()){
            switch(asm.charAt(0)){
            case '&':
                return BoolOp.And;
            case '|':
                return BoolOp.Nor;
            case '^':
                return BoolOp.Xor;
            default:
                break;
            }
        }
        return null;
    }


    public final String asm;

    private BoolOp(String asm){
        this.asm = asm;
    }

    public String toAS(){
        return this.asm;
    }
}
