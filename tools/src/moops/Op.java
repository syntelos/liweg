package moops;

import java.io.File;

/**
 * Each operator has a fixed number of arguments.
 * 
 * @see Stream
 * @see Stream$Parameter
 */
public enum Op
    implements AS
{
    /*
     * The assembler directs the virtual machine to allocate a number
     * of frame buffers for the system display width and height and
     * depth.
     * 
     * The special function registers "fb_d", "fb_w" and "fb_h" are
     * defined read only with the system display depth, width and
     * height.
     * 
     * Framebuffers are like tables in every possible way.
     * 
     * Framebuffers are shared by all programs.
     */
    FRAMEBUFFERS (0x00,"framebuffers",Argument.DATA),
    /*
     * Store to indexed frame buffer
     * 
     * blit(<fb-index>,<x>,<y>,<bits>)
     */
    BLIT         (0x01,"blit",Argument.REGDATA,Argument.REGDATA,Argument.REGDATA,Argument.REGDATA),
    /*
     * Send indexed frame buffer to output or select indexed
     * framebuffer for output -- according to the system display
     * architecture as synchronous or asynchronous.
     * 
     * show(<fb-index>)
     */
    SHOW         (0x02,"show",Argument.REGDATA),
    /*
     * SFR by value.
     * 
     * Prepare an SFR allocation for a child program "run".  SFR
     * registers are allocated in addition to user defined registers
     * (user defined registers are allocated by the "registers"
     * instruction).
     * 
     * The SFR will be identified by "REG" and will have value
     * "REG" (r/o) from this register space.
     */
    SFR_VAL     (0x03,"sfr_val",Argument.REG,Argument.REG),
    /*
     * SFR by reference.
     * 
     * Prepare an SFR allocation for a child program "run".  SFR
     * registers are allocated in addition to user defined registers.
     * 
     * The SFR will be identified by "REG" and will reference register
     * "REG" (r/w) from this register space.
     */
    SFR_REF      (0x04,"sfr_ref",Argument.REG,Argument.REG),
    /*
     * The assembler directs the virtual machine to allocate a number
     * of program slots.  With the evaluation of this instruction, all
     * program slots are empty.
     */
    PROGRAMS     (0x05,"programs",Argument.DATA),
    /*
     * The argument index is loaded with a number of bytes immediately
     * following this instruction in the stream.
     * 
     * load(<index>,<bytes>)
     */
    LOAD_PROGRAM (0x06,"load",Argument.DATA,Argument.DATA),
    /*
     * run(<prog-index>)
     */
    RUN_PROGRAM  (0x07,"run",Argument.REGDATA),
    /*
     * Define program register allocation
     * 
     * The assembler directs the virtual machine to allocate a number
     * of register slots.  All user register slots are empty with the
     * evaluation of this instruction.
     * 
     * Registers are unique to a program.  A caller may define special
     * function registers.
     * 
     * Registers' allocation may only occur within the program
     * (an instruction performs the allocation).
     * 
     * Registers are automatically de-allocated with the completion of
     * the program.
     */
    REGISTERS    (0x08,"registers",Argument.DATA),
    /*
     * Define data register (initialized to value zero)
     *
     * data <name> <type>
     */
    CREATE_DATA  (0x09,"data",Argument.REG,Argument.TYPE),
    /*
     * Define array register (initialized to value zero)
     *
     * array <name>[<w>]
     */
    CREATE_ARRAY (0x0A,"array",Argument.REG,Argument.TYPE,Argument.REGDATA),
    /*
     * Define array register (initialized to value zero)
     *
     * table <name>[<w>][<h>]
     */
    CREATE_TABLE (0x0B,"table",Argument.REG,Argument.TYPE,Argument.REGDATA,Argument.REGDATA),
    /*
     * Store to register
     * 
     * <dst> = <ref|val>
     */
    STORE_DATA   (0x0C,"store",Argument.REG,Argument.REGDATA),
    /*
     * Store to table <dst> <x> <y> with <val>
     * 
     * <dst> = <array>[<x>]
     */
    LOAD_ARRAY   (0x0D,"store",Argument.REG,Argument.REG,Argument.REGDATA),
    /*
     * <array>[<x>] = src
     */
    STORE_ARRAY  (0x0E,"store",Argument.REG,Argument.REGDATA,Argument.REG),
    /*
     * Store to table <dst> <x> <y> with <val>
     * 
     * <dst> = <table>[<a>][<b>]
     */
    LOAD_TABLE   (0x0F,"store",Argument.REG,Argument.REG,Argument.REGDATA,Argument.REGDATA),
    /*
     * 
     * <table>[<a>][<b>] = <src>
     */
    STORE_TABLE  (0x10,"store",Argument.REG,Argument.REGDATA,Argument.REGDATA,Argument.REG),

    /*
     * <dst> = <src> + <ref|val>
     */
    ADD   (0x20,"add",Argument.REG,Argument.REG,Argument.REGDATA),
    /*
     * <dst> = <src> - <ref|val>
     */
    SUB   (0x21,"sub",Argument.REG,Argument.REG,Argument.REGDATA),
    /*
     * <dst> = <src> * <ref|val>
     */
    MUL   (0x22,"mul",Argument.REG,Argument.REG,Argument.REGDATA),
    /*
     * <dst> = <src> / <ref|val>
     */
    DIV   (0x23,"div",Argument.REG,Argument.REG,Argument.REGDATA),
    /*
     * <dst> = <src> % <ref|val>
     */
    MOD   (0x24,"mod",Argument.REG,Argument.REG,Argument.REGDATA),
    /*
     * <dst> = <src> & <ref|val>
     */
    AND   (0x25,"and",Argument.REG,Argument.REG,Argument.REGDATA),
    /*
     * <dst> = <src> | <ref|val>
     */
    NOR   (0x26,"nor",Argument.REG,Argument.REG,Argument.REGDATA),
    /*
     * <dst> = <src> ^ <ref|val>
     */
    XOR   (0x27,"xor",Argument.REG,Argument.REG,Argument.REGDATA),
    /*
     * <dst> = abs(<src>)
     */
    ABS   (0x28,"abs",Argument.REG,Argument.REG),
    /*
     * <dst> = integer(<src>)
     */
    INT   (0x29,"int",Argument.REG,Argument.REG),
    /*
     * <dst> = fraction(<src>)
     */
    FRAC  (0x2A,"frac",Argument.REG,Argument.REG),
    /*
     * <dst> = exponent(<src>)
     */
    EXP   (0x2B,"exp",Argument.REG,Argument.REG),
    /*
     * <dst> = pow(<a>,<b>)
     */
    POW   (0x2C,"pow",Argument.REG,Argument.REGDATA,Argument.REGDATA),
    /*
     * <dst> = log base(<a>)(<b>)
     */
    LOG   (0x2D,"log",Argument.REG,Argument.REGDATA,Argument.REGDATA),
    /*
     * <dst> = ceil(<src>)
     */
    CEIL  (0x2E,"ceil",Argument.REG,Argument.REGDATA),
    /*
     * <dst> = floor(<src>)
     */
    FLOOR (0x2F,"floor",Argument.REG,Argument.REGDATA),
    /*
     * <dst> = sin(<src>)
     */
    SIN   (0x30,"sin",Argument.REG,Argument.REGDATA),
    /*
     * <dst> = cos(<src>)
     */
    COS   (0x31,"cos",Argument.REG,Argument.REGDATA),
    /*
     * <dst> = tan(<src>)
     */
    TAN   (0x32,"tan",Argument.REG,Argument.REGDATA),
    /*
     * <dst> = asin(<src>)
     */
    ASIN  (0x33,"asin",Argument.REG,Argument.REGDATA),
    /*
     * <dst> = acos(<src>)
     */
    ACOS  (0x34,"acos",Argument.REG,Argument.REGDATA),
    /*
     * <dst> = atan(<src>)
     */
    ATAN  (0x35,"atan",Argument.REG,Argument.REGDATA),
    /*
     * <dst> = sinh(<src>)
     */
    SINH  (0x36,"sinh",Argument.REG,Argument.REGDATA),
    /*
     * <dst> = cosh(<src>)
     */
    COSH  (0x37,"cosh",Argument.REG,Argument.REGDATA),
    /*
     * <dst> = tanh(<src>)
     */
    TANH  (0x38,"tanh",Argument.REG,Argument.REGDATA),
    /*
     * <dst> = asinh(<src>)
     */
    ASINH (0x39,"asinh",Argument.REG,Argument.REGDATA),
    /*
     * <dst> = acosh(<src>)
     */
    ACOSH (0x3A,"acosh",Argument.REG,Argument.REGDATA),
    /*
     * <dst> = atanh(<src>)
     */
    ATANH (0x3B,"atanh",Argument.REG,Argument.REGDATA),
    /*
     * <dst> = min(<a>,<b>)
     */
    MIN   (0x3C,"min",Argument.REG,Argument.REGDATA,Argument.REGDATA),
    /*
     * <dst> = max(<a>,<b>)
     */
    MAX   (0x3D,"max",Argument.REG,Argument.REGDATA,Argument.REGDATA),

    /*
     * Evaluate referenced code block for each element of the
     * referenced register.
     * 
     * eval(<ref reg>,<ref prog|pc>)
     * 
     * Register Data
     * 
     * The referenced code is evaluated once.  The reference alias
     * "register" refers to the register referenced by the caller.
     * 
     * Register Table
     * 
     * The referenced code is evaluated once for each member of the
     * table.  The reference alias "register" refers to the register
     * referenced by the caller.
     * 
     * The special function registers "register_x" and "register_y"
     * are maintained for each iteration of the evaluation sequence.
     * References to "register" refer to <table> for table
     * instructions, and to <table x y> for non-table instructions.
     * 
     * Register Array
     * 
     * The referenced code is evaluated once for each member of the
     * array.  The reference alias "register" refers to the register
     * referenced by the caller.
     * 
     * The special function register "register_x" is maintained for
     * each iteration of the evaluation sequence.  References to
     * "register" refer to <array> for array instructions, and to
     * <array x> for non-array instructions.
     * 
     */
    EVAL      (0xF5,"eval",Argument.REG,Argument.PROG),
    /*
     * wait(<ms>)
     */
    WAIT      (0xF6,"wait",Argument.REGDATA),
    /*
     * All conditional branch control operators are followed by an END
     * block delimeter.
     * 
     * Any conditional branch control operator may be followed by an
     * ELSE alternative execution block.
     */
    IF_BOOL   (0xF7,"if",Argument.REGDATA,Argument.BOOP,Argument.REGDATA),
    IF_REL    (0xF8,"if",Argument.REGDATA,Argument.RLOP,Argument.REGDATA),

    FOR_BOOL  (0xF9,"for",Argument.REGDATA,Argument.BOOP,Argument.REGDATA),
    FOR_REL   (0xFA,"for",Argument.REGDATA,Argument.RLOP,Argument.REGDATA),

    WHILE_BOOL(0xFB,"while",Argument.REGDATA,Argument.BOOP,Argument.REGDATA),
    WHILE_REL (0xFC,"while",Argument.REGDATA,Argument.RLOP,Argument.REGDATA),
    /*
     * Conditional block terminal and alternative execution block head
     */
    ELSE      (0xFD,"else"),
    /*
     * Conditional and alternative block terminal 
     */
    END       (0xFE,"end");


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
    public final String mnemonic;
    public final Argument[] args;
    public final int argslen;

    private Op(int opcode, String mnemonic, Argument... args){
        this.opcode = (byte)opcode;
        this.mnemonic = mnemonic;
        this.args = args;
        this.argslen = args.length;
    }


    public boolean isBlock(){
        return (this.isBlockStart() || this.isBlockEnd());
    }
    public boolean isBlockStart(){
        switch(this){
        case IF_BOOL:
        case IF_REL:
        case FOR_BOOL:
        case FOR_REL:
        case WHILE_BOOL:
        case WHILE_REL:
        case ELSE:
            return true;
        default:
            return false;
        }
    }
    public boolean isBlockEnd(){

        return (END == this);
    }
    public Parameter.Value[] synthesize(Stream instr, String[] tokens, File file, int lno, String line){
        int argslen = (tokens.length-1);

        if (argslen == this.argslen){

            Parameter.Value[] re = new Parameter.Value[argslen];

            for (int arg = 0, argx = 1, argc = tokens.length; argx < argc; arg++,argx++){
                Argument aa = this.args[arg];

                re[arg] = aa.synthesize(tokens[argx]);
            }
            return re;
        }
        else
            throw new IllegalStateException(String.format("%s:%d: incorrect number of arguments to %s in: %s",file,lno,this.name(),line));
    }
    public String toAS(){

        return this.mnemonic;
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

