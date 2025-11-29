module io.github.osobolev.swingx {
    exports org.jdesktop.beans;
    exports org.jdesktop.swingx;
    exports org.jdesktop.swingx.action;
    exports org.jdesktop.swingx.auth;
    exports org.jdesktop.swingx.autocomplete;
    exports org.jdesktop.swingx.autocomplete.workarounds;
    exports org.jdesktop.swingx.border;
    exports org.jdesktop.swingx.calendar;
    exports org.jdesktop.swingx.color;
    exports org.jdesktop.swingx.combobox;
    exports org.jdesktop.swingx.decorator;
    exports org.jdesktop.swingx.error;
    exports org.jdesktop.swingx.event;
    exports org.jdesktop.swingx.geom;
    exports org.jdesktop.swingx.graphics;
    exports org.jdesktop.swingx.hyperlink;
    exports org.jdesktop.swingx.icon;
    exports org.jdesktop.swingx.image;
    exports org.jdesktop.swingx.multislider;
    exports org.jdesktop.swingx.multisplitpane;
    exports org.jdesktop.swingx.painter;
    exports org.jdesktop.swingx.painter.effects;
    exports org.jdesktop.swingx.plaf;
    exports org.jdesktop.swingx.plaf.basic;
    exports org.jdesktop.swingx.plaf.basic.core;
    exports org.jdesktop.swingx.plaf.linux;
    exports org.jdesktop.swingx.plaf.macosx;
    exports org.jdesktop.swingx.plaf.metal;
    exports org.jdesktop.swingx.plaf.misc;
    exports org.jdesktop.swingx.plaf.motif;
    exports org.jdesktop.swingx.plaf.nimbus;
    exports org.jdesktop.swingx.plaf.synth;
    exports org.jdesktop.swingx.plaf.windows;
    exports org.jdesktop.swingx.prompt;
    exports org.jdesktop.swingx.renderer;
    exports org.jdesktop.swingx.rollover;
    exports org.jdesktop.swingx.search;
    exports org.jdesktop.swingx.sort;
    exports org.jdesktop.swingx.table;
    exports org.jdesktop.swingx.text;
    exports org.jdesktop.swingx.tips;
    exports org.jdesktop.swingx.tree;
    exports org.jdesktop.swingx.treetable;
    exports org.jdesktop.swingx.util;

    opens org.jdesktop.swingx.plaf.basic.resources;
    opens org.jdesktop.swingx.plaf.linux.resources;
    opens org.jdesktop.swingx.plaf.macosx.resources;
    opens org.jdesktop.swingx.plaf.windows.resources;

    requires java.logging;
    requires java.prefs;
    requires transitive java.desktop;

    provides org.jdesktop.swingx.plaf.LookAndFeelAddons with
        org.jdesktop.swingx.plaf.linux.LinuxLookAndFeelAddons,
        org.jdesktop.swingx.plaf.macosx.MacOSXLookAndFeelAddons,
        org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons,
        org.jdesktop.swingx.plaf.motif.MotifLookAndFeelAddons,
        org.jdesktop.swingx.plaf.nimbus.NimbusLookAndFeelAddons,
        org.jdesktop.swingx.plaf.windows.WindowsClassicLookAndFeelAddons,
        org.jdesktop.swingx.plaf.windows.WindowsLookAndFeelAddons;
}
