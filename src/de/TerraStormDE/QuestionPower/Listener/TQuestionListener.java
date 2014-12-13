/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.TerraStormDE.QuestionPower.Listener;

import de.TerraStormDE.QuestionPower.Enums.TQuestionAssistantValue;
import de.TerraStormDE.QuestionPower.QuestionPower;
import de.TerraStormDE.QuestionPower.Util.TQuestionAnswer;
import de.TerraStormDE.QuestionPower.Util.TQuestionAssistant;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author Hannes
 */
public class TQuestionListener implements Listener
{
    private final QuestionPower plugin;
    
    public TQuestionListener(QuestionPower instance){
        this.plugin = instance;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void steps(AsyncPlayerChatEvent e){
        TQuestionAssistant assistant = TQuestionAssistant.getAssistant(e.getPlayer());
        if(assistant != null){
            if(e.getMessage().toLowerCase().contains("close")){
                assistant.stopAssistant();
                return;
            }
            e.setCancelled(true);
            switch(assistant.getStep()){
                case QUESTION:
                    assistant.getQuestion().setQuestion(e.getMessage());
                    assistant.nextStep();
                    break;
                case AUTO_CLOSE:
                    if(isInteger(e.getMessage())){
                        assistant.getQuestion().setAutoCloseDelay(Integer.valueOf(e.getMessage()));
                        assistant.nextStep();
                    } else {
                        e.getPlayer().sendMessage(plugin.getPrefix() + "Please write a valid number!");
                    }
                    break;
                case ANSWERS:
                    List<TQuestionAnswer> answers = new ArrayList<>();
                    if(e.getMessage().contains(" ")){
                        String[] values = e.getMessage().split(" ");
                        for(String s : values){
                            answers.add(new TQuestionAnswer(s));
                        }
                    } else {
                        answers.add(new TQuestionAnswer(e.getMessage()));
                    }
                    assistant.getQuestion().setAnswers(answers);
                    assistant.nextStep();
                    break;
            }
        }
    }
    
    @EventHandler
    public void interact(PlayerInteractEvent e){
        if(!e.getAction().toString().contains("RIGHT")){
            return;
        }
        TQuestionAssistant assistant = TQuestionAssistant.getAssistant(e.getPlayer());
        if(assistant != null){
            if(assistant.getStep() != TQuestionAssistantValue.VOTE_ITEM){
                e.setCancelled(true);
                return;
            }
            if(e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getType() != Material.AIR){
                assistant.getQuestion().setVoteItem(e.getPlayer().getItemInHand());
                assistant.nextStep();
            }
        }
    }
    
    @EventHandler
    public void command(PlayerCommandPreprocessEvent e){
        TQuestionAssistant assistant = TQuestionAssistant.getAssistant(e.getPlayer());
        if(assistant != null){
            e.setCancelled(true);
        }
    }
    
    private boolean isInteger(String s){
        try{
            Integer.valueOf(s);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    
}
