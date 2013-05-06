/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg;

import liweg.parser.Expression;

import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Machine description C header file parser.
 */
public class SourceFileLiweg
    extends liweg.parser.SourceFile
{

    public SourceFileLiweg(java.io.File dir, java.lang.String file){
        super(dir,file);
    }
    public SourceFileLiweg(java.lang.String file){
        super(file);
    }


    public Expression read() throws IOException {
        LineNumberReader txt = this.text();
        try {
            return new liweg.parser.Expression(this,txt);
        }
        finally {
            txt.close();
        }
    }

}
            /*
            for (String line = src.readLine(); null != line; line = src.readLine()){

                int lno = src.getLineNumber();

                if (0 < line.length()){

                    String[] tokens = Asm.Split(line," \t");

                    if (null != tokens && 0 < tokens.length){

                        final String tokop = tokens[0];

                        final Op operator = Op.For(tokop);

                        if (null != operator){

                            Parameter.Value[] parameters = operator.synthesize(this,tokens,file,lno,line);

                            this.synthesis = new Stream(this.synthesis,operator,parameters);
                        }
                        else {

                            throw new IllegalStateException(String.format("%s:%d: unknown op '%s' in: %s",file,lno,tokop,line));
                        }
                    }
                }
            }
            */
