package ostabla;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Polygon;
import javax.swing.JPanel;

class Pozicio extends JPanel {
    int szam;                   // 28 db van osszesen 0. - 27.
    boolean also;               // also sor, v felso. 26 es 27 kivetel
    String szin = "dummy";      // amilyen szinu korong all rajta. ha nincs, akkor "dummy"
    byte korongok = 0;          // a rajta allo korongok szama
    
    Color hazakBg = Color.darkGray;                 // hazak szine
    Color fieldBg = new Color(100, 100, 100);       // a jatekter alapszine
    Color sotet = new Color(196, 98, 16);           // sotetebb haromszogek
    Color vilagos = new Color(199, 182, 165);       // vilagosabb haromszogek
    
    
    
    public Pozicio(Pozicio eredeti) {               // copy constructor
        this.szam = eredeti.szam;
        this.also = eredeti.also;
        this.szin = eredeti.szin;
        this.korongok = eredeti.korongok;
    }
    
    public Pozicio(int szam) {
        //super();
        this.szam = szam;
        
        if (szam == 0 || szam >= 25 && szam <=27) {
//            setBackground(Color.lightGray);
            setBackground(hazakBg);
        } else {
            setBackground(fieldBg);
        }
        
        if (szam == 0 || szam == 25) {
            setLayout(new GridLayout(15, 1, 2, 2));
        }
        else {
            setLayout(new GridLayout(6, 1, 0, 0));
        }
        
        if (szam<13 || szam == 26) {
            also = true;
        }
        else {
            also = false;
        }
        // ha also, akkor betesz 15 v. 6 dummy korongot
        if (also) {
            if (szam == 0) {
                for (int i=0; i<15; i++) {
                    Korong dKorong = new Korong("dummy", szam);
                    add(dKorong);
                }
            }
            else {
                for (int i=0; i<6; i++) {
                    Korong dKorong = new Korong("dummy", szam);
                    add(dKorong);
                }
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[] xPoints = new int[3];
        xPoints[0] = getWidth()/2;
        xPoints[1] = 0;
        xPoints[2] = getWidth();
        int[] yPoints = new int[3];
        yPoints[0] = getHeight()/6;
        yPoints[1] = getHeight();
        yPoints[2] = getHeight();
        Polygon haromszog = new Polygon(xPoints, yPoints, 3);
        g.setColor(vilagos);
        if (szam < 13 && szam > 0 && szam%2==0) {
            g.fillPolygon(haromszog);
        }
        g.setColor(sotet);
        if (szam < 13 && szam > 0 && szam%2==1) {
            g.fillPolygon(haromszog);
        }
        
        xPoints[0] = 0;
        xPoints[1] = getWidth()/2;
        xPoints[2] = getWidth();
        yPoints[0] = 0;
        yPoints[1] = getHeight()/6*5;
        yPoints[2] = 0;
        haromszog.xpoints = xPoints;
        haromszog.ypoints = yPoints;
        g.setColor(vilagos);
        if (szam > 12 && szam < 25 && szam%2==0) {
            g.fillPolygon(haromszog);
        }
        g.setColor(sotet);
        if (szam > 12 && szam < 25 && szam%2==1) {
            g.fillPolygon(haromszog);
        }
    }
    
    // hozzaad egy korongot a pociciohoz (vagy ha mar 5 db van rajta, akkor egy cimken jelzi a szamukat)
    void hozzaad(String szin) {
        korongok++;
        if (also) {
            if (szam != 0) {
                if (korongok >= 6) {
                    // a legfelson a labelt allitsa, h a korongok szama-5 ot mutatssa
                    Component topKorong = this.getComponent(1);
                    //((Korong)topKorong).l.setText("+" + (korongok-5));
                    ((Korong)topKorong).l.setText(String.valueOf(korongok));
                }
                else {
                    remove(0);
                    add(new Korong(szin, szam));
                }
            } 
            else {
                remove(0);
                add(new Korong(szin, szam));
            }
        }
        else {
            if (szam != 25) {
                if (korongok >= 6) {
                    Component bottomKorong = this.getComponent(4);
                    //((Korong)bottomKorong).l.setText("+" + (korongok-5));
                    ((Korong)bottomKorong).l.setText(String.valueOf(korongok));
                }
                else {
                    add(new Korong(szin, szam));
                }
            }
            else {
                add(new Korong(szin, szam));
            }
        }
        
        // szin beallitasa
        this.szin = szin;
        
        //this.updateUI();
        //validate();
        repaint();
    }
    
    // elvesz a poziciorol egy korongot
    void elvesz() {
        korongok--;
        if (also) {
            if (szam != 0) {
                if (korongok >= 6) {
                    // frissitse a labelt
                    Component topKorong = this.getComponent(1);
                    //((Korong)topKorong).l.setText("+" + (korongok-5));
                    ((Korong)topKorong).l.setText(String.valueOf(korongok));
                }
                else {
                    if (korongok == 5) {
                        // leveszi a labelt
                        Component topKorong = this.getComponent(1);
                        ((Korong)topKorong).l.setText("");
                    }
                    else {
                        // kiveszi a legalsot
                        remove(getComponentCount()-1);
                        // betesz felulre egy dummy-t
                        add(new Korong("dummy", szam), 0);
                    }
                }
            }
            else {
                // kiveszi a legalsot
                remove(getComponentCount()-1);
                // betesz felulre egy dummy-t
                add(new Korong("dummy", szam), 0);
            }
        }
        else {
            if (szam != 25){
                if (korongok >= 6) {
                    // frissitse a labelt
                    Component bottomKorong = this.getComponent(4);
                    //((Korong)bottomKorong).l.setText("+" + (korongok-5));
                    ((Korong)bottomKorong).l.setText(String.valueOf(korongok));
                }
                else {
                    if (korongok == 5) {
                        // leveszi a labelt
                        Component bottomKorong = this.getComponent(4);
                        ((Korong)bottomKorong).l.setText("");
                    }
                    else {
                        // kiveszi a legalsot
                        remove(getComponentCount()-1);
                    }
                }
            }
            else {
                // kiveszi a legalsot
                remove(getComponentCount()-1);
            }
        }
        
        if (korongok == 0) {
            szin = "dummy";
        }
        //this.updateUI();
        //validate();
        repaint();
    }
    
    // lemasol egy bejovo parameterben meghatarozott poziciot
    void masol(Pozicio masolando) {
        while (korongok > 0) {
            elvesz();
        }
        while (korongok < masolando.korongok) {
            hozzaad(masolando.szin);
        }
        validate();
    }
    
    // u.a. mint a masol, csak ellenkezo szinure
    void atSzinez(Pozicio masolando) {
        while (korongok > 0) {
            elvesz();
        }
        while (korongok < masolando.korongok) {
            hozzaad(masolando.szin.equals("fekete") ? "fehér" : "fekete");
        }
        validate();
    }
    
}