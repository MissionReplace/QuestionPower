/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.TerraStormDE.QuestionPower;

import de.TerraStormDE.QuestionPower.Commands.TCommands;
import de.TerraStormDE.QuestionPower.Listener.TQuestionListener;
import de.TerraStormDE.QuestionPower.Listener.TInventoryListener;
import de.TerraStormDE.QuestionPower.Util.TQuestionLog;
import de.TerraStormDE.QuestionPower.Util.TQuestionTimer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Hannes
 */
public class QuestionPower extends JavaPlugin
{
    
    private static QuestionPower instance;
    
    private TQuestionLog log;

    @Override
    public void onEnable()
    {
        instance = this;
        log = new TQuestionLog(instance);
        
        new TQuestionListener(instance);
        new TInventoryListener(instance);
        new TQuestionTimer(instance).start();
        getCommand("qp").setExecutor(new TCommands(instance));
    }

    @Override
    public void onDisable()
    {
        try
        {
            getQuestionLogger().save();
        } catch (IOException ex)
        {
            Logger.getLogger(QuestionPower.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getPrefix(){
        return "§8[§eQuestionPower§8]§7 ";
    }
    public String noPermissions(){
        return getPrefix() + "§cNo permissions!";
    }
    public static QuestionPower getInstance(){
        return instance;
    }
    public TQuestionLog getQuestionLogger(){
        return log;
    }
    
}
