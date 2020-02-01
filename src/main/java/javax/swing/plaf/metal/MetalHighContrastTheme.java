/*
 * Copyright (c) 2001, 2002, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package javax.swing.plaf.metal;

import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * A high contrast theme. This is used on Windows if the system property
 * awt.highContrast.on is true.
 *
 * @author Michael C. Albers
 */
class MetalHighContrastTheme extends DefaultMetalTheme {
    private static final ColorUIResource primary1 = new
                              ColorUIResource(0, 0, 0);
    private static final ColorUIResource primary2 = new ColorUIResource(
                              204, 204, 204);
    private static final ColorUIResource primary3 = new ColorUIResource(255,
                              255, 255);
    private static final ColorUIResource primaryHighlight = new
                              ColorUIResource(102, 102, 102);
    private static final ColorUIResource secondary2 = new ColorUIResource(
                              204, 204, 204);
    private static final ColorUIResource secondary3 = new ColorUIResource(
                              255, 255, 255);
    private static final ColorUIResource controlHighlight = new
                              ColorUIResource(102, 102, 102);


    // This does not override getSecondary1 (102,102,102)

    @Override
    public String getName() {
        return "Contrast";
    }

    @Override
    protected ColorUIResource getPrimary1() {
        return primary1;
    }

    @Override
    protected ColorUIResource getPrimary2() {
        return primary2;
    }

    @Override
    protected ColorUIResource getPrimary3() {
        return primary3;
    }

    @Override
    public ColorUIResource getPrimaryControlHighlight() {
        return primaryHighlight;
    }

    @Override
    protected ColorUIResource getSecondary2() {
        return secondary2;
    }

    @Override
    protected ColorUIResource getSecondary3() {
        return secondary3;
    }

    @Override
    public ColorUIResource getControlHighlight() {
        // This was super.getSecondary3();
        return secondary2;
    }

    @Override
    public ColorUIResource getFocusColor() {
        return getBlack();
    }

    @Override
    public ColorUIResource getTextHighlightColor() {
        return getBlack();
    }

    @Override
    public ColorUIResource getHighlightedTextColor() {
        return getWhite();
    }

    @Override
    public ColorUIResource getMenuSelectedBackground() {
        return getBlack();
    }

    @Override
    public ColorUIResource getMenuSelectedForeground() {
        return getWhite();
    }

    @Override
    public ColorUIResource getAcceleratorForeground() {
        return getBlack();
    }

    @Override
    public ColorUIResource getAcceleratorSelectedForeground() {
        return getWhite();
    }

    @Override
    public void addCustomEntriesToTable(UIDefaults table) {
        Border blackLineBorder = new BorderUIResource(new LineBorder(
                    getBlack()));
        Border whiteLineBorder = new BorderUIResource(new LineBorder(
                    getWhite()));
        Object textBorder = new BorderUIResource(new CompoundBorder(
                   blackLineBorder, new BasicBorders.MarginBorder()));

        Object[] defaults = new Object[] {
            "ToolTip.border", blackLineBorder,

            "TitledBorder.border", blackLineBorder,

            "TextField.border", textBorder,

            "PasswordField.border", textBorder,

            "TextArea.border", textBorder,

            "TextPane.border", textBorder,

            "EditorPane.border", textBorder,

            "ComboBox.background", getWindowBackground(),
            "ComboBox.foreground", getUserTextColor(),
            "ComboBox.selectionBackground", getTextHighlightColor(),
            "ComboBox.selectionForeground", getHighlightedTextColor(),

            "ProgressBar.foreground",  getUserTextColor(),
            "ProgressBar.background", getWindowBackground(),
            "ProgressBar.selectionForeground", getWindowBackground(),
            "ProgressBar.selectionBackground", getUserTextColor(),

            "OptionPane.errorDialog.border.background",
                        getPrimary1(),
            "OptionPane.errorDialog.titlePane.foreground",
                        getPrimary3(),
            "OptionPane.errorDialog.titlePane.background",
                        getPrimary1(),
            "OptionPane.errorDialog.titlePane.shadow",
                        getPrimary2(),
            "OptionPane.questionDialog.border.background",
                        getPrimary1(),
            "OptionPane.questionDialog.titlePane.foreground",
                        getPrimary3(),
            "OptionPane.questionDialog.titlePane.background",
                        getPrimary1(),
            "OptionPane.questionDialog.titlePane.shadow",
                        getPrimary2(),
            "OptionPane.warningDialog.border.background",
                        getPrimary1(),
            "OptionPane.warningDialog.titlePane.foreground",
                        getPrimary3(),
            "OptionPane.warningDialog.titlePane.background",
                        getPrimary1(),
            "OptionPane.warningDialog.titlePane.shadow",
                        getPrimary2(),
        };

        table.putDefaults(defaults);
    }

    /**
     * Returns true if this is a theme provided by the core platform.
     */
    @Override
    boolean isSystemTheme() {
        return (getClass() == MetalHighContrastTheme.class);
    }
}
