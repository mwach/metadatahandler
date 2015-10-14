package itti.com.pl.arena.cm.client.ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ImagePanel extends JPanel {

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    private Image img;

    public ImagePanel(String image) {

        this.img = new ImageIcon(image).getImage();
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
}
