/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.conwaysgameoflife2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Westy
 */
public class CustomCellsGrid extends JPanel implements ActionListener{
    //Stats
    public Extract ex;
    //Cell size
    private final int Size = 7;
    //Default cell boder
    private final Color defaultBoderColor = new Color(102,102,102);
    //Display size
    private final  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    //screen variables
    public final int width = (int)screenSize.getWidth(), height = (int)screenSize.getHeight();
    public final int scaler = Size;
    
    //Cells and Computation
    private Cell[][] FirstGenCells, SecondGenCells = new Cell[width/scaler][height/scaler];
    private final Random Random = new Random();
      
    private Timer Timer;
    private final int Delay = 8;
    public CustomCellsGrid(){
        Timer = new Timer(Delay,this);
        Timer.start();
        setFocusTraversalKeysEnabled(false);
        
        //populate cells array with cells with random states
        FirstGenCells = getRandomCells();
        ex = new Extract(FirstGenCells);
        ex.update();
    }
    private Cell[][] getRandomCells(){
        Cell[][] newArray = new Cell[width/scaler][height/scaler];
        for (Cell[] newArray1 : newArray) {
            for (int k = 0; k < newArray1.length; k++) {
                newArray1[k] = new Cell(Random.nextInt(2),0);
            }
        }
        return newArray;
    }
    private Cell[][] resetGenCells(Cell[][] GenCells){
        GenCells = new Cell[width/scaler][height/scaler];
        for (Cell[] newArray1 : GenCells) {
            for (int k = 0; k < newArray1.length; k++) {
                newArray1[k] = new Cell(0,0);
            }
        }
        return GenCells;
    }
    private int count=0;
    public void paint(Graphics g){
        //Backgound
        g.setColor(new Color(13,17,18));
        g.fillRect(this.getX(),this.getY(),this.getWidth(),this.getHeight());
        
        //cell grid
        drawGrid(g,width,height);
        //compute cells
        if(count >0){
          computeNextGenCells(SecondGenCells,FirstGenCells); 
          count = 0;
        }else{
            computeNextGenCells(FirstGenCells, SecondGenCells);  
            count ++;
        }
        ex.update();
    }
    
    private void computeNextGenCells(Cell[][] FirstGenCells, Cell[][] SecondGenCells){
        for(int x=0;x<FirstGenCells.length;x++){
            for(int y=0;y<FirstGenCells[x].length;y++){
                    int Sum = 0;
                    for(int i=-1;i<2;i++){
                        for(int k=-1;k<2;k++){
                            int row = (x + i+FirstGenCells.length)%FirstGenCells.length;
                            int col = (y + k + FirstGenCells[x].length)%FirstGenCells[x].length;
                            Sum += FirstGenCells[row][col].state;
                        }
                    }
                    Sum -= FirstGenCells[x][y].state;
                    
                    //Get Cell State
                    if(FirstGenCells[x][y].state == 0 && Sum == 3){
                        SecondGenCells[x][y] = new Cell(1,Sum);
                    }else if(FirstGenCells[x][y].state == 1 && Sum >1 && Sum < 4){
                        SecondGenCells[x][y]=new Cell(1,Sum);
                    }else{
                        SecondGenCells[x][y]=new Cell(0,Sum);
                    }
            }
        }
        resetGenCells(FirstGenCells);
    }
    
    public void drawGrid(Graphics g, int width, int height){
        int x = 0, y=0;
        int parx=0,pary=0;
        g.setColor(defaultBoderColor);
        for(int i=0;i<height;i+=Size){
            for(int k=0;k<width;k+=Size){
                //render cells
                if(parx< FirstGenCells.length && pary < FirstGenCells[0].length){
                    g.setColor(FirstGenCells[parx][pary].CellColor);
                    g.fillOval(x, y, Size,Size); 
                }
                x += Size;
                parx++;
            }
            y += Size;
            x=0;
            pary++;
            parx=0;
        }
        if(this.isVisible()){
            if(!this.ex.Stat.isVisible()){
                this.ex.Stat.show();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Timer.start();
        repaint();
    }
}

class Extract{
    //STAT
    public STAT Stat = new STAT();
    //Cells
    private Cell[][] GenCells;
    
    public Extract(Cell[][] GenCells){
        this.GenCells = GenCells;
    }
    
    //getTotal Cells
    public int getTotalCells(){
        return GenCells.length*GenCells[0].length;
    }
    //getLiving Cells
    public int getLivingCells(){
        int count = 0;
        for(int x=0;x<GenCells.length;x++){
            for(int y=0;y<GenCells[x].length;y++){
                if(GenCells[x][y].state == 1){
                    count++;
                }
            }
        }
        return count;
    }
    //getCellClusteres
    public int getCellClusters(){
        int count = 0;
        for(int x=0;x<GenCells.length;x++){
            for(int y=0;y<GenCells[x].length;y++){
                if(GenCells[x][y].state == 1){
                    if(x > 0 && x <GenCells.length-1 && y > 0 && y <GenCells[x].length-1){
                        if(GenCells[x][y].state==1&& GenCells[x+1][y].state==0
                                &&GenCells[x][y+1].state==0&&GenCells[x+1][y+1].state==0){
                            count ++;
                        }else if(GenCells[x][y].state==1&& GenCells[x+1][y].state==1
                                &&GenCells[x][y+1].state==1&&GenCells[x+1][y+1].state==0){
                            count --;
                        }
                    }
                }
            }
        }
        return count;
    }
    
    //update
    public void update(){
        this.Stat.updateStat(getTotalCells(), getLivingCells(), getCellClusters());
    }
}

