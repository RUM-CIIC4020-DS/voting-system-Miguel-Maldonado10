package main;
import interfaces.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import data_structures.ArrayList;

public class Election {
    /* Constructor that implements the election logic using the files candidates.csv 
    and ballots.csv as input. (Default constructor) */ 
    List<Candidate> candidateList = new ArrayList<Candidate>();
    List<Ballot> ballotList = new ArrayList<Ballot>();
    List<Integer> SecondVoteCount = new ArrayList<Integer>();
    List<String> eliminatedCandidateList = new ArrayList<String>();
    List<Candidate> removedCandList = new ArrayList<Candidate>();
    List<Integer> removedListIndexes = new ArrayList<>();

    int maxrounds = this.candidateList.size();
    int electionround = 0;

    int totalBallots = 0;
    int validBallots = 0;
    int invalidBallots = 0;
    int blankBallots = 0;

    String wname = null;
    int winnerVotes;

    List<List<Ballot>> ballotWhereCandisOneList = new ArrayList<List<Ballot>>();
    List<List<Ballot>> deathRow = new ArrayList<List<Ballot>>();

   
    public Election() {
    try (BufferedReader ballotReader = new BufferedReader(new FileReader("inputFiles/ballots.csv"));
         BufferedReader candiReader = new BufferedReader(new FileReader("inputFiles/candidates.csv"))) {

        String b;
        String c;

        while((c = candiReader.readLine()) != null){
            candidateList.add(new Candidate(c));
        }

        while((b = ballotReader.readLine()) != null){
            ballotList.add(new Ballot(b, candidateList));
        }

        for(Ballot ball : ballotList){
            if(ball.getBallotType() == 0){
                validBallots++;
            }
            if(ball.getBallotType() == 1){
                blankBallots++;
            }
            if(ball.getBallotType() == 2){
                invalidBallots++;
            }
            totalBallots++;
        }
        if(wname != null){
            
        }

        
    } catch (FileNotFoundException e) {
        System.err.println("File not found: " + e.getMessage());
    } catch (IOException e) {
        System.err.println("Error reading files: " + e.getMessage());
    }
}

    /* Constructor that receives the name of the candidate and ballot files and applies 
    the election logic. Note: The files should be found in the input folder. */
    public Election(String candidates_filename, String ballot_filename) {
        try(
            BufferedReader ballotReader = new BufferedReader(new FileReader("inputFiles/" + ballot_filename));
            BufferedReader candiReader = new BufferedReader(new FileReader("inputFiles/" + candidates_filename));
        ){
            String b;
            String c;
        
            while((c = candiReader.readLine()) != null){
                candidateList.add(new Candidate(c));
            }

            while((b = ballotReader.readLine()) != null){
                ballotList.add(new Ballot(b, candidateList));
            }

            for(Ballot ball : ballotList){
                if(ball.getBallotType() == 0){
                    validBallots++;
                }
                if(ball.getBallotType() == 1){
                    blankBallots++;
                }
                if(ball.getBallotType() == 2){
                    invalidBallots++;
                }
                totalBallots++;
            }
            
            
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading files: " + e.getMessage());
        } 

    }
    // returns the name of the winner of the election 
    public String getWinner() throws IOException {
        if (candidateList.isEmpty()){
            return null;
        }
        this.maxrounds = candidateList.size() - 1;
    
        if (this.electionround > this.maxrounds) {
            return "No determined winner";
        }
    
        ballotWhereCandisOneList.clear();
        for (Candidate c : candidateList) {
            List<Ballot> tempVoteCount = new ArrayList<Ballot>();
            
            for (Ballot b : ballotList) {
                if (b.getCandidateByRank(1) == c.getId() && c.isActive() && b.getBallotType() == 0) {
                    tempVoteCount.add(b);
                }
            }
            ballotWhereCandisOneList.add(tempVoteCount);
        }
    
        int maxVotes = 0;
        int totalBallots = getTotalValidBallots();
        int winnerIndex = -1;
        
        for (int i = 0; i < candidateList.size(); i++) {
            if (ballotWhereCandisOneList.get(i).size() > maxVotes) {
                maxVotes = ballotWhereCandisOneList.get(i).size();  
                winnerIndex = i;
            }
            System.out.println("votes: " + ballotWhereCandisOneList.get(i).size() );
        }
        System.out.println("Max votes: " + maxVotes + "/" + totalBallots);
        
        List<Candidate> eliminatingCandidates = new ArrayList<>();
        String winnerName = candidateList.get(winnerIndex).getName();

        if (maxVotes > totalBallots / 2) {
            String data = "Number of ballots: " + getTotalBallots() + "\n";
            data = data + "Number of blank ballots: " + getTotalBlankBallots() + "\n";
            data = data + "Number of invalid ballots: " + getTotalInvalidBallots() + "\n";
            int round = 1;
            for(Integer i : removedListIndexes){
                int count = 1;
                for(Candidate s : removedCandList){
                    if(count == round){
                        data = data + "Round " + round + ": " + s.getName() + " was eliminated with " + i +" #1's \n";
                        
                    }
                    count++;
                }
                round++;
            }
            
            data = data + "Winner: " + winnerName + " wins with " + ballotWhereCandisOneList.get(winnerIndex).size() + " #1's";
            String temp = winnerName;
            StringBuilder modifiedTemp = new StringBuilder();
            for (int i = 0; i < temp.length(); i++) {
                if (temp.charAt(i) == ' ') {
                    modifiedTemp.append('_');
                } else {
                    modifiedTemp.append(temp.charAt(i));
                }
            }
            temp = modifiedTemp.toString();
            temp = temp.toLowerCase();
            String o = "outputFiles/" + temp + ballotWhereCandisOneList.get(winnerIndex).size() + ".txt";
            OutputStream output = new FileOutputStream(o);
            byte[] array = data.getBytes();
            output.write(array);

            output.close();

            
            
            return winnerName;
        } else {
            System.out.println("else checked");
            int minVotes = Integer.MAX_VALUE;
            for (int i = 0; i < ballotWhereCandisOneList.size(); i++) {
                if(!ballotWhereCandisOneList.get(i).isEmpty()){
                    if (ballotWhereCandisOneList.get(i).size() < minVotes) {
                        minVotes = ballotWhereCandisOneList.get(i).size();
                    }
                }
            }
            System.out.println("minVotes: " + minVotes);
            
            for (int i = 0; i < ballotWhereCandisOneList.size(); i++) {
                if(!ballotWhereCandisOneList.get(i).isEmpty()){
                    if (ballotWhereCandisOneList.get(i).size() == minVotes && i<candidateList.size()) {
                        eliminatingCandidates.add(candidateList.get(i));
                    }
                }
            }
            if(eliminatingCandidates.size()>1){
                int highestRank = 2;
                while (eliminatingCandidates.size() > 1 && highestRank < candidateList.size()) {
                    List<List<Ballot>> deathRow = new ArrayList<>();
                    for (Candidate c : eliminatingCandidates) {
                        ArrayList<Ballot> secondVoteCount = new ArrayList<>();
                        for (Ballot b : ballotList) {
                            if (b.getCandidateByRank(highestRank) == c.getId() && c.isActive()) {
                                secondVoteCount.add(b);
                            }
                        }
                        deathRow.add(secondVoteCount);
                    }
    
                    int minSecondVotes = Integer.MAX_VALUE;
                    for (List<Ballot> d : deathRow) {
                        minSecondVotes = Math.min(minSecondVotes, d.size());
                    }
    
                    List<Candidate> candidatesToRemove = new ArrayList<>();
                    for (int i = 0; i < deathRow.size(); i++) {
                        if (deathRow.get(i).size() > minSecondVotes) {
                            candidatesToRemove.add(eliminatingCandidates.get(i));
                        }
                    }
    
                    for (Candidate c : candidatesToRemove) {
                        eliminatingCandidates.remove(c);
                    }

                    highestRank++;
                }
            } 
        }

            Candidate eliminatedCandidate = eliminatingCandidates.get(0);
            //eliminatingCandidates.remove(0);
            eliminatedCandidate.stepDown();
            int elimindex = candidateList.firstIndex(eliminatedCandidate);
            eliminatedCandidateList.add(eliminatedCandidate.getName() + "-" + ballotWhereCandisOneList.get(elimindex).size());
            removedCandList.add(eliminatedCandidate);
            removedListIndexes.add(ballotWhereCandisOneList.get(elimindex).size());

            for (Candidate c : candidateList){
                for(Ballot b : ballotList){
                    if(b.getCandidateByRank(1) == eliminatedCandidate.getId() && !c.isActive() && b.getBallotType() == 0){
                        b.eliminate(eliminatedCandidate.getId());
                    }
                }
            }

            for (Candidate c : candidateList){
                for(Ballot b : ballotList){
                    if(b.getCandidateByRank(1) == c.getId() && !c.isActive() && b.getBallotType() == 0){
                        b.eliminate(c.getId());
                    }
                }
            }

            int cin = 0;
            for(List<Ballot> l : ballotWhereCandisOneList){
                if(l.size()>0){
                    cin++;
                }
            }

            System.out.println("non empty lists " + cin);


            for(List<Ballot> l : ballotWhereCandisOneList){
                System.out.println("unedited size of list " + l.size());
            }

        System.out.println(" ");

        
        this.electionround++;
        return getWinner(); // Recur for the next round
    }
    
    // returns the total amount of ballots submitted
    public int getTotalBallots(){
        return totalBallots;
    }
    // returns the total amount of invalid ballots
    public int getTotalInvalidBallots(){
        return invalidBallots;
    }
    // returns the total amount of blank ballots
    public int getTotalBlankBallots(){
        return blankBallots;
    }
    // returns the total amount of valid ballots
    public int getTotalValidBallots(){
        return validBallots;
    }
    /* List of names for the eliminated candidates with the numbers of 1s they had, 
    must be in order of elimination. Format should be <candidate name>-<number of 1s 
    when eliminated>*/
    public List<String> getEliminatedCandidates(){
        return eliminatedCandidateList;
    } 

    public List<Candidate> getCandidates(){
        return candidateList;
    }
    public List<Ballot> getBallots(){
        return ballotList;
    }

    /**
    * Prints all the general information about the election as well as a 
    * table with the vote distribution.
    * Meant for helping in the debugging process.
     * @throws IOException 
    */
    public void printBallotDistribution() throws IOException {
        System.out.println("Total ballots:" + getTotalBallots());
        System.out.println("Total blank ballots:" + getTotalBlankBallots());
        System.out.println("Total invalid ballots:" + getTotalInvalidBallots());
        System.out.println("Total valid ballots:" + getTotalValidBallots());
        System.out.println("Winner: " + getWinner());
        
        for(String s : eliminatedCandidateList){
            System.out.println(s);
        }
        
        for(Candidate c: this.getCandidates()) {
            System.out.print(c.getName().substring(0, c.getName().indexOf(" ")) + "\t");
            for(Ballot b: this.getBallots()) {
                int rank = b.getRankByCandidate(c.getId());
                String tableline = "| " + ((rank != -1) ? rank: " ") + " ";
                System.out.print(tableline);
            }
            System.out.println("|");
        }
    }
}
