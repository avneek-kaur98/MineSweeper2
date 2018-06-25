package com.example.minesweepernew;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;

class MSButton extends AppCompatButton {
    private int Block = MainActivity.EMPTY;
    int row;
    int column;
    boolean flag = false;
    public int getBlock(){
        return this.Block;
    }

    public MSButton(Context context) {
        super(context);
    }

    public void setBlock(int block) {
        this.Block = block;
    }

    public void setRow_Column(int row,int column){
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public void setFlag(boolean value){
        this.flag = value;
    }

    public boolean isFlaged() {
        return this.flag;
    }
}
