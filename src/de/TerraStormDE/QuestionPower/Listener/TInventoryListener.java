/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.TerraStormDE.QuestionPower.Listener;

import de.TerraStormDE.QuestionPower.QuestionPower;
import de.TerraStormDE.QuestionPower.Util.TQuestion;
import de.TerraStormDE.QuestionPower.Util.TQuestionAnswer;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Hannes
 */
public class TInventoryListener implements Listener
{
    private final QuestionPower plugin;
    
    public TInventoryListener(QuestionPower instance){
        this.plugin = instance;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void click(InventoryClickEvent e){
        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR){
            return;
        }
        Player p = (Player) e.getWhoClicked();
        ItemStack current = e.getCurrentItem();
        
        if(e.getInventory().getName().equalsIgnoreCase("QuestionPower - List")){
            e.setCancelled(true);
            String display = ChatColor.stripColor(current.getItemMeta().getDisplayName());
            String[] values = display.split("#");
            int id = Integer.valueOf(values[1]);
            
            TQuestion q = TQuestion.getQuestion(id);
            if(q != null){
                e.getView().close();
                
                Inventory inv = Bukkit.createInventory(null, 9, "QuestionPower - Answers #" + q.getID());
                for(TQuestionAnswer a : q.getAnswers()){
                    ItemStack add = new ItemStack(Material.SIGN);
                    ItemMeta im = add.getItemMeta();
                    im.setDisplayName("§e§l" + a.getName());
                    im.setLore(Arrays.asList("§7Votes: §e" + a.getVotes()));
                    add.setItemMeta(im);
                    
                    inv.addItem(add);
                }
                if(p.hasPermission("qp.close")){
                    ItemStack close = new ItemStack(Material.WOOL,1,(short) DyeColor.RED.getData());
                    ItemMeta closem = close.getItemMeta();
                    closem.setDisplayName("§4§lClose");
                    close.setItemMeta(closem);
                    
                    inv.setItem(8,close);
                }
                
                
                
                p.openInventory(inv);
            }
        } else if(e.getInventory().getName().contains("QuestionPower - Answers")){
            e.setCancelled(true);
            
            String inv_name = e.getInventory().getName();
            String[] values = inv_name.split("#");
            int id = Integer.valueOf(values[1]);
            TQuestion q = TQuestion.getQuestion(id);
            if(q == null){
                e.getView().close();
                return;
            }
            
            if(e.getCurrentItem().getType() == Material.WOOL){
                q.closeQuestion(true);
                e.getView().close();
                return;
            }
            
            String display = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            for(TQuestionAnswer a : q.getAnswers()){
                if(a.getName().equalsIgnoreCase(display)){
                    q.addVote(p, display);
                    e.getView().close();
                    break;
                }
            }
        }
    }
    
    private int getProzent(int gwert,int pwert){
        return (int) (pwert*100) / gwert;
    }
    
}
