/*
 * @(#)CobolParser.java	 0.1.0
 *
 * Copyright (c) 2019 Julian M. Bass
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package cobol;

import parse.Alternation;
import parse.Empty;
import parse.Parser;
import parse.Sequence;
import parse.tokens.CaselessLiteral;
import parse.tokens.Num;
import parse.tokens.Symbol;
import parse.tokens.Tokenizer;
import parse.tokens.Word;

public class CobolParser {
	/**
	 * Return a parser that will recognize the selected COBOL source code constructs:
	 * 
	 * 
	 * This parser creates a <code>COBOL</code> object
	 * as an assembly's target.
	 *
	 * @return a parser that will recognize and build a 
	 *         <object>COBOL</object> from a source code file.
	 */
	public Parser cobol() {
		Alternation a = new Alternation();
		
		Symbol fullstop = new Symbol('.');
		fullstop.discard();
		
		a.add( ProgramID() );
		
		a.add( DivisionName() );
		
		a.add( SectionName() );
		
		a.add( DateWritten() );
		
		a.add( CommentLine() );
		
		a.add( Function() );
		
		a.add(new Empty());
		return a;
	}
	
	/*
	 * Return a parser that will recognize the grammar:
	 * 
	 *    Program Identifier = Word
	 *
	 */
	protected Parser ProgramID() {
		Sequence s = new Sequence();
		s.add(new CaselessLiteral("program-id") );
		s.add(new Symbol('.').discard());	
		s.add(new Word().setAssembler(new Program_idAssembler()));
		return s;
	}



	/*
	 * Return a parser that will recognise the grammar:
	 * 
	 *    <divisionName> division
	 *
	 */
	protected Parser DivisionName() {
		Sequence s = new Sequence();
		s.add(new Word().setAssembler(new DivisionAssembler()));
		s.add(new CaselessLiteral("division") );
		s.add(new Symbol('.').discard());
		return s;
	}
	
	/*
	 * Return a parser that will recognize the grammar:
	 * 
	 *    Section Name = Word
	 *
	 */
	protected Parser SectionName() {
		Sequence s = new Sequence();
		s.add(new Word().setAssembler(new SectionNameAssembler()));
		s.add(new CaselessLiteral("section") );
		s.add(new Symbol('.').discard());	

		return s;
	}
	
	/*
	 * Return a parser that will recognise the grammar:
	 * 
	 *    working-storage section
	 *
	 */
	protected Parser DateWritten() {
		
		Sequence s = new Sequence();
		
		s.add(new CaselessLiteral("date-written") );
		//remove the '.' appending 'date-written'
		s.add(new Symbol('.').discard());
		//get the following available sequence of numbers until the next datatype is reached
		s.add(new Num());
		//remove the hyphen between date and month
		s.add(new Symbol('-').discard());

		//This next Word actually contains month and year (which are extracted in DateAssembler
		//get the next sequence of string until the next data type is reached
		s.add(new Word());
		//remove the hyphen between '1995' and 'mb.'
		s.add(new Symbol('-').discard());
		//remove the following string until the next data type is reached ('mb')
		s.add(new Word().discard());
		//remove the '.' appending 'mb'
		s.add(new Symbol('.').discard());
		//assign a date assembler to piece together the gathered Token data types
		s.setAssembler(new DateAssembler());
		return s;
	}
	
	
	
	/*
	* Return a parser that will recognize the grammar:
	*
	* ***--- comment text
	*
	*/
	protected Parser CommentLine() {
	//System.out.println("commentLine()");
	Sequence s = new Sequence();
	s.add(new Symbol("*"));
	s.add(new Symbol("*"));
	s.add(new Symbol("*"));
	s.add(new Symbol("-"));
	s.add(new Symbol("-"));
	s.add(new Symbol("-"));
	s.add(new Word().setAssembler(new CommentLineAssembler()) );
	//s.setAssembler(new CommentLineAssembler());
	return s;
	}

	
	
	
	protected Parser Function() {
		Sequence s = new Sequence();
		
		//get the Function name
		s.add(new Word());
		//check against list of restricted words? How do we distinguish between this and program-id? check if inside of working-storage section or if last element.
		//Could also check if inside procedure.
		
		
		//remove the '.' following the function name.
		s.add(new Symbol('.').discard());
		s.setAssembler(new FunctionAssembler());
		
		return s;
	}
	


	/**
	 * Return the primary parser for this class -- cobol().
	 *
	 * @return the primary parser for this class -- cobol()
	 */
	public static Parser start() {
		return new CobolParser().cobol();
	}

	/**
	 * Returns a tokenizer that does not allow spaces to appear inside
	 * the "words" that identify cobol's grammar.
	 *
	 * @return a tokenizer that does not allow spaces to appear inside
	 * the "words" that identify cobol grammar.
	 */
	public static Tokenizer tokenizer() {
		Tokenizer t = new Tokenizer();
		t.wordState().setWordChars(' ', ' ', false);
		return t;
	}

}
