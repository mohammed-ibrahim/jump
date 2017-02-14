package org.jump;

import org.jump.parser.JumpGen;
import org.jump.parser.ParseResult;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello World.");
		
		try {
			ParseResult result = JumpGen.parse("");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
