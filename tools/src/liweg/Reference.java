/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg;

import java.util.StringTokenizer;

/**
 * Assembly language references within the instruction stream and to
 * registers are label strings.
 */
public final class Reference
    extends Object
    implements AS, Comparable<Reference>
{
    public enum Type {
        DATA, ARRAY, TABLE, STRUCT;


        public Parameter parameter(){
            switch(this){
            case DATA:
                return Parameter.REG_DATA;
            case ARRAY:
                return Parameter.REG_ARRAY;
            case TABLE:
                return Parameter.REG_TABLE;
            case STRUCT:
                return Parameter.REG_STRUCT;
            default:
                throw new IllegalStateException(this.name());
            }
        }
    }

    public abstract static class SubReference
        extends Object
    {

        private final static Object RegData(String string){
            try {
                return new Integer(string);
            }
            catch (NumberFormatException notdata){

                return new Reference(Argument.REG,string);
            }
        }

        public final static class Array
            extends SubReference
        {
            public Array(StringTokenizer strtok){
                super(strtok);
            }

            public String toString(){

                return String.format("%s[%s]",this.label,this.x);
            }
        }
        public final static class Table
            extends SubReference
        {
            public final Object y;

            public Table(StringTokenizer strtok){
                super(strtok);
                this.y = RegData(strtok.nextToken());
            }

            public String toString(){

                return String.format("%s[%s][%s]",this.label,this.x,this.y);
            }
        }
        public final static class Struct
            extends SubReference
        {
            public Struct(StringTokenizer strtok){
                super(strtok);
            }

            public String toString(){

                return String.format("%s.%s",this.label,this.x);
            }
        }


        public final String label;
        public final Object x;

        public SubReference(StringTokenizer strtok){
            super();
            this.label = strtok.nextToken();
            this.x = RegData(strtok.nextToken());
        }

        public String toString(){

            return String.format("%s[%d]",this.label,this.x);
        }
    }


    public final Argument argument;

    public final String string;

    public final Reference.Type type;

    public final String label;

    public final SubReference.Array array;

    public final SubReference.Table table;

    public final SubReference.Struct struct;


    public Reference(Argument argument, String string){
        super();
        if (null != argument && null != string && 0 < string.length()){
            this.argument = argument;
            this.string = string;

            if (0 < string.indexOf('.')){
                StringTokenizer strtok = new StringTokenizer(string,". ");
                switch(strtok.countTokens()){
                case 2:
                    {
                        SubReference.Struct struct = new SubReference.Struct(strtok);

                        this.label = struct.label;
                        this.table = null;
                        this.array = null;
                        this.struct = struct;
                        this.type = Reference.Type.STRUCT;
                    }
                    break;
                default:
                    throw new IllegalArgumentException(string);
                }
            }
            else if (0 < string.indexOf('[')){

                StringTokenizer strtok = new StringTokenizer(string,"][ ");
                switch(strtok.countTokens()){
                case 2:
                    {
                        SubReference.Array array = new SubReference.Array(strtok);

                        this.label = array.label;
                        this.table = null;
                        this.array = array;
                        this.struct = null;
                        this.type = Reference.Type.ARRAY;
                    }
                    break;
                case 3:
                    {
                        SubReference.Table table = new SubReference.Table(strtok);

                        this.label = table.label;
                        this.table = table;
                        this.array = null;
                        this.struct = null;
                        this.type = Reference.Type.TABLE;
                    }
                    break;
                default:
                    throw new IllegalArgumentException(string);
                }
            }
            else {
                this.label = string.trim();
                this.table = null;
                this.array = null;
                this.struct = null;
                this.type = Reference.Type.DATA;
            }
        }
        else
            throw new IllegalArgumentException();
    }


    public Parameter parameter(){
        return this.type.parameter();
    }
    public int hashCode(){

        return this.label.hashCode();
    }
    public boolean equals(Object that){
        if (this == that)
            return true;
        else if (null == that)
            return false;
        else
            return this.toString().equals(that.toString());
    }
    public int compareTo(Reference that){
        if (this == that)
            return 0;
        else if (null == that)
            return +1;
        else
            return this.label.compareTo(that.label);
    }
    public String toString(){
        if (null != this.table)
            return this.table.toString();
        else if (null != this.array)
            return this.array.toString();
        else
            return this.label;
    }
    public String toAS(){

        return this.toString();
    }
}
