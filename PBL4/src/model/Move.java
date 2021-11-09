package model;

public class Move {
    public int Flag = 0;
    public int StartSquare;
    public int TargetSquare;

    public Move(int startSquare,int targetSquare){
        StartSquare = startSquare;
        TargetSquare = targetSquare;
    }
    public Move(int startSquare,int targetSquare,int Flag){
        StartSquare = startSquare;
        TargetSquare = targetSquare;
        this.Flag = Flag;
    }
}
