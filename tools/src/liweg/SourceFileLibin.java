/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg;

/**
 * Machine description C header file parser.
 */
public class SourceFileLibin
    extends liweg.parser.SourceFile
{

    public SourceFileLibin(java.io.File dir, java.lang.String file){
        super(dir,file);
    }
    public SourceFileLibin(java.lang.String file){
        super(file);
    }

}
