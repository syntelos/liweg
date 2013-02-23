
/**
 * jar file executable entry point
 * 
 *    java -cp moops-X.Y.Z.jar (asm|run)
 *    java -jar moops-X.Y.Z.jar (asm|run)
 */
public class main {

    enum Opt {
        ASM, RUN, HELP;

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
        System.err.printf("    java -jar moops.jar (asm|run) ? %n");
        System.err.printf("Produces%n");
        System.err.printf("    This message.%n");
        System.exit(1);
    }

    public static void main(String[] argv){
        if (1 == argv.length){
            Opt opt = Opt.For(argv[0]);
            switch(opt){
            case ASM:
                asm.main(Shift(argv));
                break;
            case RUN:
                System.err.println("TODO");
                System.exit(1);
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
