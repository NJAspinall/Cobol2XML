package cobol;

import parse.Assembler;
import parse.Assembly;
import parse.tokens.Token;

public class CommentLineAssembler extends Assembler {
	/**
	* Pop a string, and set the target DataDivision to this
	* string.
	*
	* @param Assembly the assembly to work on
	*/
	
	@Override
	public void workOn(Assembly a) {
		// TODO Auto-generated method stub
		//System.out.println("commentLineAssembler");
		Cobol c = new Cobol();
		Token t = (Token) a.pop();
		if(t.sval() != null) {
			c.setCommentLine(t.sval().trim());
			a.setTarget(c);
		}
	}

}
