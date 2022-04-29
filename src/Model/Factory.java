package Model;

/**
 * Factory
 * @author smasson
 * @author ymouacha
 */
public class Factory {

    public static Boolean[][] creatMotif1(){
        Boolean[][] motif1 = new Boolean[10][10];
        for (int i = 0; i < motif1.length; i++) {
            for (int j = 0; j < motif1.length; j++) {
                motif1[i][j] = true;
            }
        }
        return motif1;
    }

    public static Boolean[][] creatMotif2(){
        Boolean[][] motif1 = new Boolean[10][10];
        for (int i = 0; i < motif1.length; i++) {
            for (int j = 0; j < motif1.length; j++) {
                motif1[i][j] = false;
            }
        }
        motif1[2][3] = true;
        motif1[2][4] = true;
        motif1[2][5] = true;
        motif1[2][6] = true;

        motif1[3][2] = true;
        motif1[4][2] = true;
        motif1[5][2] = true;
        motif1[6][2] = true;

        motif1[3][7] = true;
        motif1[4][7] = true;
        motif1[5][7] = true;
        motif1[6][7] = true;

        motif1[7][3] = true;
        motif1[7][4] = true;
        motif1[7][5] = true;
        motif1[7][6] = true;

        return motif1;
    }
}
