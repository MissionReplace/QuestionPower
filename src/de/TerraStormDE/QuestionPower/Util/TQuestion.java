/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.TerraStormDE.QuestionPower.Util;

import de.TerraStormDE.QuestionPower.QuestionPower;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Hannes
 */
public class TQuestion
{
    private static List<TQuestion> questions = new ArrayList<TQuestion>();
    
    private List<TQuestionAnswer> answers;
    private HashMap<String,TQuestionAnswer> votes;
    
    private String question;
    private int auto_close,current_count;
    private ItemStack item;
    private int id;
    
    private boolean active = false;
    
    public TQuestion(String question,List<TQuestionAnswer> answers){
        this.question = question;
        this.answers = answers;
        this.auto_close = -1;
        this.current_count = auto_close;
        
        this.votes = new HashMap<>();
        
                
        questions.add(this);
        
        for(int i = 0; i < questions.size();i++){
            if(getQuestion(i) == null){
                this.id = i;
                break;
            }
        }
    }
    public TQuestion(String question,List<TQuestionAnswer> answers,int auto_close){
        this(question,answers);
        
        this.auto_close = auto_close;
    }
    public TQuestion(String question,List<TQuestionAnswer> answers,int auto_close,ItemStack istack){
        this(question,answers);
        
        this.auto_close = auto_close;
        this.item = istack;
    }
    public TQuestion(String question,List<TQuestionAnswer> answers,ItemStack istack){
        this(question,answers);
        
        this.item = istack;
    }
    public TQuestion(){
        this("Default question", new ArrayList<TQuestionAnswer>(), -1, new ItemStack(Material.WOOD_SPADE));
    }
    
    
    public List<TQuestionAnswer> getAnswers(){
        return answers;
    }
    public HashMap<String,TQuestionAnswer> getVotes(){
        return votes;
    }
    public String getQuestion(){
        return question;
    }
    public int getAutoCloseDelay(){
        return auto_close;
    }
    public ItemStack getVoteItem(){
        return item;
    }
    public boolean isActive(){
        return active;
    }
    public int getID(){
        return id;
    }
    public int getTotalVotes(){
        return votes.size();
    }
    public int getCurrentCount(){
        return current_count;
    }
    
    public void setQuestion(String q){
        this.question = q;
    }
    public void setAutoCloseDelay(int delay){
        this.auto_close = delay;
        this.current_count = delay;
    }
    public void setVoteItem(ItemStack i){
        this.item = i;
    }
    public void setActive(boolean b){
        this.active = b;
    }
    public void deleteQuestion(){
        setActive(false);
        questions.remove(this);
    }
    public void countDown(){
        if(getAutoCloseDelay() != -1){
            current_count--;
            
            if(current_count == 0){
                closeQuestion(false);
            }
        }
    }
    public void closeQuestion(boolean by_admin){
        QuestionPower plugin = QuestionPower.getInstance();
        
        Bukkit.broadcastMessage(plugin.getPrefix() + "§a§l§m---------------------------");
        Bukkit.broadcastMessage(plugin.getPrefix() + "A question " + (by_admin ? "was closed!" : "is over!"));
        Bukkit.broadcastMessage(plugin.getPrefix() + "Question: §e" + getQuestion());
        Bukkit.broadcastMessage(plugin.getPrefix() + "Answers: ");
        for(TQuestionAnswer a : getAnswers()){
            Bukkit.broadcastMessage(plugin.getPrefix() + "  "  + a.getName() + ": §e" + a.getVotes() + " Votes §7(§e" + getProzent(getTotalVotes(), a.getVotes()) + "%§7)");
        }
        Bukkit.broadcastMessage(plugin.getPrefix() + "§a§l§m---------------------------");
        deleteQuestion();
    }
    
    private int getProzent(int gwert,int pwert){
        return (int) (pwert*100) / gwert;
    }
    
    
    public void addAnswer(TQuestionAnswer a){
        this.answers.add(a);
    }
    public void setAnswers(List<TQuestionAnswer> list){
        this.answers = list;
    }
    public void addVote(Player p,String answer){
        TQuestionAnswer a = getAnswer(answer);
        if(a == null){
            p.sendMessage(QuestionPower.getInstance().getPrefix() + "Answer not found!");
            return;
        }
        if(votes.containsKey(p.getName())){
            if(votes.get(p.getName()).getName().equalsIgnoreCase(a.getName())){
                p.sendMessage(QuestionPower.getInstance().getPrefix() + "The same answer as before...");
                return;
            }
            p.sendMessage(QuestionPower.getInstance().getPrefix() + "You have changed your answer to '§e" + a.getName() + "§7'");
            
            TQuestionAnswer pa = votes.get(p.getName());
            pa.removeVote();
            a.addVote();
            votes.put(p.getName(), a);
            return;
        }
        
        a.addVote();
        votes.put(p.getName(), a);
        p.sendMessage(QuestionPower.getInstance().getPrefix() + "Thanks for your answer!");
        
    }
    public void addRawVote(String player_name,String answer){
        TQuestionAnswer a = getAnswer(answer);
        if(a == null){
            return;
        }
        
        a.addVote();
        votes.put(player_name, a);
    }
    private TQuestionAnswer getAnswer(String name){
        for(TQuestionAnswer a : answers){
            if(a.getName().equalsIgnoreCase(name)){
                return a;
            }
        }
        return null;
    }
    
    
    public static List<TQuestion> getQuestions(){
        return questions;
    }
    public static TQuestion getQuestion(int id){
        for(TQuestion q : questions){
            if(q.getID() == id){
                return q;
            }
        }
        return null;
    }
}
