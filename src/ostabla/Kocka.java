package ostabla;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Kocka extends JPanel {
    // a dobokocka erteke 1-6. Ha ez 0, akkor duplazo kockarol van szo
    private byte ertek;
    // a kocka szine, lehet "fekete" v. "feher" v. "dummy", azaz szurke a duplazo kockanal
    private String szin;
    // a tetet jelzo cimke d. kockan
    private JLabel l;
    
    public Kocka(byte ertek, String szin) {
        this.ertek = ertek;
        this.szin = szin;
        if (ertek == 0){
            l = new JLabel();          // a tet erteke (a jatek.java-bol) legyen ez az ertek. ha ez 1, a kocka ne latszodjon // String.valueOf(ertek)
            add(l);
        }
    }
    
    // getter
    public byte getErtek() {
        return ertek;
    }
    
    // getter
    public String getSzin() {
        return szin;
    }
    
    // getter
    public JLabel getL() {
        return l;
    }
    
    
    // duplazaskor a d. kocka szinet es a cimkejet irja felul
    public void duplaz(String szin, int tet){
        l.setText(String.valueOf(tet));
        this.szin = szin;
        //repaint();
    }
    
    // a kiindulasi allapotot allitja vissza a d. kockan (pl uj jatek)
    public void nullaz(){
        l.setText("");
        this.szin = "dummy";
    }
    
    // magvaltoztatja a kockak szinet az ellenkezojere (beallitasok) 
    public void atSzinez() {
        if (szin.equals("fekete")) {
            szin = "fehér";
        }else szin = "fekete";
    }
    
    @Override
    public void paintComponent(Graphics g) {
        //super.paintComponent(g);
        rajzol(g);
    }
    
    // megrajzolja a kockakat a pontokkal egyutt
    public void rajzol(Graphics g) {
        switch (szin) {
            case "fehér":
                g.setColor(Color.white);
                break;
            case "fekete":
                g.setColor(Color.black);
                break;
            case "dummy":
                //g.setColor(Color.lightGray);
                g.setColor(Color.gray);
                break;
        }
        //g.fillRect(0, 0, 50, 50);
        g.fillRoundRect(0, 0, 50, 50, 20, 20);
        switch (szin) {
            case "fehér":
                g.setColor(Color.black);
                break;
            case "fekete":
                g.setColor(Color.white);
                break;
            /*case "dummy":
                g.setColor(Color.lightGray);
                break;*/
        }
        switch (ertek) {
            case 0:
                // label
                /*l = new JLabel(String.valueOf(ertek));
                add(l);*/
                break;
            case 1:
                g.fillOval(20, 20, 10, 10);
                break;
            case 2:
                g.fillOval(10, 30, 10, 10);
                g.fillOval(30, 10, 10, 10);
                break;
            case 3:
                g.fillOval(10, 10, 10, 10);
                g.fillOval(20, 20, 10, 10);
                g.fillOval(30, 30, 10, 10);
                break;
            case 4:
                g.fillOval(10, 10, 10, 10);
                g.fillOval(30, 10, 10, 10);
                g.fillOval(10, 30, 10, 10);
                g.fillOval(30, 30, 10, 10);
                break;
            case 5:
                g.fillOval(10, 10, 10, 10);
                g.fillOval(30, 10, 10, 10);
                g.fillOval(20, 20, 10, 10);
                g.fillOval(10, 30, 10, 10);
                g.fillOval(30, 30, 10, 10);
                break;
            case 6:
                g.fillOval(10, 10, 10, 10);
                g.fillOval(30, 10, 10, 10);
                g.fillOval(10, 20, 10, 10);
                g.fillOval(30, 20, 10, 10);
                g.fillOval(10, 30, 10, 10);
                g.fillOval(30, 30, 10, 10);
                break;
            default:
                break;
        }
    }
}