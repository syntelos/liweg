/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg.parser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Generic parser driver
 */
public class TargetFile
    extends java.io.File
{
    public final static Charset UTF8 = Charset.forName("UTF-8");
    public final static Charset ASCII = Charset.forName("US-ASCII");


    public TargetFile(java.io.File dir, java.lang.String file){
        super(dir,file);
    }
    public TargetFile(java.lang.String file){
        super(file);
    }


    public PrintWriter text() throws IOException {
        return new PrintWriter(new OutputStreamWriter(new FileOutputStream(this),UTF8));
    }
    public DataOutputStream encode() throws IOException {
        return new DataOutputStream(new FileOutputStream(this));
    }
    public DataInputStream decode() throws IOException {
        return new DataInputStream(new FileInputStream(this));
    }

}
