/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.TerraStormDE.QuestionPower.Util;

import de.TerraStormDE.QuestionPower.QuestionPower;
import java.util.List;
import org.bukkit.Bukkit;

/**
 *
 * @author Hannes
 */
public class TQuestionTimer
{
    private final QuestionPower plugin;

    public TQuestionTimer(QuestionPower instance){
        this.plugin = instance;
    }
    
    public void start(){
        
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
        {

            @Override
            public void run()
            {
                List<TQuestion> list = TQuestion.getQuestions();
                for(TQuestion q : list){
                    if(!q.isActive()){
                        continue;
                    }
                    q.countDown();
                }
                
            }
        }, 20, 20);
    }
    
    
}
