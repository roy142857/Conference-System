package exception.people;

/**
 * The exception shows that when a person is not in the map, you need to create a person at frist.
 */
public class ShouldCreatePersonException extends Exception {

    public ShouldCreatePersonException(String s) {
        super(s);
    }}