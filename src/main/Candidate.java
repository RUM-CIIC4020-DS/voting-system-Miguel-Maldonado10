package main;


/**
 * This is the Candidate class that sperates IDs and Names form the starting parameter line
 * @author Miguel A. Maldonado Maldonado
 * 
 * Creates a Candidate from the line. The line will have the format 
 * ID#,candidate_name .
 *
 * @param id The identification number of the candidate
 * @param name The name of this candidate
 * @param active The status of this candidate
 * 
 */

public class Candidate {
    private int id;
    private String name;
    private boolean active = true;
    public Candidate(String line) {
        int count = 1;
        while(count < line.length() && Character.isDigit(line.charAt(count))){
            count++;
        }
        id = Integer.parseInt(line.substring(0, count));
        name = line.substring(count+1);
    }
    // Returns the candidate’s id
    public int getId() {   
        return id;
    }
    // Whether the candidate is still active in the election
    public boolean isActive(){
        return active;
    } 
    public void stepDown(){
        active = false;
    }
    // Return the candidates name
    public String getName(){
        return name;
    }
}

