package main;

/* Creates a Candidate from the line. The line will have the format 
ID#,candidate_name .*/

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
    // returns the candidate’s id
    public int getId() {   
        return id;
    }
    // Whether the candidate is still active in the election
    public boolean isActive(){
        return active;
    } 
    // return the candidates name
    public String getName(){
        return name;
    }
}

