/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg.parser;

/**
 *
 */
public class SourceFile
    extends java.io.File
{

    public SourceFile(java.io.File dir, java.lang.String file){
        super(dir,file);
    }
    public SourceFile(java.lang.String file){
        super(file);
    }
}
