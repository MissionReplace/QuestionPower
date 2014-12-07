/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.TerraStormDE.QuestionPower.Util;

import de.TerraStormDE.QuestionPower.Enums.TQuestionAssistantValue;
import de.TerraStormDE.QuestionPower.QuestionPower;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author Hannes
 */
public class TQuestionAssistant
{
    
    private Player player;
    private TQuestion question;
    private TQuestionAssistantValue current;
    
    private static List<TQuestionAssistant> as = new ArrayList<>();
    
    public TQuestionAssistant(Player p){
        this.player = p;
        current = TQuestionAssistantValue.QUESTION;
        question = new TQuestion();
        
        writeCurrent();
        as.add(this);
    }
    
    public Player getPlayer(){
        return player;
    }
    public TQuestionAssistantValue getStep(){
        return current;
    }
    public TQuestion getQuestion(){
        return question;
    }
    
    public void nextStep(){
        if(current.getStepID() == 4){
            as.remove(this);
            player.sendMessage(QuestionPower.getInstance().getPrefix() + "Question success created!");
            question.setActive(true);
            
            QuestionPower.getInstance().getQuestionLogger().log(player, question);
        } else {
            current = TQuestionAssistantValue.getByID(current.getStepID()+1);
            writeCurrent();
        }
    }
    
    private static boolean isPlayerValid(Player player){
        if(player == null || Bukkit.getPlayer(player.getName()) == null || !player.isOnline()){
            return false;
        }
        return true;
    }
    
    public void stopAssistant(){
        if(isPlayerValid(player)){
            player.sendMessage(QuestionPower.getInstance().getPrefix() + "Assistant closed.");
        }
        question.deleteQuestion();
        as.remove(this);
    }
    
    public void writeCurrent(){
        for(int i = 0; i < 100;i++){
            player.sendMessage(" ");
        }
        if(!isPlayerValid(player)){
            as.remove(this);
            return;
        }
        String prefix = QuestionPower.getInstance().getPrefix();
        
        player.sendMessage(prefix + "§a§l§m---------------------------");
        player.sendMessage(prefix + getStringState(TQuestionAssistantValue.QUESTION));
        player.sendMessage(prefix + getStringState(TQuestionAssistantValue.AUTO_CLOSE));
        player.sendMessage(prefix + getStringState(TQuestionAssistantValue.ANSWERS));
        player.sendMessage(prefix + getStringState(TQuestionAssistantValue.VOTE_ITEM));
        player.sendMessage(prefix + "To close the assistent write 'close'");
        player.sendMessage(prefix + "§a§l§m---------------------------");
    }
    private String getStringState(TQuestionAssistantValue value){
        return (value == current ? value.getStep() + ": §e" + value.getInstruction() : value.getStep() + ": " + value.getInstruction());
    }
    
    
    public static TQuestionAssistant getAssistant(Player p){
        for(TQuestionAssistant a : as){
            if(!isPlayerValid(a.getPlayer()) || !isPlayerValid(p)){
                break;
            }
            if(a.getPlayer().getName().equalsIgnoreCase(p.getName())){
                return a;
            }
        }
        return null;
    }
    
}
