/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg;

/**
 * Parse a literal based on its self evident appearance in order to
 * simplify the synthesis process.
 */
public class DataValue
    extends java.lang.Number
{
    public enum Appearance {
        Bool, Int, Float, Long, Double;
    }


    public final Argument argument;

    public final String string;

    public final Object value;

    public final Appearance appearance;


    public DataValue(Argument argument, String string){
        super();
        if (null != argument && null != string && 0 < string.length()){
            this.argument = argument;
            this.string = string;
            Object value = null;
            Appearance appearance = null;
            try {
                value = Integer.decode(string);
                appearance = Appearance.Int;
            }
            catch (NumberFormatException nint){
                try {
                    value = Float.parseFloat(string);
                    appearance = Appearance.Float;
                }
                catch (NumberFormatException nfloat){
                    try {
                        value = Boolean.parseBoolean(string);
                        appearance = Appearance.Bool;
                    }
                    catch (NumberFormatException nbool){
                        try {
                            value = Long.decode(string);
                            appearance = Appearance.Long;
                        }
                        catch (NumberFormatException nlong){
                            try {
                                value = Double.parseDouble(string);
                                appearance = Appearance.Double;
                            }
                            catch (NumberFormatException ndouble){

                                throw new NumberFormatException(string);
                            }
                        }
                    }
                }
            }
            this.value = value;
            this.appearance = appearance;
        }
        else
            throw new IllegalArgumentException();
    }


    public boolean booleanValue(){
        switch(this.appearance){
        case Bool:
            return ( ((Boolean)this.value).booleanValue());


        default:
            return (0 != ((Number)this.value).intValue());
        }
    }
    public int intValue(){
        switch(this.appearance){
        case Bool:
            if ( ((Boolean)this.value).booleanValue())
                return 1;
            else
                return 0;

        default:
            return ((Number)this.value).intValue();
        }
    }
    public long longValue(){
        switch(this.appearance){
        case Bool:
            if ( ((Boolean)this.value).booleanValue())
                return 1;
            else
                return 0;

        default:
            return ((Number)this.value).longValue();
        }
    }
    public float floatValue(){
        switch(this.appearance){
        case Bool:
            if ( ((Boolean)this.value).booleanValue())
                return 1;
            else
                return 0;

        default:
            return ((Number)this.value).floatValue();
        }
    }
    public double doubleValue(){
        switch(this.appearance){
        case Bool:
            if ( ((Boolean)this.value).booleanValue())
                return 1;
            else
                return 0;

        default:
            return ((Number)this.value).doubleValue();
        }
    }
    public String toString(){
        return string;
    }
}
