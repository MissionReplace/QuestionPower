/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.TerraStormDE.QuestionPower.Util;

/**
 *
 * @author Hannes
 */
public class TQuestionAnswer
{
    
    private String name;
    private int votes;
    
    public TQuestionAnswer(String name){
        this.name = name;
        this.votes = 0;
    }
    
    public void addVote(){
        votes++;
    }
    public void removeVote(){
        votes--;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public int getVotes(){
        return votes;
    }
    
}
