/*
 * $Id: GradientThumbRenderer.java 4082 2011-11-15 18:39:43Z kschaefe $
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
package org.jdesktop.swingx.color;

import org.jdesktop.swingx.JXMultiThumbSlider;
import org.jdesktop.swingx.multislider.ThumbRenderer;
import org.jdesktop.swingx.util.PaintUtils;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GradientThumbRenderer extends JComponent implements ThumbRenderer {

    private static final Logger LOG = Logger.getLogger(GradientThumbRenderer.class.getName());

    private Image thumbBlack;
    private Image thumbGray;

    public GradientThumbRenderer() {
        try {
            thumbBlack = ImageIO.read(GradientThumbRenderer.class.getResource("/icons/thumb_black.png"));
            thumbGray = ImageIO.read(GradientThumbRenderer.class.getResource("/icons/thumb_gray.png"));
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Cannot load icons", ex);
        }
    }

    private boolean selected;

    @Override
    protected void paintComponent(Graphics g) {
        JComponent thumb = this;
        int w = thumb.getWidth();
        g.setColor(getForeground());
        g.fillRect(0, 0, w - 1, w - 1);
        if (selected) {
            g.drawImage(thumbBlack, 0, 0, null);
        } else {
            g.drawImage(thumbGray, 0, 0, null);
        }
    }

    public JComponent getThumbRendererComponent(JXMultiThumbSlider<?> slider, int index, boolean selected) {
        Color c = (Color) slider.getModel().getThumbAt(index).getObject();
        c = PaintUtils.removeAlpha(c);
        this.setForeground(c);
        this.selected = selected;
        return this;
    }
}
