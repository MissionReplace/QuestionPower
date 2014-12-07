/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.TerraStormDE.QuestionPower.Util;

import de.TerraStormDE.QuestionPower.QuestionPower;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Hannes
 */
public class TQuestionLog
{
    
    private File f,restore;
    private final QuestionPower plugin;
    
    public TQuestionLog(QuestionPower instance){
        this.plugin = instance;
        
        plugin.getDataFolder().mkdir();
        f = new File(plugin.getDataFolder(),"log.yml");
        restore = new File(plugin.getDataFolder(),"tmp.dat");
        
        if(!f.exists()){
            try
            {
                f.createNewFile();
            } catch (IOException ex)
            {
                Logger.getLogger(TQuestionLog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try
        {
            load();
            restore.delete();
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(TQuestionLog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(TQuestionLog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void log(Player start,TQuestion question){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd - HH:mm:ss");
        Date d = new Date();
        String datum = format.format(d);
        
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f,true));
            writer.write(datum + ": " + start.getName() + " has created a question (" + question.getQuestion() + ")");
            writer.write("\n");
            
            writer.flush();
            writer.close();
        } catch (IOException ex)
        {
            Logger.getLogger(TQuestionLog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void save() throws IOException {
        if(!restore.exists()){
            restore.createNewFile();
        }
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(restore,true));
        for(TQuestion q : TQuestion.getQuestions()){
            writer.write(q.getQuestion() + ";" + 
                    q.getAutoCloseDelay() + ";" + 
                    q.getVoteItem().getTypeId() +
                    (q.getVoteItem().getData().getData() == 0 ? "" : ":" + q.getVoteItem().getData().getData()) + (q.getVotes().isEmpty() ? "" : ";"));
            
            for(Map.Entry<String,TQuestionAnswer> entry : q.getVotes().entrySet()){
                if(q.getVotes().size() == 1){
                    writer.write(entry.getKey() + "," + entry.getValue().getName());
                } else {
                    writer.write(entry.getKey() + "," + entry.getValue().getName() + "|");
                }
                
            }
            
            writer.write(";");
            
            for(TQuestionAnswer a : q.getAnswers()){
                writer.write(a.getName() + (q.getAnswers().size() == 1 ? "" : ","));
            }
            
            writer.write("\n");
        }
        writer.flush();
        writer.close();
       
    }
    public void load() throws FileNotFoundException, IOException{
        if(restore.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(restore));
            String line;
            while((line = reader.readLine()) != null){
                if(!line.isEmpty()){
                    TQuestion q = new TQuestion();
                    
                    String[] values = line.split(";");
                    String question = values[0];
                    int delay = Integer.valueOf(values[1]);
                    ItemStack vote = toItemStack(values[2]);
                    
                    HashMap<String,String> player_list = new HashMap<>();
                    ArrayList<String> answers = new ArrayList<>();
                    
                    if(values.length >= 4){
                        String players = values[3];
                        if(players.contains("|")){
                            String[] parts = players.split("|");
                            for(String s : parts){
                                String[] p_parts = s.split(",");
                                if(!player_list.containsKey(p_parts[0])){
                                    player_list.put(p_parts[0],p_parts[1]);
                                }
                            }
                        } else {
                            String[] parts = players.split(",");
                            player_list.put(parts[0], parts[1]);
                        }
                    }
                    
                    if(values.length >= 5){
                        String answers_str = values[4];
                        String[] parts = answers_str.split(",");
                        for(String s : parts){
                            answers.add(s);
                        }
                    }
                    
                    q.setQuestion(question);
                    q.setAutoCloseDelay(delay);
                    q.setVoteItem(vote);
                    for(String s : answers){
                        q.addAnswer(new TQuestionAnswer(s));
                    }
                    for(Map.Entry<String,String> entry : player_list.entrySet()){
                        q.addRawVote(entry.getKey(), entry.getValue());
                    }
                    q.setActive(true);
                }
            }
            
            reader.close();
        }
    }
    private ItemStack toItemStack(String input){
        int id = 0;
        short data = 0;
        
        if(input.contains(":")){
            String[] values = input.split(":");
            id = Integer.valueOf(values[0]);
            data = Short.valueOf(values[1]);
            
        } else {
            id = Integer.valueOf(input);
        }
        
        return new ItemStack(Material.getMaterial(id),1,data);
    }
}
