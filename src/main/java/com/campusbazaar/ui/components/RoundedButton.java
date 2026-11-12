package com.campusbazaar.ui.components;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Custom Anti-Aliased Rounded Swing Button.
 * Features smooth hover transitions and custom color schemes.
 */
public class RoundedButton extends JButton {
    private Color normalBg;
    private Color hoverBg;
    private Color pressedBg;
    private int cornerRadius;
    private boolean isHovered = false;
    private boolean isPressed = false;

    public RoundedButton(String text, Color normalBg, Color hoverBg, Color textColor, int cornerRadius) {
        super(text);
        this.normalBg = normalBg;
        this.hoverBg = hoverBg;
        this.pressedBg = hoverBg.darker();
        this.cornerRadius = cornerRadius;

        setForeground(textColor);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    public RoundedButton(String text, Color normalBg, Color hoverBg) {
        this(text, normalBg, hoverBg, Color.WHITE, 16);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isPressed) {
            g2.setColor(pressedBg);
        } else if (isHovered) {
            g2.setColor(hoverBg);
        } else {
            g2.setColor(normalBg);
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();

        super.paintComponent(g);
    }
}

