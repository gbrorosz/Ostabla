/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ostabla;

/**
 *
 * @author Gabor
 */
public class Statisztika extends javax.swing.JFrame {
    
    // %-ban is kifejezve
    int nyertJatekJatekPl;        // a jatek alatt 0, a vegen 0 vagy 1
    int nyertJatekJatekAi;        // a jatek alatt 0, a vegen 0 vagy 1
    int nyertJatekMeccsPl;
    int nyertJatekMeccsAi;
    int nyertJatekOsszPl;
    int nyertJatekOsszAi;
    
    // lejatszott jatekok osszesitve (pl altal nyert + ai altal nyert) * jatek, meccs, ossz
    
    int gammonJatekPl;      // a jatek alatt 0
    int gammonJatekAi;      // a jatek alatt 0
    int gammonMeccsPl;
    int gammonMeccsAi;
    int gammonOsszPl;
    int gammonOsszAi;
    
    int backGammonJatekPl;      // a jatek alatt 0
    int backGammonJatekAi;      // a jatek alatt 0
    int backGammonMeccsPl;
    int backGammonMeccsAi;
    int backGammonOsszPl;
    int backGammonOsszAi;
    
    int nyertPontokJatekPl;      // a jatek alatt 0, a vegen 0 (ha vesztett) vagy tet * bg szorzo (ha nyert)
    int nyertPontokJatekAi;      // a jatek alatt 0, a vegen 0 (ha vesztett) vagy tet * bg szorzo (ha nyert)
    int nyertPontokMeccsPl;
    int nyertPontokMeccsAi;
    int nyertPontokOsszPl;
    int nyertPontokOsszAi;
    
    // %-ban is kifejezve
    int nyertMeccsMeccsPl;      // 0 vagy 1
    int nyertMeccsMeccsAi;      // 0 vagy 1
    int nyertMeccsOsszPl;
    int nyertMeccsOsszAi;
    
    int[] dobottKockakJatekPl = new int[7];         // minden dobott kocka, a kezdok is. Mindegy, hogy le lett-e lepve, duplak nelkul
    int[] dobottKockakJatekAi = new int[7];
    int[] dobottKockakMeccsPl = new int[7];
    int[] dobottKockakMeccsAi = new int[7];
    int[] dobottKockakOsszPl = new int[7];
    int[] dobottKockakOsszAi = new int[7];
    
    int duplakJatekPl;
    int duplakJatekAi;
    int duplakMeccsPl;
    int duplakMeccsAi;
    int duplakOsszPl;
    int duplakOsszAi;
    
//    dobott kockak osszerteke (* pl, ai * jatek, meccs, ossz), duplakkal (2 db 5-os 20-nak szamit), lepesek.ertek osszege
    int dobottKockakErtekJatekPl;
    int dobottKockakErtekJatekAi;
    int dobottKockakErtekMeccsPl;
    int dobottKockakErtekMeccsAi;
    int dobottKockakErtekOsszPl;
    int dobottKockakErtekOsszAi;
    
    // %-ban is kifejezve
    int kezdesJogaJatekPl;      // 0 vagy 1
    int kezdesJogaJatekAi;      // 0 vagy 1
    int kezdesJogaMeccsPl;
    int kezdesJogaMeccsAi;
    int kezdesJogaOsszPl;
    int kezdesJogaOsszAi;
    
    java.util.Date ido;
    String utolsoTorles = "";
    
    /**
     * Creates new form Statisztika
     */
    public Statisztika() {
        initComponents();
        
        setLocationRelativeTo(((Tabla)Tabla.getFrames()[0]));
    }
    
    // frissiti a cimkeket a fenti valtozoknak megfeleloen
    void setLabels() {
        nyertJatekJatekPlLabel.setText(String.valueOf(nyertJatekJatekPl));
        nyertJatekJatekAiLabel.setText(String.valueOf(nyertJatekJatekAi));
        nyertJatekMeccsPlLabel.setText(String.valueOf(nyertJatekMeccsPl));
        nyertJatekMeccsAiLabel.setText(String.valueOf(nyertJatekMeccsAi));
        nyertJatekOsszPlLabel.setText(String.valueOf(nyertJatekOsszPl));
        nyertJatekOsszAiLabel.setText(String.valueOf(nyertJatekOsszAi));
        
        gammonJatekPlLabel.setText(String.valueOf(gammonJatekPl));
        gammonJatekAiLabel.setText(String.valueOf(gammonJatekAi));
        gammonMeccsPlLabel.setText(String.valueOf(gammonMeccsPl));
        gammonMeccsAiLabel.setText(String.valueOf(gammonMeccsAi));
        gammonOsszPlLabel.setText(String.valueOf(gammonOsszPl));
        gammonOsszAiLabel.setText(String.valueOf(gammonOsszAi));
        
        backGammonJatekPlLabel.setText(String.valueOf(backGammonJatekPl));
        backGammonJatekAiLabel.setText(String.valueOf(backGammonJatekAi));
        backGammonMeccsPlLabel.setText(String.valueOf(backGammonMeccsPl));
        backGammonMeccsAiLabel.setText(String.valueOf(backGammonMeccsAi));
        backGammonOsszPlLabel.setText(String.valueOf(backGammonOsszPl));
        backGammonOsszAiLabel.setText(String.valueOf(backGammonOsszAi));
        
        nyertPontokJatekPlLabel.setText(String.valueOf(nyertPontokJatekPl));
        nyertPontokJatekAiLabel.setText(String.valueOf(nyertPontokJatekAi));
        nyertPontokMeccsPlLabel.setText(String.valueOf(nyertPontokMeccsPl));
        nyertPontokMeccsAiLabel.setText(String.valueOf(nyertPontokMeccsAi));
        nyertPontokOsszPlLabel.setText(String.valueOf(nyertPontokOsszPl));
        nyertPontokOsszAiLabel.setText(String.valueOf(nyertPontokOsszAi));
        
        nyertMeccsMeccsPlLabel.setText(String.valueOf(nyertMeccsMeccsPl));
        nyertMeccsMeccsAiLabel.setText(String.valueOf(nyertMeccsMeccsAi));
        nyertMeccsOsszPlLabel.setText(String.valueOf(nyertMeccsOsszPl));
        nyertMeccsOsszAiLabel.setText(String.valueOf(nyertMeccsOsszAi));
        
        dobottKockakJatek1Label.setText(String.valueOf(dobottKockakJatekPl[1]) + " : " + String.valueOf(dobottKockakJatekAi[1]));
        dobottKockakJatek2Label.setText(String.valueOf(dobottKockakJatekPl[2]) + " : " + String.valueOf(dobottKockakJatekAi[2]));
        dobottKockakJatek3Label.setText(String.valueOf(dobottKockakJatekPl[3]) + " : " + String.valueOf(dobottKockakJatekAi[3]));
        dobottKockakJatek4Label.setText(String.valueOf(dobottKockakJatekPl[4]) + " : " + String.valueOf(dobottKockakJatekAi[4]));
        dobottKockakJatek5Label.setText(String.valueOf(dobottKockakJatekPl[5]) + " : " + String.valueOf(dobottKockakJatekAi[5]));
        dobottKockakJatek6Label.setText(String.valueOf(dobottKockakJatekPl[6]) + " : " + String.valueOf(dobottKockakJatekAi[6]));
        
        dobottKockakMeccs1Label.setText(String.valueOf(dobottKockakMeccsPl[1]) + " : " + String.valueOf(dobottKockakMeccsAi[1]));
        dobottKockakMeccs2Label.setText(String.valueOf(dobottKockakMeccsPl[2]) + " : " + String.valueOf(dobottKockakMeccsAi[2]));
        dobottKockakMeccs3Label.setText(String.valueOf(dobottKockakMeccsPl[3]) + " : " + String.valueOf(dobottKockakMeccsAi[3]));
        dobottKockakMeccs4Label.setText(String.valueOf(dobottKockakMeccsPl[4]) + " : " + String.valueOf(dobottKockakMeccsAi[4]));
        dobottKockakMeccs5Label.setText(String.valueOf(dobottKockakMeccsPl[5]) + " : " + String.valueOf(dobottKockakMeccsAi[5]));
        dobottKockakMeccs6Label.setText(String.valueOf(dobottKockakMeccsPl[6]) + " : " + String.valueOf(dobottKockakMeccsAi[6]));
        
        dobottKockakOssz1Label.setText(String.valueOf(dobottKockakOsszPl[1]) + " : " + String.valueOf(dobottKockakOsszAi[1]));
        dobottKockakOssz2Label.setText(String.valueOf(dobottKockakOsszPl[2]) + " : " + String.valueOf(dobottKockakOsszAi[2]));
        dobottKockakOssz3Label.setText(String.valueOf(dobottKockakOsszPl[3]) + " : " + String.valueOf(dobottKockakOsszAi[3]));
        dobottKockakOssz4Label.setText(String.valueOf(dobottKockakOsszPl[4]) + " : " + String.valueOf(dobottKockakOsszAi[4]));
        dobottKockakOssz5Label.setText(String.valueOf(dobottKockakOsszPl[5]) + " : " + String.valueOf(dobottKockakOsszAi[5]));
        dobottKockakOssz6Label.setText(String.valueOf(dobottKockakOsszPl[6]) + " : " + String.valueOf(dobottKockakOsszAi[6]));
        
        duplakJatekPlLabel.setText(String.valueOf(duplakJatekPl));
        duplakJatekAiLabel.setText(String.valueOf(duplakJatekAi));
        duplakMeccsPlLabel.setText(String.valueOf(duplakMeccsPl));
        duplakMeccsAiLabel.setText(String.valueOf(duplakMeccsAi));
        duplakOsszPlLabel.setText(String.valueOf(duplakOsszPl));
        duplakOsszAiLabel.setText(String.valueOf(duplakOsszAi));
        
        dobottKockakErtekJatekPlLabel.setText(String.valueOf(dobottKockakErtekJatekPl));
        dobottKockakErtekJatekAiLabel.setText(String.valueOf(dobottKockakErtekJatekAi));
        dobottKockakErtekMeccsPlLabel.setText(String.valueOf(dobottKockakErtekMeccsPl));
        dobottKockakErtekMeccsAiLabel.setText(String.valueOf(dobottKockakErtekMeccsAi));
        dobottKockakErtekOsszPlLabel.setText(String.valueOf(dobottKockakErtekOsszPl));
        dobottKockakErtekOsszAiLabel.setText(String.valueOf(dobottKockakErtekOsszAi));
        
        kezdesJogaJatekPlLabel.setText(String.valueOf(kezdesJogaJatekPl));
        kezdesJogaJatekAiLabel.setText(String.valueOf(kezdesJogaJatekAi));
        kezdesJogaMeccsPlLabel.setText(String.valueOf(kezdesJogaMeccsPl));
        kezdesJogaMeccsAiLabel.setText(String.valueOf(kezdesJogaMeccsAi));
        kezdesJogaOsszPlLabel.setText(String.valueOf(kezdesJogaOsszPl));
        kezdesJogaOsszAiLabel.setText(String.valueOf(kezdesJogaOsszAi));
        
        utolsoTorlesLabel.setText("* Az utolsó törlés óta: " + utolsoTorles);
    }
    
    // lenullazza a jatekra (es meccsre) vonatkozo ertekeket
    void ujJatek(boolean ujMeccs) {
        nyertJatekJatekPl = 0;
        nyertJatekJatekAi = 0;
        gammonJatekPl = 0;
        gammonJatekAi = 0;
        backGammonJatekPl = 0;
        backGammonJatekAi = 0;
        nyertPontokJatekPl = 0;
        nyertPontokJatekAi = 0;
        for (int i=1; i<=6; i++) {
            dobottKockakJatekPl[i] = 0;
            dobottKockakJatekAi[i] = 0;
        }
        duplakJatekPl = 0;
        duplakJatekAi = 0;
        dobottKockakErtekJatekPl = 0;
        dobottKockakErtekJatekAi = 0;
        kezdesJogaJatekPl = 0;
        kezdesJogaJatekAi = 0;
        // ha uj meccs is, akkor lenullazza a meccsre vonatkozo ertekeket is
        if (ujMeccs) {
            nyertJatekMeccsPl = 0;
            nyertJatekMeccsAi = 0;
            gammonMeccsPl = 0;
            gammonMeccsAi = 0;
            backGammonMeccsPl = 0;
            backGammonMeccsAi = 0;
            nyertPontokMeccsPl = 0;
            nyertPontokMeccsAi = 0;
            nyertMeccsMeccsPl = 0;
            nyertMeccsMeccsAi = 0;
            for (int i=1; i<=6; i++) {
                dobottKockakMeccsPl[i] = 0;
                dobottKockakMeccsAi[i] = 0;
            }
            duplakMeccsPl = 0;
            duplakMeccsAi = 0;
            dobottKockakErtekMeccsPl = 0;
            dobottKockakErtekMeccsAi = 0;
            kezdesJogaMeccsPl = 0;
            kezdesJogaMeccsAi = 0;
        }
        setLabels();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        nyertJatekJatekPlLabel = new javax.swing.JLabel();
        nyertJatekMeccsPlLabel = new javax.swing.JLabel();
        nyertJatekOsszPlLabel = new javax.swing.JLabel();
        nyertJatekJatekAiLabel = new javax.swing.JLabel();
        nyertJatekMeccsAiLabel = new javax.swing.JLabel();
        nyertJatekOsszAiLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        gammonJatekPlLabel = new javax.swing.JLabel();
        gammonMeccsPlLabel = new javax.swing.JLabel();
        gammonOsszPlLabel = new javax.swing.JLabel();
        gammonJatekAiLabel = new javax.swing.JLabel();
        gammonMeccsAiLabel = new javax.swing.JLabel();
        gammonOsszAiLabel = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        backGammonJatekPlLabel = new javax.swing.JLabel();
        backGammonMeccsPlLabel = new javax.swing.JLabel();
        backGammonOsszPlLabel = new javax.swing.JLabel();
        backGammonJatekAiLabel = new javax.swing.JLabel();
        backGammonMeccsAiLabel = new javax.swing.JLabel();
        backGammonOsszAiLabel = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        nyertPontokJatekPlLabel = new javax.swing.JLabel();
        nyertPontokMeccsPlLabel = new javax.swing.JLabel();
        nyertPontokOsszPlLabel = new javax.swing.JLabel();
        nyertPontokJatekAiLabel = new javax.swing.JLabel();
        nyertPontokMeccsAiLabel = new javax.swing.JLabel();
        nyertPontokOsszAiLabel = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        nyertMeccsMeccsPlLabel = new javax.swing.JLabel();
        nyertMeccsOsszPlLabel = new javax.swing.JLabel();
        nyertMeccsMeccsAiLabel = new javax.swing.JLabel();
        nyertMeccsOsszAiLabel = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        dobottKockakJatek1Label = new javax.swing.JLabel();
        dobottKockakMeccs1Label = new javax.swing.JLabel();
        dobottKockakOssz1Label = new javax.swing.JLabel();
        dobottKockakJatek2Label = new javax.swing.JLabel();
        dobottKockakMeccs2Label = new javax.swing.JLabel();
        dobottKockakOssz2Label = new javax.swing.JLabel();
        dobottKockakJatek3Label = new javax.swing.JLabel();
        dobottKockakMeccs3Label = new javax.swing.JLabel();
        dobottKockakOssz3Label = new javax.swing.JLabel();
        dobottKockakJatek4Label = new javax.swing.JLabel();
        dobottKockakMeccs4Label = new javax.swing.JLabel();
        dobottKockakOssz4Label = new javax.swing.JLabel();
        dobottKockakJatek5Label = new javax.swing.JLabel();
        dobottKockakMeccs5Label = new javax.swing.JLabel();
        dobottKockakOssz5Label = new javax.swing.JLabel();
        dobottKockakJatek6Label = new javax.swing.JLabel();
        dobottKockakMeccs6Label = new javax.swing.JLabel();
        dobottKockakOssz6Label = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        duplakJatekPlLabel = new javax.swing.JLabel();
        duplakMeccsPlLabel = new javax.swing.JLabel();
        duplakOsszPlLabel = new javax.swing.JLabel();
        duplakJatekAiLabel = new javax.swing.JLabel();
        duplakMeccsAiLabel = new javax.swing.JLabel();
        duplakOsszAiLabel = new javax.swing.JLabel();
        dobottKockakErtekMeccsAiLabel = new javax.swing.JLabel();
        dobottKockakErtekOsszAiLabel = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        dobottKockakErtekJatekPlLabel = new javax.swing.JLabel();
        dobottKockakErtekMeccsPlLabel = new javax.swing.JLabel();
        dobottKockakErtekOsszPlLabel = new javax.swing.JLabel();
        dobottKockakErtekJatekAiLabel = new javax.swing.JLabel();
        kezdesJogaMeccsAiLabel = new javax.swing.JLabel();
        kezdesJogaOsszAiLabel = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        kezdesJogaJatekPlLabel = new javax.swing.JLabel();
        kezdesJogaMeccsPlLabel = new javax.swing.JLabel();
        kezdesJogaOsszPlLabel = new javax.swing.JLabel();
        kezdesJogaJatekAiLabel = new javax.swing.JLabel();
        torlesGomb = new javax.swing.JButton();
        utolsoTorlesLabel = new javax.swing.JLabel();

        setTitle("Statisztika");
        setResizable(false);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Játék");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("Meccs");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Összesen *");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Nyert játék");

        jLabel7.setText("Játékos");

        jLabel8.setText("Gép");

        nyertJatekJatekPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertJatekJatekPlLabel.setText("0");

        nyertJatekMeccsPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertJatekMeccsPlLabel.setText("0");

        nyertJatekOsszPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertJatekOsszPlLabel.setText("0");

        nyertJatekJatekAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertJatekJatekAiLabel.setText("0");

        nyertJatekMeccsAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertJatekMeccsAiLabel.setText("0");

        nyertJatekOsszAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertJatekOsszAiLabel.setText("0");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setText("Nyert gammon");

        jLabel10.setText("Játékos");

        jLabel11.setText("Gép");

        gammonJatekPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gammonJatekPlLabel.setText("0");

        gammonMeccsPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gammonMeccsPlLabel.setText("0");

        gammonOsszPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gammonOsszPlLabel.setText("0");

        gammonJatekAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gammonJatekAiLabel.setText("0");

        gammonMeccsAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gammonMeccsAiLabel.setText("0");

        gammonOsszAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gammonOsszAiLabel.setText("0");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel12.setText("Nyert backgammon");

        jLabel13.setText("Játékos");

        jLabel14.setText("Gép");

        backGammonJatekPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        backGammonJatekPlLabel.setText("0");

        backGammonMeccsPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        backGammonMeccsPlLabel.setText("0");

        backGammonOsszPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        backGammonOsszPlLabel.setText("0");

        backGammonJatekAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        backGammonJatekAiLabel.setText("0");

        backGammonMeccsAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        backGammonMeccsAiLabel.setText("0");

        backGammonOsszAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        backGammonOsszAiLabel.setText("0");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel15.setText("Nyert pont");

        jLabel16.setText("Játékos");

        jLabel17.setText("Gép");

        nyertPontokJatekPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertPontokJatekPlLabel.setText("0");

        nyertPontokMeccsPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertPontokMeccsPlLabel.setText("0");

        nyertPontokOsszPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertPontokOsszPlLabel.setText("0");

        nyertPontokJatekAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertPontokJatekAiLabel.setText("0");

        nyertPontokMeccsAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertPontokMeccsAiLabel.setText("0");

        nyertPontokOsszAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertPontokOsszAiLabel.setText("0");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel18.setText("Nyert meccs");

        jLabel19.setText("Játékos");

        jLabel20.setText("Gép");

        nyertMeccsMeccsPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertMeccsMeccsPlLabel.setText("0");

        nyertMeccsOsszPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertMeccsOsszPlLabel.setText("0");

        nyertMeccsMeccsAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertMeccsMeccsAiLabel.setText("0");

        nyertMeccsOsszAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nyertMeccsOsszAiLabel.setText("0");

        jLabel21.setText("1:");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel22.setText("Dobott kockák (Játékos : Gép) ");

        jLabel23.setText("2:");

        jLabel24.setText("3:");

        jLabel25.setText("4:");

        jLabel26.setText("5:");

        jLabel27.setText("6:");

        dobottKockakJatek1Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakJatek1Label.setText("0 : 0");

        dobottKockakMeccs1Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakMeccs1Label.setText("0 : 0");

        dobottKockakOssz1Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakOssz1Label.setText("0 : 0");

        dobottKockakJatek2Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakJatek2Label.setText("0 : 0");

        dobottKockakMeccs2Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakMeccs2Label.setText("0 : 0");

        dobottKockakOssz2Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakOssz2Label.setText("0 : 0");

        dobottKockakJatek3Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakJatek3Label.setText("0 : 0");

        dobottKockakMeccs3Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakMeccs3Label.setText("0 : 0");

        dobottKockakOssz3Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakOssz3Label.setText("0 : 0");

        dobottKockakJatek4Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakJatek4Label.setText("0 : 0");

        dobottKockakMeccs4Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakMeccs4Label.setText("0 : 0");

        dobottKockakOssz4Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakOssz4Label.setText("0 : 0");

        dobottKockakJatek5Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakJatek5Label.setText("0 : 0");

        dobottKockakMeccs5Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakMeccs5Label.setText("0 : 0");

        dobottKockakOssz5Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakOssz5Label.setText("0 : 0");

        dobottKockakJatek6Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakJatek6Label.setText("0 : 0");

        dobottKockakMeccs6Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakMeccs6Label.setText("0 : 0");

        dobottKockakOssz6Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakOssz6Label.setText("0 : 0");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel28.setText("Dobott duplák");

        jLabel29.setText("Játékos");

        jLabel30.setText("Gép");

        duplakJatekPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        duplakJatekPlLabel.setText("0");

        duplakMeccsPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        duplakMeccsPlLabel.setText("0");

        duplakOsszPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        duplakOsszPlLabel.setText("0");

        duplakJatekAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        duplakJatekAiLabel.setText("0");

        duplakMeccsAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        duplakMeccsAiLabel.setText("0");

        duplakOsszAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        duplakOsszAiLabel.setText("0");

        dobottKockakErtekMeccsAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakErtekMeccsAiLabel.setText("0");

        dobottKockakErtekOsszAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakErtekOsszAiLabel.setText("0");

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel31.setText("Dobott kockák összértéke");

        jLabel32.setText("Játékos");

        jLabel33.setText("Gép");

        dobottKockakErtekJatekPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakErtekJatekPlLabel.setText("0");

        dobottKockakErtekMeccsPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakErtekMeccsPlLabel.setText("0");

        dobottKockakErtekOsszPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakErtekOsszPlLabel.setText("0");

        dobottKockakErtekJatekAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dobottKockakErtekJatekAiLabel.setText("0");

        kezdesJogaMeccsAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        kezdesJogaMeccsAiLabel.setText("0");

        kezdesJogaOsszAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        kezdesJogaOsszAiLabel.setText("0");

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel34.setText("Kezdés joga");

        jLabel35.setText("Játékos");

        jLabel36.setText("Gép");

        kezdesJogaJatekPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        kezdesJogaJatekPlLabel.setText("0");

        kezdesJogaMeccsPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        kezdesJogaMeccsPlLabel.setText("0");

        kezdesJogaOsszPlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        kezdesJogaOsszPlLabel.setText("0");

        kezdesJogaJatekAiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        kezdesJogaJatekAiLabel.setText("0");

        torlesGomb.setText("Törlés");
        torlesGomb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                torlesGombActionPerformed(evt);
            }
        });

        utolsoTorlesLabel.setText("* Az utolsó törlés óta:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13)
                            .addComponent(jLabel17)
                            .addComponent(jLabel28)
                            .addComponent(jLabel20)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel27)
                            .addComponent(jLabel26)
                            .addComponent(jLabel25)
                            .addComponent(jLabel24)
                            .addComponent(jLabel23)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dobottKockakOssz1Label, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dobottKockakOssz2Label, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dobottKockakOssz3Label, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dobottKockakOssz4Label, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dobottKockakOssz5Label, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dobottKockakOssz6Label, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(nyertMeccsOsszPlLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(nyertMeccsOsszAiLabel, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addComponent(jLabel35)
                            .addComponent(jLabel32)
                            .addComponent(jLabel33)
                            .addComponent(jLabel36))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(kezdesJogaOsszPlLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(kezdesJogaOsszAiLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dobottKockakErtekOsszAiLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(dobottKockakErtekOsszPlLabel, javax.swing.GroupLayout.Alignment.TRAILING)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(utolsoTorlesLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(torlesGomb))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 221, Short.MAX_VALUE)
                                        .addComponent(nyertJatekJatekPlLabel))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(nyertJatekJatekAiLabel))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel3)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(gammonJatekPlLabel)
                                                    .addComponent(gammonJatekAiLabel)
                                                    .addComponent(backGammonJatekAiLabel)
                                                    .addComponent(backGammonJatekPlLabel)
                                                    .addComponent(nyertPontokJatekPlLabel)
                                                    .addComponent(nyertPontokJatekAiLabel)
                                                    .addComponent(duplakJatekPlLabel)
                                                    .addComponent(duplakJatekAiLabel)
                                                    .addComponent(dobottKockakErtekJatekPlLabel)
                                                    .addComponent(dobottKockakErtekJatekAiLabel)
                                                    .addComponent(kezdesJogaJatekPlLabel)
                                                    .addComponent(kezdesJogaJatekAiLabel)
                                                    .addComponent(dobottKockakJatek1Label)
                                                    .addComponent(dobottKockakJatek2Label)
                                                    .addComponent(dobottKockakJatek3Label)
                                                    .addComponent(dobottKockakJatek4Label)
                                                    .addComponent(dobottKockakJatek5Label)
                                                    .addComponent(dobottKockakJatek6Label))
                                                .addGap(1, 1, 1)))))
                                .addGap(92, 92, 92)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(gammonMeccsAiLabel)
                                    .addComponent(gammonMeccsPlLabel)
                                    .addComponent(backGammonMeccsAiLabel)
                                    .addComponent(backGammonMeccsPlLabel)
                                    .addComponent(nyertPontokMeccsPlLabel)
                                    .addComponent(nyertPontokMeccsAiLabel)
                                    .addComponent(nyertMeccsMeccsPlLabel)
                                    .addComponent(nyertMeccsMeccsAiLabel)
                                    .addComponent(dobottKockakMeccs1Label)
                                    .addComponent(dobottKockakMeccs2Label)
                                    .addComponent(dobottKockakMeccs3Label)
                                    .addComponent(dobottKockakMeccs4Label)
                                    .addComponent(dobottKockakMeccs5Label)
                                    .addComponent(dobottKockakMeccs6Label)
                                    .addComponent(duplakMeccsPlLabel)
                                    .addComponent(duplakMeccsAiLabel)
                                    .addComponent(dobottKockakErtekMeccsPlLabel)
                                    .addComponent(dobottKockakErtekMeccsAiLabel)
                                    .addComponent(kezdesJogaMeccsPlLabel)
                                    .addComponent(kezdesJogaMeccsAiLabel)
                                    .addComponent(nyertJatekMeccsPlLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nyertJatekMeccsAiLabel)
                                    .addComponent(jLabel4)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(134, 134, 134)
                                    .addComponent(gammonOsszAiLabel))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(nyertJatekOsszAiLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(nyertJatekOsszPlLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(gammonOsszPlLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(nyertPontokOsszPlLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(nyertPontokOsszAiLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(backGammonOsszPlLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(backGammonOsszAiLabel, javax.swing.GroupLayout.Alignment.TRAILING))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(duplakOsszAiLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(duplakOsszPlLabel, javax.swing.GroupLayout.Alignment.TRAILING)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31)
                            .addComponent(jLabel29))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(gammonJatekPlLabel)
                            .addComponent(gammonMeccsPlLabel)
                            .addComponent(gammonOsszPlLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(gammonJatekAiLabel)
                            .addComponent(gammonMeccsAiLabel)
                            .addComponent(gammonOsszAiLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nyertJatekMeccsPlLabel)
                            .addComponent(nyertJatekJatekPlLabel)
                            .addComponent(nyertJatekOsszPlLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nyertJatekMeccsAiLabel)
                            .addComponent(nyertJatekJatekAiLabel)
                            .addComponent(nyertJatekOsszAiLabel))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(backGammonJatekPlLabel)
                                    .addComponent(backGammonMeccsPlLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(backGammonJatekAiLabel)
                                    .addComponent(backGammonMeccsAiLabel)
                                    .addComponent(backGammonOsszAiLabel))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15))
                    .addComponent(backGammonOsszPlLabel))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nyertPontokMeccsPlLabel)
                            .addComponent(nyertPontokJatekPlLabel)
                            .addComponent(nyertPontokOsszPlLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nyertPontokMeccsAiLabel)
                            .addComponent(nyertPontokJatekAiLabel)
                            .addComponent(nyertPontokOsszAiLabel))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nyertMeccsOsszPlLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nyertMeccsOsszAiLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nyertMeccsMeccsPlLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nyertMeccsMeccsAiLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dobottKockakOssz1Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dobottKockakOssz2Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dobottKockakOssz3Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dobottKockakOssz4Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dobottKockakOssz5Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dobottKockakOssz6Label))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dobottKockakMeccs1Label)
                            .addComponent(dobottKockakJatek1Label))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dobottKockakMeccs2Label)
                            .addComponent(dobottKockakJatek2Label))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dobottKockakMeccs3Label)
                            .addComponent(dobottKockakJatek3Label))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dobottKockakMeccs4Label)
                            .addComponent(dobottKockakJatek4Label))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dobottKockakMeccs5Label)
                            .addComponent(dobottKockakJatek5Label))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dobottKockakMeccs6Label)
                            .addComponent(dobottKockakJatek6Label))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(duplakJatekPlLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(duplakJatekAiLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(duplakMeccsPlLabel)
                            .addComponent(duplakOsszPlLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(duplakMeccsAiLabel)
                            .addComponent(duplakOsszAiLabel))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel33))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(dobottKockakErtekJatekPlLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dobottKockakErtekJatekAiLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(dobottKockakErtekMeccsPlLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dobottKockakErtekMeccsAiLabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel34))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dobottKockakErtekOsszPlLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dobottKockakErtekOsszAiLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel36))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(kezdesJogaOsszPlLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(kezdesJogaOsszAiLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(kezdesJogaJatekPlLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(kezdesJogaJatekAiLabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(torlesGomb)
                            .addComponent(utolsoTorlesLabel)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(kezdesJogaMeccsPlLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kezdesJogaMeccsAiLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // torli az "osszes" oszlop ertekeit
    private void torlesGombActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_torlesGombActionPerformed
        // TODO add your handling code here:
        ido = new java.util.Date();
        utolsoTorles = ido.toString();
        
        nyertJatekOsszPl = 0;
        nyertJatekOsszAi = 0;
        gammonOsszPl = 0;
        gammonOsszAi = 0;
        backGammonOsszPl = 0;
        backGammonOsszAi = 0;
        nyertPontokOsszPl = 0;
        nyertPontokOsszAi = 0;
        nyertMeccsOsszPl = 0;
        nyertMeccsOsszAi = 0;
        for (int i=1; i<=6; i++) {
            dobottKockakOsszPl[i] = 0;
            dobottKockakOsszAi[i] = 0;
        }
        duplakOsszPl = 0;
        duplakOsszAi = 0;
        dobottKockakErtekOsszPl = 0;
        dobottKockakErtekOsszAi = 0;
        kezdesJogaOsszPl = 0;
        kezdesJogaOsszAi = 0;
        
        setLabels();
    }//GEN-LAST:event_torlesGombActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Statisztika.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Statisztika.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Statisztika.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Statisztika.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Statisztika().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel backGammonJatekAiLabel;
    private javax.swing.JLabel backGammonJatekPlLabel;
    private javax.swing.JLabel backGammonMeccsAiLabel;
    private javax.swing.JLabel backGammonMeccsPlLabel;
    private javax.swing.JLabel backGammonOsszAiLabel;
    private javax.swing.JLabel backGammonOsszPlLabel;
    private javax.swing.JLabel dobottKockakErtekJatekAiLabel;
    private javax.swing.JLabel dobottKockakErtekJatekPlLabel;
    private javax.swing.JLabel dobottKockakErtekMeccsAiLabel;
    private javax.swing.JLabel dobottKockakErtekMeccsPlLabel;
    private javax.swing.JLabel dobottKockakErtekOsszAiLabel;
    private javax.swing.JLabel dobottKockakErtekOsszPlLabel;
    private javax.swing.JLabel dobottKockakJatek1Label;
    private javax.swing.JLabel dobottKockakJatek2Label;
    private javax.swing.JLabel dobottKockakJatek3Label;
    private javax.swing.JLabel dobottKockakJatek4Label;
    private javax.swing.JLabel dobottKockakJatek5Label;
    private javax.swing.JLabel dobottKockakJatek6Label;
    private javax.swing.JLabel dobottKockakMeccs1Label;
    private javax.swing.JLabel dobottKockakMeccs2Label;
    private javax.swing.JLabel dobottKockakMeccs3Label;
    private javax.swing.JLabel dobottKockakMeccs4Label;
    private javax.swing.JLabel dobottKockakMeccs5Label;
    private javax.swing.JLabel dobottKockakMeccs6Label;
    private javax.swing.JLabel dobottKockakOssz1Label;
    private javax.swing.JLabel dobottKockakOssz2Label;
    private javax.swing.JLabel dobottKockakOssz3Label;
    private javax.swing.JLabel dobottKockakOssz4Label;
    private javax.swing.JLabel dobottKockakOssz5Label;
    private javax.swing.JLabel dobottKockakOssz6Label;
    private javax.swing.JLabel duplakJatekAiLabel;
    private javax.swing.JLabel duplakJatekPlLabel;
    private javax.swing.JLabel duplakMeccsAiLabel;
    private javax.swing.JLabel duplakMeccsPlLabel;
    private javax.swing.JLabel duplakOsszAiLabel;
    private javax.swing.JLabel duplakOsszPlLabel;
    private javax.swing.JLabel gammonJatekAiLabel;
    private javax.swing.JLabel gammonJatekPlLabel;
    private javax.swing.JLabel gammonMeccsAiLabel;
    private javax.swing.JLabel gammonMeccsPlLabel;
    private javax.swing.JLabel gammonOsszAiLabel;
    private javax.swing.JLabel gammonOsszPlLabel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel kezdesJogaJatekAiLabel;
    private javax.swing.JLabel kezdesJogaJatekPlLabel;
    private javax.swing.JLabel kezdesJogaMeccsAiLabel;
    private javax.swing.JLabel kezdesJogaMeccsPlLabel;
    private javax.swing.JLabel kezdesJogaOsszAiLabel;
    private javax.swing.JLabel kezdesJogaOsszPlLabel;
    private javax.swing.JLabel nyertJatekJatekAiLabel;
    private javax.swing.JLabel nyertJatekJatekPlLabel;
    private javax.swing.JLabel nyertJatekMeccsAiLabel;
    private javax.swing.JLabel nyertJatekMeccsPlLabel;
    private javax.swing.JLabel nyertJatekOsszAiLabel;
    private javax.swing.JLabel nyertJatekOsszPlLabel;
    private javax.swing.JLabel nyertMeccsMeccsAiLabel;
    private javax.swing.JLabel nyertMeccsMeccsPlLabel;
    private javax.swing.JLabel nyertMeccsOsszAiLabel;
    private javax.swing.JLabel nyertMeccsOsszPlLabel;
    private javax.swing.JLabel nyertPontokJatekAiLabel;
    private javax.swing.JLabel nyertPontokJatekPlLabel;
    private javax.swing.JLabel nyertPontokMeccsAiLabel;
    private javax.swing.JLabel nyertPontokMeccsPlLabel;
    private javax.swing.JLabel nyertPontokOsszAiLabel;
    private javax.swing.JLabel nyertPontokOsszPlLabel;
    private javax.swing.JButton torlesGomb;
    private javax.swing.JLabel utolsoTorlesLabel;
    // End of variables declaration//GEN-END:variables
}
