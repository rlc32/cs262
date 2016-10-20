package edu.calvin.cs262.lab06;



/**
 *
 *
 * @author deitel
 * @author rlc32
 * @version fall, 2016
 */
public class Player {

    private String id, name, email;

    public Player(String new_id, String new_name, String new_email) {
        id = new_id;
        name = new_name;
        email = new_email;

    }

    public String getID(){return id;}
    public String getName(){return name;}
    public String getEmail(){return email;}
}
