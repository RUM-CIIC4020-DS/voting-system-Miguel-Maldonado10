package main;
import interfaces.List;

public class Election {
    /* Constructor that implements the election logic using the files candidates.csv 
    and ballots.csv as input. (Default constructor) */ 
    public Election(){

    }
    /* Constructor that receives the name of the candidate and ballot files and applies 
    the election logic. Note: The files should be found in the input folder. */
    public Election(String candidates_filename, String ballot_filename){

    }
    // returns the name of the winner of the election 
    public String getWinner(){
        return "nah";
    }
    // returns the total amount of ballots submitted
    public int getTotalBallots(){
        return -1;
    }
    // returns the total amount of invalid ballots
    public int getTotalInvalidBallots(){
        return -1;
    }
    // returns the total amount of blank ballots
    public int getTotalBlankBallots(){
        return -1;
    }
    // returns the total amount of valid ballots
    public int getTotalValidBallots(){
        return -1;
    }
    /* List of names for the eliminated candidates with the numbers of 1s they had, 
    must be in order of elimination. Format should be <candidate name>-<number of 1s 
    when eliminated>*/
    public List<String> getEliminatedCandidates(){
        return null;
    } 

    /**
    * Prints all the general information about the election as well as a 
    * table with the vote distribution.
    * Meant for helping in the debugging process.
    */
    /*public void printBallotDistribution() {
        System.out.println("Total ballots:" + getTotalBallots());
        System.out.println("Total blank ballots:" + getTotalBlankBallots());
        System.out.println("Total invalid ballots:" + getTotalInvalidBallots());
        System.out.println("Total valid ballots:" + getTotalValidBallots());
        System.out.println(getEliminatedCandidates());
        for(Candidate c: this.getCandidates()) {
            System.out.print(c.getName().substring(0, c.getName().indexOf(" ")) + "\t");
            for(Ballot b: this.getBallots()) {
                int rank = b.getRankByCandidate(c.getId());
                String tableline = "| " + ((rank != -1) ? rank: " ") + " ";
                System.out.print(tableline);
            }
            System.out.println("|");
        }
    }*/
   
    
  
   
}
