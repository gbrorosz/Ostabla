/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ostabla;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Gabor
 */
class Tabla extends javax.swing.JFrame {
    Pozicio[] poziciok;                   // mind a 28 poziciot tartalmazo tomb
    Pozicio[] kezdoAllas;                 // a kezdeti feltoltes utan masolatot keszit a poziciok tombrol (a kezdo allasrol), hogy vissza lehessen ezt allitani uj jateknal
    Jatek jat;                            // Jatek obj, ez a vezerlo objektuma a programnak
    JPanel toolBar;                       // a felso sav, amin a gombok, kockak, stb vannak
    JPanel kockaPanel;                    // ezen van a 2-4 dobokocka
    JTextArea uzi1;                       // uzenet a felhasznalonak
    JPanel pontokPanel;                   // a meccs es pip pontokat tartalmazo panel
    Kocka duplazo;                        // duplazo kocka
    
    Properties pr;                        // ebben az objektumban menti el es tolti be az adatokat
    OutputStream os = null;
    InputStream is = null;
    
    Ment m;                                 // dialog ablak
    Betolt b;                               // dialog ablak
    Opciok o;                               // ablak
    Statisztika s;                          // ablak

    String szinSema;                        // a tabla hatterszineire utalo String
    
    

    /**
     * Creates new form Tabla
     */
    public Tabla() {
        initComponents();
        
        // sajat kod -->
        poziciok = new Pozicio[28];
        kezdoAllas = new Pozicio[28];
        jat = new Jatek(this);
        pr = new Properties();
        s = new Statisztika();
        szinSema = "alap";
        
        setTitle("Ostábla");
        jatekFelulet.setLayout(new GridLayout(2, 14, 0, 0));            // kesobb a gap-eket levenni!
        for (int i=13; i<19; i++) {
            Pozicio p = new Pozicio(i);
            p.addMouseListener(jat);
            jatekFelulet.add(p);
            poziciok[i] = p;
        }
        Pozicio p1 = new Pozicio(26);
        p1.addMouseListener(jat);
        jatekFelulet.add(p1);
        poziciok[26] = p1;
        for (int i=19; i<26; i++) {
            Pozicio p = new Pozicio(i);
            p.addMouseListener(jat);
            jatekFelulet.add(p);
            poziciok[i] = p;
        }
        for (int i=12; i>6; i--) {
            Pozicio p = new Pozicio(i);
            p.addMouseListener(jat);
            jatekFelulet.add(p);
            poziciok[i] = p;
        }
        Pozicio p2 = new Pozicio(27);
        p2.addMouseListener(jat);
        jatekFelulet.add(p2);
        poziciok[27] = p2;
        for (int i=6; i>=0; i--) {
            Pozicio p = new Pozicio(i);
            p.addMouseListener(jat);
            jatekFelulet.add(p);
            poziciok[i] = p;
        }
        
        // alapallas
        for (int i=0; i<2; i++) {
            poziciok[1].hozzaad(jat.aiColor);
            poziciok[25-1].hozzaad(jat.playersColor);
        }
        for (int i=0; i<5; i++) {
            poziciok[6].hozzaad(jat.playersColor);
            poziciok[25-6].hozzaad(jat.aiColor);
        }
        for (int i=0; i<3; i++) {
            poziciok[8].hozzaad(jat.playersColor);
            poziciok[25-8].hozzaad(jat.aiColor);
        }
        for (int i=0; i<5; i++) {
            poziciok[12].hozzaad(jat.aiColor);
            poziciok[25-12].hozzaad(jat.playersColor);
        }
        
        for (int i=0; i<28; i++) {
            kezdoAllas[i] = new Pozicio(poziciok[i]);
        }
        
        kontrol.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        //kontrol.setBackground(Color.gray);
        kontrol.setBackground(Color.lightGray);
        
        JButton visszaGomb = new JButton("<<");
        visszaGomb.addActionListener(jat);
        visszaGomb.setMargin(new  Insets(5, 5, 5, 5));
        visszaGomb.setEnabled(false);
        kontrol.add(visszaGomb);
        
        JButton eloreGomb = new JButton(">>");
        eloreGomb.addActionListener(jat);
        eloreGomb.setMargin(new  Insets(5, 5, 5, 5));
        eloreGomb.setEnabled(false);
        kontrol.add(eloreGomb);
        
        JButton okGomb = new JButton("OK");
        okGomb.addActionListener(jat);
        okGomb.setMargin(new  Insets(5, 5, 5, 5));
        //okGomb.setEnabled(false);
        kontrol.add(okGomb);
        
        
        kockaPanel = new JPanel();
        GridLayout grid = new GridLayout(1, 4, 10, 10);
        kockaPanel.setLayout(grid);
        kockaPanel.setPreferredSize(new Dimension(230, 50));
        kockaPanel.setBackground(Color.lightGray);
        kontrol.add(kockaPanel);
        
        duplazo = new Kocka((byte)0, "dummy");
        duplazo.addMouseListener(jat);
        duplazo.setPreferredSize(new Dimension(50, 50));
        duplazo.getL().setForeground(Color.lightGray);
        duplazo.getL().setFont(new Font("Default", Font.BOLD, 30));
//        duplazo.setEnabled(false);
        kontrol.add(duplazo);
        
        JPanel uzenetPanel = new JPanel();
        BoxLayout box2 = new BoxLayout(uzenetPanel, BoxLayout.Y_AXIS);
        uzenetPanel.setLayout(box2);
        //uzenetPanel.setPreferredSize(new Dimension(200, 50));
        //uzi1 = new JLabel("", JLabel.RIGHT);
        uzi1 = new JTextArea("Öné a " + jat.playersColor + ". Dobjanak egy-egy kockával! (OK gomb)");
        uzi1.setEditable(false);
        uzi1.setPreferredSize(new Dimension(160, 50));
        uzi1.setLineWrap(true);
        uzi1.setWrapStyleWord(true);
        uzi1.setBackground(Color.lightGray);
        uzi1.setFont(new Font("Default", Font.BOLD, 12));
        uzenetPanel.add(uzi1);
        kontrol.add(uzenetPanel);
        uzenetPanel.setBackground(Color.lightGray);
        
        pontokPanel = new JPanel();
        pontokPanel.setLayout(new GridLayout(3, 3, 10, 0));
        
        JLabel uresLabel = new JLabel("", JLabel.RIGHT);
        JLabel pontHeaderLabel = new JLabel("meccs", JLabel.RIGHT);
        JLabel pipHeaderLabel = new JLabel("pip", JLabel.RIGHT);
        JLabel aiLabel = new JLabel("Gép:", JLabel.RIGHT);
        //JLabel aiLabel = new JLabel(jat.aiColor, JLabel.RIGHT);
        aiLabel.setForeground(jat.aiColor.equals("fehér")? Color.white : Color.black);
        JLabel plLabel = new JLabel("Játékos:", JLabel.RIGHT);
        //JLabel plLabel = new JLabel(jat.playersColor, JLabel.RIGHT);
        plLabel.setForeground(jat.playersColor.equals("fehér")? Color.white : Color.black);
        
        JLabel playerScoreLabel = new JLabel(String.valueOf(jat.playerScore) + "/" + jat.match, JLabel.RIGHT);
        playerScoreLabel.setForeground(jat.playersColor.equals("fehér")? Color.white : Color.black);
        JLabel aiScoreLabel = new JLabel(String.valueOf(jat.aiScore) + "/" + jat.match, JLabel.RIGHT);
        aiScoreLabel.setForeground(jat.aiColor.equals("fehér")? Color.white : Color.black);
        JLabel playerPipLabel = new JLabel(String.valueOf(jat.playersPip), JLabel.RIGHT);
        playerPipLabel.setForeground(jat.playersColor.equals("fehér")? Color.white : Color.black);
        JLabel aiPipLabel = new JLabel(String.valueOf(jat.aiPip), JLabel.RIGHT);
        aiPipLabel.setForeground(jat.aiColor.equals("fehér")? Color.white : Color.black);
        pontokPanel.add(uresLabel);
        pontokPanel.add(pontHeaderLabel);
        pontokPanel.add(pipHeaderLabel);
        pontokPanel.add(aiLabel);
        pontokPanel.add(aiScoreLabel);
        pontokPanel.add(aiPipLabel);
        pontokPanel.add(plLabel);
        pontokPanel.add(playerScoreLabel);
        pontokPanel.add(playerPipLabel);
        kontrol.add(pontokPanel);
        pontokPanel.setBackground(Color.lightGray);
        
        
        toolBar = kontrol;
        
        // <-- sajat kod
    }
    
    public void ujJatekMehet(boolean mehet) {               // setter
        if (mehet) {
            ujJatek.setEnabled(true);
        }
        else {
            ujJatek.setEnabled(false);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jatekFelulet = new javax.swing.JPanel();
        kontrol = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        ujJatek = new javax.swing.JMenuItem();
        jatekBetoltese = new javax.swing.JMenuItem();
        jatekMentese = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        opciok = new javax.swing.JMenuItem();
        statisztika = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        tartalom = new javax.swing.JMenuItem();
        nevjegy = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(900, 900));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jatekFelulet.setBackground(new java.awt.Color(128, 128, 128));

        javax.swing.GroupLayout jatekFeluletLayout = new javax.swing.GroupLayout(jatekFelulet);
        jatekFelulet.setLayout(jatekFeluletLayout);
        jatekFeluletLayout.setHorizontalGroup(
            jatekFeluletLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 776, Short.MAX_VALUE)
        );
        jatekFeluletLayout.setVerticalGroup(
            jatekFeluletLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 465, Short.MAX_VALUE)
        );

        kontrol.setBackground(new java.awt.Color(128, 128, 128));

        javax.swing.GroupLayout kontrolLayout = new javax.swing.GroupLayout(kontrol);
        kontrol.setLayout(kontrolLayout);
        kontrolLayout.setHorizontalGroup(
            kontrolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        kontrolLayout.setVerticalGroup(
            kontrolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jMenu1.setText("Fájl");

        ujJatek.setText("Új játék");
        ujJatek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ujJatekActionPerformed(evt);
            }
        });
        jMenu1.add(ujJatek);

        jatekBetoltese.setText("Játék betöltése");
        jatekBetoltese.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jatekBetolteseActionPerformed(evt);
            }
        });
        jMenu1.add(jatekBetoltese);

        jatekMentese.setText("Játék mentése");
        jatekMentese.setEnabled(false);
        jatekMentese.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jatekMenteseActionPerformed(evt);
            }
        });
        jMenu1.add(jatekMentese);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Eszközök");

        opciok.setText("Beállítások");
        opciok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opciokActionPerformed(evt);
            }
        });
        jMenu2.add(opciok);

        statisztika.setText("Statisztika");
        statisztika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statisztikaActionPerformed(evt);
            }
        });
        jMenu2.add(statisztika);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Súgó");

        tartalom.setText("Tartalom");
        tartalom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tartalomActionPerformed(evt);
            }
        });
        jMenu3.add(tartalom);

        nevjegy.setText("Névjegy");
        nevjegy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nevjegyActionPerformed(evt);
            }
        });
        jMenu3.add(nevjegy);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jatekFelulet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(kontrol, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(kontrol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jatekFelulet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // file menu uj jatekra kattintva visszaallitja az alapallast
    private void ujJatekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ujJatekActionPerformed
        // TODO add your handling code here:
        mentesMehet(false);                                  // ne lehessen menteni a jatekot az elso valodi dobas elott (mert nincs lepesek list, kocka1,2 es ertelme sincs sok)
        
        // Jatek parameterek
        jat.playersPip = 167;
        jat.aiPip = 167;
        // uj meccs, ha valaki megnyerte a meccset, vagy ha egy jatek vege (es ebben a duplazas nem elfogadasabol adodo "jatek vege" is benne van) elott inditunk uj jatekot
        if (jat.aiScore >= jat.match || jat.playerScore >= jat.match || !jat.jatekVege) {        // || poziciok[0].korongok != 15 && poziciok[25].korongok != 15
            jat.aiScore = 0;
            jat.playerScore = 0;
            jat.crawford = 0;
            // statisztika
            s.ujJatek(true);
        }
        // csak uj jatek
        else {
            if (jat.aiScore == jat.match-1 || jat.playerScore == jat.match-1) {
                jat.crawford ++;
            }
            // statisztika
            s.ujJatek(false);
        }
        System.out.println("--------------------");
        System.out.println("Uj jatek");
        System.out.println("jat.crawford: " + jat.crawford);
        
        jat.jatekVege = false;
        jat.lepesek.clear();            // hogy ne lepkedhessen amikor meg nem kell (167, 167, ok elott)
        jat.playersTurn = false;        // hogy ne lepkedhessen amikor meg nem kell (167, 167, ok elott)
        jat.tet = 1;
        jat.kezdoKockaPl = 0;
        jat.kezdoKockaAi = 0;
        
        // poziciok
        for (int i=0; i<28; i++) {
            // kezdetben a pl fekete volt, errol keszult masolat a kezdoAllas. Ha azota nem cserelt szint, akkor csak lemasoljuk. Ha cserelt, akkor lemasoljuk atszinezve.
            if (jat.playersColor.equals("fekete")) {
                poziciok[i].masol(kezdoAllas[i]);
            }
            else {
                poziciok[i].atSzinez(kezdoAllas[i]);
            }
        }
        
        // toolbar
        toolBar.getComponent(0).setEnabled(false);
        toolBar.getComponent(1).setEnabled(false);
        toolBar.getComponent(2).setEnabled(true);
        kockaPanel.removeAll();
        //kockaPanel.updateUI();
        duplazo.nullaz();
        uzi1.setText("Öné a " + jat.playersColor + ". Dobjanak egy-egy kockával! (OK gomb)");
        ((JLabel)pontokPanel.getComponent(4)).setText(String.valueOf(jat.aiScore) + "/" + jat.match);
        ((JLabel)pontokPanel.getComponent(7)).setText(String.valueOf(jat.playerScore) + "/" + jat.match);
        ((JLabel)pontokPanel.getComponent(5)).setText(String.valueOf(jat.aiPip));
        ((JLabel)pontokPanel.getComponent(8)).setText(String.valueOf(jat.playersPip));
        toolBar.repaint();
    }//GEN-LAST:event_ujJatekActionPerformed

    public void mentesMehet(boolean mehet) {               // setter
        if (mehet) {
            jatekMentese.setEnabled(true);
        }
        else {
            jatekMentese.setEnabled(false);
        }
    }
    
    public void betoltesMehet(boolean mehet) {               // setter
        if (mehet) {
            jatekBetoltese.setEnabled(true);
        }
        else {
            jatekBetoltese.setEnabled(false);
        }
    }
    
    
    private void jatekMenteseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jatekMenteseActionPerformed
        mentes();
    }//GEN-LAST:event_jatekMenteseActionPerformed

    // elmenti az allast (pontokkal, stb egyutt) egy "mentettJatek.properties" file-ba
    void mentes() {
        try {
            os = new FileOutputStream("mentettJatek.properties");
            pr.clear();
            // jat parameterei
            pr.setProperty("playersTurn", jat.playersTurn ? "true" : "false");
            pr.setProperty("playersColor", jat.playersColor);
            pr.setProperty("aiColor", jat.aiColor);
            pr.setProperty("kocka1", String.valueOf(jat.kocka1));
            pr.setProperty("kocka2", String.valueOf(jat.kocka2));
            pr.setProperty("lepes", String.valueOf(jat.lepes));
            pr.setProperty("playersPip", String.valueOf(jat.playersPip));
            pr.setProperty("aiPip", String.valueOf(jat.aiPip));
            pr.setProperty("playerScore", String.valueOf(jat.playerScore));
            pr.setProperty("aiScore", String.valueOf(jat.aiScore));
            pr.setProperty("match", String.valueOf(jat.match));
            pr.setProperty("tet", String.valueOf(jat.tet));
            pr.setProperty("plDuplazhat", jat.plDuplazhat ? "true" : "false");
            pr.setProperty("kezdoKockaPl", String.valueOf(jat.kezdoKockaPl));
            pr.setProperty("kezdoKockaAi", String.valueOf(jat.kezdoKockaAi));
            pr.setProperty("crawford", String.valueOf(jat.crawford));
            pr.setProperty("jatekVege", jat.jatekVege ? "true" : "false");

            if (jat.lepesek != null) {     // betoltesnel  nullazza le, majd ha van illyen propery akkor tolse be, ha nincs, akkor ne (amikor kezdo kocka van meg csak (167,167) es a lepesek ures)
                for (int i=0; i<jat.lepesek.size(); i++) {
                    Lepesek l = jat.lepesek.get(i);
                    pr.setProperty("lepesek_"+i+"_ertek", String.valueOf(l.ertek));
                    pr.setProperty("lepesek_"+i+"_mozgatott", String.valueOf(l.mozgatott));
                    pr.setProperty("lepesek_"+i+"_utesek", l.utesek ? "true" : "false");
                }
            }else pr.setProperty("lepesek", "null");
            
            // whitelists
            pr.setProperty("whiteList1_size", String.valueOf(jat.whiteList1.size()));
            for (int i=0; i<jat.whiteList1.size(); i++) {
                int w = jat.whiteList1.get(i);
                pr.setProperty("whiteList1_"+i, String.valueOf(w));
            }
            pr.setProperty("whiteList2_size", String.valueOf(jat.whiteList2.size()));
            for (int i=0; i<jat.whiteList2.size(); i++) {
                int w = jat.whiteList2.get(i);
                pr.setProperty("whiteList2_"+i, String.valueOf(w));
            }
            
            // tab parameterei
            for (int i=0; i<28; i++) {
                pr.setProperty("poziciok_"+i+"_szin", poziciok[i].szin);
                pr.setProperty("poziciok_"+i+"_korongok", String.valueOf(poziciok[i].korongok));
            }

            // gombok
            pr.setProperty("visszagomb", toolBar.getComponent(0).isEnabled() ? "enabled" : "disabled");
            pr.setProperty("eloregomb", toolBar.getComponent(1).isEnabled() ? "enabled" : "disabled");
            pr.setProperty("okgomb", toolBar.getComponent(2).isEnabled() ? "enabled" : "disabled");

            // dobokockak es duplazo kocka a betoltesnel majd peldanyositasra es bealitasra kerulnek a lepesek, lepes, playersturn, pip-ek, tet, plduplazhat parameterek alapjan.

            // uzenet
            if (uzi1.getText().equals("Mentett file nem található!")) {             // ha ezt elmenti majd betolti az megteveszto
                pr.setProperty("uzenet", "");
            }else pr.setProperty("uzenet", uzi1.getText());
            // pontokpanel frissitesre kerul majd a jat parameterei alapjan
            
            // statisztika, jatekra es meccsre vonatkozo ertekek
            pr.setProperty("nyertJatekJatekPl", String.valueOf(s.nyertJatekJatekPl));
            pr.setProperty("nyertJatekJatekAi", String.valueOf(s.nyertJatekJatekAi));
            pr.setProperty("nyertJatekMeccsPl", String.valueOf(s.nyertJatekMeccsPl));
            pr.setProperty("nyertJatekMeccsAi", String.valueOf(s.nyertJatekMeccsAi));
            pr.setProperty("gammonJatekPl", String.valueOf(s.gammonJatekPl));
            pr.setProperty("gammonJatekAi", String.valueOf(s.gammonJatekAi));
            pr.setProperty("gammonMeccsPl", String.valueOf(s.gammonMeccsPl));
            pr.setProperty("gammonMeccsAi", String.valueOf(s.gammonMeccsAi));
            pr.setProperty("backGammonJatekPl", String.valueOf(s.backGammonJatekPl));
            pr.setProperty("backGammonJatekAi", String.valueOf(s.backGammonJatekAi));
            pr.setProperty("backGammonMeccsPl", String.valueOf(s.backGammonMeccsPl));
            pr.setProperty("backGammonMeccsAi", String.valueOf(s.backGammonMeccsAi));
            pr.setProperty("nyertPontokJatekPl", String.valueOf(s.nyertPontokJatekPl));
            pr.setProperty("nyertPontokJatekAi", String.valueOf(s.nyertPontokJatekAi));
            pr.setProperty("nyertPontokMeccsPl", String.valueOf(s.nyertPontokMeccsPl));
            pr.setProperty("nyertPontokMeccsAi", String.valueOf(s.nyertPontokMeccsAi));
            pr.setProperty("nyertMeccsMeccsPl", String.valueOf(s.nyertMeccsMeccsPl));
            pr.setProperty("nyertMeccsMeccsAi", String.valueOf(s.nyertMeccsMeccsAi));
            for (int i=1; i<=6; i++) {
                pr.setProperty("dobottKockakJatekPl_"+i, String.valueOf(s.dobottKockakJatekPl[i]));
                pr.setProperty("dobottKockakJatekAi_"+i, String.valueOf(s.dobottKockakJatekAi[i]));
                pr.setProperty("dobottKockakMeccsPl_"+i, String.valueOf(s.dobottKockakMeccsPl[i]));
                pr.setProperty("dobottKockakMeccsAi_"+i, String.valueOf(s.dobottKockakMeccsAi[i]));
            }
            pr.setProperty("duplakJatekPl", String.valueOf(s.duplakJatekPl));
            pr.setProperty("duplakJatekAi", String.valueOf(s.duplakJatekAi));
            pr.setProperty("duplakMeccsPl", String.valueOf(s.duplakMeccsPl));
            pr.setProperty("duplakMeccsAi", String.valueOf(s.duplakMeccsAi));
            pr.setProperty("dobottKockakErtekJatekPl", String.valueOf(s.dobottKockakErtekJatekPl));
            pr.setProperty("dobottKockakErtekJatekAi", String.valueOf(s.dobottKockakErtekJatekAi));
            pr.setProperty("dobottKockakErtekMeccsPl", String.valueOf(s.dobottKockakErtekMeccsPl));
            pr.setProperty("dobottKockakErtekMeccsAi", String.valueOf(s.dobottKockakErtekMeccsAi));
            pr.setProperty("kezdesJogaJatekPl", String.valueOf(s.kezdesJogaJatekPl));
            pr.setProperty("kezdesJogaJatekAi", String.valueOf(s.kezdesJogaJatekAi));
            pr.setProperty("kezdesJogaMeccsPl", String.valueOf(s.kezdesJogaMeccsPl));
            pr.setProperty("kezdesJogaMeccsAi", String.valueOf(s.kezdesJogaMeccsAi));

            // save properties to project root folder
            pr.store(os, null);
            uzi1.setText("Játék elmentve.");
        
        } 
        catch (IOException io) {
            io.printStackTrace();
        }
        finally { 
            if (os != null) { 
                try { 
                    os.close(); 
                } 
                catch (IOException e) { 
                    e.printStackTrace(); 
                } 
            } 
        }
    }
    
    // elmenti a statisztika "osszes" oszlopanak ertekeit egy "statOssz.properties" file-ba
    void statOsszMentes() {
        try {
            os = new FileOutputStream("statOssz.properties");
            pr.clear();
            // statisztika - osszesen ertekek
            pr.setProperty("nyertJatekOsszPl", String.valueOf(s.nyertJatekOsszPl));
            pr.setProperty("nyertJatekOsszAi", String.valueOf(s.nyertJatekOsszAi));
            pr.setProperty("gammonOsszPl", String.valueOf(s.gammonOsszPl));
            pr.setProperty("gammonOsszAi", String.valueOf(s.gammonOsszAi));
            pr.setProperty("backGammonOsszPl", String.valueOf(s.backGammonOsszPl));
            pr.setProperty("backGammonOsszAi", String.valueOf(s.backGammonOsszAi));
            pr.setProperty("nyertPontokOsszPl", String.valueOf(s.nyertPontokOsszPl));
            pr.setProperty("nyertPontokOsszAi", String.valueOf(s.nyertPontokOsszAi));
            pr.setProperty("nyertMeccsOsszPl", String.valueOf(s.nyertMeccsOsszPl));
            pr.setProperty("nyertMeccsOsszAi", String.valueOf(s.nyertMeccsOsszAi));
            for (int i=1; i<=6; i++) {
                pr.setProperty("dobottKockakOsszPl_"+i, String.valueOf(s.dobottKockakOsszPl[i]));
                pr.setProperty("dobottKockakOsszAi_"+i, String.valueOf(s.dobottKockakOsszAi[i]));
            }
            pr.setProperty("duplakOsszPl", String.valueOf(s.duplakOsszPl));
            pr.setProperty("duplakOsszAi", String.valueOf(s.duplakOsszAi));
            pr.setProperty("dobottKockakErtekOsszPl", String.valueOf(s.dobottKockakErtekOsszPl));
            pr.setProperty("dobottKockakErtekOsszAi", String.valueOf(s.dobottKockakErtekOsszAi));
            pr.setProperty("kezdesJogaOsszPl", String.valueOf(s.kezdesJogaOsszPl));
            pr.setProperty("kezdesJogaOsszAi", String.valueOf(s.kezdesJogaOsszAi));
            pr.setProperty("utolsoTorles", s.utolsoTorles);

            // save properties to project root folder
            pr.store(os, null);
        
        } 
        catch (IOException io) {
            io.printStackTrace();
        }
        finally { 
            if (os != null) { 
                try { 
                    os.close(); 
                } 
                catch (IOException e) { 
                    e.printStackTrace(); 
                } 
            } 
        }
    }
    
    private void jatekBetolteseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jatekBetolteseActionPerformed
        betoltes();
    }//GEN-LAST:event_jatekBetolteseActionPerformed

    // betolti a mentett allast (pontokkal, stb egyutt) egy "mentettJatek.properties" file-bol
    void betoltes() {
        try {
            
            is = new FileInputStream("mentettJatek.properties");
            pr.clear();
            pr.load(is);
            
            // jat parameterei
            if (pr.getProperty("playersTurn").equals("true")) {
                jat.playersTurn = true;
            }else jat.playersTurn = false;
            //jat.playersColor = pr.getProperty("playersColor");
            //jat.aiColor = pr.getProperty("aiColor");
            pr.getProperty("kocka1");
            jat.kocka1 = (byte)Integer.parseInt(pr.getProperty("kocka1"));
            jat.kocka2 = (byte)Integer.parseInt(pr.getProperty("kocka2"));
            jat.lepes = (byte)Integer.parseInt(pr.getProperty("lepes"));
            jat.playersPip = Integer.parseInt(pr.getProperty("playersPip"));
            jat.aiPip = Integer.parseInt(pr.getProperty("aiPip"));
            jat.playerScore = Integer.parseInt(pr.getProperty("playerScore"));
            jat.aiScore = Integer.parseInt(pr.getProperty("aiScore"));
            jat.match = Integer.parseInt(pr.getProperty("match"));
            jat.tet = Integer.parseInt(pr.getProperty("tet"));
            if (pr.getProperty("plDuplazhat").equals("true")) {
                jat.plDuplazhat = true;
            }else jat.plDuplazhat = false;
            jat.kezdoKockaPl = (byte)Integer.parseInt(pr.getProperty("kezdoKockaPl"));
            jat.kezdoKockaAi = (byte)Integer.parseInt(pr.getProperty("kezdoKockaAi"));
            jat.crawford = Integer.parseInt(pr.getProperty("crawford"));
            if (pr.getProperty("jatekVege").equals("true")) {
                jat.jatekVege = true;
            }else jat.jatekVege = false;
            
            jat.lepesek.clear();     // mert a mentesbe nem kerul bele a lepesek, ha az elejen mentunk (167,167), igy nem lehet visszaolvasni es felulirni az aktualis erteket (amire ratoltjuk)
            if (jat.kocka1 != 0 && jat.kocka2 != 0) {       // ezek mar betoltodtek a file-bol, (ha igaz, tudjuk, h van erteke a lepesek-nek a file-ban)
                // elso ket lepest mindenkepp beolvassuk 
                Lepesek l0 = new Lepesek();
                l0.ertek = (byte)Integer.parseInt(pr.getProperty("lepesek_0_ertek"));
                l0.mozgatott = Integer.parseInt(pr.getProperty("lepesek_0_mozgatott"));
                if (pr.getProperty("lepesek_0_utesek").equals("true")) {
                    l0.utesek = true;
                }else l0.utesek = false;
                jat.lepesek.add(l0);
                Lepesek l1 = new Lepesek();
                l1.ertek = (byte)Integer.parseInt(pr.getProperty("lepesek_1_ertek"));
                l1.mozgatott = Integer.parseInt(pr.getProperty("lepesek_1_mozgatott"));
                if (pr.getProperty("lepesek_1_utesek").equals("true")) {
                    l1.utesek = true;
                }else l1.utesek = false;
                jat.lepesek.add(l1);
                // ha dupla volt, akkor a tobbit is
                if (jat.kocka1 == jat.kocka2) {
                    Lepesek l2 = new Lepesek();
                    l2.ertek = (byte)Integer.parseInt(pr.getProperty("lepesek_2_ertek"));
                    l2.mozgatott = Integer.parseInt(pr.getProperty("lepesek_2_mozgatott"));
                    if (pr.getProperty("lepesek_2_utesek").equals("true")) {
                        l2.utesek = true;
                    }else l2.utesek = false;
                    jat.lepesek.add(l2);
                    Lepesek l3 = new Lepesek();
                    l3.ertek = (byte)Integer.parseInt(pr.getProperty("lepesek_3_ertek"));
                    l3.mozgatott = Integer.parseInt(pr.getProperty("lepesek_3_mozgatott"));
                    if (pr.getProperty("lepesek_3_utesek").equals("true")) {
                        l3.utesek = true;
                    }else l3.utesek = false;
                    jat.lepesek.add(l3);
                } 
            }
            
            
            // tab parameterei
            // poziciok
            System.out.println("beolvasott playersColor: " + pr.getProperty("playersColor"));
            for (int i=0; i<28; i++) {
                Pozicio p = new Pozicio(i);
                p.szin = pr.getProperty("poziciok_"+i+"_szin");
                int w = Integer.valueOf(pr.getProperty("poziciok_"+i+"_korongok"));
                p.korongok = (byte)w;
                if (pr.getProperty("playersColor").equals(jat.playersColor)) {
                    poziciok[i].masol(p);
                }
                else {
                    poziciok[i].atSzinez(p);
                }
            }
            
            // whitelists
            jat.whiteList1.clear();
            jat.whiteList2.clear();
            if (Integer.parseInt(pr.getProperty("whiteList1_size")) != 0) {
                for (int i=0; i<Integer.parseInt(pr.getProperty("whiteList1_size")); i++) {
                    jat.whiteList1.add(Integer.parseInt(pr.getProperty("whiteList1_"+i)));
                }
            }
            if (Integer.parseInt(pr.getProperty("whiteList2_size")) != 0) {
                for (int i=0; i<Integer.parseInt(pr.getProperty("whiteList2_size")); i++) {
                    jat.whiteList2.add(Integer.parseInt(pr.getProperty("whiteList2_"+i)));
                }
            }
            
            //////////////////////////////////
            
            System.out.print("whitelist1: ");
            for (int i=0; i<jat.whiteList1.size(); i++) {
                System.out.print(jat.whiteList1.get(i) + ", ");
            }
            System.out.println();
            
            System.out.print("whitelist2: ");
            for (int i=0; i<jat.whiteList2.size(); i++) {
                System.out.print(jat.whiteList2.get(i) + ", ");
            }
            System.out.println();
            
            ////////////////////////////////////
            
            // gombok
            toolBar.getComponent(0).setEnabled(pr.getProperty("visszagomb").equals("enabled") ? true : false);
            toolBar.getComponent(1).setEnabled(pr.getProperty("eloregomb").equals("enabled") ? true : false);
            toolBar.getComponent(2).setEnabled(pr.getProperty("okgomb").equals("enabled") ? true : false);
            mentesMehet(true);          // kulonben az uj jatek utani egybol betoltes utan a mentes disabled lenne
            
            // dobokockak es duplazo kocka a betoltesnel majd peldanyositasra es bealitasra kerulnek a lepesek, lepes, playersturn, pip-ek, tet, plduplazhat parameterek alapjan.
            // elozo kockak etuntetese a GUI-n
            kockaPanel.removeAll();
            // kockak megjelenitese a GUI-n
            for (int i=0; i<jat.lepesek.size(); i++) {
                Kocka koc = new Kocka(jat.lepesek.get(i).ertek, jat.playersTurn ? jat.playersColor : jat.aiColor);
                koc.addMouseListener(jat);
                kockaPanel.add(koc);
                if (i < jat.lepes) {
                    koc.setVisible(false);
                }
            }
            kockaPanel.updateUI();
            
            // duplazo kocka
            if (jat.tet == 1) {
                duplazo.nullaz();
            }
            else {
                duplazo.duplaz(jat.plDuplazhat ? jat.playersColor : jat.aiColor, jat.tet);
            }
            
            // uzenet
            uzi1.setText(pr.getProperty("uzenet"));
            
            // pontokpanel frissitesre kerul majd a jat parameterei alapjan
            ((JLabel)pontokPanel.getComponent(7)).setText(String.valueOf(jat.playerScore) + "/" + jat.match);
            ((JLabel)pontokPanel.getComponent(4)).setText(String.valueOf(jat.aiScore) + "/" + jat.match);
            ((JLabel)pontokPanel.getComponent(5)).setText(String.valueOf(jat.aiPip));
            ((JLabel)pontokPanel.getComponent(8)).setText(String.valueOf(jat.playersPip));
            
            // statisztika, jatekra es meccsre vonatkozo ertekek
            s.nyertJatekJatekPl = Integer.parseInt(pr.getProperty("nyertJatekJatekPl"));
            s.nyertJatekJatekAi = Integer.parseInt(pr.getProperty("nyertJatekJatekAi"));
            s.nyertJatekMeccsPl = Integer.parseInt(pr.getProperty("nyertJatekMeccsPl"));
            s.nyertJatekMeccsAi = Integer.parseInt(pr.getProperty("nyertJatekMeccsAi"));
            s.gammonJatekPl = Integer.parseInt(pr.getProperty("gammonJatekPl"));
            s.gammonJatekAi = Integer.parseInt(pr.getProperty("gammonJatekAi"));
            s.gammonMeccsPl = Integer.parseInt(pr.getProperty("gammonMeccsPl"));
            s.gammonMeccsAi = Integer.parseInt(pr.getProperty("gammonMeccsAi"));
            s.backGammonJatekPl = Integer.parseInt(pr.getProperty("backGammonJatekPl"));
            s.backGammonJatekAi = Integer.parseInt(pr.getProperty("backGammonJatekAi"));
            s.backGammonMeccsPl = Integer.parseInt(pr.getProperty("backGammonMeccsPl"));
            s.backGammonMeccsAi = Integer.parseInt(pr.getProperty("backGammonMeccsAi"));
            s.nyertPontokJatekPl = Integer.parseInt(pr.getProperty("nyertPontokJatekPl"));
            s.nyertPontokJatekAi = Integer.parseInt(pr.getProperty("nyertPontokJatekAi"));
            s.nyertPontokMeccsPl = Integer.parseInt(pr.getProperty("nyertPontokMeccsPl"));
            s.nyertPontokMeccsAi = Integer.parseInt(pr.getProperty("nyertPontokMeccsAi"));
            s.nyertMeccsMeccsPl = Integer.parseInt(pr.getProperty("nyertMeccsMeccsPl"));
            s.nyertMeccsMeccsAi = Integer.parseInt(pr.getProperty("nyertMeccsMeccsAi"));
            for (int i=1; i<=6; i++) {
                s.dobottKockakJatekPl[i] = Integer.valueOf(pr.getProperty("dobottKockakJatekPl_"+i));
                s.dobottKockakJatekAi[i] = Integer.valueOf(pr.getProperty("dobottKockakJatekAi_"+i));
                s.dobottKockakMeccsPl[i] = Integer.valueOf(pr.getProperty("dobottKockakMeccsPl_"+i));
                s.dobottKockakMeccsAi[i] = Integer.valueOf(pr.getProperty("dobottKockakMeccsAi_"+i));
            }
            s.duplakJatekPl = Integer.parseInt(pr.getProperty("duplakJatekPl"));
            s.duplakJatekAi = Integer.parseInt(pr.getProperty("duplakJatekAi"));
            s.duplakMeccsPl = Integer.parseInt(pr.getProperty("duplakMeccsPl"));
            s.duplakMeccsAi = Integer.parseInt(pr.getProperty("duplakMeccsAi"));
            s.dobottKockakErtekJatekPl = Integer.parseInt(pr.getProperty("dobottKockakErtekJatekPl"));
            s.dobottKockakErtekJatekAi = Integer.parseInt(pr.getProperty("dobottKockakErtekJatekAi"));
            s.dobottKockakErtekMeccsPl = Integer.parseInt(pr.getProperty("dobottKockakErtekMeccsPl"));
            s.dobottKockakErtekMeccsAi = Integer.parseInt(pr.getProperty("dobottKockakErtekMeccsAi"));
            s.kezdesJogaJatekPl = Integer.parseInt(pr.getProperty("kezdesJogaJatekPl"));
            s.kezdesJogaJatekAi = Integer.parseInt(pr.getProperty("kezdesJogaJatekAi"));
            s.kezdesJogaMeccsPl = Integer.parseInt(pr.getProperty("kezdesJogaMeccsPl"));
            s.kezdesJogaMeccsAi = Integer.parseInt(pr.getProperty("kezdesJogaMeccsAi"));
            s.setLabels();
        } 
        catch (java.io.FileNotFoundException ex) {            // IOException
            System.out.println("error: " + ex.getMessage());
            uzi1.setText("Mentett file nem található!");        // vegye le ezt az uzenetet, ha ujra betoltjuk
	} 
        catch (IOException ex) { 
            Logger.getLogger(Tabla.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally {
            if (is != null) {
                try {
                    is.close();
                } 
                catch (IOException ex) {
                    Logger.getLogger(Tabla.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
	}
        
    }
    
    // betolti a statisztika "osszes" oszlopanak ertekeit egy "statOssz.properties" file-bol
    void statOsszBetoltes() {
        try {
            is = new FileInputStream("statOssz.properties");
            
            pr.load(is);
            
            // statisztika - osszesen ertekek
            s.nyertJatekOsszPl = Integer.parseInt(pr.getProperty("nyertJatekOsszPl"));
            s.nyertJatekOsszAi = Integer.parseInt(pr.getProperty("nyertJatekOsszAi"));
            s.gammonOsszPl = Integer.parseInt(pr.getProperty("gammonOsszPl"));
            s.gammonOsszAi = Integer.parseInt(pr.getProperty("gammonOsszAi"));
            s.backGammonOsszPl = Integer.parseInt(pr.getProperty("backGammonOsszPl"));
            s.backGammonOsszAi = Integer.parseInt(pr.getProperty("backGammonOsszAi"));
            s.nyertPontokOsszPl = Integer.parseInt(pr.getProperty("nyertPontokOsszPl"));
            s.nyertPontokOsszAi = Integer.parseInt(pr.getProperty("nyertPontokOsszAi"));
            s.nyertMeccsOsszPl = Integer.parseInt(pr.getProperty("nyertMeccsOsszPl"));
            s.nyertMeccsOsszAi = Integer.parseInt(pr.getProperty("nyertMeccsOsszAi"));
            for (int i=1; i<=6; i++) {
                s.dobottKockakOsszPl[i] = Integer.valueOf(pr.getProperty("dobottKockakOsszPl_"+i));
                s.dobottKockakOsszAi[i] = Integer.valueOf(pr.getProperty("dobottKockakOsszAi_"+i));
            }
            s.duplakOsszPl = Integer.parseInt(pr.getProperty("duplakOsszPl"));
            s.duplakOsszAi = Integer.parseInt(pr.getProperty("duplakOsszAi"));
            s.dobottKockakErtekOsszPl = Integer.parseInt(pr.getProperty("dobottKockakErtekOsszPl"));
            s.dobottKockakErtekOsszAi = Integer.parseInt(pr.getProperty("dobottKockakErtekOsszAi"));
            s.kezdesJogaOsszPl = Integer.parseInt(pr.getProperty("kezdesJogaOsszPl"));
            s.kezdesJogaOsszAi = Integer.parseInt(pr.getProperty("kezdesJogaOsszAi"));
            s.utolsoTorles = pr.getProperty("utolsoTorles");
            s.setLabels();
        } 
        catch (java.io.FileNotFoundException ex) {
            System.out.println("error: " + ex.getMessage());
	} 
        catch (IOException ex) { 
            Logger.getLogger(Tabla.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally {
            if (is != null) {
                try {
                    is.close();
                } 
                catch (IOException ex) {
                    Logger.getLogger(Tabla.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
	}
    }
    
    // az Ostabla ablak bezarasakor elmenti statisztika "osszes" oszlopanak ertekeit es megjenit egy mentesre (jatek allasara) vonatkozo dialogust
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        statOsszMentes();
        if (jatekMentese.isEnabled()) {         // csak akkor kerdez ra a mentesre, ha van mit menteni (az elso lepes elott nem), vagy ha a timer megy (ekkor a manuakis mentes sem mukodik)
            m = new Ment(this, true);
            m.setVisible(true);
        }
    }//GEN-LAST:event_formWindowClosing

    // a program megnyitasakor betolti statisztika "osszes" oszlopanak ertekeit es megjenit egy betoltesre (jatek allasara) vonatkozo dialogust
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        statOsszBetoltes();
        if (jatekBetoltese.isEnabled()) {
            b = new Betolt(this, true);
            b.setVisible(true);
        }
    }//GEN-LAST:event_formWindowOpened

    // megjeleniti a Beallitasok ablakot
    private void opciokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opciokActionPerformed
        // TODO add your handling code here:
        if (o == null) {
            o = new Opciok();
        }
        o.setVisible(true);
        if (jat.kezdoKockaPl == 0 && jat.kezdoKockaAi == 0 && jat.playerScore == 0 && jat.aiScore == 0) {        // csak a meccs elejen lehet allitani a meccs hosszat
            o.meccsHosszaEnabled(true);
        } else o.meccsHosszaEnabled(false);
    }//GEN-LAST:event_opciokActionPerformed

    // megjeleniti a Statisztika ablakot
    private void statisztikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statisztikaActionPerformed
        // TODO add your handling code here:
        s.setVisible(true);
    }//GEN-LAST:event_statisztikaActionPerformed

    // megjeleniti a Sugo ablakot
    private void tartalomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tartalomActionPerformed
        try {
            // TODO add your handling code here:
            Runtime.getRuntime().exec("hh.exe sugo.chm");
        } catch (IOException ex) {
            Logger.getLogger(Tabla.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tartalomActionPerformed

    // megjeleniti a Nevjegy ablakot
    private void nevjegyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nevjegyActionPerformed
        // TODO add your handling code here:
        Nevjegy n = new Nevjegy();
        n.setVisible(true);
        n.setLocationRelativeTo(this);
    }//GEN-LAST:event_nevjegyActionPerformed

    // atszinezi a tablat a Beallitasok ablakban megadott szinsemanak megfeleloen
    void hatterSzinBeallitas(String sema) {
        for (int i=0; i<28; i++) {
            switch (sema) {
                case "alap":
                    poziciok[i].hazakBg = Color.darkGray;
                    poziciok[i].fieldBg = new Color(100, 100, 100);
                    poziciok[i].sotet = new Color(196, 98, 16);
                    poziciok[i].vilagos = new Color(199, 182, 165);
                    break;
                case "élénk":
                    poziciok[i].hazakBg = new Color(0, 140, 208);
                    poziciok[i].fieldBg = new Color(62, 192, 255);
                    poziciok[i].sotet = new Color(223, 0, 0);
                    poziciok[i].vilagos = new Color(20, 175, 25);
                    break;
                case "pasztell":
                    poziciok[i].hazakBg = new Color(140, 70, 70);
                    poziciok[i].fieldBg = new Color(185, 120, 120);
                    poziciok[i].sotet = new Color(62, 150, 183);
                    poziciok[i].vilagos = new Color(172, 191, 217);
                    break;
                case "kontraszt":
                    poziciok[i].hazakBg = new Color(215, 107, 0);
                    poziciok[i].fieldBg = new Color(255, 156, 57);
                    poziciok[i].sotet = new Color(0, 138, 138);
                    poziciok[i].vilagos = new Color(0, 195, 195);
                    break;
            }
            if (i == 0 || i >= 25 && i <=27) {
                poziciok[i].setBackground(poziciok[i].hazakBg);
            } else {
                poziciok[i].setBackground(poziciok[i].fieldBg);
            }
            poziciok[i].repaint();
        }
    }
    
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
            java.util.logging.Logger.getLogger(Tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tabla().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jatekBetoltese;
    private javax.swing.JPanel jatekFelulet;
    private javax.swing.JMenuItem jatekMentese;
    private javax.swing.JPanel kontrol;
    private javax.swing.JMenuItem nevjegy;
    private javax.swing.JMenuItem opciok;
    private javax.swing.JMenuItem statisztika;
    private javax.swing.JMenuItem tartalom;
    private javax.swing.JMenuItem ujJatek;
    // End of variables declaration//GEN-END:variables
}
