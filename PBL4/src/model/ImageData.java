package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class ImageData {
    public static ImageData Instance;
    public Map<Integer, Image> keyNameValueImage = new HashMap<Integer, Image>();
    private ImageData(){
        int[] imageNames = {
                Piece.Black+Piece.Bishop,
                Piece.White+Piece.Bishop,
                Piece.Black+Piece.King,
                Piece.White+Piece.King,
                Piece.Black+Piece.Knight,
                Piece.White+Piece.Knight,
                Piece.Black+Piece.Pawn,
                Piece.White+Piece.Pawn,
                Piece.Black+Piece.Queen,
                Piece.White+Piece.Queen,
                Piece.Black+Piece.Rook,
                Piece.White+Piece.Rook,
        };
        try {
            for (int imageName : imageNames) {
                //out.println(imageName);
                Image img = loadImage("resource\\"+imageName + ".png");
                keyNameValueImage.put(imageName, img);
            }
            //out.println(keyNameValueImage.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ImageData getInstance(){
        if(Instance==null){
            Instance=new ImageData();
        }
        return Instance;
    }

    private Image loadImage(String imgFileName) throws Exception {
       /* ClassLoader classLoader = getClass().getClassLoader();
        URL resURL = classLoader.getResource(imgFileName);*/
        /*if (resURL == null) {
            return null;
        } else {*/
        File imgFile = new File(imgFileName);
        return ImageIO.read(imgFile);
        //}
    }

}
