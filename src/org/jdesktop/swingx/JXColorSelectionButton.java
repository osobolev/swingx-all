/*
 * $Id: JXColorSelectionButton.java 4082 2011-11-15 18:39:43Z kschaefe $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
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

package org.jdesktop.swingx;

import org.jdesktop.swingx.color.EyeDropperColorChooserPanel;
import org.jdesktop.swingx.plaf.UIManagerExt;
import org.jdesktop.swingx.util.GraphicsUtilities;
import org.jdesktop.swingx.util.OS;
import org.jdesktop.swingx.util.PaintUtils;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

/**
 * A button which allows the user to select a single color. The button has a platform
 * specific look. Ex: on Mac OS X it will mimic an NSColorWell. When the user
 * clicks the button it will open a color chooser set to the current background
 * color of the button. The new selected color will be stored in the background
 * property and can be retrieved using the getBackground() method. As the user is
 * choosing colors within the color chooser the background property will be updated.
 * By listening to this property developers can make other parts of their programs
 * update.
 *
 * @author joshua@marinacci.org
 */
public class JXColorSelectionButton extends JButton {

    private static final Logger LOG = Logger.getLogger(JXColorSelectionButton.class.getName());

    private BufferedImage colorwell;
    private JDialog dialog = null;
    private JColorChooser chooser = null;
    private Color initialColor = null;

    /**
     * Creates a new instance of JXColorSelectionButton
     */
    public JXColorSelectionButton() {
        this(Color.red);
    }

    /**
     * Creates a new instance of JXColorSelectionButton set to the specified color.
     *
     * @param col The default color
     */
    public JXColorSelectionButton(Color col) {
        setBackground(col);
        this.addActionListener(new ActionHandler());
        this.setContentAreaFilled(false);
        this.setOpaque(false);

        try {
            colorwell = ImageIO.read(JXColorSelectionButton.class.getResource("color/colorwell.png"));
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Cannot load icon", ex);
        }

        this.addPropertyChangeListener("background", propertyChangeEvent -> getChooser().setColor(getBackground()));
    }

    /**
     * A listener class to update the button's background when the selected
     * color changes.
     */
    private static class ColorChangeListener implements ChangeListener {

        private final JXColorSelectionButton button;

        ColorChangeListener(JXColorSelectionButton button) {
            this.button = button;
        }

        public void stateChanged(ChangeEvent changeEvent) {
            button.setBackground(button.getChooser().getColor());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) {
        // want disabledForeground when disabled, current colour otherwise
        Color fillColor = isEnabled()
            ? PaintUtils.removeAlpha(getBackground())
            : UIManagerExt.getSafeColor("Button.disabledForeground", Color.LIGHT_GRAY);

        // draw the colorwell image (should only be on OSX)
        if (OS.isMacOSX() && colorwell != null) {
            Insets ins = new Insets(5, 5, 5, 5);
            GraphicsUtilities.tileStretchPaint(g, this, colorwell, ins);

            // fill in the color area
            g.setColor(fillColor);
            g.fillRect(ins.left, ins.top, getWidth() - ins.left - ins.right, getHeight() - ins.top - ins.bottom);
            // draw the borders
            g.setColor(PaintUtils.setBrightness(fillColor, 0.85f));
            g.drawRect(ins.left, ins.top, getWidth() - ins.left - ins.right - 1, getHeight() - ins.top - ins.bottom - 1);
            g.drawRect(ins.left + 1, ins.top + 1, getWidth() - ins.left - ins.right - 3, getHeight() - ins.top - ins.bottom - 3);
        } else {
            Graphics2D g2 = (Graphics2D) g.create();

            try {
                g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
                g2.setColor(Color.LIGHT_GRAY);
                int diam = Math.min(getWidth(), getHeight());
                final int inset = 3;
                g2.fill(new Ellipse2D.Float(inset, inset, diam - 2 * inset, diam - 2 * inset));
                g2.setColor(fillColor);
                final int border = 1;
                g2.fill(new Ellipse2D.Float(inset + border, inset + border, diam - 2 * inset - 2 * border, diam - 2 * inset - 2 * border));
            } finally {
                g2.dispose();
            }
        }
    }

    /**
     * Conditionally create and show the color chooser dialog.
     */
    private void showDialog() {
        if (dialog == null) {
            dialog = JColorChooser.createDialog(this, "Choose a color", true, getChooser(), actionEvent -> {
                Color color = getChooser().getColor();
                if (color != null) {
                    setBackground(color);
                }
            }, actionEvent -> setBackground(initialColor));
            dialog.getContentPane().add(getChooser());
            getChooser().getSelectionModel().addChangeListener(new ColorChangeListener(this));
        }

        initialColor = getBackground();
        dialog.setVisible(true);
    }

    /**
     * Get the JColorChooser that is used by this JXColorSelectionButton. This
     * chooser instance is shared between all invocations of the chooser, but is unique to
     * this instance of JXColorSelectionButton.
     *
     * @return the JColorChooser used by this JXColorSelectionButton
     */
    public JColorChooser getChooser() {
        if (chooser == null) {
            chooser = new JColorChooser();
            // add the eyedropper color chooser panel
            chooser.addChooserPanel(new EyeDropperColorChooserPanel());
        }
        return chooser;
    }

    /**
     * Set the JColorChooser that is used by this JXColorSelectionButton.
     * chooser instance is shared between all invocations of the chooser,
     * but is unique to
     * this instance of JXColorSelectionButton.
     *
     * @param chooser The new JColorChooser to use.
     */
    public void setChooser(JColorChooser chooser) {
        JColorChooser oldChooser = getChooser();
        this.chooser = chooser;
        firePropertyChange("chooser", oldChooser, chooser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet() || colorwell == null) {
            return super.getPreferredSize();
        }

        return new Dimension(colorwell.getWidth(), colorwell.getHeight());
    }

    /**
     * A private class to conditionally create and show the color chooser
     * dialog.
     */
    private class ActionHandler implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            showDialog();
        }
    }
}
