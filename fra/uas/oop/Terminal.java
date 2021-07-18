package edu.fra.uas.oop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class provides some simple methods for input/output from and to a
 * terminal.
 * <p>
 * <p><b>Never modify this class, never upload it to Praktomat. This is only for your
 * local use. If an assignment tells you to use this class for input and output
 * never use System.out or System.in in the same assignment.
 */
public final class Terminal {
    
    /**
     * BufferedReader for reading from standard input line-by-line.
     */
    private static final BufferedReader IN = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Private constructor to avoid object generation.
     */
    private Terminal() {
    }

    /**
     * Prints the given error-{@code message} with the prefix "{@code Error, }".
     * <p>
     * <p>More specific, this method behaves exactly as if the following code got executed:
     * <blockquote><pre>
     * System.err.println("Error, " + message);</pre>
     * </blockquote>
     *
     * @param message the error message to be printed
     */
    public static void printError(final String message) {
        String out = "Error, " + message;
        System.err.println(out);
    }

    /**
     * Prints the string representation of an {@code Object} and then terminate the line.
     * <p>
     * <p>If the argument is {@code null}, then the string {@code "null"} is printed, otherwise the object's string
     * value {@code obj.toString()} is printed.
     * <p>
     * <p>More specific, this method behaves exactly as if the following code got executed:
     * <blockquote><pre>
     * System.out.println(Object x);</pre>
     *
     * @param object the {@code Object} to be printed
     * @see String#valueOf(Object) String#valueOf(Object)
     */
    public static void printLine(final Object object) {
        System.out.println(object);
    }

    /**
     * Prints an array of characters and then terminates the line.
     * <p>
     * <p>If the argument is {@code null}, then a {@code NullPointerException} is thrown, otherwise the value of {@code
     * new String(charArray)}* is printed.
     * <p>
     * <p>More specific, this method behaves exactly as if the following code got executed:
     * <blockquote><pre>
     * System.out.println(char[] x);</pre>
     *
     * @param charArray an array of chars to be printed
     * @see String#valueOf(char[]) String#valueOf(char[])
     */
    public static void printLine(final char[] charArray) {
        /*
         * Note: This method's sole purpose is to ensure that the Terminal-class behaves exactly as
         * System.out regarding output. (System.out.println(char[]) calls String.valueOf(char[])
         * which itself returns 'new String(char[])' and is therefore the only method that behaves
         * differently when passing the provided parameter to the System.out.println(Object)
         * method.)
         */
        printLine((Object) charArray);
    }

    /**
     * Reads a line of text. 
     * A line is considered to be terminated by any one of a line feed ('\n'), 
     * a carriage return ('\r'), 
     * or a carriage return followed immediately by a linefeed.
     *
     * @return a {@code String} containing the contents of the line, 
     * not including any line-termination characters, 
     * or {@code null} if the end of the stream has been reached without reading any characters
     */
    public static String readLine() {
        try {
            String in = IN.readLine();
            return in;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}