/*
 * $Id: GradientTrackRenderer.java 4082 2011-11-15 18:39:43Z kschaefe $
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
import org.jdesktop.swingx.multislider.Thumb;
import org.jdesktop.swingx.multislider.TrackRenderer;
import org.jdesktop.swingx.util.PaintUtils;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * <p><b>Dependency</b>: Because this class relies on LinearGradientPaint and
 * RadialGradientPaint, it requires the optional MultipleGradientPaint.jar</p>
 */
public class GradientTrackRenderer extends JComponent implements TrackRenderer<Color> {

    private final Paint checkerPaint;

    public GradientTrackRenderer() {
        checkerPaint = PaintUtils.getCheckerPaint();
    }

    private JXMultiThumbSlider<Color> slider;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintComponent(g);
    }

    @Override
    protected void paintComponent(Graphics gfx) {
        Graphics2D g = (Graphics2D) gfx;

        // get the list of colors
        List<Thumb<Color>> stops = slider.getModel().getSortedThumbs();
        int len = stops.size();

        // set up the data for the gradient
        float[] fractions = new float[len];
        Color[] colors = new Color[len];
        int i = 0;
        for (Thumb<Color> thumb : stops) {
            colors[i] = thumb.getObject();
            fractions[i] = thumb.getPosition();
            i++;
        }

        // calculate the track area
        int thumbWidth = 12;
        int trackWidth = slider.getWidth() - thumbWidth;
        g.translate(thumbWidth / 2, 12);
        Rectangle2D rect = new Rectangle(0, 0, trackWidth, 20);

        // fill in the checker
        g.setPaint(checkerPaint);
        g.fill(rect);

        // fill in the gradient
        Point2D start = new Point2D.Float(0, 0);
        Point2D end = new Point2D.Float(trackWidth, 0);
        MultipleGradientPaint paint = new LinearGradientPaint(
            (float) start.getX(), (float) start.getY(), (float) end.getX(), (float) end.getY(), fractions, colors
        );
        g.setPaint(paint);
        g.fill(rect);

        // draw a border
        g.setColor(Color.black);
        g.draw(rect);
        g.translate(-thumbWidth / 2, -12);
    }

    public JComponent getRendererComponent(JXMultiThumbSlider<Color> slider) {
        this.slider = slider;
        return this;
    }
}
