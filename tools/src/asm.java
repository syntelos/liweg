/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
import liweg.SourceFileLiweg;
import liweg.TargetFileLibin;

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




    private final static void usage(){
        System.err.printf("Usage%n");
        System.err.printf("    java -jar liweg.jar asm file.liweg%n");
        System.err.printf("Produces%n");
        System.err.printf("    file.libin%n");
        System.exit(1);
    }

    public static void main(String[] argv){
        if (1 == argv.length){

            final SourceFileLiweg src = new SourceFileLiweg(argv[0]);
            if (src.isFile()){
                final TargetFileLibin dst = new TargetFileLibin(Src2Bin(src));
                try {
                    new liweg.Asm(src).assemble().writeVM(dst);
                }
                catch (IOException exc){
                    System.err.printf("asm: error assembling file '%s'%n\t",src);
                    exc.printStackTrace();
                    System.exit(1);
                }
            }
            else {
                System.err.printf("asm: error: file not found '%s'%n",src);
                System.exit(1);
            }
        }
        else
            usage();
    }


    private final static String Src2Bin(File src){

        final String ap = src.getAbsolutePath();

        final int dot = ap.lastIndexOf('.');
        /*
         * Rewrite string "src" to "bin"
         */
        if (0 < dot){
            /*
             * Have a '.' in the file name
             */
            int sep = ap.lastIndexOf(File.separatorChar);
            if (0 > sep && '/' != File.separatorChar){
                /*
                 * Accept universal front-slash
                 */
                sep = ap.lastIndexOf('/');
            }

            if (-1 < sep){

                if (sep < dot){
                    /*
                     * Truncate stem in expected case for absolute path
                     *
                     * "abc/def.ghi"
                     */
                    return (ap.substring(0,dot)+".libin");
                }
            }
            else {
                /*
                 * Truncate stem in unexpected case of absolute path
                 * lacking any path component
                 */
                return (ap.substring(0,dot)+".libin");
            }
        }
        /*
         * Append, send to target file
         */
        return (ap+".libin");
    }
}
