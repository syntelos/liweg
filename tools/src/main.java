/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */

/**
 * jar file executable entry point
 * 
 *    java -cp liweg-X.Y.Z.jar (asm|run)
 *    java -jar liweg-X.Y.Z.jar (asm|run)
 */
public class main {

    enum Opt {
        ASM, DASM, RUN, HELP;

        final static Opt For(String arg){
            try {
                return Opt.valueOf(arg.toUpperCase());
            }
            catch (RuntimeException exc){

                return Opt.HELP;
            }
        }
    }

    private final static void usage(){
        System.err.printf("Usage%n");
        System.err.printf("    java -jar liweg.jar (asm|dasm|run) input.file %n");
        System.exit(1);
    }

    public static void main(String[] argv){
        if (1 <= argv.length){
            Opt opt = Opt.For(argv[0]);
            switch(opt){
            case ASM:
                asm.main(Shift(argv));
                System.exit(0);
                break;
            case DASM:
                dasm.main(Shift(argv));
                System.exit(0);
                break;
            case RUN:
                run.main(Shift(argv));
                System.exit(0);
                break;
            default:
                usage();
                break;
            }
        }
        else
            usage();
    }

    private final static String[] Shift(String[] argv){
        int len = argv.length;
        if (1 >= len)
            return new String[0];
        else {
            len -= 1;
            String[] shift = new String[len];
            System.arraycopy(argv,1,shift,0,len);
            return shift;
        }
    }
}
