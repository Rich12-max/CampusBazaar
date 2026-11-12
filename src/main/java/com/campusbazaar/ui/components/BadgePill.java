package com.campusbazaar.ui.components;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Aesthetic Rounded Pill Badge component for status & categories.
 */
public class BadgePill extends JLabel {
    private Color badgeColor;

    public BadgePill(String text, Color badgeColor, Color textColor) {
        super(text);
        this.badgeColor = badgeColor;
        setForeground(textColor);
        setFont(new Font("Segoe UI", Font.BOLD, 10));
        setBorder(new EmptyBorder(3, 8, 3, 8));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(badgeColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}

