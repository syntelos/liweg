/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * liweg assembler
 */
public class asm {

    private final static Charset ASCII = Charset.forName("US-ASCII");


    private final static void usage(){
        System.err.printf("Usage%n");
        System.err.printf("    java -jar liweg.jar asm file.liweg%n");
        System.err.printf("Produces%n");
        System.err.printf("    file.libin%n");
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
                        final liweg.Asm asm = new liweg.Asm(src,txt);

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
            return new File( ap.substring(0,idx)+".libin");
        else
            return new File( ap+".libin");
    }
}
