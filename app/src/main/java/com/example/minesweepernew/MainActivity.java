package com.example.minesweepernew;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    LinearLayout root;
    ArrayList<LinearLayout> Rows;
    public int SIZE_R;
    public int SIZE_C;
    MSButton grid[][];
    int neighbur_row[] = {-1, -1, -1, 0, 0, 1, 1, 1};
    int neighbour_column[] = {-1, 0, 1, -1, 1, -1, 0, 1};
    boolean firstClick;
    public static int MINE = -1;
    public static int ONE = 1;
    public static int TWO = 2;
    public static int THREE = 3;
    public static int EMPTY = 0;


    private int Total_mines;
    static int curr_mines ;
    int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        level = intent.getIntExtra(Home_Screen.Level_Key, 0);

        SetLevel(level);


        setBoard();
    }

    private void SetLevel(int level) {
        if (level == 1) {
            SIZE_R = 6;
            SIZE_C = 4;
            Total_mines = 5;

        } else if (level == 2) {
            SIZE_R = 10;
            SIZE_C = 6;
            Total_mines = 10;
        }
    }


    public void setBoard() {
        root = findViewById(R.id.root);
        root.removeAllViews();
        Rows = new ArrayList<>();
        grid = new MSButton[SIZE_R][SIZE_C];
        curr_mines = Total_mines;
        firstClick = false;
        for (int i = 0; i < SIZE_R; i++) {
            LinearLayout row = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            row.setLayoutParams(layoutParams);
            Rows.add(row);
            root.addView(row);
        }


        for (int i = 0; i < SIZE_R; i++) {
            for (int j = 0; j < SIZE_C; j++) {
                MSButton button = new MSButton(this);
                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                button.setLayoutParams(layoutParams);
                button.setRow_Column(i, j);

                Rows.get(i).addView(button);
                button.setOnClickListener(this);
                button.setOnLongClickListener(this);
                grid[i][j] = button;
            }
        }
    }

    @Override
    public void onClick(View view) {
        MSButton button = (MSButton) view;
        if (!firstClick) {
            firstClick = true;
            setMines();
            setGrid();
           if(button.getBlock()==MINE){
                ChangeConfigurations(button);

            }

        }
        button.setText(button.getBlock() + "");

        button.setEnabled(false);
        checkGameStatus(button);
        if (button.getBlock() == EMPTY) {
            boolean visited[][] = new boolean[grid.length][grid[0].length];
            revealNeighbours(button, visited);
        }

    }

    private void checkGameStatus(MSButton button) {

        if (button.getBlock() == MINE) {
            //disable all button
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    grid[i][j].setText(grid[i][j].getBlock() + "");
                    grid[i][j].setEnabled(false);
                }
            }
            Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].isEnabled() && grid[i][j].getBlock() != MINE) {
                    return;
                }
            }
        }

        Toast.makeText(this, "Game Won", Toast.LENGTH_SHORT).show();

        //disable all buttons

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j].setEnabled(false);
            }
        }

    }

    private void setGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].getBlock() == MINE)
                    incBlock(grid[i][j]);
            }
        }
    }

    private void incBlock(MSButton msButton) {

        for (int i = 0; i < neighbur_row.length; i++) {
            if (check(msButton.getRow() + neighbur_row[i], msButton.getColumn() + neighbour_column[i])
                    && (grid[msButton.getRow() + neighbur_row[i]][msButton.getColumn() + neighbour_column[i]].getBlock() != MINE)) {
                grid[msButton.getRow() + neighbur_row[i]][msButton.getColumn() + neighbour_column[i]].
                        setBlock(grid[msButton.getRow() + neighbur_row[i]][msButton.getColumn() + neighbour_column[i]].getBlock() + 1);
            }
        }
    }

    private void revealNeighbours(MSButton button, boolean visited[][]) {
        if (button.isFlaged()) {
            visited[button.getRow()][button.getColumn()] = true;
            return;
        }
        if (!button.isFlaged()) {
            if (button.getBlock() == ONE || button.getBlock() == TWO || button.getBlock() == THREE) {
                button.setText(button.getBlock() + "");
                button.setEnabled(false);
                return;
            }
        }
        if (button.getBlock() == EMPTY) {
            button.setText(button.getBlock() + "");
            button.setEnabled(false);
        }

        if (button.getBlock() == MINE || button.isFlaged()) {
            return;
        }


        int row = button.getRow();
        int column = button.getColumn();
        visited[button.getRow()][button.getColumn()] = true;

        for (int i = 0; i < neighbur_row.length; i++) {
            if (check(button.getRow() + neighbur_row[i], button.getColumn() + neighbour_column[i]))
                if (!visited[button.getRow() + neighbur_row[i]][button.getColumn() + neighbour_column[i]])
                    revealNeighbours(grid[button.getRow() + neighbur_row[i]][button.getColumn() + neighbour_column[i]], visited);
        }
    }


    private void ChangeConfigurations(MSButton button) {


        button.setBlock(EMPTY);
        int row = button.getRow();
        int column = button.getColumn();

        for (int i = 0; i < neighbur_row.length; i++) {
            if (check(button.getRow() + neighbur_row[i], button.getColumn() + neighbour_column[i])) {
                if (grid[row + neighbur_row[i]][column + neighbour_column[i]].getBlock() == MINE) {
                    button.setBlock(button.getBlock() + 1);
                }

                if (grid[row + neighbur_row[i]][column + neighbour_column[i]].getBlock() != MINE)
                    grid[row + neighbur_row[i]][column + neighbour_column[i]]
                            .setBlock(grid[row + neighbur_row[i]][column + neighbour_column[i]].getBlock() - 1);

            }
        }

        grid[0][0].setBlock(MINE);

        for (int i = 0; i < neighbur_row.length; i++) {
            if (check(neighbur_row[i], neighbour_column[i])) {
                if (grid[0 + neighbur_row[i]][0 + neighbour_column[i]].getBlock() != MINE) {
                    grid[0 + neighbur_row[i]][neighbour_column[i]].setBlock(grid[0 + neighbur_row[i]][neighbour_column[i]].getBlock() + 1);
                }

            }
        }


    }

    private boolean check(int neighbur_row, int neighbour_column) {
        if (neighbur_row >= 0 && neighbur_row < SIZE_R) {
            if (neighbour_column >= 0 && neighbour_column < SIZE_C) {
                return true;
            }
        }
        return false;
    }

    private boolean check(MSButton msButton) {

        if (msButton.getRow() >= 0 && msButton.getRow() < SIZE_R) {
            if (msButton.getColumn() >= 0 && msButton.getColumn() < SIZE_C) {
                return true;
            }
        }
        return false;
    }

    private void setMines() {
        Random random = new Random();
        int row[] = new int[Total_mines];
        int column[] = new int[Total_mines];


        int i = 0;
        boolean isSame = false;
        while (i < Total_mines) {
            int x = random.nextInt(SIZE_R - 1);
            int y = random.nextInt(SIZE_C - 1);
            isSame = false;
            if (x == 0 && y == 0) {
                continue;
            }
                for (int j = 0; j < i; j++) {
                    if ((row[j] == x) && (column[j] == y)) {
                        isSame = true;
                    }
                }
                if(!isSame) {
                    row[i] = x;
                    column[i] = y;
                    grid[row[i]][column[i]].setBlock(MINE);
                    i++;
                }
            }

    }

    @Override
    public boolean onLongClick(View view) {
        MSButton button = (MSButton) view;


        if (!button.isFlaged()) {
            button.setText("F");
            button.setFlag(true);
            Toast.makeText(this, curr_mines + " mines left", Toast.LENGTH_LONG).show();
            curr_mines--;
        } else {
            button.setText("");
            button.setFlag(false);
            Toast.makeText(this, curr_mines + " mines left", Toast.LENGTH_LONG).show();
            curr_mines++;
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(R.id.reset==id){
            setBoard();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
}


