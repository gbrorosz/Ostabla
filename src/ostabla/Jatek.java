package ostabla;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.Timer;

class Jatek implements MouseListener, ActionListener, ItemListener {
    // maga a gui
    Tabla tab;
    // az osszes poziciot tartalmazo tomb
    Pozicio[] poz;
    // az a pozicio ami mozgatasra van beallitva, ebbol kell elvenni
    Pozicio mozgatando;
    // az a pozicio ami fogadja a mozgatott korongot, ehhez kell hozzaadni
    Pozicio cel;
    // jatekoson van-e a sor
    boolean playersTurn;
    // a jatekos korongjainak szine
    String playersColor = "fekete";
    // a gep korongjainak szine
    String aiColor = "fehér";
    // az 1. kocka erteke
    byte kocka1;
    // az 2. kocka erteke
    byte kocka2;
    // hanyas lepes KOVETKEZIK a 2 v 4 elemu "lepesek" listabol
    byte lepes = 0;
    // a lepesek listaja (2 v 4 elemu)
    ArrayList<Lepesek> lepesek = new ArrayList<Lepesek>();
    // azon poziciok listaja Kocka1-re, amelyeket ha mozgatunk, a kovetkezo kockat is le tudjuk lepni.
    ArrayList<Integer> whiteList1 = new ArrayList<Integer>();
    // azon poziciok listaja Kocka2-re, amelyeket ha mozgatunk, a kovetkezo kockat is le tudjuk lepni.
    ArrayList<Integer> whiteList2 = new ArrayList<Integer>();
    // a jatekos pip count-ja
    int playersPip = 167;
    // a gep pip count-ja
    int aiPip = 167;
    // a gep lepeset idozito timer
    Timer t = new Timer(700, this);
    // az animaciot idozito timer
    Timer vill = new Timer(35, this);
    // szamlalja, hogy hanyszor jelent meg es tunt el a korong az animacio soran
    byte szamlalo = 0;
    // az animalt korong
    Korong villogo = null;
    // veletlen szam generator a kockadobashoz
    Random r = new Random();
    // a tobb fazisbol allo lepesek ebben taroljak a tovabbi lepesek mozgatando pozicioit
    ArrayList<Integer> folytatando = new ArrayList<Integer>();
    // a jatekos meccs pontjai
    int playerScore = 0;
    // a gep meccs pontjai
    int aiScore = 0;
    // a meccs hossza
    int match = 3;
    // az aktualis tet
    int tet = 1;
    // a duplazo kocka a jatekosnal van-e
    boolean plDuplazhat;
    // a jatekos kezdo dobasnal dobott 1 kockaja
    byte kezdoKockaPl = 0;
    // a gep kezdo dobasnal dobott 1 kockaja
    byte kezdoKockaAi = 0;
    // duplazas dialogus ablak
    Duplaz d;
    // ha ez = 1, akkor crawford game, (nincs d. kocka egy jatek erejeig). tabla/ujjatek allitja
    int crawford = 0;
    // hangeffektusok ki- bekapcsolva
    boolean hangok = false;
    AudioClip lepesHang;
    AudioClip dobasHang;
    AudioClip duplazasHang;
    AudioClip kockacsereHang;
    AudioClip nyeresHang;
    AudioClip vesztesHang;
    AudioClip utesHang;
    // igaz, ha a jatek veget ert
    boolean jatekVege = false;
    
    // konstruktor
    public Jatek(Tabla t) {
        tab = t;
        poz = tab.poziciok;
        // audioklippek
        URL url = null;
        try {
            url = Paths.get("audio/lepesHang.wav").toUri().toURL();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Jatek.class.getName()).log(Level.SEVERE, null, ex);
        }
        lepesHang = Applet.newAudioClip(url);
        try {
            url = Paths.get("audio/dobasHang.wav").toUri().toURL();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Jatek.class.getName()).log(Level.SEVERE, null, ex);
        }
        dobasHang = Applet.newAudioClip(url);
        try {
            url = Paths.get("audio/duplazasHang.wav").toUri().toURL();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Jatek.class.getName()).log(Level.SEVERE, null, ex);
        }
        duplazasHang = Applet.newAudioClip(url);
        try {
            url = Paths.get("audio/kockacsereHang.wav").toUri().toURL();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Jatek.class.getName()).log(Level.SEVERE, null, ex);
        }
        kockacsereHang = Applet.newAudioClip(url);
        try {
            url = Paths.get("audio/nyeresHang.wav").toUri().toURL();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Jatek.class.getName()).log(Level.SEVERE, null, ex);
        }
        nyeresHang = Applet.newAudioClip(url);
        try {
            url = Paths.get("audio/vesztesHang.wav").toUri().toURL();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Jatek.class.getName()).log(Level.SEVERE, null, ex);
        }
        vesztesHang = Applet.newAudioClip(url);
        try {
            url = Paths.get("audio/utesHang.wav").toUri().toURL();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Jatek.class.getName()).log(Level.SEVERE, null, ex);
        }
        utesHang = Applet.newAudioClip(url);
    }
    
    
    // jatekfelulet esemenykezeloje
    @Override
    public void mouseClicked(MouseEvent e) {
        if (playersTurn) {
            if (e.getComponent() instanceof Pozicio && ((Pozicio)e.getComponent()).szin.equals(playersColor)) {
                mozgatas((Pozicio)e.getComponent());
            }
            else if (e.getComponent() instanceof Kocka) {
                Kocka kattintott = (Kocka)e.getComponent();
                // ha a dobokockakra klikkel, akkor ez tortenik
                if (kattintott.getErtek() != 0) {
                    if (lepes == 0 && kocka1 != kocka2) {
                        kockaCsere();
                    }
                }
                // ha a duplazo kockara klikkel
                else if (!jatekVege){
                    tab.uzi1.setText("Csak dobás előtt duplázhat");
                }
            }
        }
        else {
            if (e.getComponent() instanceof Kocka) {
                Kocka kattintott = (Kocka)e.getComponent();
                // ha a duplazo kockara klikkel, akkor ez tortenik
                if (kattintott.getErtek() == 0) {
                    System.out.println("");
                    if (tab.toolBar.getComponent(2).isEnabled()) {                  // miutan az ai lepett es mielott OK-ra kattint a user
                        if (aiPip != 167 || playersPip != 167) {                    // kezdo lepesnel nem duplazhat (csak akkor duplazhat, ha nem kezdo)
                            if (crawford != 1) {                                    // ha nem Crawford Game
                                if ((plDuplazhat || tet == 1) && tet < 64) {        // ha lehet duplaznia a jatekosnak
                                    if (szaladasVan() && esely() < 1.3 || !szaladasVan() && esely() < 1.6 || playerScore + tet >= match) {   // ha ai elfogadja a duplat!!!
                                        tet *= 2;
                                        kattintott.duplaz(aiColor, tet);
                                        tab.toolBar.repaint();
                                        plDuplazhat = false;
                                        if (hangok) {
                                            duplazasHang.play();
                                        }
                                    }
                                    else {
                                        // ai feladja
                                        jatekVege = true;
                                        tab.toolBar.getComponent(0).setEnabled(false);
                                        tab.toolBar.getComponent(1).setEnabled(false);
                                        tab.toolBar.getComponent(2).setEnabled(false);
                                        playerScore += tet;
                                        ((JLabel)tab.pontokPanel.getComponent(7)).setText(String.valueOf(playerScore) + "/" + match);
                                        
                                        // statisztika
                                        tab.s.nyertJatekJatekPl++;
                                        tab.s.nyertJatekMeccsPl++;
                                        tab.s.nyertJatekOsszPl++;
                                        tab.s.nyertPontokJatekPl += tet;
                                        tab.s.nyertPontokMeccsPl += tet;
                                        tab.s.nyertPontokOsszPl += tet;
                                        
                                        String uzenet = "A Gép faladja. A Játékos nyerte a játékot";
                                        if (playerScore >= match) {
                                            uzenet += " és a meccset.";
                                            tab.s.nyertMeccsMeccsPl++;
                                            tab.s.nyertMeccsOsszPl++;
                                        }
                                        tab.s.setLabels();
                                        tab.uzi1.setText(uzenet);
                                        if (hangok) {
                                            nyeresHang.play();
                                        }
                                    }
                                }
                                else if (!jatekVege) tab.uzi1.setText("Nem duplázhat.");
                            }
                            else if (!jatekVege) tab.uzi1.setText("Nem duplázhat (Crawford Game).");
                        }
                        else if (!jatekVege) tab.uzi1.setText("Nem duplázhat kezdő lépésnél.");
                    }
                    else if (!jatekVege) tab.uzi1.setText("Nem duplázhat amíg az ellenfele nem végzett a lépéssel.");
                }
            }
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        // nem csinal semmit
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        // nem csinal semmit
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        // nem csinal semmit
    }
    @Override
    public void mouseExited(MouseEvent e) {
        // nem csinal semmit
    }
    
    // gombok, timerek esemenykezeloje
    @Override
    public void actionPerformed(ActionEvent e) {
        String source = e.getActionCommand();
        switch (source) {
            case "<<":
                vissza();
                break;
            case ">>":
                elore();
                break;
            case "OK":
                if (aiPip == 167 && playersPip == 167) {
                    if (kezdoKockaPl == 0) {
                        //leveszi a kockakat, es az uzenetet
                        //tab.uzi1.setText("");
                        tab.kockaPanel.removeAll();
                        
                        kezdoKockaPl = (byte)(r.nextInt(6)+1);
                        Kocka koc1 = new Kocka(kezdoKockaPl, playersColor);
                        koc1.addMouseListener(this);
                        tab.kockaPanel.add(koc1);
                        kezdoKockaAi = (byte)(r.nextInt(6)+1);
                        Kocka koc2 = new Kocka(kezdoKockaAi, aiColor);
                        koc2.addMouseListener(this);
                        tab.kockaPanel.add(koc2);
                        tab.kockaPanel.updateUI();
                        if (hangok) {
                            dobasHang.stop();
                            dobasHang.play();
                        }
                        
                        if (kezdoKockaPl == kezdoKockaAi) {
                            kezdoKockaPl = 0;
                            kezdoKockaAi = 0;
                            tab.uzi1.setText("Döntetlen, dobjon újra!");
                        }
                        else {
                            // uzenet, hogy ... kezd x,y kockakkal, nyomja meg az OK-t
                            tab.uzi1.setText((kezdoKockaPl > kezdoKockaAi ? "Ön" : "A Gép") + " kezd a dobott kockákkal. Kattintson az OK gombra!");
                        }
                    }
                    // beallitja, hogy ki lep es atadja a kockakat a dobas()-nak
                    else {
                        tab.toolBar.getComponent(2).setEnabled(false);          // OK gomb
                        tab.mentesMehet(true);                                  // innentol lehet menteni a jatekot
                        if (kezdoKockaPl > kezdoKockaAi) {
                            playersTurn = true;
                            tab.s.kezdesJogaJatekPl++;
                            tab.s.kezdesJogaMeccsPl++;
                            tab.s.kezdesJogaOsszPl++;
                        } 
                        else {
                            playersTurn = false;
                            tab.s.kezdesJogaJatekAi++;
                            tab.s.kezdesJogaMeccsAi++;
                            tab.s.kezdesJogaOsszAi++;
                        }
                        tab.s.setLabels();
                        dobas();
                    }
                    
                }
                else {
                    tab.toolBar.getComponent(0).setEnabled(false);          // vissza gomb
                    tab.toolBar.getComponent(2).setEnabled(false);          // OK gomb
                    playersTurn = !playersTurn;
                    if (!playersTurn) {
                        if (crawford != 1) {                                // ha nem Crawford Game
                            if ((!plDuplazhat || tet == 1) && tet < 64) {
                                if (aiScore + tet < match) {                // ha a tet megnyeresevel megnyerne a meccset is, akkor ne duplazzon
                                    if (szaladasVan() && esely() < 0.885 || !szaladasVan() && esely() < 0.8125 || playerScore + tet >= match) {       // ha erdemes duplazni
                                        System.out.println("AI felajanlja a duplat!");                                                                // mielott dobna
                                        // uj felugro dialog ablak, kulon osztaly az ablaknak, a jatek annak is az esemenykezeloje?
                                        // bezar gomb inaktiv (set default close opration = do nothing in design mode)
                                        if (d == null) {            // ha meg nincs letrehozva a duplazo dialog
                                            d = new Duplaz(tab, true);
                                        }
                                        System.out.println("aiColor: " + aiColor);
                                        d.setVisible(true);
                                    }
                                }
                            }
                        }
                    }
                    System.out.println("---------------------");
                    System.out.println(playersTurn ? "Jatekos lep" : "AI lep");
                    if (!jatekVege) dobas();
                }
                break;
            case "tick":
                if (lepes < lepesek.size() && !blokkolva(poz, lepesek.get(lepes).ertek)) {
                    aiVezerlo();
                }
                break;
            case "mvill":
                mVill();
                break;
            case "cvill":
                cVill();
                break;
            case "elfogad":
                tet *= 2;
                tab.duplazo.duplaz(playersColor, tet);
                tab.toolBar.repaint();
                plDuplazhat = true;
                // becsukja a dialog ablakot!!!!!!!
                d.setVisible(false);
                if (hangok) {
                    duplazasHang.play();
                }
                break;
            case "nem fogad el":
                jatekVege = true;
                tab.toolBar.getComponent(2).setEnabled(false);
                aiScore += tet;
                ((JLabel)tab.pontokPanel.getComponent(4)).setText(String.valueOf(aiScore) + "/" + match);
                
                // statisztika
                tab.s.nyertJatekJatekAi++;
                tab.s.nyertJatekMeccsAi++;
                tab.s.nyertJatekOsszAi++;
                tab.s.nyertPontokJatekAi += tet;
                tab.s.nyertPontokMeccsAi += tet;
                tab.s.nyertPontokOsszAi += tet;
                
                String uzenet = "A Játékos faladja. A Gép nyerte a játékot";
                if (aiScore >= match) {
                    uzenet += " és a meccset.";
                    tab.s.nyertMeccsMeccsAi++;
                    tab.s.nyertMeccsOsszAi++;
                }
                tab.s.setLabels();
                tab.uzi1.setText(uzenet);
                System.out.println(uzenet);
                //tab.ujJatekMehet(true);
                // becsukja a dialog ablakot!!!!!!!
                d.setVisible(false);
                if (hangok) {
                    vesztesHang.play();
                }
                break;
            case "ments":
                tab.mentes();
                tab.m.dispose();
                break;
            case "nements":
                tab.m.dispose();
                break;
            case "tolts":
                tab.betoltes();
                tab.b.dispose();
                break;
            case "netolts":
                tab.b.dispose();
                break;
        }
    }
    
    // opciok esemenykezeloje
    @Override
    public void itemStateChanged(ItemEvent e) {
        String sourceName = ((JComboBox)e.getSource()).getName();
        
        switch (sourceName) {
            case "meccsHossza":
                if (match != Integer.valueOf((String)e.getItem())) {
                    match = Integer.valueOf((String)e.getItem());
                    ((JLabel)tab.pontokPanel.getComponent(4)).setText(String.valueOf(aiScore) + "/" + match);
                    ((JLabel)tab.pontokPanel.getComponent(7)).setText(String.valueOf(playerScore) + "/" + match);
                    System.out.println("Item:  " + e.getItem());
                }
                break;
            case "jatekosFigurai":
                if (!((String)e.getItem()).equals(playersColor)) {          // ez csak egy szuro, hogy ne 2 ertek jojjon at, csak valtozas tenye triggereli ezt
                    //System.out.println("Item:  " + e.getItem());
                    if (playersColor.equals("fekete")) {
                        playersColor = "fehér";
                        aiColor = "fekete";
                    }
                    else {
                        playersColor = "fekete";
                        aiColor = "fehér";
                    }
                    for (int i=0; i<28; i++) {
                        Pozicio p = new Pozicio(poz[i]);
                        poz[i].atSzinez(p);
                    }
                    // dobokockak atszinezese
                    for (int i=0; i<tab.kockaPanel.getComponentCount(); i++) {
                        ((Kocka)tab.kockaPanel.getComponent(i)).atSzinez();
                    }
                    tab.kockaPanel.updateUI();
                    // d. kocka atszinezese
                    if (!tab.duplazo.getSzin().equals("dummy")) {
                        tab.duplazo.atSzinez();
                        tab.toolBar.repaint();
                    }
                    // uzenet
                    //tab.uzi1.setText("");
                    if (tab.uzi1.getText().startsWith("Öné")) {
                        String uzenet = tab.uzi1.getText().replaceFirst(aiColor, playersColor);
                        tab.uzi1.setText(uzenet);
                    }
                    //pontokpanel update
                    ((JLabel)tab.pontokPanel.getComponent(7)).setForeground(playersColor.equals("fehér")? Color.white : Color.black);
                    ((JLabel)tab.pontokPanel.getComponent(4)).setForeground(aiColor.equals("fehér")? Color.white : Color.black);
                    ((JLabel)tab.pontokPanel.getComponent(5)).setForeground(aiColor.equals("fehér")? Color.white : Color.black);
                    ((JLabel)tab.pontokPanel.getComponent(8)).setForeground(playersColor.equals("fehér")? Color.white : Color.black);
                    
                    //((JLabel)tab.pontokPanel.getComponent(6)).setText(playersColor);
                    ((JLabel)tab.pontokPanel.getComponent(6)).setForeground(playersColor.equals("fehér")? Color.white : Color.black);
                    //((JLabel)tab.pontokPanel.getComponent(3)).setText(aiColor);
                    ((JLabel)tab.pontokPanel.getComponent(3)).setForeground(aiColor.equals("fehér")? Color.white : Color.black);
                }
                break;
            case "tablaSzinei":
                if (!tab.szinSema.equals(e.getItem())) {
                    tab.szinSema = (String)e.getItem();
                    tab.hatterSzinBeallitas(tab.szinSema);
                }
                
                break;
            case "sebesseg":
                if (t.getDelay() != Integer.valueOf((String)e.getItem())) {
                    t.setDelay(Integer.valueOf((String)e.getItem()));
                    System.out.println("timer delay: " + t.getDelay());
                }
                break;
            case "hangok":
                boolean h;
                if (e.getItem().equals("ki")) {
                    h = false;
                }
                else {
                    h = true;
                }
                if (hangok != h) {
                    hangok = h;
                }
                break;
        }
    }
    
    // a mozgatando korongot animalja es elinditja a lepest a beallitott mozgatando es cel poziciokkal
    void mVill() {
        if (szamlalo == 0) {
            if (mozgatando.also) {                                                                 // ha also sor
                if (mozgatando.korongok<5) {
                    villogo = (Korong)mozgatando.getComponent(6-mozgatando.korongok);
                }
                else {
                    villogo = (Korong)mozgatando.getComponent(1);
                }
            }
            else {                                                                                 // ha felso sor
                if (mozgatando.korongok<5) {
                    villogo = (Korong)mozgatando.getComponent(mozgatando.korongok-1);
                }
                else {
                    villogo = (Korong)mozgatando.getComponent(4);
                }
            }
        }
        if (villogo.isVisible()) {
            villogo.setVisible(false);
        }
        else {
            villogo.setVisible(true);
        }
        villogo.repaint();
        szamlalo++;
        if (szamlalo == 4) {
            vill.stop();
            lep();
            szamlalo = 0;
            vill.setActionCommand("cvill");
            vill.start();
        }
    }
    
    // a cel korongot animalja
    void cVill() {
        if (szamlalo == 0) {
            if (cel.also) {                                                                 // ha also
                if (cel.korongok<5) {
                    villogo = (Korong)cel.getComponent(6-cel.korongok);
                }
                else {
                    villogo = (Korong)cel.getComponent(1);
                }
            }
            else {                                                                              // ha felso
                if (cel.korongok<5) {
                    villogo = (Korong)cel.getComponent(cel.korongok-1);
                }
                else {
                    villogo = (Korong)cel.getComponent(4);
                }
            }
        }
        if (villogo.isVisible()) {
            villogo.setVisible(false);
        }
        else {
            villogo.setVisible(true);
        }
        villogo.repaint();
        szamlalo++;
        if (szamlalo == 2) {
            vill.stop();
            szamlalo = 0;
        }
    }
    
    // A dobas()-ban mar eldolt, hogy az elso kocka nincs blokkolva.
    // kalkulalja, hogy az AI-nak mi legyen a kovetkezo lepese.
    void aiVezerlo() {
        
        int m = 0;                  // a mozgatasra kijelolt pozicio szama
        
        if (!folytatando.isEmpty()) {
            System.out.println("folytatandok: ");
            for (int f : folytatando) {
                System.out.println(f + ", ");
            }
            System.out.println();
            
            m = folytatando.get(0);
            folytatando.remove(0);
        }
        // ha nincs folytatando lepes
        else {
            // ha ki van utve
            if (poz[27].korongok != 0) {
                // ha az elso lepesnel tartunk es a ket kocka kulonbozo es a masodik sincs blokkolva
                if (lepes == 0 && kocka1 != kocka2 && !blokkolva(poz, lepesek.get(lepes+1).ertek)) {
                    // edit prioritas1_Belepteto(). ha egy van kiutve es egy masikkal tudna zarni/utni, akkor a masik kockat hasznalja bejovetelre
                    prioritas1_Belepteto();
                }
                m = 27;
            }
            // ha nincs kiutve
            else {
                m = prioritas2_Szaladas();
                
                if (m == 0){
                    if (hazAllapota(aiColor) > 3 && hazAllapota(playersColor) < 4) {
                        System.out.println("I - hazak allapota alapjan lep");
                        m = utesKor();
                    }
                    else if (hazAllapota(playersColor) > 3 && hazAllapota(aiColor) < 4 || blotVan()) {
                        System.out.println("I - hazak allapota alapjan lep");
                        m = zarasKor();
                    }
                    else {
                        System.out.println("II - pip alapjan lep");
                        // ha vesztore all
                        if (aiPip > playersPip) {
                            m = utesKor();
                        }
                        // ha nyerore all (vagy tie)
                        else {
                            m = zarasKor();
                        }
                    }
                }
                // csak tesztelesre
                /*if (m == 0) {
                    System.out.println("RANDOM MOVE");
                    int vsz = r.nextInt(mozgathatok(lepesek.get(lepes).ertek).size());
                    m= mozgathatok(lepesek.get(lepes).ertek).get(vsz);
                }*/
            }
        }
        if (m != 0) {
            aiMozgatas(poz[m]);
            System.out.println("Moving: " + m + ", " + lepesek.get(lepes).ertek + " ertekkel");
        }
    }
    
    // aiVezerlo() alprogramja, ha az utese a prioritas
    int utesKor() {
        // utes > (zaras) > lepes
        System.out.println("--- utes > (zaras) > lepes ---");
        int m = prioritas4_Utes(); 
        if (aiPip - playersPip < 30) {
            if (m == 0) {
                m = prioritas3_Zaras();
            }
        }
        if (m == 0) {
            m = prioritas5_Lepes();
        }
        return m;
    }
    
    // aiVezerlo() alprogramja, ha az zarase a prioritas
    int zarasKor() {
        // zaras > (utes) > lepes
        System.out.println("--- zaras > (utes) > lepes ---");
        int m = prioritas3_Zaras();
        if (playersPip - aiPip < 30) {
            if (m == 0) {
                m = prioritas4_Utes();
            }
        }
        if (m == 0) {
            m = prioritas5_Lepes();
        }
        return m;
    }
    
    // kivalassza, hogy melyik kockaval jojjon be, ha kell kockat cserel, m erteken nem valtoztat (m=27)
    void prioritas1_Belepteto() {
        ArrayList<Integer> uList1 = utniTudok0(poz, lepesek.get(lepes).ertek);
        ArrayList<Integer> uList2 = utniTudok0(poz, lepesek.get(lepes+1).ertek);
        ArrayList<Integer> zList1 = zarniTudok0(poz, lepesek.get(lepes).ertek);
        ArrayList<Integer> zList2 = zarniTudok0(poz, lepesek.get(lepes+1).ertek);
        boolean csere;
        if (whiteList1.contains(27) && !whiteList2.contains(27)) {
            csere = false;
        }
        else if (!whiteList1.contains(27) && whiteList2.contains(27)) {
            csere = true;
        }
        // ha egyik sem, vagy mindketto whitelist-es
        else {
            /* Kulonbseg csak az, hogy ha egyik sem akkor csak egyet fog lepni! Azt valasztja, amelyikkel uthet, zarhat. 
            Ha egyikkel sem tud se utni, se zarni, akkor a nagyobbal jon be. */

            // melyikkel uthet
            boolean elsovelTudUtni = uList1.contains(27);
            boolean masodikkalTudUtni = uList2.contains(27);
            // ha az elsovel tud utni, de a masodikkal nem
            if (elsovelTudUtni && !masodikkalTudUtni) {
                csere = false;
            }
            // ha az elsovel nem tud utni, de a masodikkal igen
            else if (!elsovelTudUtni && masodikkalTudUtni) {
                csere = true;
            }
            // ha mindkettovel tud utni, akkor usson a kisebb ertekkel.
            else if (elsovelTudUtni && masodikkalTudUtni) {
                if (lepesek.get(lepes).ertek < lepesek.get(lepes+1).ertek) {
                    csere = false;
                }
                else {
                    csere = true;
                }
            }
            // ha egyikkel sem tud utni, akkor 
            else {
                // ha ütni vagy zárni lehet valamelyik kockával a fielden, akkor a másikkal be jönni 
                if (poz[27].korongok == 1) {
                    // utes 
                    if (aiPip > playersPip) {
                        if (!uList2.isEmpty() && uList1.isEmpty()) {
                            csere = false;
                        }
                        else if (uList2.isEmpty() && !uList1.isEmpty()) {
                            csere = true;
                            System.out.println("Cserelek kockat, hatha valaki mas a fielden hasznat venne az 1. kockanak utesre ");
                        }
                        else {
                            if (lepesek.get(lepes).ertek < lepesek.get(lepes+1).ertek) {
                                csere = false;
                            }
                            else {
                                csere = true;
                            }
                        }
                    }
                    // zarasnal (nyeresre all az AI)
                    else {
                        // ha valaki mas a fielden hasznat venne a 2. kockanak zarasra
                        if (!zList2.isEmpty() && !zList2.contains(27) && zList1.isEmpty()) {
                            csere = false;
                        }
                        else if (!zList1.isEmpty() && !zList1.contains(27) && zList2.isEmpty()) {
                            csere = true;
                            System.out.println("Cserelek kockat, hatha valaki mas a fielden hasznat venne az 1. kockanak zarasra ");
                        }
                        else {
                            // megvizsgalja, hogy melyikkel tud zarni
                            boolean elsovelTudZarni = zList1.contains(27);
                            boolean masodikkalTudZarni = zList2.contains(27);
                            // ha az elsovel tud zarni, de a masodikkal nem
                            if (elsovelTudZarni && !masodikkalTudZarni) {
                                csere = false;
                            }
                            // ha az elsovel nem tud zarni, de a masodikkal igen
                            else if (!elsovelTudZarni && masodikkalTudZarni) {
                                csere = true;
                            }
                            // ha mindkettovel vagy egyikkel sem tud zarni, akkor...
                            else {
                                if (lepesek.get(lepes).ertek > lepesek.get(lepes+1).ertek) {
                                    csere = false;
                                }
                                else {
                                    csere = true;
                                }
                            }
                        }
                    }
                }
                
                
                else {
                    // megvizsgalja, hogy melyikkel tud zarni
                    boolean elsovelTudZarni = zList1.contains(27);
                    boolean masodikkalTudZarni = zList2.contains(27);
                    // ha az elsovel tud zarni, de a masodikkal nem
                    if (elsovelTudZarni && !masodikkalTudZarni) {
                        csere = false;
                    }
                    // ha az elsovel nem tud zarni, de a masodikkal igen
                    else if (!elsovelTudZarni && masodikkalTudZarni) {
                        csere = true;
                    }
                    // ha mindkettovel vagy egyikkel sem tud zarni, akkor...
                    else {
                        // ha rosszul all
                        if (playersPip < aiPip) {
                            if (lepesek.get(lepes).ertek < lepesek.get(lepes+1).ertek) {
                                csere = false;
                            }
                            else {
                                csere = true;
                            }
                        }
                        // ha jol all
                        else {
                            if (lepesek.get(lepes).ertek > lepesek.get(lepes+1).ertek) {
                                csere = false;
                            }
                            else {
                                csere = true;
                            }
                        }
                    }
                }
            }
        }
        if (csere) kockaCsere();
    }
    
    // ha a ket ellenfel mar tuljutott egymas osszes korongjan, nem lesz utesi lehetoseg, akkor ez a teendo. Cel a bg es g elkerulese, hogy minel elobb beerjen a hazba az osszes koronggal es kipakolja
    int prioritas2_Szaladas() {
        if (szaladasVan()) {
            if (vegJatek(poz)){
                // ha olyat dobott, ahol van
                int m = 25-lepesek.get(lepes).ertek;            // az a poz, ahova kijon a kocka (19-24)
                if (poz[m].szin.equals(aiColor)) {
                    if (lephet(m)) {
                        return m;
                    }
                }
                /* ha m folott vannak mozgathatok for (int i=m-1; i>=19; i--), akkor azzal lepje le, ahonnan uresre lepne, ha olyan nincs akkor
                azzal amelyik messzebb van a haztol, ha csak egy van, akkor azzal. Ha nincs egy sem, akkor az m alatt levo legkozelebbivel lepjen */
                int[] mozgathatokAhazban = new int[7];
                ArrayList<Integer> mozg = mozgathatok(poz, lepesek.get(lepes).ertek);
                for (int i=0; i<mozg.size(); i++) {
                    mozgathatokAhazban[25-mozg.get(i)] = poz[mozg.get(i)].korongok;             // feltolti a 7es tombot, forditva
                }
                // megszamolja, hogy hany pozicion vannak folotte
                byte hanyan = 0;
                byte melyik = 0;
                for (int i=25-m+1; i<=6; i++) {
                    if (mozgathatokAhazban[i] > 0) {
                        hanyan++;
                        melyik = (byte)i;
                    }
                }
                // ha csak egy van folotte, akkor azzal, ha lephet()  :)
                if (hanyan == 1) {
                    if (lephet(25-melyik)) {
                        return 25-melyik;                                                       // visszafordit
                    }
                }
                // ha nincsenek folotte, akkor az m alatt levo legkozelebbivel lepjen
                else if (hanyan == 0) {
                    for (int i=25-m-1; i>0; i--) {
                        if (mozgathatokAhazban[i] > 0) {
                            if (lephet(25-i)) {
                                return 25-i;
                            }
                        }
                    }
                }
                // ha tobb pozicion vannak folotte
                /*akkor azzal lepje le, ahonnan uresre lepne, ha olyan nincs akkor azzal amelyik messzebb van a haztol*/
                else {
                    for (int i=6; i>=25-m+1; i--) {
                        if (mozgathatokAhazban[i] > 0 && mozgathatokAhazban[i-lepesek.get(lepes).ertek] == 0) {
                            if (lephet(25-i)) {
                                return 25-i;
                            }
                        }
                    }
                    for (int i=6; i>=25-m+1; i--) {
                        if (mozgathatokAhazban[i] > 0) {
                            if (lephet(25-i)) {
                                return 25-i;
                            }
                        }
                    }
                }
            }
            // ha szaladas es nem vegjatek
            else {
                // ha van korongja az ellenfel hazaban: (backgammon elkerulesere)
                // a mozgathatok kozul, ciklusban: a legkisebbel(alulrol) lepjen, ha lephet, ha nem akkor a ciklus kov elemet probalja, stb.
                // ha nincs:
                // amelyikkel a legkevesebb koronggal rendelkezo mezore lephet a sajat hazaban
                // ha ilyen nincs, akkor a legnagyobbal
                ArrayList<Integer> mlista = mozgathatok(poz, lepesek.get(lepes).ertek);                      // ha van az ellenfel hazaban
                for (int i=0; i<mlista.size(); i++) {
                    if (mlista.get(i) <= 6) {
                        if (lephet(mlista.get(i))) {
                            return mlista.get(i);
                        }
                    }
                }
                byte legkisebb = poz[19].korongok;                      // a hazaban melyik pozicion van a legkevesebb korong
                for (int i=20; i<=24; i++) {
                    if (poz[i].korongok < legkisebb) {
                        legkisebb = poz[i].korongok;
                        
                    }
                }
                //System.out.println("AI hazaban legkevesebb korong egy pozicion: " + legkisebb);         // amelyikkel a legkevesebb koronggal rendelkezo mezore lephet a sajat hazaban
                for (int i=0; i<mlista.size(); i++) {
                    Pozicio c = celSzamito(poz[mlista.get(i)], lepesek.get(lepes).ertek);
                    if (mlista.get(i) < 19 && c.szam  >= 19 && c.szam <= 24 && c.korongok == legkisebb){
                        if (lephet(mlista.get(i))) {
                            return mlista.get(i);
                        }
                    }
                }
                for (int i=0; i<mlista.size(); i++) {
                    if (lephet(mlista.get(i))) {
                        return mlista.get(i);
                    }
                }
            }
        }
        // ha nincs szaladas
        return 0;
    }
    
    /*ha a ket ellenfel mar tuljutott egymas osszes korongjan. ha az (ai legkisebb pozicioja +1) tol szamitva, a 24-esig 
    (26-ig, mert ha ki van utve a jatekos, akkor sincs szaladas) nincs player szinu pozicio.*/
    boolean szaladasVan () {
        int legkisebb =0;
        for (int i=1; i<25; i++) {
            if (poz[i].szin.equals(aiColor)) {
                legkisebb = i;
                break;
            }
        }
        for (int i=legkisebb+1; i<27; i++) {
            if (poz[i].szin.equals(playersColor)) {
                return false;
            }
        }
        return true;
    }
    
    // a hatralevo kocka(kk)al mely poziciokra lephet
    // (uteshez) param: ha nem uteshez kell az adat, akkor ugy szamol, hogy oda nem lephet, ahol 1 db ellenseges korong van
    // ertek parameterrel, kockara specifikusan: (byte ertek) --> (pl. 1-rol 3,4 > 4,8   4,3 > 5,8)
    ArrayList<Byte> lehetsegesCelok(int mozg, byte ertek, boolean uteshez) {
        ArrayList<Byte> lcList = new ArrayList<Byte>();             // lehetseges celok listaja
        ArrayList<Byte> lcList2 = new ArrayList<Byte>();            // lehetseges celok listaja 0 es 25 nelkul
        ArrayList<Byte> lcList3 = new ArrayList<Byte>();            // lehetseges celok listaja blokkoltak nelkul
        
        // eloszor bepakoljuk az ertekeket
        for (int i=lepes; i<lepesek.size(); i++) {
            lcList.add(lepesek.get(i).ertek);
        }
        
        // ha ket ertek van benne akkor megcsereli, ha ezt a meghivo program keri
        if (lcList.size() == 2 && ertek == lepesek.get(lepes+1).ertek) {                                                // if (lcList.size() == 2 && ertek == kocka2)
            byte seged;
            seged = lcList.get(0);
            lcList.set(0, lcList.get(1));
            lcList.set(1, seged);
        }
        
        // utana lecsereljuk az ertekeket a mozgatando+ertek osszegere. 
        for (int i=0; i<lcList.size(); i++) {
            lcList.set(i, (byte)(celSzamito(poz[mozg], lcList.get(i))).szam);           //csak akkor hozzaadni, ha mozhathato !!!!!!!!!!
            mozg = lcList.get(i);
        }
        
        //lcList.sort(null);                                          // novekvo sorrendbe rendez
        Collections.sort(lcList);
        
        // utana eltavolitjuk a 0-sokat es a 25-osoket. ezeket csak vegjateknal lehetne felhasznalni ??
        for (int i=0; i<lcList.size(); i++) {
            if (lcList.get(i) != 25) {       // ha nincs vegjatek, akkor kivesszuk a 25-ot a listabol - nem! removed (vegJatek(poz) ? true :...) 
                lcList2.add(lcList.get(i));
            }
        }
        // blokkoltakat es az azt kovetoket is eltavolitjuk, (ahol egy fekete van (utheto) az benne marad a listaban) - itt nem, az utesnel igen
        int blokkolt=100;
        
        if (uteshez) {
            for (int i=0; i<lcList2.size(); i++) {
                if (poz[lcList2.get(i)].szin.equals(playersColor) && poz[lcList2.get(i)].korongok >= 2) {                    // (uteshez)
                    blokkolt = lcList2.get(i);
                    break;
                }
            }
        }
        else {
            for (int i=0; i<lcList2.size(); i++) {
                if (poz[lcList2.get(i)].szin.equals(playersColor)) {                                                        //  (zarashoz)
                    blokkolt = lcList2.get(i);
                    break;
                }
            }
        }
        
        for (int i=0; i<lcList2.size(); i++) {
            if (lcList2.get(i) < blokkolt) {
                lcList3.add(lcList2.get(i));
            }
        }
        return lcList3;
    }
    
    // az ellenfél utolsó korongját megkeresi
    int utolso() {
        int utolso =0;
        for (int i=26; i>=1; i--) {
            if (poz[i].szin.equals(playersColor)) {
                utolso = i;
                break;
            }
        }
        return utolso;
    }
    
    /* azokat az alprogramokat rendezi prioritas szerint, amelyek sem utes-t, sem zaras-t nem csinalnak, 
    de kozuk van hozza. Egyre semlegesebb lepesek vannak a lista vege fele. */
    int prioritas5_Lepes() {
        int m;
        
        // kiszabaditas
        m = kiszabaditas();
        if (m != 0) {
            return m;
        }
        
        // ha vesztore all, nyit (ha lehet es ha kell), elokesziti az utest
        if (aiPip > playersPip) {
            // ha lagalabb 20-szal vezet a pl. es nem all tul jol a haza, az ai probal moge kerulni (max 4 koronggal,  2 anchor-t kialakitani)
            if (aiPip - playersPip > 20 && hazAllapota(playersColor) < 4 && backStones() < 4) {
                // nyit, amig atkerultek elegen az ellenfel moge
                m = nyitas(lepesek.get(lepes).ertek);
                if (m != 0) {
                    return m;
                }
                if (lepes == 0 && kocka1!=kocka2) {
                    m = nyitas(lepesek.get(lepes+1).ertek);
                    if (m != 0) {
                        return m;
                    }
                }
            }
        }
        // ha nyerore all, probalja atugrani a pl. korongjait, elokesziti a zarast
        else {
            m = atUgras(lepesek.get(lepes).ertek);
            if (m != 0) {
                return m;
            }
            if (lepes == 0 && kocka1!=kocka2) {
                m = atUgras(lepesek.get(lepes+1).ertek);
                if (m != 0) {
                    return m;
                }
            }
        }
        
        // mindket esetben (akar vesztore, akar nyerore all)
        
        // vegjateknal, bear off
        if (vegJatek(poz)) {
            m = kiPakol(lepesek.get(lepes).ertek);
            if (m != 0) {
                return m;
            }
            if (lepes == 0 && kocka1!=kocka2) {
                m = kiPakol(lepesek.get(lepes+1).ertek);
                if (m != 0) {
                    return m;
                }
            }
        }
        
        
        
        m = hazEpites(lepesek.get(lepes).ertek);
        if (m != 0) {
            return m;
        }
        if (lepes == 0 && kocka1!=kocka2) {
            m = hazEpites(lepesek.get(lepes+1).ertek);
            if (m != 0) {
                return m;
            }
        }
        
        m = semlegesLepes();
        if (m != 0) {
            return m;
        }
        System.out.println("A lepes() nem talalt egy db mozgathatot sem!!! ");          // ennek nem szabad megtortennie!!
        return 0;
        
    }
    
    // azoknak a pozicioknak a listaja, amelyek at tudjak ugrani az ellenfel utolso poziciojat, akar tobb kocka osszegevel is.
    ArrayList<Osszeggel> atUgrok(byte ertek) {
        ArrayList<Osszeggel> uList = new ArrayList<Osszeggel>();
        
        for (int i=0; i<mozgathatok(poz, ertek).size(); i++) {
            Pozicio m = poz[mozgathatok(poz, ertek).get(i)];
            if (m.szam < utolso() && m.korongok >=3) {
                for (int j=0; j<lehetsegesCelok(m.szam, ertek, false).size(); j++) {
                    int lc = lehetsegesCelok(m.szam, ertek, false).get(j);
                    if (lc > utolso()) {
                        //uList.add(m.szam);
                        Osszeggel ossz = new Osszeggel(m.szam, j, lc);
                        uList.add(ossz);
                        break;
                    }
                }
            }
            // ha ketto van es mindkettovel atugorjuk az utolsot
            else if (m.szam < utolso() && m.korongok == 2) {
                // ha meg legalabb 2-t lephetunk
                if (lepesek.size()-lepes >= 2) {
                    // duplara is ervenyes
                    int c1 = celSzamito(m, kocka1).szam;
                    int c2 = celSzamito(m, kocka2).szam;
                    if (mozgathatok(poz, kocka1).contains(m.szam) && mozgathatok(poz, kocka2).contains(m.szam)) {
                        if (c1 > utolso() && c2 > utolso()
                                || c1 > utolso() && poz[c2].szin.equals(aiColor)
                                || c2 > utolso() && poz[c1].szin.equals(aiColor)
                                ) {
                            Osszeggel ossz = new Osszeggel(m.szam, 0, c1);              // itt nem kell tovabbvinni a celt, hanem a m-et megegyszer. atUgras-ban maualisan hozzaadni a folytatandohoz
                            uList.add(ossz);
                        }
                    }
                }
            }
        }
        return uList;
    }
    
    // az atUgrok listarol azt mozgatja, amelyik a legkevesebb korongot tart. mezore tud lepni. kockacserevel is probalja
    int atUgras(byte ertek) {
        int m = 0;
        ArrayList<Osszeggel> uList = atUgrok(ertek);
        
        if (lepes == 0 && kocka1!=kocka2 && ertek == lepesek.get(lepes+1).ertek) {
            if (!uList.isEmpty()) {
                for (int h=0; h<15; h++) {
                    for (int i=0; i<uList.size(); i++) {
                        m = uList.get(i).m;
                        int j = uList.get(i).j;
                        int c = celSzamito(poz[m], ertek).szam;
                        if (poz[c].korongok == h){
                            kockaCsere();
                            if (lephet(m)) {
                                int kozbulso = m;
                                for (int k=0; k<j; k++) {
                                    kozbulso += lepesek.get(lepes).ertek;
                                    folytatando.add(kozbulso);
                                }
                                // manualisan hozzaadjuk az m-et a folytatandokhoz, mert itt 0-val ter vissza a j, ekkor a fenti for ciklus (k) nem fut le egyszer sem
                                if (poz[m].korongok == 2) {
                                    folytatando.add(m);
                                }
                                System.out.println("ATUGRAS - kockacsere");
                                return m;
                            }
                            else {
                                kockaCsere();
                            }
                        }
                    }
                }
            }
        }
        else {
            if (!uList.isEmpty()) {
                for (int h=0; h<15; h++) {
                    for (int i=0; i<uList.size(); i++) {
                        m = uList.get(i).m;
                        int j = uList.get(i).j;
                        int c = celSzamito(poz[m], ertek).szam;
                        if (poz[c].korongok == h){
                            if (lephet(m)) {
                                int kozbulso = m;
                                for (int k=0; k<j; k++) {
                                    kozbulso += lepesek.get(lepes).ertek;
                                    folytatando.add(kozbulso);
                                }
                                // manualisan hozzaadjuk az m-et a folytatandokhoz, mert itt 0-val ter vissza a j, ekkor a fenti for ciklus (k) nem fut le egyszer sem
                                if (poz[m].korongok == 2) {
                                    folytatando.add(m);
                                }
                                System.out.println("ATUGRAS");
                                return m;
                            }
                        }
                    }
                }
            }
        }
        return m;
    }
    
    // Lepes metodushoz.
    /* azok, amelyek be tudnak lepni a haz ures poziciojara, vagy le tudnak zarni egyet, az utolso ellenseges pozicio moge - maguk nyitasa nelkul*/
    ArrayList<Osszeggel> hazEpitok(byte ertek) {
        
        ArrayList<Osszeggel> hList = new ArrayList<Osszeggel>();
        for (int i=0; i<mozgathatok(poz, ertek).size(); i++) {
            Pozicio m = poz[mozgathatok(poz, ertek).get(i)];
            if (m.korongok >= 3) {
                for (int j=0; j<lehetsegesCelok(m.szam, ertek, false).size(); j++) {
                    int lc = lehetsegesCelok(m.szam, ertek, false).get(j);
                    if (lc>utolso() && lc>18 && lc<25 && (poz[lc].szin.equals("dummy") || (poz[lc].szin.equals(aiColor) && poz[lc].korongok==1))) {
                        Osszeggel ossz = new Osszeggel(m.szam, j, lc);
                        hList.add(ossz);
                        break;
                    }
                }
            }
        }
        return hList;
    }
    
    /* a hazEpitok listabol a legalacsonyabb pozicion allot mozgatja, amelyik lephet (a lepes kihasznalasnak megfeleloen) akar osszeggel is, 
    mindket kockaval probalva. Az (ertek) parameterrel: a kocka erteke amivel kerdezi a meghivo prg. Ha ez a jobb oldali kocka (lepes+1), akkor meg kell cserelni oket */
    int hazEpites(byte ertek) {
        int m = 0;
        ArrayList<Osszeggel> hList = hazEpitok(ertek);
        
        if (lepes == 0 && kocka1!=kocka2 && ertek == lepesek.get(lepes+1).ertek) {
            if (!hList.isEmpty()) {
                for (int i=0; i<hList.size(); i++) {
                    m = hList.get(i).m;         // a mozgatando
                    int j = hList.get(i).j;     // hanyszor kell leptetni
                    kockaCsere();
                    if (lephet(m)) {
                        int kozbulso = m;
                        for (int k=0; k<j; k++) {
                            kozbulso += lepesek.get(lepes).ertek;
                            folytatando.add(kozbulso);
                        }
                        System.out.println("HAZ EPITESE - kockacsere");
                        return m;
                    }
                    else {
                        kockaCsere();
                    }
                }
            }
        }
        else {
            if (!hList.isEmpty()) {
                for (int i=0; i<hList.size(); i++) {
                    m = hList.get(i).m;         // a mozgatando
                    int j = hList.get(i).j;     // hanyszor kell leptetni
                    if (lephet(m)) {
                        int kozbulso = m;
                        for (int k=0; k<j; k++) {
                            kozbulso += lepesek.get(lepes).ertek;
                            folytatando.add(kozbulso);
                        }
                        System.out.println("HAZ EPITESE");
                        return m;
                    }
                }
            }
        }
        return m;
    }
    
    // a zarasi es az utesi taktikanak egyarant potencialisan kedvezo lepesek, hasznossag szerinti sorrendben. Mindket helyzetben hasznalhatoak.
    int semlegesLepes() {
        int m = 0;
        
        // kulonbseggel lep
        if (lepes == 0 && kocka1!=kocka2) {
            m = lepes1(lepesek.get(lepes).ertek, true);                             // egyik kockaval kezdve az utolsotol 
            if (m != 0) {
                System.out.println("LEPES 1 - utolsotol");
                return m;
            }
            m = lepes1(lepesek.get(lepes+1).ertek, true);                           // masik kockaval kezdve az utolsotol
            if (m != 0) {
                System.out.println("LEPES 1 - utolsotol - kockacsere");
                return m;
            }
            m = lepes1(lepesek.get(lepes).ertek, false);                            // egyik kockaval kezdve a vegerol
            if (m != 0) {
                System.out.println("LEPES 1 - poz. 24-tol");
                return m;
            }
            m = lepes1(lepesek.get(lepes+1).ertek, false);                          // masik kockaval kezdve a vegerol
            if (m != 0) {
                System.out.println("LEPES 1 - poz. 24-tol - kockacsere");
                return m;
            }
        }
        
        
        
        
        // lepniTudok4a (duplaval)
        if (kocka1 == kocka2 && lepesek.size()-lepes >= 2) {
            m = lepes4a();
            if (m != 0) {
                System.out.println("LEPES 4a");
                return m;
            }
        }
        
        // egyrol egyre
        m = lepes3a(lepesek.get(lepes).ertek);
        if (m != 0) {
            System.out.println("LEPES 3a");
            return m;
        }
        if (lepes == 0 && kocka1!=kocka2) {
            m = lepes3a(lepesek.get(lepes+1).ertek);
            if (m != 0) {
                System.out.println("LEPES 3a - kockacsere");
                return m;
            }
        }
        
        
        // tobbrol tobbre
        m = lepes3(lepesek.get(lepes).ertek);
        if (m != 0) {
            System.out.println("LEPES 3");
            return m;
        }
        if (lepes == 0 && kocka1!=kocka2) {
            m = lepes3(lepesek.get(lepes+1).ertek);
            if (m != 0) {
                System.out.println("LEPES 3 - kockacsere");
                return m;
            }
        }
        
        
        
        // lelepi az utolso mogott
        m = utolsoMogottiLepes(lepesek.get(lepes).ertek);
        if (m != 0) {
            System.out.println("LEPES az utolso mogott");
            return m;
        }
        if (lepes == 0 && kocka1!=kocka2) {
            m = utolsoMogottiLepes(lepesek.get(lepes+1).ertek);
            if (m != 0) {
                System.out.println("LEPES az utolso mogott - kockacsere");
                return m;
            }
        }
        
        /*m = leEpites(4);
        if (m != 0) {
            return m;
        }*/
        
        m = leEpites(1);
        if (m != 0) {
            return m;
        }
        
        System.out.println("--- A legalso mozgathato mozgatasa ---");
        ArrayList<Integer> mList = mozgathatok(poz, lepesek.get(lepes).ertek);
        for (int i=0; i<mList.size(); i++) {
            if (lephet(mList.get(i))) {
                return mList.get(i);
            }
        }
        // ha a fenti ciklus lefutott es nem talalt olyat ami lephet, az csak ugy lehet, ha a masik whitelisten van vmi az elso pedig ures
        kockaCsere();
        mList = mozgathatok(poz, lepesek.get(lepes).ertek);
        for (int i=0; i<mList.size(); i++) {
            if (lephet(mList.get(i))) {
                return mList.get(i);
            }
        }
        System.out.println("----------nem talalt egy db mozgathatot sem amelyik lephet()----------");
        return m;
    }
    
    /* A tornyokat keresi a legnnagyobbtol (15) a parameterben megadott szamuig, 1->24 fele haladva, ha talal es az tud lepnimaganal (barmelyik kockaval) 
    kisebb elemszamu poziciora, (vagy nem a hazaba, vagy az utolso moge, vagy sajat szinure), akkor azzal lep, ha lephet()*/
    int leEpites(int alsohatar) {
        ArrayList<Integer> mozg1 = mozgathatok(poz, lepesek.get(lepes).ertek);
        ArrayList<Integer> mozg2 = null;
        if (lepes == 0 && kocka1 != kocka2) {
            mozg2 = mozgathatok(poz, lepesek.get(lepes+1).ertek);
        }
        
        for (int h=15; h>=alsohatar; h--) {
            for (int i=1; i<25; i++) {
                int m = poz[i].szam;
                if (lepes == 0 && kocka1 != kocka2) {
                    if (!mozg1.contains(m) && mozg2.contains(m)) {
                        int c = celSzamito(poz[m], lepesek.get(lepes+1).ertek).szam;
                        // ha belep a hazaba az utolso ele, akkor az sajat szinu legyen (utes v. uresre ne)
                        if (poz[m].korongok == h && poz[m].korongok > poz[c].korongok && (c < 19 || c > utolso() || poz[c].szin.equals(aiColor))) {
                            kockaCsere();
                            if (lephet(m)) {
                                System.out.println("LEEPITES: a legnagyobb " + (alsohatar-1) + "-nel nagyobb tornyot - kockacsere");
                                return m;
                            }else kockaCsere();
                        }
                    }
                }
                if (mozg1.contains(m)){
                    int c = celSzamito(poz[m], lepesek.get(lepes).ertek).szam;
                    // ha belep a hazaba az utolso ele, akkor az sajat szinu legyen (utes v. uresre ne)
                    if (poz[m].korongok == h && poz[m].korongok > poz[c].korongok && (c < 19 || c > utolso() || poz[c].szin.equals(aiColor))) {
                        if (lephet(m)) {
                            System.out.println("LEEPITES: a legnagyobb " + (alsohatar-1) + "-nel nagyobb tornyot");
                            return m;
                        }
                    }
                }
            }
        }
        return 0;
    }
    
    // kiveteles lepes, utes es zaras is lehet egyben
    /* azon poziciok listaja, amelyek 2 kul. kocka kulonbsegevel tudnak lepni es razarni anelkul, hogy nyitnanak, akar utessel is. 
    (utolsotol) param: A jatekos utolso korongjatol lefele haladva keresi-e, vagy legfelulrol */
    ArrayList<Integer> lepniTudok1(byte ertek, boolean utolsotol) {
        
        ArrayList<Integer> lList = new ArrayList<Integer>();            // lepni tudo poziciok listaja
        byte ertek1;
        byte ertek2;
        if (ertek == kocka1) {
            ertek1 = kocka1;
            ertek2 = kocka2;
        }
        else {
            ertek1 = kocka2;
            ertek2 = kocka1;
        }
        
        if (utolsotol) {
            for (int i=mozgathatok(poz, ertek1).size()-1; i>=0; i--) {
                Pozicio m = poz[mozgathatok(poz, ertek1).get(i)];
                // if (m.szam < utolso() && m.korongok >= 3)
                if (m.szam < utolso() && m.korongok != 2) {
                    Pozicio c = celSzamito(m, ertek1);
                    // if (c.szam < utolso() && c.szin.equals("dummy") && raZarhatok(ertek2).contains(c.szam) && poz[c.szam-ertek2].korongok !=1 )              //ha egyel zarnank ra, az mar zaras lenne, ami nem semleges
                    if (c.szam < utolso() && (c.szin.equals("dummy") || (c.szin.equals(playersColor) && c.korongok == 1)) && raZarhatok(ertek2).contains(c.szam)) {
                        lList.add(m.szam);
                    }
                }
            }
        }
        else {
            for (int i=mozgathatok(poz, ertek1).size()-1; i>=0; i--) {
                Pozicio m = poz[mozgathatok(poz, ertek1).get(i)];
                // if (m.korongok >= 3)
                if (m.korongok != 2) {
                    Pozicio c = celSzamito(m, ertek1);
                    // if (c.szin.equals("dummy") && raZarhatok(ertek2).contains(c.szam) && poz[c.szam-ertek2].korongok != 1)
                    if ((c.szin.equals("dummy") || (c.szin.equals(playersColor) && c.korongok == 1)) && raZarhatok(ertek2).contains(c.szam)) {
                        lList.add(m.szam);
                    }
                }
            }
        }
        
        return lList;
    }
    
    /* a lepniTudok1 listabol azzal a legnagyobb szamuval lep, amelyik lephet() es a parjat amelyik razar, beallitja folytatando lepesnek.
    (utolsotol) param: A jatekos utolso korongjatol lefele haladva keresi-e, vagy legfelulrol*/
    int lepes1(byte ertek, boolean utolsotol) {
        int m = 0;
        ArrayList<Integer> mList = mozgathatok(poz, ertek);
        ArrayList<Integer> lt1 = lepniTudok1(ertek, true);          // utolsotol
        ArrayList<Integer> lt2 = lepniTudok1(ertek, false);         // poz. 24-tol
        
        if (ertek == lepesek.get(lepes+1).ertek) {
            if (!(utolsotol ? lt1 : lt2).isEmpty()) {
                for (int i=mList.size()-1; i>=0; i--) {
                    if ((utolsotol ? lt1 : lt2).contains(mList.get(i))) {
                        int c = celSzamito(poz[mList.get(i)], ertek).szam;
                        kockaCsere();
                        if (lephet(mList.get(i))) {
                            m = mList.get(i);
                            folytatando.add(c-(ertek == kocka1 ? kocka2 : kocka1));
                            return m;
                        }
                        else {
                            kockaCsere();
                        }
                    }
                }
            }
        }
        
        
        else {
            if (!(utolsotol ? lt1 : lt2).isEmpty()) {
                for (int i=mList.size()-1; i>=0; i--) {
                    if ((utolsotol ? lt1 : lt2).contains(mList.get(i))) {
                        int c = celSzamito(poz[mList.get(i)], ertek).szam;
                        if (lephet(mList.get(i))) {
                            m = mList.get(i);
                            folytatando.add(c-(ertek == kocka1 ? kocka2 : kocka1));
                            return m;
                        }
                    }
                }
            }
        }
        
        return m;
    }
    
    /* osszeggel (vagy siman) tobbrol tobbre , az utolso ellenfel poz. ele lepni tudo poziciok listaja (3.) */
    ArrayList<Osszeggel> lepniTudok3(byte ertek) {
        ArrayList<Osszeggel> lList = new ArrayList<Osszeggel>();
        for (int i=0; i<mozgathatok(poz, ertek).size(); i++) {
            Pozicio m = poz[mozgathatok(poz, ertek).get(i)];
            if (m.korongok >= 3) {
                for (int j=0; j<lehetsegesCelok(m.szam, ertek, false).size(); j++) {
                    int lc = lehetsegesCelok(m.szam, ertek, false).get(j);
                    if (lc < utolso() && poz[lc].szin.equals(aiColor) && poz[lc].korongok >= 2 && poz[lc].korongok <= m.korongok) {
                        Osszeggel ossz = new Osszeggel(m.szam, j, lc);
                        lList.add(ossz);
                        break;
                    }
                }
            }
        }
        return lList;
    }
    
    /* a lepniTudok3 (tobbrol tobbre) listajarol eloszor poz 7-tol felfele keresi azt amelyik lephet(), majd poz 1-tol is.
    ha osszeggel lep, beallitja a folytatando lepeseket is. tobb fele kocka ertekkel meghivhato, ezt a parametert adja tovabb a lista keszito lepniTudok3 funkcionak */
    int lepes3 (byte ertek) {
        int m = 0;
        ArrayList<Osszeggel> lList = lepniTudok3(ertek);
        
        if (lepes == 0 && kocka1!=kocka2 && ertek == lepesek.get(lepes+1).ertek) {
            if (!lList.isEmpty()) {
                for (int i=0; i<lList.size(); i++) {
                    m = lList.get(i).m;         // a mozgatando
                    int j = lList.get(i).j;     // hanyszor kell leptetni
                    if (m >= 7) {
                        kockaCsere();
                        if (lephet(m)) {
                            int kozbulso = m;
                            for (int k=0; k<j; k++) {
                                kozbulso += lepesek.get(lepes).ertek;
                                folytatando.add(kozbulso);
                            }
                            return m;
                        }
                        else {
                            kockaCsere();
                        }
                    }
                }
                for (int i=0; i<lList.size(); i++) {
                    m = lList.get(i).m;         // a mozgatando
                    int j = lList.get(i).j;     // hanyszor kell leptetni
                    kockaCsere();
                    if (lephet(m)) {
                        int kozbulso = m;
                        for (int k=0; k<j; k++) {
                            kozbulso += lepesek.get(lepes).ertek;
                            folytatando.add(kozbulso);
                        }
                        return m;
                    }
                    else {
                        kockaCsere();
                    }
                }
            }
        }
        else {
            if (!lList.isEmpty()) {
                for (int i=0; i<lList.size(); i++) {
                    m = lList.get(i).m;         // a mozgatando
                    int j = lList.get(i).j;     // hanyszor kell leptetni
                    if (m >= 7) {
                        if (lephet(m)) {
                            int kozbulso = m;
                            for (int k=0; k<j; k++) {
                                kozbulso += lepesek.get(lepes).ertek;
                                folytatando.add(kozbulso);
                            }
                            return m;
                        }
                    }
                }
                for (int i=0; i<lList.size(); i++) {
                    m = lList.get(i).m;         // a mozgatando
                    int j = lList.get(i).j;     // hanyszor kell leptetni
                    if (lephet(m)) {
                        int kozbulso = m;
                        for (int k=0; k<j; k++) {
                            kozbulso += lepesek.get(lepes).ertek;
                            folytatando.add(kozbulso);
                        }
                        return m;
                    }
                }
            }
        }
        return m;
    }
    
    // osszeggel (vagy siman) egyrol egyre , az utolso ellenfel poz. ele lepni tudo poziciok listaja (3.a)
    ArrayList<Osszeggel> lepniTudok3a(byte ertek) {
        ArrayList<Osszeggel> lList = new ArrayList<Osszeggel>();
        for (int i=0; i<mozgathatok(poz, ertek).size(); i++) {
            Pozicio m = poz[mozgathatok(poz, ertek).get(i)];
            if (m.korongok == 1) {
                for (int j=0; j<lehetsegesCelok(m.szam, ertek, false).size(); j++) {
                    int lc = lehetsegesCelok(m.szam, ertek, false).get(j);
                    if (lc < utolso() && poz[lc].szin.equals("dummy")) {
                        Osszeggel ossz = new Osszeggel(m.szam, j, lc);
                        lList.add(ossz);
                        break;
                    }
                }
            }
        }
        return lList;
    }
    
    /* a lepniTudok3a (egyrol egyre lepok) listajarol eloszor poz 7-tol felfele keresi azt amelyik lephet(), majd poz 1-tol is.
    ha osszeggel lep, beallitja a folytatando lepeseket is. tobb fele kocka ertekkel meghivhato, ezt a parametert adja tovabb a lista keszito lepniTudok3a funkcionak */
    int lepes3a (byte ertek) {
        int m = 0;
        ArrayList<Osszeggel> lList = lepniTudok3a(ertek);
        
        if (lepes == 0 && kocka1!=kocka2 && ertek == lepesek.get(lepes+1).ertek) {
            if (!lList.isEmpty()) {
                for (int i=0; i<lList.size(); i++) {
                    m = lList.get(i).m;         // a mozgatando
                    int j = lList.get(i).j;     // hanyszor kell leptetni
                    if (m >= 7) {
                        kockaCsere();
                        if (lephet(m)) {
                            int kozbulso = m;
                            for (int k=0; k<j; k++) {
                                kozbulso += lepesek.get(lepes).ertek;
                                folytatando.add(kozbulso);
                            }
                            return m;
                        }
                        else {
                            kockaCsere();
                        }
                    }
                }
                for (int i=0; i<lList.size(); i++) {
                    m = lList.get(i).m;         // a mozgatando
                    int j = lList.get(i).j;     // hanyszor kell leptetni
                    kockaCsere();
                    if (lephet(m)) {
                        int kozbulso = m;
                        for (int k=0; k<j; k++) {
                            kozbulso += lepesek.get(lepes).ertek;
                            folytatando.add(kozbulso);
                        }
                        return m;
                    }
                    else {
                        kockaCsere();
                    }
                }
            }
        }
        else {
            if (!lList.isEmpty()) {
                for (int i=0; i<lList.size(); i++) {
                    m = lList.get(i).m;         // a mozgatando
                    int j = lList.get(i).j;     // hanyszor kell leptetni
                    if (m >= 7) {
                        if (lephet(m)) {
                            int kozbulso = m;
                            for (int k=0; k<j; k++) {
                                kozbulso += lepesek.get(lepes).ertek;
                                folytatando.add(kozbulso);
                            }
                            return m;
                        }
                    }
                }
                for (int i=0; i<lList.size(); i++) {
                    m = lList.get(i).m;         // a mozgatando
                    int j = lList.get(i).j;     // hanyszor kell leptetni
                    if (lephet(m)) {
                        int kozbulso = m;
                        for (int k=0; k<j; k++) {
                            kozbulso += lepesek.get(lepes).ertek;
                            folytatando.add(kozbulso);
                        }
                        return m;
                    }
                }
            }
        }
        return m;
    }
    
    /* duplaval ures mezore lepni tudo poziciok listaja (4a.) (nyitas nelkul, pl. utolso korongja ele, azaz annal kisebb szamu poziciora. 
    Ha atugorna azokat, az mar nem semleges lepes lenne, hanem az atugras/zaras kategoria)*/
    ArrayList<Integer> lepniTudok4a(byte ertek) {
        ArrayList<Integer> lList = new ArrayList<Integer>();
        for (int i=0; i<mozgathatok(poz, ertek).size(); i++) {
            Pozicio m = poz[mozgathatok(poz, ertek).get(i)];
            if (m.korongok == 2 || m.korongok >= 4) {
                int c = celSzamito(m, ertek).szam;
                if (c < utolso() && poz[c].szin.equals("dummy")) {
                    lList.add(m.szam);
                }
            }
        }
        return lList;
    }
    
    /* a mozgathatok listajan halad eloszor alulrol felfele a 7. poziciotol (anchor-t beken hagyja), 
    ha nem talal olyat, amelyik lepniTudok4a is egyben, akkor az 1. poziciotol ujra nezi */
    int lepes4a() {
        int m = 0;
        ArrayList<Integer> mList = mozgathatok(poz, lepesek.get(lepes).ertek);
        ArrayList<Integer> lt = lepniTudok4a(lepesek.get(lepes).ertek);
        for (int i=0; i<mList.size(); i++) {
            if (mList.get(i) >= 7) {
                if (lt.contains(mList.get(i))) {
                    m = mList.get(i);
                    folytatando.add(m);
                    return m;
                }
            }
        }
        for (int i=0; i<mList.size(); i++) {
            if (lt.contains(mList.get(i))) {
                m = mList.get(i);
                folytatando.add(m);
                return m;
            }
        }
        return m;
    }
    
    /* osszeggel (vagy siman) nem kettorol 0-ra, 1-re, 2-re, 24-tol szamitva az elso, maganal kisebbre, az utolso ellenfel poz. mogott lepni tudo pozicio. 
    Mindket kockaval megkiserelve, ha ez lehet. */
    Osszeggel utolsoMogottiek(byte ertek) {
        //ArrayList<Osszeggel> lList = new ArrayList<Osszeggel>();
        Osszeggel ossz = null;
        
        if (lepes == 0 && kocka1!=kocka2 && ertek == lepesek.get(lepes+1).ertek) {
            kor:
            for (int h=0; h<=2; h++) {                                                              
                for (int i=mozgathatok(poz, ertek).size()-1; i>=0; i--) {
                    Pozicio m = poz[mozgathatok(poz, ertek).get(i)];
                    if (m.szam > utolso() && m.korongok != 2) {                                     // a nem kettoket nezi
                        for (int j=0; j<lehetsegesCelok(m.szam, ertek, false).size(); j++) {
                            int lc = lehetsegesCelok(m.szam, ertek, false).get(j);
                            if (poz[lc].korongok < m.korongok && poz[lc].korongok == h) {           // tud-e maganal kisebbre lepni es az 0, 1, 2
                                kockaCsere();
                                if (lephet(m.szam)) {
                                    ossz = new Osszeggel(m.szam, j, lc);
                                    break kor;
                                }
                                else {
                                    kockaCsere();
                                }
                            }
                        }
                    }
                }
            }
            return ossz;
        }
        
        
        
        else {
            kor:
            for (int h=0; h<=2; h++) {
                for (int i=mozgathatok(poz, ertek).size()-1; i>=0; i--) {
                    Pozicio m = poz[mozgathatok(poz, ertek).get(i)];
                    if (m.szam > utolso() && m.korongok != 2) {                                     // a nem kettoket nezi
                        for (int j=0; j<lehetsegesCelok(m.szam, ertek, false).size(); j++) {
                            int lc = lehetsegesCelok(m.szam, ertek, false).get(j);
                            if (poz[lc].korongok < m.korongok && poz[lc].korongok == h) {           // tud-e maganal kisebbre lepni es az 0, 1, 2
                                if (lephet(m.szam)) {
                                    ossz = new Osszeggel(m.szam, j, lc);
                                    break kor;
                                }
                            }
                        }
                    }
                }
            }
            return ossz;
        }
    }
    
    // ha van ilyen (utolsoMogottiek), akkor azzal lep. Ha kombinalt lepes, akkor beallitja a folytatando lepeseket.
    int utolsoMogottiLepes(byte ertek) {
        Osszeggel mozgatando = utolsoMogottiek(ertek);
        if (mozgatando != null) {
            int kozbulso = mozgatando.m;
            for (int j=0; j<mozgatando.j; j++) {
                kozbulso += lepesek.get(lepes).ertek;
                folytatando.add(kozbulso);
            }
            return mozgatando.m;
        }
        else {
            return 0;
        }
    }
    
    /* A legalabb 2 korongot tart. poziciokat keresi, also ertektol (kezdo param) felfele haladva 24 fele, amelyek az utolso ellenfel poz. ele lepni tud uresre,
    vagy a pontosan 2 korongot tart. poziciokat az utolso elott, amirol sajat szinure (v. uresre) lepve a 2-bol egyet hagy hatra (ez is nyitasnak szamit).
    Ezeket listan gyujti.*/
    ArrayList<Integer> nyitniTudok(byte ertek, int kezdo) {
        
        ArrayList<Integer> nList = new ArrayList<Integer>();            // nyitni tudo poziciok listaja, expose to utes
        ArrayList<Integer> mozg = mozgathatok(poz, ertek);
        for (int i=0; i<mozg.size(); i++) {
            int m = mozg.get(i);
            if (m >= kezdo && poz[m].korongok > 1) {
                int c = celSzamito(poz[m], ertek).szam;
                if (c < utolso() && poz[c].szin.equals("dummy")) {
                    nList.add(m);
                }
                else if (m < utolso() && poz[m].korongok==2 && !poz[c].szin.equals(playersColor)) {
                    nList.add(m);
                }
            }
        }
        return nList;
    }
    
    /* Ha pipben vezet, akkor az 1-tol 24-ig, ha nem, akkor a 7-tol 24-ig nezett nyitniTudok listarol vett pozicioval lep 
    (Ha a pl. vezet, akkor probalja megtartani az anchor pozicioit.)*/
    int nyitas(byte ertek) {
        int m = 0;
        ArrayList<Integer> nyt1 = nyitniTudok(ertek, 1);
        ArrayList<Integer> nyt7 = nyitniTudok(ertek, 7);
        boolean csere = (lepes == 0 && kocka1!=kocka2 && ertek == lepesek.get(lepes+1).ertek);
        
        if (!(playersPip>aiPip ? nyt1 : nyt7).isEmpty()) {
            m = (playersPip>aiPip ? nyt1 : nyt7).get(0);         // lehetne ciklusban is a lista elemeit vegigprobalva
            if (csere) kockaCsere();
            if (lephet(m)) {
                System.out.println(playersPip>aiPip ? "NYITAS: 1-tol felfele" : "NYITAS: 7-tol felfele");
                return m;
            }
            else {
                if (csere) kockaCsere();
            }
        }
        return m;
    }
    
    /* A parameterben megadott fel hazaban hany az adott fel altal lezart sajat pozicio van (nem igazan anchor ez), 
    ez jellemzi, hogy mennyirevan kesz a haza */
    byte hazAllapota(String oldal) {
        byte anchor = 0;
        if (oldal.equals(aiColor)) {
            for (int i=19; i<25; i++) {
                if (poz[i].szin.equals(aiColor) && poz[i].korongok > 1) {
                    anchor++;
                }
            }
        }
        else {
            for (int i=1; i<7; i++) {
                if (poz[i].szin.equals(playersColor) && poz[i].korongok > 1) {
                    anchor++;
                }
            }
        }
        return anchor;
    }
    
    // van-e 1 db single ai korong a hazaban es vannak mogotte (utes lehetosege fennall). Ezt mindenkepp zarni kell.
    boolean blotVan() {
        boolean blotvan = false;
        for (int i=19; i<=24; i++) {
            if (poz[i].szin.equals(aiColor) && poz[i].korongok == 1 && poz[i].szam < utolso()) {
                blotvan = true;
                break;
            }
        }
        return blotvan;
    }
    
    /* megadja, hogy hany korongja van az ai-nak a jatekos korongjai tobbsege (2/3) mogott. Nyitas muveletnel hasznos (back game). */
    int backStones() {
        int osszes = 15 - poz[0].korongok;
        int db = 0;
        int honnan = 0;
        int aidb = 0;
        for (int i=26; i>0; i--) {
            if (poz[i].szin.equals(playersColor)) {
                db += poz[i].korongok;
            }
            if (db >= osszes / 3 * 2) {
                honnan = i;
                break;
            }
        }
        for (int i=honnan-1; i>0; i--) {
            if (poz[i].szin.equals(aiColor)) {
                aidb += poz[i].korongok;
            }
        }
        aidb += poz[27].korongok;
        return aidb;
    }
    
    // visszaadja, hogy hany a maximalis, sorban egymas mellett levo anchor szama egy pozicio elott. Esely szamitasanal hasznos.
    byte akadalyok (int m, String kinek) {
        byte max = 0;
        byte szaml = 0;
        
        if (kinek.equals(aiColor)) {
            for (int i=(m==27 ? 1 : m+1); i<25; i++) {
                if (poz[i].szin.equals(playersColor) && poz[i].korongok >=2) {
                    szaml++;
                    if (szaml > max) {
                        max = szaml;
                    }
                }
                else {
                    szaml = 0;
                }
            }
            return max;
        }
        else {
            for (int i=(m==26 ? 24 : m-1); i>0; i--) {
                if (poz[i].szin.equals(aiColor) && poz[i].korongok >=2) {
                    szaml++;
                    if (szaml > max) {
                        max = szaml;
                    }
                }
                else {
                    szaml = 0;
                }
            }
            return max;
        }
    }
    
    // visszaadja azt a "beragadt" poziciot, amelyik elott 7-es, 6-os, stb fal van valahol az utjan.
    // ha 12. poz. tol lefele ilyet talal a mozgathatok kozt, akkor azzal lep. Mindket kockaval megkiserli, ha lehet.
    int kiszabaditas() {
        ArrayList<Integer> mozg1 = mozgathatok(poz, lepesek.get(lepes).ertek);
        ArrayList<Integer> mozg2 = null;
        if (lepes == 0 && kocka1 != kocka2) {
            mozg2 = mozgathatok(poz, lepesek.get(lepes+1).ertek);
        }
        
        for (int h=7; h>=3; h--) {
            for (int i=12; i>0; i--) {
                if (lepes == 0 && kocka1 != kocka2 && !mozg1.contains(i) && mozg2.contains(i) && akadalyok(i, aiColor) == h) {
                    kockaCsere();
                    if (lephet(i)) {
                        System.out.println("KISZABADITAS (kockacsere) " + "m: " + i + ", akadalyok: " + akadalyok(i, aiColor));
                        return i;
                    }
                    else {
                        kockaCsere();
                    }
                }
                if (mozg1.contains(i) && akadalyok(i, aiColor) == h) {
                    if (lephet(i)) {
                        System.out.println("KISZABADITAS " + "m: " + i + ", akadalyok: " + akadalyok(i, aiColor));
                        return i;
                    }
                }
            }
        }
        return 0;
    }
    
    
    /* A lehetseges utes kombinaciokat veszi sorra prioritasi sorrendben, dupla v. nem dupla (kockacserevel is megkiserelve) dobasokra specifikusan. 
    Az elso mukodot adja vissza. */
    int prioritas4_Utes() {
        
        // ha dupla
        if (kocka1 == kocka2) {
            for (int i=1; i<25; i++) {
                if (poz[i].szin.equals(aiColor)) {
                    int m = poz[i].szam;
                    if (lepes <= 2) {
                        if (utniTudok4a(lepesek.get(lepes).ertek).contains(m)) {
                            System.out.println("UTES 4a");
                            folytatando.add(m);
                            return m;
                        }
                    }
                    if (lepes == 0) {
                        if (utniTudok4b(lepesek.get(lepes).ertek).contains(m)) {
                            System.out.println("UTES 4b");
                            int c = celSzamito(poz[m], lepesek.get(lepes).ertek).szam;
                            folytatando.add(m);
                            folytatando.add(c);
                            folytatando.add(c);
                            return m;
                        }
                    }
                    if (lepes <= 2) {
                        if (uthet(m, lepesek.get(lepes).ertek, 2)) {
                            System.out.println("UTES 2");
                            return m;
                        }
                    }
                    if (uthet(m, lepesek.get(lepes).ertek, 3)) {
                        System.out.println("UTES 3");
                        return m;
                    }
                }
            }
        }
        // ha nem dupla
        else {
            for (int i=1; i<25; i++) {
                if (poz[i].szin.equals(aiColor)) {
                    int m = poz[i].szam;
                    // ha tudunk lepni ketszer
                    if (lepes == 0 && !blokkolva(poz, lepesek.get(lepes+1).ertek)) {
                        if (utniTudok1(lepesek.get(lepes).ertek).contains(m)) {
                            if (lephet(m)) {
                                System.out.println("UTES 1");
                                int c = celSzamito(poz[m], lepesek.get(lepes).ertek).szam;
                                folytatando.add(c-lepesek.get(lepes+1).ertek);
                                return m;
                            }
                        }
                        else if (utniTudok1(lepesek.get(lepes+1).ertek).contains(m)) {
                            kockaCsere();
                            if (lephet(m)) {
                                System.out.println("UTES 1 - kockacsere");
                                int c = celSzamito(poz[m], lepesek.get(lepes).ertek).szam;
                                folytatando.add(c-lepesek.get(lepes+1).ertek);
                                return m;
                            }
                            else {
                                kockaCsere();
                            }
                        }
                        
                        else if (uthet(m, lepesek.get(lepes).ertek, 2)) {
                            System.out.println("UTES 2");
                            return m;
                        }
                        else if (uthet(m, lepesek.get(lepes+1).ertek, 2)) {
                            System.out.println("UTES 2 - kockacsere");
                            return m;
                        }
                        
                        
                        // ezt mindenkepp vegrehajtja alabb, ha 0., ha 1. lepes
                        else if (uthet(m, lepesek.get(lepes).ertek, 3)) {
                            System.out.println("UTES 3");
                            return m;
                        }
                        else if (uthet(m, lepesek.get(lepes+1).ertek, 3)) {
                            System.out.println("UTES 3 - kockacsere");
                            return m;
                        }
                    }
                    
                    // if (utniTudok3(lepesek.get(lepes).ertek).contains(m))
                    if (uthet(m, lepesek.get(lepes).ertek, 3)) {
                        System.out.println("UTES 3");
                        return m;
                    }
                }
            }
        }
        return 0;
    }
    
    /* igaz, ha a mozgatando eleme az adott utesmod (2. v 3.) szerinti listanak (utniTudok2 v utniTudok3), 
    ekkor fel is tolti a folytatando listat, mindket kockaval probalja, ha lehetseges. mindez csak ha lephet()*/
    boolean uthet(int mozgatando, byte ertek, int utesmod) {
        Osszeggel mozg=null;
        ArrayList<Osszeggel> lista=null;
        switch (utesmod) {
            case 2:
                lista = utniTudok2(ertek);
                break;
            case 3:
                lista = utniTudok3(ertek);
                break;
        }
        for (Osszeggel ossz : lista) {
            if (ossz.m == mozgatando) {
                mozg = ossz;
            }
        }
        if (mozg!=null) {
            if (lepes == 0 && ertek == lepesek.get(lepes+1).ertek && kocka1!=kocka2) {
                kockaCsere();
                if (lephet(mozgatando)) {
                    int kozbulso = mozgatando;
                    for (int j=0; j<mozg.j; j++) {
                        kozbulso += lepesek.get(lepes).ertek;
                        folytatando.add(kozbulso);
                    }
                    return true;
                }
                else {
                    kockaCsere();
                }
            }
            else if (lephet(mozgatando)) {
                int kozbulso = mozgatando;
                for (int j=0; j<mozg.j; j++) {
                    kozbulso += lepesek.get(lepes).ertek;
                    folytatando.add(kozbulso);
                }
                return true;
            }
        }
        return false;
    }
    
    // minden ai poz ami utni tud, egyszeruen, egy lepesbol, kockacsere nelkul. prioritas1_Belepteto hasznalja
    ArrayList<Integer> utniTudok0(Pozicio[] allas, byte ertek) {
        ArrayList<Integer> uList = new ArrayList<Integer>();
        for (int i=1; i<28; i++) {
            if (i != 25 && i != 24) {
                if (allas[i].szin.equals(aiColor)) {
                    int c = celSzamito(allas[i], ertek).szam;
                    if (c < 25 && c > 0) {
                        if (allas[c].szin.equals(playersColor) && allas[c].korongok == 1) {
                            uList.add(i);
                        }
                    }
                }
            }
        }
        return uList;
    }
    
    // kulonbseggel utni-zarni tudo poziciok listaja (egyikkel ut, a masik ertekkel razar) Kettot nem bont meg. 
    ArrayList<Integer> utniTudok1(byte ertek) {
        ArrayList<Integer> uList = new ArrayList<Integer>();
        byte ertek1;
        byte ertek2;
        if (ertek == kocka1) {
            ertek1 = kocka1;
            ertek2 = kocka2;
        }
        else {
            ertek1 = kocka2;
            ertek2 = kocka1;
        }
        
        for (int i=mozgathatok(poz, ertek1).size()-1; i>=0; i--) {
            Pozicio m = poz[mozgathatok(poz, ertek1).get(i)];
            if (m.korongok == 1 || m.korongok >= 3) {
                Pozicio c = celSzamito(m, ertek1);
                if (c.szin.equals(playersColor) && c.korongok == 1 && raZarhatok(ertek2).contains(c.szam)) {
                    uList.add(m.szam);
                }
            }
        }
        return uList;
    }
    
    // osszeggel utni-zarni (egy nem 2 korongot tartalmazo poz. ut es tovabblep sajat szinure) tudo poziciok listaja (2.)
    ArrayList<Osszeggel> utniTudok2(byte ertek) {
        ArrayList<Osszeggel> uList = new ArrayList<Osszeggel>();
        
        for (int i=0; i<mozgathatok(poz, ertek).size(); i++) {
            Pozicio m = poz[mozgathatok(poz, ertek).get(i)];
            if (m.korongok == 1 || m.korongok >= 3) {
                kor:
                for (int j=0; j<lehetsegesCelok(m.szam, ertek, true).size(); j++) {
                    int lc = lehetsegesCelok(m.szam, ertek, true).get(j);
                    if (poz[lc].szin.equals(aiColor)) {
                        for (int k=0; k<=j-1; k++) {
                            int lc1 = lehetsegesCelok(m.szam, ertek, true).get(k);
                            if (poz[lc1].szin.equals(playersColor) && poz[lc1].korongok == 1) {
                                Osszeggel ossz = new Osszeggel(m.szam, j, lc);                  // az a poz., amelyik osszeggel utni tud
                                uList.add(ossz);
                                break kor;
                            }
                        }
                    }
                }
            }
        }
        return uList;
    }
    
    /* tobb kocka osszegevel is akar (vagy siman) utni tudo poziciok listaja (3.) 
    A veg pozicioban nyitva marad, nem zarja le. Ezert a legkedvezotlenebb fajta az utesek kozul.
    Ha 40-nel tobbel vezet a pl es nem tul jol all a haza, akkor az ai a sajat hazaba is beut ezzel*/
    ArrayList<Osszeggel> utniTudok3(byte ertek) {
        ArrayList<Osszeggel> uList = new ArrayList<Osszeggel>();
        boolean lehetHazbaIs = (aiPip - playersPip > 40  && hazAllapota(playersColor) <= 3);
        
        for (int i=0; i<mozgathatok(poz, ertek).size(); i++) {
            Pozicio m = poz[mozgathatok(poz, ertek).get(i)];
            // removed if (m.szam>12 ? (m.korongok == 1 || m.korongok >= 3) :  true) from this line, (fent nem bont kettot, lent igen), igy most barhol bont barmit uteshez
            for (int j=0; j<lehetsegesCelok(m.szam, ertek, true).size(); j++) {
                int lc = lehetsegesCelok(m.szam, ertek, true).get(j);
//                System.out.println("utni tudok 3, m=" + m.szam);
//                System.out.println("lehetseges cel:" + lc + ", ertek: " + ertek);
                if ((lehetHazbaIs ? true : lc<19) && poz[lc].szin.equals(playersColor) && poz[lc].korongok == 1) {      // a sajat hazaba nem ut lezaratlanul, csak a tobbi utes moddal. De igen, removed: (lc<19 &&), visszaraktam
                    Osszeggel ossz = new Osszeggel(m.szam, j, lc);                  // az a poz., amelyik osszeggel utni tud
                    uList.add(ossz);
                    break;      // a legkisebb szamu a legjobb, az megy vissza legtobbet utesnel
                }
            }
        }
        return uList;
    }
    
    // duplaval utni-zarni tudo poziciok listaja (4a.) Egyikkel ut, masikkal arra razar. Nem hagy nyitva maga utan
    ArrayList<Integer> utniTudok4a(byte ertek) {
        ArrayList<Integer> uList = new ArrayList<Integer>();
        for (int i=0; i<mozgathatok(poz, ertek).size(); i++) {
            Pozicio m = poz[mozgathatok(poz, ertek).get(i)];
            if (m.korongok == 2 || m.korongok >= 4) {
                int c = celSzamito(m, ertek).szam;
                if (poz[c].szin.equals(playersColor) && poz[c].korongok == 1) {
                    uList.add(m.szam);
                }
            }
        }
        return uList;
    }
    
    // duplaval 4 lepesbol utni-zarni tudo poziciok listaja (4b.) Dupla dobasnal kettot mozgat ugyanarrol a helyrol, es meg egyszer ugyanazt a kettot (utes a vegen)
    ArrayList<Integer> utniTudok4b(byte ertek) {
        ArrayList<Integer> uList = new ArrayList<Integer>();
        for (int i=0; i<mozgathatok(poz, ertek).size(); i++) {
            Pozicio m = poz[mozgathatok(poz, ertek).get(i)];
            if (m.korongok == 2 || m.korongok >= 4) {
                ArrayList<Byte> lclist = lehetsegesCelok(m.szam, ertek, true);
                if (lclist.size() >= 2) {
                    int c = lclist.get(1);
                    if (poz[c].szin.equals(playersColor) && poz[c].korongok == 1) {
                        uList.add(m.szam);
                    }
                }
            }
        }
        return uList;
    }
    
    
    /* A lehetseges zaras kombinaciokat veszi sorra prioritasi sorrendben,(kockacserevel is megkiserelve). 
    Fentrol lefele keres, tehat a hazahoz kozelebb allo nyitott pozicio bezarasat preferalja. Az elso mukodot adja vissza. */
    int prioritas3_Zaras() {
        int m = 0;
        for (int i=27; i>0; i--) {
            if (poz[i].szin.equals(aiColor)) {
                if (lepes == 0 && kocka1 != kocka2 && !blokkolva(poz, lepesek.get(lepes+1).ertek)) {
                    
                    if (zarniTudok1(kocka1).contains(poz[i].szam)) {
                        if (lephet(poz[i].szam)) {
                            System.out.println("ZARAS 1");
                            int c = celSzamito(poz[poz[i].szam], lepesek.get(lepes).ertek).szam;
                            folytatando.add(c-lepesek.get(lepes+1).ertek);
                            return poz[i].szam;
                        }
                    }
                    
                    else if (zarniTudok1(kocka2).contains(poz[i].szam)) {
                        kockaCsere();
                        if (lephet(poz[i].szam)) {
                            System.out.println("ZARAS 1 - kockacsere");
                            int c = celSzamito(poz[poz[i].szam], lepesek.get(lepes).ertek).szam;
                            folytatando.add(c-lepesek.get(lepes+1).ertek);
                            return poz[i].szam;
                        }
                        else {
                            kockaCsere();
                        }
                    }
                    
                    
                    
                    m = zarniTudok2(kocka1, poz[i].szam);
                    if (m != 0) {
                        if (lephet(m)) {
                            System.out.println("ZARAS 2");
                            return m;
                        }
                    }
                    else {
                        m = zarniTudok2(kocka2, poz[i].szam);
                        if (m != 0) {
                            kockaCsere();
                            if (lephet(m)) {
                                System.out.println("ZARAS 2 - kockacsere");
                                return m;
                            }else kockaCsere();
                        }
                    }
                    
                    
                    if (zarniTudok3(kocka1).contains(poz[i].szam)) {
                        if (lephet(poz[i].szam)) {
                            System.out.println("ZARAS 3");
                            return poz[i].szam;
                        }
                    }

                    else if (zarniTudok3(kocka2).contains(poz[i].szam)) {
                        kockaCsere();
                        if (lephet(poz[i].szam)) {
                            System.out.println("ZARAS 3 - kockacsere");
                            return poz[i].szam;
                        }
                        else {
                            kockaCsere();
                        }
                    }
                }
                else {
                    
                    m = zarniTudok2(lepesek.get(lepes).ertek, poz[i].szam);
                    if (m != 0) {
                        if (lephet(m)) {
                            System.out.println("ZARAS 2");
                            return m;
                        }
                    }
                    
                    if (zarniTudok3(lepesek.get(lepes).ertek).contains(poz[i].szam)) {
                        if (lephet(poz[i].szam)) {
                            System.out.println("ZARAS 3, " + lepesek.get(lepes).ertek + " ertekkel");
                            return poz[i].szam;
                        }
                    }
                }
            }
        }
        return 0;
    }
    
    // minden ai poz ami zarni tud, egyszeruen, egy lepesbol, kockacsere nelkul. prioritas1_Belepteto hasznalja
    ArrayList<Integer> zarniTudok0(Pozicio[] allas, byte ertek) {
        ArrayList<Integer> zList = new ArrayList<Integer>();
        for (int i=27; i>0; i--) {
            if (i != 25 && i != 24) {
                if (allas[i].szin.equals(aiColor)) {
                    int c = celSzamito(allas[i], ertek).szam;
                    if (c < 25 && c > 0) {
                        if (allas[c].szin.equals(aiColor) && allas[c].korongok == 1) {
                            zList.add(i);
                        }
                    }
                }
            }
        }
        return zList;
    }
    
    /* Az ellenfel utolso pozicioja elotti, azaz veszelyben levo, 1 db korongot tartalmazo kulonbseggel zarni tudo poziciok listaja 
    (az 1-gyel lep, a masik ertekkel razar valahonnan)*/
    ArrayList<Integer> zarniTudok1(byte ertek) {
        
        ArrayList<Integer> zList = new ArrayList<Integer>();            // zarni tudo poziciok listaja
        byte ertek1;
        byte ertek2;
        if (ertek == kocka1) {
            ertek1 = kocka1;
            ertek2 = kocka2;
        }
        else {
            ertek1 = kocka2;
            ertek2 = kocka1;
        }
        
        for (int i=mozgathatok(poz, ertek1).size()-1; i>=0; i--) {
            Pozicio m = poz[mozgathatok(poz, ertek1).get(i)];
            if (m.szam < utolso() && m.korongok == 1) {
                Pozicio c = celSzamito(m, ertek1);
                if (c.szam <= utolso() && (c.szin.equals("dummy") || (c.szin.equals(playersColor) && c.korongok == 1)) && raZarhatok(ertek2).contains(c.szam)) {
                    zList.add(m.szam);
                }
            }
        }
        
        return zList;
    }
    
    /* zaras  egyre, A parameterben megadott poziciora (ahol az egy van) akar osszeggel is razarni kepes pozicio. 
    Kettot nem bont meg. Csak az utolso() elotti, tehat a veszelyben levoket zarja le */
    int zarniTudok2(byte ertek, int mire) {
        
        for (int i=mozgathatok(poz, ertek).size()-1; i>=0; i--) {
            Pozicio m = poz[mozgathatok(poz, ertek).get(i)];
            if (m.korongok == 1 || m.korongok >= 3) {
                for (int j=0; j<lehetsegesCelok(m.szam, ertek, false).size(); j++) {
                    int lc = lehetsegesCelok(m.szam, ertek, false).get(j);
                    if (lc < utolso() && poz[lc].szin.equals(aiColor) && poz[lc].korongok == 1 && poz[lc].szam == mire) {
                        return m.szam;
                    }
                }
            }
        }
        return 0;
    }
    
    /* zaras egyel, az utolso() elotti, tehat a veszelyben levo egyedul allo korongokat keresi (szinten fentrol lefele) 
    amelyekkel lepve (akar osszeggel is) vagy sajat szinre lepne, vagy atugorna az utolso() ellenfelet (itt zarasnak minosul) */
    ArrayList<Integer> zarniTudok3(byte ertek) {
        // az ellenfél utolsó korongjának megkeresese 
        int utolso =0;
        for (int i=26; i>=1; i--) {
            if (poz[i].szin.equals(playersColor)) {
                utolso = i;
                break;
            }
        }
        ArrayList<Integer> zList = new ArrayList<Integer>();            // zarni tudo poziciok listaja
        for (int i=mozgathatok(poz, ertek).size()-1; i>=0; i--) {
            Pozicio m = poz[mozgathatok(poz, ertek).get(i)];
            if (m.szam < utolso && m.korongok == 1) {
                for (int j=0; j<lehetsegesCelok(m.szam, ertek, false).size(); j++) {
                    int lc = lehetsegesCelok(m.szam, ertek, false).get(j);
                    if (lc > utolso || poz[lc].szin.equals(aiColor)) {
                        if (!zList.contains(m.szam)) {
                            zList.add(m.szam);
                        }
                    }
                }
            }
        }
        return zList;
    }
    
    // lista azokrol a poziciokrol, amikre lepni tud az AI az adott ertekkel egy lepesbol es 2 korongot nem megbontva. Ket kocka kulonbseget felhasznalo lepesekhez kell.
    ArrayList<Integer> raZarhatok(byte ertek) {
        ArrayList<Integer> rList = new ArrayList<Integer>();
        for (int i=mozgathatok(poz, ertek).size()-1; i>=0; i--) {
            Pozicio m = poz[mozgathatok(poz, ertek).get(i)];
            if (m.korongok != 2) {
                Pozicio c = celSzamito(m, ertek);
                rList.add(c.szam);
            }
        }
        return rList;
    }
    
    // a kocka ertekebol es abbol, hogy melyik fel lep, meghatarozza az adott mozgatando poziciohoz tartozo cel poziciot
    Pozicio celSzamito(Pozicio mozgatando, byte ertek) {
        if (playersTurn) {
            byte b = (byte)(mozgatando.szam - ertek);
            if (b<=0) {
                return poz[0];
            }
            else if (mozgatando.szam == 26) {
                return poz[25 - ertek];
                }
            else {
                return poz[mozgatando.szam - ertek];
            }
        }
        else {
            if (mozgatando.szam == 27) {
                return poz[ertek];
            }
            else {
                byte b = (byte)(mozgatando.szam + ertek);
                if (b>=25) {
                    return poz[25];
                }
                else {
                    return poz[mozgatando.szam + ertek];
                }
            }
        }
    }
    
    /* A jatekos ha lepteti a korongjait (kattintas esemenykezeloje hivja meg ezt), a kattintott m poziciot beallitja mozgatandonak, 
    abbol szamolt celt a cel pozicionak es elinditja a mozgatas folyamatat ami lepesbol es annak animaciojabol all (maga az animacio inditja a lepest). 
    Elotte meg ellenorzi, hogy szabalyos-e a lepes, ha kihasznalatlanul maradna egy kocka, akkor nem engedi lepni es uzenetet kuld a felhasznalonak. */
    void mozgatas(Pozicio m) {
        if (lepes < lepesek.size() && !blokkolva(poz, lepesek.get(lepes).ertek)){
            mozgatando = m;
            cel = celSzamito(mozgatando, lepesek.get(lepes).ertek);
            if (mozgathatok(poz, lepesek.get(lepes).ertek).contains(mozgatando.szam)) {
                if (lephet(mozgatando.szam)) {
                    //lep();
                    vill.setActionCommand("mvill");
                    vill.start();
                }
                else {
                    tab.uzi1.setText("Ha lehet, akkor mindkét kocka értékét le kell lépni!");
                }
            }
        }
    }
    
    /* A gep ha lepteti a korongjait (aiVezerlo() hivja meg ezt a kikalkulalt m ertekkel), az m poziciot beallitja mozgatandonak, 
    abbol szamolt celt a cel pozicionak es elinditja a mozgatas folyamatat ami lepesbol es annak animaciojabol all (maga az animacio inditja a lepest). */
    void aiMozgatas(Pozicio m) {
        mozgatando = m;
        cel = celSzamito(mozgatando, lepesek.get(lepes).ertek);
        //lep();
        vill.setActionCommand("mvill");
        vill.start();
    }
    
    /* Uj kockakat general veletlen szamok alapjan, vagy atveszi a kezdo kockak ertekeit, megjeleniti azokat a feluleten, eloallitja a lepesek listat, 
    frissiti a ststisztikat, whitelist-k gyartasat kezdemenyezi, elinditja az ai lepes timer-et, egyeb funkciok */
    void dobas() {
        lepes = 0;
        lepesek.clear();
        whiteList1.clear();
        whiteList2.clear();
        
        tab.uzi1.setText("");
        
        // ha kezdo lepes
        if (aiPip == 167 && playersPip == 167) {
            if (playersTurn) {
                tab.uzi1.setText("Lépjen a saját korongjaira kattintva amíg van kocka (majd OK gomb)!");
            }
            kocka1 = kezdoKockaPl;
            kocka2 = kezdoKockaAi;
            Lepesek l = new Lepesek();
            l.ertek = kocka1;
            lepesek.add(l);
            Lepesek k = new Lepesek();
            k.ertek = kocka2;
            lepesek.add(k);
            
        }
        else {
            // elozo kockak etuntetese a GUI-n
            tab.kockaPanel.removeAll();
            
            kocka1 = (byte)(r.nextInt(6)+1);
            kocka2 = (byte)(r.nextInt(6)+1);
            if (kocka1 == kocka2) {
                for (int i=0; i<4; i++) {
                    Lepesek l = new Lepesek();
                    l.ertek = kocka1;
                    lepesek.add(l);
                }
            }
            else {
                Lepesek l = new Lepesek();
                l.ertek = kocka1;
                lepesek.add(l);
                Lepesek k = new Lepesek();
                k.ertek = kocka2;
                lepesek.add(k);
            }
            
            // kockak megjelenitese a GUI-n
            for (int i=0; i<lepesek.size(); i++) {
                Kocka koc = new Kocka(lepesek.get(i).ertek, playersTurn ? playersColor : aiColor);
                koc.addMouseListener(this);
                tab.kockaPanel.add(koc);
            }
            tab.kockaPanel.updateUI();
            //tab.kockaPanel.repaint();
            if (hangok) {
                dobasHang.stop();
                dobasHang.play();
            }
        }
        
        // statisztika
        byte osszErtek = 0;
        for (int i=0; i<lepesek.size(); i++) {
            osszErtek += lepesek.get(i).ertek;
        }
        if (playersTurn) {
            tab.s.dobottKockakJatekPl[kocka1]++;
            tab.s.dobottKockakJatekPl[kocka2]++;
            tab.s.dobottKockakMeccsPl[kocka1]++;
            tab.s.dobottKockakMeccsPl[kocka2]++;
            tab.s.dobottKockakOsszPl[kocka1]++;
            tab.s.dobottKockakOsszPl[kocka2]++;
            tab.s.dobottKockakErtekJatekPl += osszErtek;
            tab.s.dobottKockakErtekMeccsPl += osszErtek;
            tab.s.dobottKockakErtekOsszPl += osszErtek;
            if (kocka1 == kocka2) {
                tab.s.duplakJatekPl++;
                tab.s.duplakMeccsPl++;
                tab.s.duplakOsszPl++;
            }
        }
        else {
            tab.s.dobottKockakJatekAi[kocka1]++;
            tab.s.dobottKockakJatekAi[kocka2]++;
            tab.s.dobottKockakMeccsAi[kocka1]++;
            tab.s.dobottKockakMeccsAi[kocka2]++;
            tab.s.dobottKockakOsszAi[kocka1]++;
            tab.s.dobottKockakOsszAi[kocka2]++;
            tab.s.dobottKockakErtekJatekAi += osszErtek;
            tab.s.dobottKockakErtekMeccsAi += osszErtek;
            tab.s.dobottKockakErtekOsszAi += osszErtek;
            if (kocka1 == kocka2) {
                tab.s.duplakJatekAi++;
                tab.s.duplakMeccsAi++;
                tab.s.duplakOsszAi++;
            }
        }
        tab.s.setLabels();
        
        boolean elsoBlokkolva = blokkolva(poz, lepesek.get(lepes).ertek);
        boolean masodikBlokkolva = blokkolva(poz, lepesek.get(lepes+1).ertek);
        // ha mindket kocka blokkolva van
        if (elsoBlokkolva && masodikBlokkolva) {
            tab.uzi1.setText("A " + (playersTurn ? "Játékos" : "Gép") + " nem tud lépni!");
            tab.toolBar.getComponent(2).setEnabled(true);      // OK gomb
        }
        // Ha legalabb az egyik kocka nincs blokkolva, dupla eseteben nyilvan ha egyik sincs
        else {
            if (kocka1 != kocka2) {
                lepesKihasznalas();
            }
            if (!playersTurn) {
                if (elsoBlokkolva) {
                    kockaCsere();
                }
                tab.mentesMehet(false);                                  // nem lehet menteni / betolteni a jatekot amig az ai lepked
                tab.betoltesMehet(false);
                tab.ujJatekMehet(false);
                t.setActionCommand("tick");
                t.start();
                System.out.println("ai backstones: " + backStones());
            }
        }
        // tesztkod
        /*if (!playersTurn) {
            System.out.println("Mozgathatok " + lepesek.get(lepes).ertek + " ertekkel: ");
            for (int i=0; i<mozgathatok(poz, lepesek.get(lepes).ertek).size(); i++) {
                System.out.println(mozgathatok(poz, lepesek.get(lepes).ertek).get(i) + ", akadalyok: " + akadalyok(mozgathatok(poz, lepesek.get(lepes).ertek).get(i), aiColor));
            }
            System.out.println();
        }
        
        if (whiteList1 != null) {
            System.out.print("whitelist1: ");
            for (int i=0; i<whiteList1.size(); i++) {
                System.out.print(whiteList1.get(i) + ", ");
            }
            System.out.println();
        }
        
        if (whiteList2 != null) {
            System.out.print("whitelist2: ");
            for (int i=0; i<whiteList2.size(); i++) {
                System.out.print(whiteList2.get(i) + ", ");
            }
            System.out.println();
        }*/
        // tesztkod vege
        
    }
    
    // mevizsgalja, h egy adott allasnal, az aktualis kocka erteknel minden korongunk blokkolt-e v sem
    boolean blokkolva(Pozicio[] allas, byte ertek) {
        if (mozgathatok(allas, ertek).isEmpty()) {
            return true;
        }else return false;
    }
    
    /* minden folyamatot ez vezerel, ami egy lepes kapcsan felmerul. pl korongok mozgatasa, pip frissitese, 
    gombok es menu elemek ki-bekapcsolasa, ai lepes timer-et leallitja, ha az lelepte az osszes lepest, stb mind itt indul el*/
    void lep() {
        tab.uzi1.setText("");
        
        //mozgatottak[lepes] = mozgatando.szam;
        /*if (playersTurn) {
            lepesek.get(lepes).mozgatott = mozgatando.szam;
        }*/
        lepesek.get(lepes).mozgatott = mozgatando.szam;
        
        
        // utes, ha van a cel pozicion 1 mas szinu korong. Ha van legalabb egy korong, akkor dummy nem lehet a cel.
        if (cel.korongok == 1 && !mozgatando.szin.equals(cel.szin)) {
            poz[playersTurn ? 27 : 26].hozzaad(cel.szin);
            cel.elvesz();
            lepesek.get(lepes).utesek = true;
            if (hangok) {
                utesHang.stop();
                utesHang.play();
            }
        }
        else {
            lepesek.get(lepes).utesek = false;
            if (hangok) {
                lepesHang.stop();
                lepesHang.play();
            }
        }
        mozgatando.elvesz();
        cel.hozzaad(playersTurn ? playersColor : aiColor);
        lepes++;
        System.out.println("lepes: " + lepes);
        if (lepes == lepesek.size()) {
            tab.toolBar.getComponent(1).setEnabled(false);      // elore gomb
            tab.toolBar.getComponent(2).setEnabled(true);      // OK gomb
            if (!playersTurn) {
                if (t.isRunning()) {        // csak ha ai lepked, nem elore eseteben
                    t.stop();
                    System.out.println("timer is running: " + t.isRunning());
                    tab.mentesMehet(true);                                  // innentol ujra lehet menteni / betolteni a jatekot
                    tab.betoltesMehet(true);
                    tab.ujJatekMehet(true);
                }
                tab.toolBar.getComponent(0).setEnabled(true);                       // visszagomb csak akkor jon elo, ha lelepte az osszeset, vagy nem tud lepni
                
            }
        }
        else {
            if (lepesek.get(lepes).mozgatott == 0) {
                tab.toolBar.getComponent(1).setEnabled(false);  //elore gomb
            }
            if (blokkolva(poz, lepesek.get(lepes).ertek)) {
                tab.uzi1.setText("A " + (playersTurn ? "Játékos" : "Gép") + " nem tud lépni!");
                tab.toolBar.getComponent(2).setEnabled(true);      // OK gomb
                if (!playersTurn) {
                    if (t.isRunning()) {        // csak ha ai lepked, nem elore eseteben
                        t.stop();
                        System.out.println("timer is running: " + t.isRunning());
                        tab.mentesMehet(true);                                  // innentol ujra lehet menteni / betolteni a jatekot
                        tab.betoltesMehet(true);
                        tab.ujJatekMehet(true);
                    }
                    tab.toolBar.getComponent(0).setEnabled(true);                   // visszagomb csak akor jon elo, ha lelepte az osszeset, vagy nem tud lepni
                }
            }
        }
        if (!t.isRunning()) {
            tab.toolBar.getComponent(0).setEnabled(true);       // visszagomb set enabled
        }
        // kocka eltunik
        tab.kockaPanel.getComponent(lepes-1).setVisible(false);
        
        pipSzamolo();
        
        // Ha vege a jateknak
        if (poz[0].korongok == 15 || poz[25].korongok == 15) {
            jatekVege = true;
            if (!playersTurn) {
                if (t.isRunning()) {
                    t.stop();
                    System.out.println("timer is running: " + t.isRunning());
                    tab.mentesMehet(true);                                  // innentol ujra lehet menteni / betolteni a jatekot
                    tab.betoltesMehet(true);
                    tab.ujJatekMehet(true);
                }
            }
            playersTurn = true;
            lepes = (byte)lepesek.size();
            tab.toolBar.getComponent(2).setEnabled(false);
            tab.toolBar.getComponent(0).setEnabled(false);
            kiErtekeles();
        }
    }
    
    // kiertekeli a befejezett jatek eredmenyet (pontok, gammon, bg, stb), frissiti a statisztikat ennek megfeleloen, kozli az eredmenyt a felhasznaloval
    void kiErtekeles() {
        boolean pl;         // player nyert
        byte backgammon = 1;
        
        // ki nyert
        if (poz[0].korongok == 15) {
            pl = true;
            if (hangok) {                         
                nyeresHang.play();
            }
            // statisztika
            tab.s.nyertJatekJatekPl++;
            tab.s.nyertJatekMeccsPl++;
            tab.s.nyertJatekOsszPl++;
        }
        else {
            pl = false;
            if (hangok) {
                vesztesHang.play();
            }
            // statisztika
            tab.s.nyertJatekJatekAi++;
            tab.s.nyertJatekMeccsAi++;
            tab.s.nyertJatekOsszAi++;
        }
        
        // backgammon/gammon megallapitasa
        if (poz[pl ? 25 : 0].korongok == 0) {
            backgammon = 2;
        }
        if (backgammon == 2) {
            boolean van = false;
            for (int i=(pl ? 1 : 19); i<=(pl ? 6 : 24); i++) {
                if (poz[i].szin.equals(pl ? aiColor : playersColor)) {
                    van = true;
                    break;
                }
            }
            if (poz[pl ? 27 : 26].korongok != 0) van = true;
            if (van) {
                backgammon = 3;
            }
        }
            // statisztika
        if (pl) {
            if (backgammon == 2) {
                tab.s.gammonJatekPl++;
                tab.s.gammonMeccsPl++;
                tab.s.gammonOsszPl++;
            }
            else if (backgammon == 3) {
                tab.s.backGammonJatekPl++;
                tab.s.backGammonMeccsPl++;
                tab.s.backGammonOsszPl++;
            }
        }
        else {
            if (backgammon == 2) {
                tab.s.gammonJatekAi++;
                tab.s.gammonMeccsAi++;
                tab.s.gammonOsszAi++;
            }
            else if (backgammon == 3) {
                tab.s.backGammonJatekAi++;
                tab.s.backGammonMeccsAi++;
                tab.s.backGammonOsszAi++;
            }
        }
        
        // pontok
        if (pl) {
            playerScore += backgammon * tet;      // * duplazoKocka
            ((JLabel)tab.pontokPanel.getComponent(7)).setText(String.valueOf(playerScore) + "/" + match);
            // statisztika
            tab.s.nyertPontokJatekPl += backgammon * tet;
            tab.s.nyertPontokMeccsPl += backgammon * tet;
            tab.s.nyertPontokOsszPl += backgammon * tet;
            if (playerScore >= match) {
                tab.s.nyertMeccsMeccsPl++;
                tab.s.nyertMeccsOsszPl++;
            }
        }
        else { 
            aiScore += backgammon * tet;
            ((JLabel)tab.pontokPanel.getComponent(4)).setText(String.valueOf(aiScore) + "/" + match);
            // statisztika
            tab.s.nyertPontokJatekAi += backgammon * tet;
            tab.s.nyertPontokMeccsAi += backgammon * tet;
            tab.s.nyertPontokOsszAi += backgammon * tet;
            if (aiScore >= match) {
                tab.s.nyertMeccsMeccsAi++;
                tab.s.nyertMeccsOsszAi++;
            }
        }
        
        // uzenet kiiratasa
        String uzenet = "";
        if (backgammon == 3) {
            uzenet += "Backgammon! ";
        }
        else if (backgammon == 2) {
            uzenet += "Gammon! ";
        }
        uzenet += pl ? "A Játékos" : "A Gép";
        uzenet += " nyerte a játékot";
        //uzenet += " (" + backgammon * tet + " pont)";
        if ((pl ? playerScore : aiScore) >= match) {
            uzenet += " és a meccset.";
        }
        tab.uzi1.setText(uzenet);
        //tab.ujJatekMehet(true);
        
        tab.s.setLabels();
    }
    
    // mindel lepes (vagy visszalepes, vagy elore lepes) alkalmaval frissiti a pip count ertekeit a feluleten
    void pipSzamolo() {
        int pl = 0;
        int ai = 0;
        for (int i=0; i<28; i++) {
            if (i != 0 && i != 25) {
                if (i==27) {
                    ai += poz[i].korongok * 25;
                }
                else if (i==26) {
                    pl += poz[i].korongok * 25;
                }
                else {
                    if (poz[i].szin.equals(aiColor)) {
                        ai += poz[i].korongok * (25-i);
                    }
                    else if (poz[i].szin.equals(playersColor)) {
                        pl += poz[i].korongok * i;
                    }
                }
            }
        }
        playersPip = pl;
        aiPip = ai;
        // update pip count display
        ((JLabel)tab.pontokPanel.getComponent(5)).setText(String.valueOf(aiPip));
        ((JLabel)tab.pontokPanel.getComponent(8)).setText(String.valueOf(playersPip));
    }
    
    // a felek nyeresi eselyeit hasonlitja ossze bizonyos szituaciokban, a duplazas megitelesehez szukseges
    double esely() {
        if (szaladasVan()) {
            double esely = (double)aiPip / (double)playersPip;
            System.out.println("esely (aiPip / playersPip): " + esely);
            return esely;
        }
        else {
            // attol is fuggjon, hogy hany poz korong van beragadva.
            // szorzat: a beragadt korongok * a blokk hossza (hatvanyozottan szamit)
            int plOsszesen = 0;
            int aiOsszesen = 0;
            byte a;
            for (int i=1; i<28; i++) {
                if (i != 25) {                                  // ai haza
                    if (poz[i].szin.equals(playersColor)) {
                        a = akadalyok(i, playersColor);
                        if (a > 2) plOsszesen += a*a * poz[i].korongok;         // 2 koronggal beragadni 3 blokk moge nem ua. mint 1 koronggal 6 moge, ezert a negyzetre emeles, majd osztes 50-nel alabb
                    }
                    else if (poz[i].szin.equals(aiColor)) {
                        a = akadalyok(i, aiColor);
                        if (a > 2)aiOsszesen += a*a * poz[i].korongok;
                    }
                }
            }
            System.out.println("2-nel nagyobb blokk miatt beragadt korongok * a blokk hossza: ");
            System.out.println("plOsszesen: " + plOsszesen);
            System.out.println("aiOsszesen: " + aiOsszesen);
            
            double aiEsely = playersPip * (1 + hazAllapota(aiColor) * 0.2) * (1 + poz[26].korongok * 0.2) * (1 + (double)plOsszesen / 50);
            double plEsely = aiPip * (1 + hazAllapota(playersColor) * 0.2) * (1 + poz[27].korongok * 0.2) * (1 + (double)aiOsszesen / 50);
            System.out.println("plEsely: " + plEsely);
            System.out.println("aiEsely: " + aiEsely);
            double esely = plEsely / aiEsely;
            System.out.println("esely (plEsely / aiEsely): " + esely);
            return esely;
        }
    }
    
    // a vissza gomb hatasara visszalepteti a korongokat az eredeti helyukre es frissiti a pip ertekeit
    void vissza() {
        // az egesz ,etodust egyszerusiteni: ertek, celszamolo, stb.
        lepes--;
        System.out.println("lepes: " + lepes);
        if (playersTurn) {
            // ha pl. poz 2rol vitte ki 0-ra 5-os ertekkel
            if (lepesek.get(lepes).mozgatott - lepesek.get(lepes).ertek <= 0) {
                poz[0].elvesz();
            }
            else {
                if (lepesek.get(lepes).mozgatott == 26) {
                    poz[25 - lepesek.get(lepes).ertek].elvesz();
                    if (lepesek.get(lepes).utesek) {
                        poz[25 - lepesek.get(lepes).ertek].hozzaad(aiColor);
                        poz[27].elvesz();
                    }
                }
                else {
                    poz[lepesek.get(lepes).mozgatott - lepesek.get(lepes).ertek].elvesz();
                    if (lepesek.get(lepes).utesek) {
                        poz[lepesek.get(lepes).mozgatott - lepesek.get(lepes).ertek].hozzaad(aiColor);
                        poz[27].elvesz();
                    }
                }
            }
        }
        
        else {
            // ha pl. poz 23 rol vitte ki 25-re 5-os ertekkel
            if (lepesek.get(lepes).mozgatott + lepesek.get(lepes).ertek >= 25) {
                if (lepesek.get(lepes).mozgatott == 27) {
                    poz[lepesek.get(lepes).ertek].elvesz();
                    if (lepesek.get(lepes).utesek) {
                        poz[lepesek.get(lepes).ertek].hozzaad(playersColor);
                        poz[26].elvesz();
                    }
                }
                else {
                    poz[25].elvesz();
                }
            }
            else {
                poz[lepesek.get(lepes).mozgatott + lepesek.get(lepes).ertek].elvesz();
                    if (lepesek.get(lepes).utesek) {
                    poz[lepesek.get(lepes).mozgatott + lepesek.get(lepes).ertek].hozzaad(playersColor);
                    poz[26].elvesz();
                }
            }
        }
        
        poz[lepesek.get(lepes).mozgatott].hozzaad(playersTurn ? playersColor : aiColor);
        //playersTurn = true;
        // visszagomb set disabled
        if (lepes == 0) {
            tab.toolBar.getComponent(0).setEnabled(false);
        }
        // eloregomb set enabled
        tab.toolBar.getComponent(1).setEnabled(true);
        // OK gomb set disabled
        tab.toolBar.getComponent(2).setEnabled(false);
        // uzenetet leveszi
        tab.uzi1.setText("");
        // kocka megjelenik
        tab.kockaPanel.getComponent(lepes).setVisible(true);
        tab.kockaPanel.updateUI();
        pipSzamolo();
    }
    
    // az elore gomb hatasara elore lepteti a korongokat az eltarolt ertekek szerint (csak akkor tudunk elore lepni, ha mar visszaleptunk valahonnan)
    void elore() {
        mozgatando = poz[lepesek.get(lepes).mozgatott];
        cel = celSzamito(mozgatando, lepesek.get(lepes).ertek);
        lep();
    }
    
    // az adott allasnal az adott felnek bent van-e az osszes korongja a hazaban (1-6 es 19-24 poziciok)
    boolean vegJatek(Pozicio[] allas) {
        boolean vj = false;
        if (playersTurn) {
            for (int i=7; i<27; i++) {
                if (allas[i].szin.equals(playersColor)) {
                    vj = false;
                    break;
                }
                else {
                    vj = true;
                }
            }
        }
        else {
            for (int i=18; i>0; i--) {
                if (allas[i].szin.equals(aiColor)) {
                    vj = false;
                    break;
                }
                else {
                    vj = true;
                }
            }
            if (allas[27].szin.equals(aiColor)) {
                vj = false;
            }
        }
        return vj;
    }
    
    // az adott allasnal es kocka erteknel a mozgathato korongok listajat adja vissza.
    ArrayList<Integer> mozgathatok(Pozicio[] allas, byte ertek) {
        ArrayList<Integer> mList = new ArrayList<Integer>();
        int c;
        if (allas[playersTurn ? 26 : 27].korongok == 0) {
            for (int i=1; i<25; i++) {
                if (allas[i].szin.equals(playersTurn ? playersColor : aiColor)) {
                    c = playersTurn ? allas[i].szam - ertek : allas[i].szam + ertek;
                    if (playersTurn ? c > 0 : c < 25) {
                        if (!allas[c].szin.equals(playersTurn ? aiColor : playersColor) || allas[c].korongok < 2) {      // ha nincs blokkolva a c, akkor az i-t begyujtjuk
                            mList.add(i);
                        }
                    }
                    else if (playersTurn ? c == 0 : c == 25) {
                        if (vegJatek(allas)) {
                            mList.add(i);
                        }
                    }
                    else if (playersTurn ? c < 0 : c > 25){
                        if (vegJatek(allas)) {
                            //ArrayList<Integer> nagyobbak = new ArrayList<>();
                            boolean van = false;
                            if (playersTurn) {
                                for (int n=i+1; n<=6; n++) {
                                    if (allas[n].szin.equals(playersColor)) {
                                        //nagyobbak.add(n);
                                        van = true;
                                        break;
                                    }
                                }
                            }
                            else {
                                for (int n=i-1; n>=19; n--) {
                                    if (allas[n].szin.equals(aiColor)) {
                                        //nagyobbak.add(n);
                                        van = true;
                                        break;
                                    }
                                }
                            }
                            // ha nincs folotte korong a hazban
                            if (!van) {
                                mList.add(i);
                            }
                        }
                    }
                }
            }
        } else {                                    // ha ki vagyok utve
            c = playersTurn ? 25 - ertek : ertek;
            if (!allas[c].szin.equals(playersTurn ? aiColor : playersColor) || allas[c].korongok < 2) {
                mList.add(playersTurn ? 26 : 27);
            }
        }
        return mList;
    }
    
    /* szimpla dobasnal egy listaba begyujti, hogy melyik poziciokkal lehet lelepni az adott erteket ugy, 
    hogy a masodik ertek is lelepheto legyen. Mindket kockara elkesziti a listat. 
    Ugy kell lepni a bg-ban, hogy mindket kockat le tudjuk lepni, ha ez lehetseges */
    void lepesKihasznalas() {
        
        if (kocka1 != kocka2) {
            // azon poziciok listaja Kocka1-re es Kocka2-re, amelyeket ha mozgatunk, a kovetkezo lepesben nem leszunk blokkolva. 
            ArrayList<Integer> lList1 = new ArrayList<Integer>();
            ArrayList<Integer> lList2 = new ArrayList<Integer>();
            ArrayList<Integer> mList1 = mozgathatok(poz, kocka1);
            ArrayList<Integer> mList2 = mozgathatok(poz, kocka2);
            
            // listak feltoltese
            for (int i=0; i<mList1.size(); i++) {
                // lep a "proba" tablan, mozgathatok i. elemevel, a kocka 1 ertekevel.
                if (!probalep(poz, mList1.get(i), kocka1)) {
                    lList1.add(mList1.get(i));
                }
            }
            for (int i=0; i<mList2.size(); i++) {
                // lep a "proba" tablan, mozgathatok i. elemevel, a kocka 2 ertekevel.
                if (!probalep(poz, mList2.get(i), kocka2)) {
                    lList2.add(mList2.get(i));
                }
            }
            whiteList1 = lList1;
            whiteList2 = lList2;
        }
    }
    
    /* Megvizsgalja (csak az nulladik lepesnel es csak ha szimplat, azaz 2 kulonbozo kockat dobtunk), hogy 
    a mozgatando pozicio rajta van-e a whitelist-en es eszerint engedi lepni. */
    boolean lephet(int m) {
        if (lepes == 0 && kocka1 != kocka2) {
            // a kockak fel lettek-e cserelve
            ArrayList<Integer> aktualisLista = new ArrayList<Integer>();
            if (lepesek.get(lepes).ertek == kocka1) {
                aktualisLista = whiteList1;
            }
            else {
                aktualisLista = whiteList2;
            }
            // Ha a mozgatando pozicio rajta van az adott kocka ertekehez tartozo listan
            if (aktualisLista.contains(m)) {
                return true;
            }
            /*Ha a mozgatando pozicio nincs rajta az adott kocka ertekehez tartozo listan,
            de van ennel jobb kihasznalasu lepes*/
            else if (!whiteList1.isEmpty() || !whiteList2.isEmpty()){
                System.out.println("nem lephet() a " + m + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                return false;
            }
            // mindket lista ures
            else {
                return true;
            }
        }
        else {
            return true;
        }
    }
    
    /* egy adott poziciot leptet egy adott tablan egy adott ertekkel es az igy kapott allasrol megallapitja, 
    hogy a masik kocka ertekevel lehet-e rajta lepni egyaltalan, vegul visszacsinalja a lepest.*/
    boolean probalep(Pozicio[] probaTabla, int mozg, byte ertek) {
        Pozicio probaMozgatando = probaTabla[mozg];
        Pozicio probaCel = celSzamito(probaMozgatando, ertek);
        boolean blokkolva;
        boolean utes = false;
        // szimulalt lepes
        if (probaCel.korongok == 1 && !probaMozgatando.szin.equals(probaCel.szin)) {
            utes = true;
            probaTabla[playersTurn ? 27 : 26].korongok++;
            probaTabla[playersTurn ? 27 : 26].szin = playersTurn ? aiColor : playersColor;
            probaCel.korongok--;
            if (probaCel.korongok == 0) {
                probaCel.szin = "dummy";
            }
        }
        probaMozgatando.korongok--;
        if (probaMozgatando.korongok == 0) {
            probaMozgatando.szin = "dummy";
        }
        probaCel.korongok++;
        probaCel.szin = playersTurn ? playersColor : aiColor;
        // check
        blokkolva = blokkolva(probaTabla, ertek == kocka1 ? kocka2 : kocka1);
        // szimulalt lepest visszacsinalja (azert, hogy a tabla valtozatlan maradjon)
        probaCel.korongok--;
        if (probaCel.korongok == 0) {
            probaCel.szin = "dummy";
        }
        probaMozgatando.korongok++;
        probaMozgatando.szin = playersTurn ? playersColor : aiColor;
        if (utes) {
            probaCel.korongok++;
            probaCel.szin = playersTurn ? aiColor : playersColor;
            probaTabla[playersTurn ? 27 : 26].korongok--;
            if (probaTabla[playersTurn ? 27 : 26].korongok == 0) {
                probaTabla[playersTurn ? 27 : 26].szin = "dummy";
            }
        }
        return blokkolva;
    }
    
    // 0. lepesnel es ket kulonbozo kockanal megcsereli a ket kockat a gui-n es megcsereli a lepesek lista elemeit is.
    void kockaCsere() {
        byte w = lepesek.get(0).ertek;
        lepesek.get(0).ertek = lepesek.get(1).ertek;
        lepesek.get(1).ertek = w;
        Kocka elsoKocka = (Kocka)tab.kockaPanel.getComponent(0);
        Kocka masodikKocka = (Kocka)tab.kockaPanel.getComponent(1);
        tab.kockaPanel.removeAll();
        tab.kockaPanel.add(masodikKocka);
        tab.kockaPanel.add(elsoKocka);
        tab.kockaPanel.updateUI();
        System.out.println("A kockacsere sikeresen megtortent.");
        if (hangok) {
            kockacsereHang.stop();
            kockacsereHang.play();
        }
    }

    /* kipakolja (bear  off) a hazban levo korongokat a 25. poziciora, ha a dobasnak megfelelo helyen all korong es tud is lepni. 
    Ha nyeresre all, nem bont 2 korongbol allo poziciot, ha az utes lehetosege fennall.*/
    int kiPakol(byte ertek) {
        boolean csere = (lepes == 0 && kocka1!=kocka2 && ertek == lepesek.get(lepes+1).ertek);
        int m = 25-ertek;
        if (mozgathatok(poz, ertek).contains(m) && (aiPip<playersPip ? (poz[m].korongok != 2 || m > utolso()) : true)) {     // 2-t ne bontson, ha vezet
            if (csere) kockaCsere();
            if (lephet(m)) {
                System.out.println(csere ? "KIPAKOLAS - kockacsere" : "KIPAKOLAS");
                return m;
            }
            else {
                if (csere) kockaCsere();
            }
        }
        return 0;
    }
    
}

/* tarolja egy lepes jellemzoit: ertek (hany mezot lepett), a mozgatott korong kiindulasi poziciojat, es hogy a lepessel kiutott-e egy ellenseges figurat, vagy sem. 
A Lepesek peldanyai egy listaban kerulnek eltarolasra. Minden kockadobas alkalmaval ez a lista frissul. 
Ketto vagy negy eleme lehet es minden eleme (azaz egy lepes) egy dobott kockahoz tartozik. */
class Lepesek {
    byte ertek;
    int mozgatott;
    boolean utesek;
}

/* peldanyai a gep egy olyan tobb mozzanatbol allo lepeset reprezentaljak, amihez ketto vagy tobb kocka szukseges 
es egy korongot a kockak ertekeinek osszegevel mozgat tobb lepcsoben. Tarolja a mozgatando korong kiindulasi poziciojanak szamat es hogy hanyszor 
kell meg az elso lepesen kivul leptetni, hogy a kivant celpoziciot elerjuk vele. */
class Osszeggel {
    int m;      // mozgatando szam
    int j;      // hanyszor kell meg mozgatni, hogy  a celt elerje
    int c;      // cel
    
    public Osszeggel(int m, int j, int c) {
        this.m = m;
        this.j = j;
        this.c = c;
    }
}