package ostabla;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;

class Korong extends JPanel {
    String szin;        // a korong szine, "fekete", "feher" v "dummy"
    Color c;            
    JLabel l;           // 5-nel tobb korongot jelzo cimke
    int pozSzama;
    
    // konstruktor
    public Korong(String szin, int pozSzama) {
        this.szin = szin;
        this.pozSzama = pozSzama;
        // label
        //setLayout(new BorderLayout());
        l = new JLabel("", JLabel.LEADING);
        switch (szin) {
            case "fekete":
                l.setForeground(Color.white);
                break;
            case "fehér":
                l.setForeground(Color.black);
                break;
        }
        
        //l.setForeground(Color.lightGray);
        l.setFont(new Font("Default", Font.BOLD, 38));
        add(l);
        
        switch (szin) {
            case "fehér":
                c = Color.white;
                break;
            
            case "fekete":
                c = Color.black;
                break;
            
            case "dummy":
                setVisible(false);
                break;
        }
    }
    
    // paint, megrajzolja a korongot
    @Override
    public void paintComponent(Graphics g) {
        //super.paintComponent(g);
        g.setColor(c);
        if (pozSzama == 0 || pozSzama == 25) {
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        else {
            g.fillOval(0, 0, this.getWidth(), this.getHeight());
//            g.setColor(Color.gray);
//            g.fillOval(getWidth()/4, getHeight()/4, this.getWidth()/2, this.getHeight()/2);
        }
    }
    // inset (padding)
    /*@Override
    public Insets getInsets() {
        Insets squeeze = new Insets(0, this.getWidth()/3, 0, 0);
        return squeeze;
    }*/
    
}