/*
 * $Id: EyeDropperColorChooserPanel.java 4082 2011-11-15 18:39:43Z kschaefe $
 *
 * Copyright 2008 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx.color;

import org.jdesktop.swingx.JXColorSelectionButton;
import org.jdesktop.swingx.util.PaintUtils;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.MouseInputAdapter;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>EyeDropperColorChooserPanel is a pluggable panel for the
 * {@link JColorChooser} which allows the user to grab any
 * color from the screen using a magnifying glass.</p>
 *
 * <p>Example usage:</p>
 * <pre><code>
 * public static void main(String... args) {
 *     SwingUtilities.invokeLater(() -> {
 *         JColorChooser chooser = new JColorChooser();
 *         chooser.addChooserPanel(new EyeDropperColorChooserPanel());
 *         JFrame frame = new JFrame();
 *         frame.add(chooser);
 *         frame.pack();
 *         frame.setVisible(true);
 *     });
 * }
 * </code></pre>
 *
 * @author joshua@marinacci.org
 */
public class EyeDropperColorChooserPanel extends AbstractColorChooserPanel {

    private static final Logger LOG = Logger.getLogger(EyeDropperColorChooserPanel.class.getName());

    private final JButton activeColor = new JXColorSelectionButton();
    private final JButton eyeDropper = new JButton();
    private final JTextField hexColor = new JTextField();
    private final JLabel lblNo = new JLabel();
    private final JPanel magPanel = new MagnifyingPanel();
    private final JTextField rgbColor = new JTextField();

    /**
     * Creates new EyeDropperColorChooserPanel
     */
    public EyeDropperColorChooserPanel() {
        initComponents();
        MouseInputAdapter mia = new MouseInputAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
            }

            @Override
            public void mouseDragged(MouseEvent evt) {
                Point pt = evt.getPoint();
                SwingUtilities.convertPointToScreen(pt, evt.getComponent());
                ((MagnifyingPanel) magPanel).setMagPoint(pt);
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                Color newColor = new Color(((MagnifyingPanel) magPanel).activeColor);
                getColorSelectionModel().setSelectedColor(newColor);
            }
        };
        eyeDropper.addMouseListener(mia);
        eyeDropper.addMouseMotionListener(mia);
        try {
            eyeDropper.setIcon(new ImageIcon(EyeDropperColorChooserPanel.class.getResource("mag.png")));
            eyeDropper.setText("");
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Cannot load icon", ex);
        }

        magPanel.addPropertyChangeListener(evt -> {
            Color color = new Color(((MagnifyingPanel) magPanel).activeColor);
            activeColor.setBackground(color);
            hexColor.setText(PaintUtils.toHexString(color).substring(1));
            rgbColor.setText(color.getRed() + "," + color.getGreen() + "," + color.getBlue());
        });
    }

    private static class MagnifyingPanel extends JPanel {

        private Point2D point;
        private int activeColor;

        public void setMagPoint(Point2D point) {
            this.point = point;
            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            if (point != null) {
                Rectangle rect = new Rectangle((int) point.getX() - 10, (int) point.getY() - 10, 20, 20);
                try {
                    BufferedImage img = new Robot().createScreenCapture(rect);
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
                    int oldColor = activeColor;
                    activeColor = img.getRGB(img.getWidth() / 2, img.getHeight() / 2);
                    firePropertyChange("activeColor", oldColor, activeColor);
                } catch (AWTException ex) {
                    LOG.log(Level.WARNING, "Cannot grab screen", ex);
                }
            }
            g.setColor(Color.black);
            g.drawRect(getWidth() / 2 - 5, getHeight() / 2 - 5, 10, 10);
        }
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        setLayout(new GridBagLayout());

        eyeDropper.setText("eye");
        add(eyeDropper, new GridBagConstraints());

        magPanel.setLayout(new BorderLayout());

        magPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        magPanel.setMinimumSize(new Dimension(100, 100));
        magPanel.setPreferredSize(new Dimension(100, 100));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(0, 0, 0, 12);
        add(magPanel, gridBagConstraints);

        activeColor.setEnabled(false);
        activeColor.setPreferredSize(new Dimension(40, 40));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(2, 0, 2, 0);
        add(activeColor, gridBagConstraints);

        hexColor.setEditable(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(2, 0, 2, 0);
        add(hexColor, gridBagConstraints);

        JTextArea taHelp = new JTextArea();
        taHelp.setColumns(20);
        taHelp.setEditable(false);
        taHelp.setLineWrap(true);
        taHelp.setRows(5);
        taHelp.setText("Drag the magnifying glass to select a color from the screen.");
        taHelp.setWrapStyleWord(true);
        taHelp.setOpaque(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 10.0;
        gridBagConstraints.weighty = 10.0;
        gridBagConstraints.insets = new Insets(0, 0, 7, 0);
        add(taHelp, gridBagConstraints);

        lblNo.setText("#");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(0, 4, 0, 4);
        add(lblNo, gridBagConstraints);

        rgbColor.setEditable(false);
        rgbColor.setText("255,255,255");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(2, 0, 2, 0);
        add(rgbColor, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(0, 4, 0, 4);
        add(new JLabel("RGB"), gridBagConstraints);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateChooser() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildChooser() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
        return "Grab from Screen";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getSmallDisplayIcon() {
        return new ImageIcon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getLargeDisplayIcon() {
        return new ImageIcon();
    }
}
