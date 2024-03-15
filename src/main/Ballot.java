package main;
import interfaces.List;
import data_structures.SinglyLinkedList;

/* Creates a ballot based on the line it receives. The format for line is 
id#,candidate_name . It also receives a List of all the candidates in the 
elections.*/

public class Ballot {
    private boolean isValid = true;
    private int bn = -1; //ballot number
    private SinglyLinkedList<Integer> candidateIDs = new SinglyLinkedList<Integer>();
    private SinglyLinkedList<Integer> Preference = new SinglyLinkedList<Integer>();
    
    public Ballot(String line, List<Candidate> candidates){
        int count = 0;

        if(line.length()==0){
            isValid = false;
        } 
        while(count < line.length() && Character.isDigit(line.charAt(count))){
            count++;
        }
        bn = Integer.parseInt(line.substring(0, count));

        int d = 0;

        if(line.length()<count){
            isValid = false;
        }else{
            while (count < line.length()) {
                if (Character.isDigit(line.charAt(count)) && d % 2 == 0) {
                    int start = count;
            
                    while (count < line.length() && Character.isDigit(line.charAt(count))) {
                        count++;
                    }
            
                    candidateIDs.add(Integer.parseInt(line.substring(start, count)));
                    d++;
                }
            
                if (Character.isDigit(line.charAt(count)) && d % 2 == 1) {
                    int start = count;
            
                    while (count < line.length() && Character.isDigit(line.charAt(count))) {
                        count++;
                    }
            
                    Preference.add(Integer.parseInt(line.substring(start, count)));
                    d++;
                }
            
                count++;
            }
            
        }

        /*if(candidates.size() < Preference.size()){
            isValid = false;
        }*/

        for(Integer p : Preference){
            if(p>candidates.size()){
                isValid = false;
            }
        }
        for(int i= 0; i<Preference.size()-1;i++){
            for(int j= i+1; j<Preference.size();j++){
                if(Preference.get(i).equals(Preference.get(j)))
                    isValid = false;
            }
        }

        for(int i= 0; i<candidateIDs.size()-1;i++){
            for(int j= i+1; j<candidateIDs.size();j++){
                if(candidateIDs.get(i).equals(candidateIDs.get(j)))
                    isValid = false;
            }
        }

        int skipI = 0;
        int skipJ = skipI + 1;

        while(skipJ != Preference.size() && Preference.size() > 0){
            if(Preference.get(skipI) + 1 == Preference.get(skipJ)){
                skipI++;
                skipJ = skipI+1;
            } else{
                skipJ++;
            }
        }
        if(skipI != Preference.size()-1){
            isValid=false;
        }
    }

    // Returns the ballot number
    
    public int getBallotNum(){
        return bn;
    }
    
    //Returns the rank for that candidate, if no rank is available return -1
    
    public int getRankByCandidate(int candidateID){

        int c = candidateIDs.firstIndex(candidateID);

        if(Preference.size() == 0 || c == -1){
            isValid = false;
            return -1;
        }
            
        return Preference.get(c);
    }
    
    //Returns the candidate with that rank, if no candidate is available return -1.
    
    public int getCandidateByRank(int rank){
        
        int c = Preference.firstIndex(rank);
        if(c == -1 || candidateIDs.size() == 0){
            isValid = false;
            return -1;
        }
            
        return candidateIDs.get(c);
    }
    
    // Eliminates the candidate with the given id
    
    public boolean eliminate(int candidateId) {
        int c = candidateIDs.firstIndex(candidateId);
    
        if (c == -1) {
            return false;
        }
        candidateIDs.remove(c);
    
        if (Preference.isEmpty()) {
            // Handle the case where Preference is empty
            return true;
        }
    
        int max = Preference.get(0);
        int maxindex = 0;
        int curr = Preference.get(0);
    
        for (int i = 0; i < Preference.size(); i++) {
            if (max <= Preference.get(i)) {
                max = Preference.get(i);
                maxindex = i;
            }
            if (curr <= Preference.get(i)) {
                curr = Preference.get(i);
            } else {
                Preference.set(i, curr - 1);
                curr = Preference.get(i);
            }
        }
    
        Preference.remove(maxindex);
    
        return true;
    }
    
    
    /* Returns an integer that indicates if the ballot is: 0 – valid, 1 – blank or 2 -
    invalid */
    
    public int getBallotType(){
        if(Preference.size() == 0){
            return 1;
        }
            
        if(!isValid){
            return 2;
        }
            
        return 0;
    }
}
