/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg;

/**
 * Bit string, integer and floating point register (variable) type
 * having length from one to sixty four bits.
 * 
 * <h3>Byte code format</h3>
 * 
 * A single byte encodes all data types as signed or unsigned, integer
 * or floating point bit strings having length one to sixty four bits.
 * Floating point types have length thirty two or sixty four,
 * exclusively.
 * 
 * <table>
 * <th>
 * <td> 7 </td>
 * <td> 6 ... 1 </td>
 * <td> 0 </td>
 * </th>
 * <tr>
 * <td> Sign </td>
 * <td> Length </td>
 * <td> FP </td>
 * </tr>
 * </table>
 * 
 * <h3>Type Code</h3>
 * 
 * <p> The <code>sign</code> and <code>fp</code> bits are a type code
 * having four unique values. </p>
 * 
 * <h4>Unsigned Integer</h4>
 * 
 * <p> The type code <code>[sign: 0, fp: 0]</code> identifies the
 * floating point type.  This type has any non zero
 * <code>length</code> number of bits from one to sixty four.  </p>
 * 
 * <p> The <code>length</code> value of zero may be employed to denote
 * a stream having out of band I/O control, or it may denote an
 * invalid or null type, depending on the context usage protocol. </p>
 *
 * <h4>Signed Integer</h4>
 * 
 * <p> The type code <code>[sign: 1, fp: 0]</code> identifies a signed
 * integer type.  This type has <code>length</code> number of bits in
 * 8 bit multiples, exclusively. </p>
 *
 * <h4>Signed Floating point</h4>
 * 
 * <p> The type code <code>[sign: 1, fp: 1]</code> identifies the
 * floating point type.  This type has two valid values for
 * <code>length</code>, 32 or 64. </p>
 *
 * <h4>List of Octet</h4>
 * 
 * <p> The type code <code>[sign: 0, fp: 1]</code> ("unsigned fp")
 * identifies the octet list type. </p>
 * 
 * <p> This type has a payload of zero to sixty four bytes. </p>
 * 
 * @see Op.java
 * @see OpArg.java
 */
public final class DataType
    extends Object
    implements AS
{

    public final static DataType BIT = new DataType(1);

    public final static DataType UBYTE = new DataType(false,8);

    public final static DataType UINT = new DataType(false,16);

    public final static DataType ULONG = new DataType(false,32);

    public final static DataType SBYTE = new DataType(true,8);

    public final static DataType SINT = new DataType(true,16);

    public final static DataType SLONG = new DataType(true,32);

    public final static DataType FLOAT = new DataType(true,32,true);
    /*
     * Partial set of OpArg symbol types
     */
    public final static DataType NAME = DataType.UINT;
    public final static DataType TYPE = DataType.UBYTE;
    public final static DataType FRBX = DataType.UINT;
    public final static DataType MS = DataType.UINT;
    public final static DataType VMPX = DataType.UINT;
    public final static DataType BOOP = DataType.UBYTE;
    public final static DataType RLOP = DataType.UBYTE;
    public final static DataType REF = DataType.UINT;

    public enum builtins
        implements AS
    {
        BIT(DataType.BIT),
        UBYTE(DataType.UBYTE),
        UINT(DataType.UINT),
        ULONG(DataType.ULONG),
        SBYTE(DataType.SBYTE),
        SINT(DataType.SINT),
        SLONG(DataType.SLONG),
        FLOAT(DataType.FLOAT);

        public final DataType identity;

        private builtins(DataType id){
            this.identity = id;
        }

        public String toAS(){
            return this.name().toLowerCase();
        }

        public final static builtins For(byte code){
            final boolean signed = (0 > code);
            final int length = ((code>>1)&0x1F);
            final boolean fp = (1 == (code & 1));

            if (signed){

                if (fp){

                    if (4 == length){

                        return builtins.FLOAT;
                    }
                }
                else {

                    switch(length){
                    case 8:
                        return builtins.SBYTE;
                    case 16:
                        return builtins.SINT;
                    case 32:
                        return builtins.SLONG;
                    }
                }
            }
            else {
                switch(length){
                case 1:
                    return builtins.BIT;
                case 8:
                    return builtins.UBYTE;
                case 16:
                    return builtins.UINT;
                case 32:
                    return builtins.ULONG;
                }
            }
            return null;
        }
    }

    private final static lxl.Map<Integer,builtins> Map = new lxl.Map();
    static {
        for (builtins bi: builtins.values()){
            Map.put(bi.identity.hashCode(),bi);
        }
    }

    public final static builtins Kind(DataType ot){
        if (null != ot)
            return Map.get(ot.hashCode());
        else
            return null;
    }
    public final static DataType Identity(DataType ot){
        if (null != ot){
            builtins bi = Map.get(ot.hashCode());
            if (null != bi)
                return bi.identity;
            else
                return ot;
        }
        else
            return null;
    }

    public final static DataType For(String token){
        if (null == token)
            return null;
        else if (-1 < token.indexOf(':')){

            return Identity(new DataType(token));
        }
        else {
            try {
                return builtins.valueOf(token.toUpperCase()).identity;
            }
            catch (RuntimeException exc){
                return null;
            }
        }
    }
    public final static boolean Sign(String token){
        if (null == token || 1 > token.length())
            return false;
        else if (1 == token.length()){
            switch(token.charAt(0)){
            case 's':
            case 'S':
                return true;
            case 'u':
            case 'U':
                return false;
            default:
                throw new IllegalArgumentException(token);
            }
        }
        else if ("signed".equals(token) || "true".equals(token))
            return true;
        else if ("unsigned".equals(token) || "false".equals(token))
            return false;
        else
            throw new IllegalArgumentException(token);
    }
    public final static boolean FP(String token){
        if (null == token || 1 > token.length())
            return false;
        else if (1 == token.length()){
            switch(token.charAt(0)){
            case 'f':
            case 'F':
                return true;
            case 'b':
            case 'B':
            case 'i':
            case 'I':
            case 's':
            case 'S':
                return false;
            default:
                throw new IllegalArgumentException(token);
            }
        }
        else if ("float".equals(token) || "true".equals(token))
            return true;
        else if ("int".equals(token) || "integer".equals(token) || "bit".equals(token) || "string".equals(token) || "false".equals(token))
            return false;
        else
            throw new IllegalArgumentException(token);
    }


    public final boolean signed;

    public final int length;

    public final boolean fp;

    public final byte code;


    public DataType(String longspec){
        this(Asm.Split(longspec,":"));
    }
    public DataType(String... spec){
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
                this.signed = Sign(spec[0]);
                this.length = Integer.parseInt(spec[1]);
                this.fp = false;
            }
            break;
        case 3:
            if ("type".equals(spec[0])){
                this.signed = Sign(spec[1]);
                this.length = Integer.parseInt(spec[2]);
                this.fp = false;
            }
            else {
                this.signed = Sign(spec[0]);
                this.length = Integer.parseInt(spec[1]);
                this.fp = FP(spec[2]);
            }
            break;
        case 4:
            if ("type".equals(spec[0])){
                this.signed = Sign(spec[1]);
                this.length = Integer.parseInt(spec[2]);
                this.fp = FP(spec[3]);
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

        this.code = Code(this.signed,this.length,this.fp);
    }
    public DataType(int length){

        this(true,length,false);
    }
    public DataType(boolean s, int length){

        this(s,length,false);
    }
    public DataType(boolean s, int length, boolean fp){
        super();
        if (0 < length){
            this.signed = (1 < length && s);
            this.length = length;
            this.fp = (4 == length && fp && s);
            this.code = Code(this.signed,this.length,this.fp);
        }
        else
            throw new IllegalArgumentException();
    }


    /**
     * @return Null or type alias.
     */
    public builtins kind(){

        return DataType.Kind(this);
    }
    public Object valueOf(String term){
        builtins kind = DataType.Kind(this);
        if (null != kind){
            switch(kind){
            case BIT:
                if ("0".equals(term))
                    return Boolean.FALSE;
                else if ("1".equals(term))
                    return Boolean.TRUE;
                else
                    return new Boolean(term);

            case UBYTE:
                return Integer.decode(term);
            case UINT:
                return Long.decode(term);
            case ULONG:
                return Long.decode(term);
            case SBYTE:
                return Integer.decode(term);
            case SINT:
                return Integer.decode(term);
            case SLONG:
                return Integer.decode(term);
            case FLOAT:
                return new Float(term);
            default:
                throw new IllegalStateException(kind.name());
            }
        }
        else {
            return Integer.decode(term);
        }
    }
    public int hashCode(){
        return this.code;
    }
    public boolean equals(Object that){
        if (this == that)
            return true;
        else if (that instanceof DataType)
            return this.equals( (DataType)that);
        else
            return false;
    }
    public boolean equals(DataType that){
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

        return toAS();
    }
    public String toAS(){
        builtins kind = builtins.For(this.code);
        if (null != kind)
            return kind.toAS();
        else
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
    public final static byte Code(boolean signed, int length, boolean fp){
        int h;
        if (signed)
            h = 0x80;
        else
            h = 0;

        h |= (length << 1);

        if (fp)
            h |= 1;

        return (byte)h;
    }
}
