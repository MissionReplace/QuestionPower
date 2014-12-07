/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.TerraStormDE.QuestionPower.Commands;

import de.TerraStormDE.QuestionPower.QuestionPower;
import de.TerraStormDE.QuestionPower.Util.TQuestion;
import de.TerraStormDE.QuestionPower.Util.TQuestionAnswer;
import de.TerraStormDE.QuestionPower.Util.TQuestionAssistant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Hannes
 */
public class TCommands implements CommandExecutor
{
    private final QuestionPower plugin;
    
    public TCommands(QuestionPower instance){
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player)){
            return true;
        }
        Player p = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("qp")){
            if(args.length == 0){
                if(p.hasPermission("qp.info")){
                    p.sendMessage(plugin.getPrefix() + "§e/qp create");
                    p.sendMessage(plugin.getPrefix() + "§e/qp show");
                    return true;
                } else {
                    p.sendMessage(plugin.noPermissions());
                    return true;
                }
            } else if(args[0].equalsIgnoreCase("create")){
                if(p.hasPermission("qp.create")){
                    new TQuestionAssistant(p);
                    return true;
                } else {
                    p.sendMessage(plugin.noPermissions());
                    return true;
                }
            } else if(args[0].equalsIgnoreCase("show")){
                if(p.hasPermission("qp.show")){
                    if(TQuestion.getQuestions().size() > 0){
                        int lines = 0;
                        while(lines * 9 < TQuestion.getQuestions().size()){
                            lines++;
                        }
                        Inventory inv = Bukkit.createInventory(null, lines*9, "QuestionPower - List");
                        int slot = 0;
                        
                        for(TQuestion q : TQuestion.getQuestions()){
                            if(q.isActive()){
                                ItemStack i = q.getVoteItem();
                                i.setAmount(1);
                                ItemMeta im = i.getItemMeta();
                                im.setDisplayName("§eQuestionPower §7 - §e#" + q.getID());
                                
                                List<String> lore = new ArrayList<>();
                                lore.add(""); lore.add("§7Question: §e" + q.getQuestion());
                                lore.add("§7Answers:");
                                for(TQuestionAnswer a : q.getAnswers()){
                                    lore.add("  §7" + a.getName() + ": §e" + a.getVotes());
                                }
                                
                                im.setLore(lore);
                                
                                i.setItemMeta(im);
                                
                                
                                
                                inv.setItem(slot, i);
                                
                                slot++;
                            }
                        }
                        
                        p.openInventory(inv);
                        return true;
                    } else {
                        p.sendMessage(plugin.getPrefix() + "Sorry, there are no questions in the list!");
                        return true;
                    }
                } else {
                    p.sendMessage(plugin.noPermissions());
                    return true;
                }
            }
        }
        
        return false;
    }
    
}
