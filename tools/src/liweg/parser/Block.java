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

        final State begin = new State(true);
        {
            final State text = new State(true);
            final State nest = new State(true);
            final State end = new State(true);

            begin.addTransition(new Transition('{',text));
            text.addTransition(new Transition(' ','z',text));
            text.addTransition(new Transition('|',text));
            text.addTransition(new Transition('{',nest));
            text.addTransition(new Transition('}',end));
            nest.addTransition(new Transition(' ','z',nest));
            nest.addTransition(new Transition('|',nest));
            nest.addTransition(new Transition('}',text));
        }
        final Automaton BlockBody = new Automaton(begin);


        final Basic cx = new Basic(pcx,true,new Object[][]{
                {"Block.Head",BasicAutomata.MakeString("{")},
                {"Block.Body",BlockBody},
                {"Block.Tail",BasicAutomata.MakeString("}")},
            });
        PATTERN = new jauk.Re(cx,"<_>*(liweg|for|while|if)~(<Block.Head>)*<Block.Body>");
    }



    public Block(Expression p, int lno, String s){
        super(p,lno);
        this.setText(s);
    }
}
