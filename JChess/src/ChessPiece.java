enum Player {
    WHITE,
    BLACK,
}

enum Rank {
    KING,
    QUEEN,
    BISHOP,
    ROOK,
    KNIGHT,
    PAWN,
}

public class ChessPiece {
    private int col;
    private int row;
    private Player player;
    private Rank rank;
    private String imgName;

    public ChessPiece(int col, int row, Player player, Rank rank, String imgName) {
        super();
        this.col = col;
        this.row = row;
        this.player = player;
        this.rank = rank;
        this.imgName = imgName;
    }

    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }


    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }


    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }


    public Rank getRank() {
        return rank;
    }
    public void setRank(Rank rank) {
        this.rank = rank;
    }


    public String getImgName() {
        return imgName;
    }
    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}

