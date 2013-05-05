/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * liweg assembler
 */
public class dasm {


    private final static void usage(){
        System.err.printf("Usage%n");
        System.err.printf("    java -jar liweg.jar dasm file.libin%n");
        System.exit(1);
    }

    public static void main(String[] argv){
        if (1 == argv.length){
            final File src = new File(argv[0]);
            if (src.isFile()){
                try {
                    DataInputStream txt = new DataInputStream(new FileInputStream(src));
                    try {

                    }
                    finally {
                        txt.close();
                    }
                }
                catch (IOException exc){
                    System.err.printf("dasm: error: while reading file %s%n\t",src);
                    exc.printStackTrace();
                    System.exit(1);
                }
            }
            else {
                System.err.printf("dasm: error: file not found %s%n",src);
                System.exit(1);
            }
        }
        else
            usage();
    }
}
