/******************************************************************************
 *  Compilation:  javac MyLZW.java
 *  Execution:    java MyLZW - < input.txt   (compress)
 *  Execution:    java MyLZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   http://algs4.cs.princeton.edu/55compression/abraLZW.txt
 *                http://algs4.cs.princeton.edu/55compression/ababLZW.txt
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.TST;

/**
 *  The {@code MyLZW} class provides static methods for compressing
 *  and expanding a binary input using LZW compression over the 8-bit extended
 *  ASCII alphabet with 12-bit codewords.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/55compress">Section 5.5</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class MyLZW {
	private static final int C = 4;
    private static final int R = 256;        // number of input chars
    private static final int L = 4096;       // number of codewords = 2^W
    //private static final int W = 12;         
	private static final int maxW = 16;			//set max width
	private static final int minW = 9;			//set min width
	private static final int maxL = 65536;		//number of codewords for range 9-16
	private static int current_width = 9;
	

    // Do not instantiate.
    private MyLZW() { }

    /**
     * Reads a sequence of 8-bit bytes from standard input; compresses
     * them using LZW compression with 12-bit codewords; and writes the results
     * to standard output.
     */
    public static void compress(int inputMode) 
	{
        if(inputMode==0)		//do nothing mode
		{
			BinaryStdOut.write(0, C);
			String input = BinaryStdIn.readString();
			TST<Integer> st = new TST<Integer>();
			for(int i = 0; i < R; i++)
				st.put("" + (char) i, i);
			int code = R+1;  // R is codeword for EOF

			while(input.length() > 0) 
			{
				String s = st.longestPrefixOf(input);  // Find max prefix match s.
				BinaryStdOut.write(st.get(s), current_width);      // Print s's encoding.
				int t = s.length();
				if(t < input.length() && code < maxL)    // Add s to symbol table.
				{
					st.put(input.substring(0, t + 1), code++);
					if(code > Math.pow(2, current_width) && current_width < maxW)
						current_width++;
				}
				input = input.substring(t);            // Scan past s in input.
			}
			BinaryStdOut.write(R, current_width);
			BinaryStdOut.close();
			return;
		}
		else if(inputMode==1)		//reset mode
		{
			int written = 0;			//this is for codes being written into dictionary
			BinaryStdOut.write(1, C);
			String input = BinaryStdIn.readString();
			TST<Integer> st = new TST<Integer>();
			for(int i = 0; i < R; i++)
				st.put("" + (char) i, i);
			int code = R+1;  // R is codeword for EOF

			while(input.length() > 0) 
			{
				String s = st.longestPrefixOf(input);  // Find max prefix match s.
				BinaryStdOut.write(st.get(s), current_width);      // Print s's encoding.
				int t = s.length();
				written++;				//increment amount of codes written into dic.
				
				if(t < input.length() && code < maxL)    // Add s to symbol table.
				{
					st.put(input.substring(0, t + 1), code++);
					if(code > Math.pow(2, current_width) && current_width < maxW)
						current_width++;
				}
				else if(code >= maxL)		//reset codebook!
				{
					st = new TST<Integer>();
					
					for(int i = 0; i < R; i++)		//same loop as above
					{
						st.put("" + (char) i, i);
					}
					code = R+1;			//R is codword for EOF ..... (same as above)
					current_width = minW;
				}

				input = input.substring(t);            // Scan past s in input.
			}
			BinaryStdOut.write(R, current_width);
			BinaryStdOut.close();
			return;
		}
		else if(inputMode==2)		//monitor mode
		{
			//declare variables for compression ratio test!!
			int compressRatio1 = 0;
			int compressRatio2 = 0;
			int newCompressRatio = 0;
			int newCompressRatio2 = 0;
			
			int written = 0;			//this is for codes being written into dictionary
			
			BinaryStdOut.write(2, C);
			String input = BinaryStdIn.readString();
			TST<Integer> st = new TST<Integer>();
			for(int i = 0; i < R; i++)
				st.put("" + (char) i, i);
			int code = R+1;  // R is codeword for EOF

			while(input.length() > 0) 
			{
				String s = st.longestPrefixOf(input);  // Find max prefix match s.
				BinaryStdOut.write(st.get(s), current_width);      // Print s's encoding.
				int t = s.length();
				written++;				//increment amount of codes written into dic.
				
				if(t < input.length() && code >= maxL)		//reset codebook if needed.
				{
					newCompressRatio += t * 8;
					newCompressRatio2 += current_width;
					double oldRatio = getCompressionRatio(compressRatio1,compressRatio2);
					double newRatio = getCompressionRatio(newCompressRatio, newCompressRatio2);
					
					if((oldRatio/newRatio) >= 1.1)		//"ratio of ratios" exceeds 1.1 ---> MUST RESET CODEBOOK
					{
						st = new TST<Integer>();
						for(int i = 0; i < R; i++)
							st.put("" + (char) i, i);
						code = R+1;  // R is codeword for EOF
						compressRatio1 = 0;
						compressRatio2 = 0;
						newCompressRatio = 0;
						newCompressRatio2 = 0;
						current_width = minW;		//reset to 9
						
					}
				}
				else if(code < maxL)
				{
					compressRatio1 += t * 8;
					newCompressRatio += t * 8;
					compressRatio2 += current_width;
					newCompressRatio2 += current_width;
				}
				
				if(t < input.length() && code < maxL)    // Add s to symbol table.
				{
					st.put(input.substring(0, t + 1), code++);
					if(code >= Math.pow(2, current_width) && current_width < maxW)
						current_width++;
				}
				input = input.substring(t);            // Scan past s in input.
			}
			BinaryStdOut.write(R, current_width);
			BinaryStdOut.close();
			return;
		}
    }

    /**
     * Reads a sequence of bit encoded using LZW compression with
     * 12-bit codewords from standard input; expands them; and writes
     * the results to standard output.
     */
    public static void expand() 
	{
		int inputMode = BinaryStdIn.readInt(C);
		if(inputMode==0)		//do nothing mode expansion
		{
			String[] st = new String[maxL];
			int i; 				// next available codeword value

			// initialize symbol table with all 1-character strings
			for(i = 0; i < R; i++)
				st[i] = "" + (char) i;
			st[i++] = "";                        // (unused) lookahead for EOF

			int codeword = BinaryStdIn.readInt(current_width);
			if(codeword == R) 
				return;           // expanded message is empty string
			String val = st[codeword];

			while(true) 
			{
				BinaryStdOut.write(val);
				codeword = BinaryStdIn.readInt(current_width);
				if(codeword == R) 
					break;
				String s = st[codeword];
				if(i == codeword) 
					s = val + val.charAt(0);   // special case hack
				if(i < maxL) 
					st[i++] = val + s.charAt(0);
				if(i >= Math.pow(2, current_width) && current_width < maxW)
					current_width++;
				val = s;
			}
			BinaryStdOut.close();
		}
		else if(inputMode==1)		//reset mode expansion
		{
			String[] st = new String[maxL];
			int i; 				// next available codeword value

			// initialize symbol table with all 1-character strings
			for(i = 0; i < R; i++)
				st[i] = "" + (char) i;
			st[i++] = "";                        // (unused) lookahead for EOF

			int codeword = BinaryStdIn.readInt(current_width);
			if(codeword == R) 
				return;           // expanded message is empty string
			String val = st[codeword];
			
			int written = 0;		//same variable for codewords written as in compression methods

			while(true) 
			{
				BinaryStdOut.write(val);
				if( i < maxL) codeword = BinaryStdIn.readInt(current_width);	//if length of 16 bits hasnt been reached
				else codeword = BinaryStdIn.readInt(minW);				//reset back to 9 bits (min.)
				
				written++;		//increment
				
				if(codeword == R) 
					break;
				
				String s = st[codeword];
				
				if(i == codeword) 
					s = val + val.charAt(0);   // special case hack
				
				if(i < maxL) 
					st[i++] = val + s.charAt(0);
				else
				{
					st = new String[maxL];
					for(i = 0; i < R; i++)
						st[i] = "" + (char) i;
					st[i++] = ""; 
					current_width = minW;		//reset to min. width codewrod
				}
				if(i >= Math.pow(2, current_width) && current_width < maxW)
					current_width++;
				val = s;
			}
			BinaryStdOut.close();
		}
		else if(inputMode==2)		//monitor mode expansion
		{
			//declare variables for compression ratio test!!
			int compressRatio1 = 0;
			int compressRatio2 = 0;
			int newCompressRatio = 0;
			int newCompressRatio2 = 0;
			
			
			String[] st = new String[maxL];
			int i; 				// next available codeword value

			// initialize symbol table with all 1-character strings
			for(i = 0; i < R; i++)
				st[i] = "" + (char) i;
			st[i++] = "";                        // (unused) lookahead for EOF

			int codeword = BinaryStdIn.readInt(current_width);
			if(codeword == R) 
				return;           // expanded message is empty string
			String val = st[codeword];
			
			int written = 0;		//same variable for codewords written as in compression methods

			while(true) 
			{
				BinaryStdOut.write(val);
				written++;		//increment
				
				if( i < maxL)
				{
					newCompressRatio += val.length() * 8;
					compressRatio1 = newCompressRatio; 
					newCompressRatio2 += current_width;
					compressRatio2 = newCompressRatio2;
				}					
				else
				{
					newCompressRatio += val.length() * 8;
					newCompressRatio2 += current_width;
				}
				
				double oldRatio = getCompressionRatio(compressRatio1,compressRatio2);
				double newRatio = getCompressionRatio(newCompressRatio, newCompressRatio2);
				
				if( i >= maxL && compressRatio2 > 0 && newCompressRatio2 > 0 && (oldRatio/newRatio)>=1.1)
				{
					st = new String[maxL];
					i = 0;
					for(i = 0; i < R; i++)
						st[i] = "" + (char) i;
					st[i++] = ""; 
					newCompressRatio=0;
					newCompressRatio2=0;
					current_width = minW;		//reset to min. width codewrod
				}
				
				if(i >= Math.pow(2, current_width)-1 && current_width < maxW)
					current_width++;
				
				codeword = BinaryStdIn.readInt(current_width);
				if(codeword == R) 
					break;
				
				String s = st[codeword];
				
				if(i == codeword) 
					s = val + val.charAt(0);   // special case hack
				
				if(i < maxL) 
					st[i++] = val + s.charAt(0);
				
	
				val = s;
			}
			BinaryStdOut.close();
		}
		return;
    }

    /**
     * Sample client that calls {@code compress()} if the command-line
     * argument is "-" an {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) 
	{
        if(args[0].equals("-")) 
			compress(getInputMode(args[1]));
        else if(args[0].equals("+")) 
			expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
	
	public static int getInputMode(String m)
	{
		if(m.equals("n")) 
			return 0;
		else if(m.equals("r"))
			return 1;
		else if(m.equals("m"))
			return 2;
		return -1;
	}
	
	public static double getCompressionRatio(int x, int y)
	{
		return (((double)x) / ((double)y));		//calculate current compress. ratio and cast to double
	}

}

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
