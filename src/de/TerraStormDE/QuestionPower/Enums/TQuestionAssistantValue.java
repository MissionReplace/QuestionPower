/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.TerraStormDE.QuestionPower.Enums;

/**
 *
 * @author Hannes
 */
public enum TQuestionAssistantValue
{
    
    QUESTION(1,"First","Write a question"),
    AUTO_CLOSE(2,"Second","Write a number ( after this time the question will end )"),
    ANSWERS(3,"Third","Create answers ( format: answer1 answer2 answer3 )"),
    VOTE_ITEM(4,"Fourth","Select a vote item ( click with a item )");
    
    private int step_id;
    private String step;
    private String instruction;
    
    private TQuestionAssistantValue(int step_id,String step,String instruction){
        this.step_id = step_id;
        this.step = step;
        this.instruction = instruction;
    }
    
    public String getStep(){
        return step;
    }
    public String getInstruction(){
        return instruction;
    }
    public int getStepID(){
        return step_id;
    }
    public static TQuestionAssistantValue getByID(int step_id){
        for(TQuestionAssistantValue v : values()){
            if(v.getStepID() == step_id){
                return v;
            }
        }
        return null;
    }
}
