/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg;

import liweg.parser.Expression;

/**
 * Machine description C header file parser.
 */
public class TargetFileLiweg
    extends liweg.parser.TargetFile
{

    public TargetFileLiweg(java.io.File dir, java.lang.String file){
        super(dir,file);
    }
    public TargetFileLiweg(java.lang.String file){
        super(file);
    }


}
