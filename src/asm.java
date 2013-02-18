
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;

/**
 * moops assembler
 */
public class asm {

    private final static Charset ASCII = Charset.forName("US-ASCII");


    private final static void usage(){
        System.err.printf("Usage%n");
        System.err.printf("    java -jar moops.jar asm file.moops%n");
        System.err.printf("Produces%n");
        System.err.printf("    file.mbin%n");
        System.exit(1);
    }

    public static void main(String[] argv){
        if (1 == argv.length){
            final File src = new File(argv[0]);
            if (src.isFile()){
                final File dst = Src2Bin(src);
                try {
                    LineNumberReader txt = new LineNumberReader(new InputStreamReader(new FileInputStream(src),ASCII));
                    try {
                        final moops.Asm asm = new moops.Asm(src,txt);

                        asm.assemble();

                        DataOutputStream bin = new DataOutputStream(new FileOutputStream(dst));
                        try {
                            asm.writeVM(bin);
                        }
                        finally {
                            bin.flush();
                            bin.close();
                        }
                    }
                    finally {
                        txt.close();
                    }
                }
                catch (IOException exc){
                    System.err.printf("asm: error: while reading file %s%n\t",src);
                    exc.printStackTrace();
                    System.exit(1);
                }
            }
            else {
                System.err.printf("asm: error: file not found %s%n",src);
                System.exit(1);
            }
        }
        else
            usage();
    }

    private final static File Src2Bin(File src){
        final String ap = src.getAbsolutePath();
        final int idx = ap.lastIndexOf('.');
        if (0 < idx)
            return new File( ap.substring(0,idx)+".mbin");
        else
            return new File( ap+".mbin");
    }
}
