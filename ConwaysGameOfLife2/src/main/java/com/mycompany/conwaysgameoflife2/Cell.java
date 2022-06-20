/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.conwaysgameoflife2;

import java.awt.Color;
import java.util.Random;

/**
 *
 * @author Westy
 * cell logic:
 * each cell gets a color based on neighbors count 1-4
 * if cell is dead (state = 0) it gets a default color
 */
public class Cell {
    //for testing perposes.
    Random Random = new Random();
    
    public int state = 0;
    private final Color Default_Color = new Color(13,17,18),
            N1_Color = new Color(51,51,51),
            N2_Color = new Color(58,91,91),
            N3_Color = new Color(91,124,124),
            N4_Color = new Color(147,226,226);
    public int Neighbors = 0;
    
    public Color CellColor;  
    
    public Cell(int state, int Neighbors){
        this.state = state;
        this.Neighbors = Neighbors;
        adapt();
    }
    public int state(){
        return this.state;
    }
    public Cell lives(int state){
        this.state = state;
        adapt();
        return this;
    }
    public Cell dies(int state){
        this.state = state;
        CellColor = Default_Color;
        return this;
    }
    
    public void adapt(){
        if(Neighbors == 0){
            this.CellColor = Default_Color;
        }
        
        //Neigbor color state
        if(Neighbors == 1){
            CellColor = N1_Color;
        }else if(Neighbors == 2){
            CellColor = N2_Color;
        }else if(Neighbors == 3){
            CellColor = N3_Color;
        }else if(Neighbors == 4){
            CellColor = N4_Color;
        }
        
        //advanced cell tructure system
    }
}
