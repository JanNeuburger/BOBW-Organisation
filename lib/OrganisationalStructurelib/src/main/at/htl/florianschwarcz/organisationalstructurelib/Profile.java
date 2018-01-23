/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htl.florianschwarcz.organisationalstructurelib;

import java.util.HashMap;

/**
 *
 * @author Florian Schwarcz
 */
public class Profile {
    private HashMap<String, Integer> attributes;
    
    public Profile(){
    }

    public HashMap<String, Integer> getAttributes(){
        return attributes;
    }

    /**
     * Adds a new attribute to the set or updates the value if it already exists.
     * @param name
     * @param value
     */
    public void addAttribute(String name, int value){
        if(attributes == null){
            attributes = new HashMap<>();
        }
        if(attributes.containsKey(name)){
            changeValue(name, value);
            return;
        }
        attributes.put(name, value);
    }

    /**
     * Adds some attributes to the set.
     * @param name
     * @param value
     * @return False if name and value have different lengths.
     */
    public boolean addAttributes(String[] name, int[] value){
        if(name.length != value.length){
            return false;
        }
        if(attributes == null){
            attributes = new HashMap<>();
        }
        for(int i = 0; i < name.length; i++){
            if(attributes.containsKey(name[i])){
                changeValue(name[i], value[i]);
            }else{
                attributes.put(name[i], value[i]);
            }
        }
        return true;
    }

    /**
     * Changes the value of an existing attribute.
     * @param name
     * @param value
     * @return True if successfully changed, false if not in set.
     */
    public boolean changeValue(String name, int value){
        if(attributes == null){
            return false;
        }
        Integer currValue = attributes.get(name);
        if(currValue == null){
            return false;
        }
        currValue = value;
        return true;
    }

    /**
     * Returns the value of an attribute.
     * @param name
     * @return Value of the attribute, -1 if not in set.
     */
    public int getValue(String name){
        if(attributes == null){
            return -1;
        }
        Integer value = attributes.get(name);
        if(value == null){
            return -1;
        }
        return value;
    }

    /**
     * Compares two profiles using the difference of
     * all attributes which are in both sets.
     * @param p1
     * @param p2
     * @return
     */
    public static int compareProfilesSoft(Profile p1, Profile p2){
        int score = 0;
        for(String key : p1.getAttributes().keySet()){
            if(p2.getValue(key) != -1){
                score += p1.getValue(key) - p2.getValue(key);
            }
        }
        return score;
    }

    /**
     * Compares two profiles using the differenct of all
     * attributes, if p2 does not contain an attribute of p1
     * the score is decrased by the value of p2.
     * @param p1
     * @param p2
     * @return
     */
    public static int compareProfilesHard(Profile p1, Profile p2){
        int score = 0;
        for(String key : p1.getAttributes().keySet()){
            if(p2.getValue(key) != -1) {
                score += p1.getValue(key) - p2.getValue(key);
            }else{
                score -= p2.getValue(key);
            }
        }
        return score;
    }

    /**
     * Compares two profiles and returns which only has
     * higher attributes.
     * @param p1
     * @param p2
     * @return 1, if p1 has only higher or equal attributes, -1 if p2 has only higher or equal attributes, 0 if no one has only higher or equal attributes
     */
    public static int compareAllHigherOrEqual(Profile p1, Profile p2){
        int higher = 0;
        for(String key : p1.getAttributes().keySet()){
            if(p2.getValue(key) != -1){
                if(p1.getValue(key) > p2.getValue(key) && higher == -1){
                    return 0;
                }
                if(p1.getValue(key) < p2.getValue(key) && higher == 1){
                    return 0;
                }
                if(p1.getValue(key) > p2.getValue(key)){
                    higher = 1;
                }
                else if(p1.getValue(key) < p2.getValue(key)){
                    higher = -1;
                }
            }
        }
        return higher;
    }
}
