package com.example.minesweepernew;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout root;
    ArrayList<LinearLayout> Rows;
    public int SIZE_R = 10;
    public int SIZE_C = 8;
    MSButton grid[][];
    int neighbur_row[] = {-1,-1,-1,0,0,1,1,1};
    int neighbour_column[] = {-1,0,1,-1,1,-1,0,1};
    boolean firstClick;
    public static int MINE = -1;
    public static int ONE = 1;
    public static int TWO = 2;
    public static int THREE = 3;
    public static int EMPTY = 0;

    private int Total_mines= 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setBoard();
    }


    public void setBoard(){
        root = findViewById(R.id.root);
        Rows = new ArrayList<>();
        grid = new MSButton[SIZE_R][SIZE_C];
        firstClick = false;
        for(int i=0;i<SIZE_R;i++){
            LinearLayout row = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
            row.setLayoutParams(layoutParams);
            Rows.add(row);
            root.addView(row);
        }



        for(int i=0;i<SIZE_R;i++){
            for(int j=0;j<SIZE_C;j++){
                MSButton button = new MSButton(this);
                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
                button.setLayoutParams(layoutParams);
                button.setRow_Column(i,j);

                Rows.get(i).addView(button);
                button.setOnClickListener(this);
                grid[i][j] = button;
            }
        }
    }

    @Override
    public void onClick(View view) {
        MSButton button = (MSButton)view;
        if(!firstClick){
            firstClick = true;
            setMines();
            if(button.getBlock()==MINE){
                ChangeConfigurations(button);
            }
        }
        button.setText(button.getBlock()+"");
    }

    private void ChangeConfigurations(MSButton button) {


        button.setBlock(EMPTY);
       int row= button.getRow();
        int column = button.getColumn();

        for(int i=0;i<neighbur_row.length;i++){
            if(check(button.getRow()+neighbur_row[i],button.getColumn()+neighbour_column[i])){
                if(grid[row+neighbur_row[i]][column+neighbour_column[i]].getBlock()==MINE){
                    button.setBlock(button.getBlock()+1);
                }
                else{
                    grid[row+neighbur_row[i]][column+neighbour_column[i]].setBlock(grid[row+neighbur_row[i]][column+neighbour_column[i]].getBlock()-1);
                }
            }
        }

        for(int i=0;i<neighbur_row.length;i++){
            if(check(neighbur_row[i],neighbour_column[i])){
                if(grid[0+neighbur_row[i]][0+neighbour_column[i]].getBlock()!=MINE){
                   grid[0+neighbur_row[i]][neighbour_column[i]].setBlock(grid[0+neighbur_row[i]][neighbour_column[i]].getBlock()+1);
                }

            }
        }



    }

    private boolean check(int neighbur_row, int neighbour_column) {
        if(neighbur_row>=0 &&neighbur_row<SIZE_R ){
            if(neighbour_column>=0 && neighbour_column<SIZE_C){
                return true;
            }
        }
        return false;
    }

    private boolean check(MSButton msButton) {

        if(msButton.getRow()>=0 && msButton.getRow()<SIZE_R ){
            if(msButton.getColumn()>=0 && msButton.getColumn()<SIZE_C){
                return true;
            }
        }
        return false;
    }

    private void setMines() {
        Random random = new Random();
        int row[] = new int[Total_mines];
        int column[] = new int[Total_mines];


      int i=0;
      while(i<Total_mines){
          int x =random.nextInt(SIZE_R-1);
          int y = random.nextInt(SIZE_C-1);
          if(x==0 && y==0){
              continue;
          }else{
              row[i] = x;
              column[i] = y;
              grid[row[i]][column[i]].setBlock(MINE);
              i++;

          }
      }
    }
}
