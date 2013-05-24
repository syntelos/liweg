/*
 * Copyright (C) 2013 John Pritchard.  All rights reserved.
 */
package liweg.parser;

import jauk.Pattern;
import jauk.Re;
import automaton.Automaton;
import automaton.Context;
import automaton.NamedAutomata;
import automaton.NamedAutomata.Basic;
import automaton.NamedAutomata.Builtin;
import automaton.BasicAutomata;
import automaton.State;
import automaton.Transition;

/**
 * Block expressions include
 * <ul>
 * <li>function-program</li>
 * <li>initializer-store</li>
 * <li>if</li>
 * <li>for</li>
 * <li>while</li>
 * <li>else</li>
 * </ul>
 */
public class Block
    extends Expression
{
    public final static Pattern PATTERN;
    static {
        final NamedAutomata pcx = Builtin.Init();

        final State begin = new State("begin",true);
        {
            /*
             * ASCII text range
             */
            final State text = new State("text",true);
            final State nest01 = new State("nest01",true);
            final State nest02 = new State("nest02",true);

            final State end = new State("end",true); // '#' Empty automaton

            begin.addTransition(new Transition('{',text));
            begin.addTransition(new Transition('}',end));

            text.addTransition(new Transition('{',nest01));
            text.addTransition(new Transition('}',end));
            text.addTransition(new Transition('\t','z',text));
            text.addTransition(new Transition('|',text));
            text.addTransition(new Transition('~',text));

            nest01.addTransition(new Transition('}',text));
            nest01.addTransition(new Transition('{',nest02));
            nest01.addTransition(new Transition('\t','z',nest01));
            nest01.addTransition(new Transition('|',nest01));
            nest01.addTransition(new Transition('~',nest01));

            nest02.addTransition(new Transition('}',nest01));
            nest02.addTransition(new Transition('\t','z',nest02));
            nest02.addTransition(new Transition('|',nest02));
            nest02.addTransition(new Transition('~',nest02));
        }
        final Automaton BlockBody = new Automaton(begin);


        final Basic cx = new Basic(pcx,true,new Object[][]{
                {"Block.Head",BasicAutomata.MakeString("{")},
                {"Block.Body",BlockBody},
                {"Block.Tail",BasicAutomata.MakeString("}")},
            });
        PATTERN = new jauk.Re(cx,"~(<Block.Head>)*<Block.Body>",false);
    }



    public Block(Expression p, int lno, String s){
        super(p,lno);
        this.setText(s);
    }
}
