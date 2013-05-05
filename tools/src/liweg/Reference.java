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
        Label, Array, Table, PC;
    }

    public final static class Array
        extends Object
    {
        public final String label;
        public final int x;

        public Array(StringTokenizer strtok){
            super();
            this.label = strtok.nextToken();
            this.x = Integer.parseInt(strtok.nextToken());
        }

        public String toString(){

            return String.format("%s[%d]",this.label,this.x);
        }
    }
    public final static class Table
        extends Object
    {
        public final String label;
        public final int x, y;

        public Table(StringTokenizer strtok){
            super();
            this.label = strtok.nextToken();
            this.x = Integer.parseInt(strtok.nextToken());
            this.y = Integer.parseInt(strtok.nextToken());
        }

        public String toString(){

            return String.format("%s[%d][%d]",this.label,this.x,this.y);
        }
    }


    public final Argument argument;

    public final String string;

    public final Type type;

    public final String label;

    public final Array array;

    public final Table table;


    public Reference(Argument argument, String string){
        super();
        if (null != argument && null != string && 0 < string.length()){
            this.argument = argument;
            this.string = string;

            StringTokenizer strtok = new StringTokenizer(string,"][ ");
            switch(strtok.countTokens()){
            case 1:
                {
                    this.label = strtok.nextToken();
                    this.table = null;
                    this.array = null;
                    this.type = Type.Label;
                }
                break;
            case 2:
                {
                    Array array = new Array(strtok);

                    this.label = array.label;
                    this.table = null;
                    this.array = array;
                    this.type = Type.Array;
                }
                break;
            case 3:
                {
                    Table table = new Table(strtok);

                    this.label = table.label;
                    this.table = table;
                    this.array = null;
                    this.type = Type.Table;
                }
                break;
            default:
                throw new IllegalArgumentException(string);
            }
        }
        else
            throw new IllegalArgumentException();
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
