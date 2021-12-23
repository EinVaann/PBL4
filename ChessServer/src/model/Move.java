package model;

public class Move {
    public int Flag = 0;
    //Nonrmal movec                 0
    //Castle king side              1
    //Castle queen side             2
    //En Passant                    4
    //Pawn double move              3
    //Promote                       5
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
