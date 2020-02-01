/*
 * Copyright (c) 2002, 2008, Oracle and/or its affiliates. All rights reserved.
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
package javax.swing.plaf.synth;

import java.awt.*;
import java.lang.ref.WeakReference;
import java.net.*;
import javax.swing.*;
import sun.awt.AppContext;
import sun.swing.plaf.synth.Paint9Painter;

/**
 * ImagePainter fills in the specified region using an Image. The Image
 * is split into 9 segments: north, north east, east, south east, south,
 * south west, west, north west and the center. The corners are defined
 * by way of an insets, and the remaining regions are either tiled or
 * scaled to fit.
 *
 * @author Scott Violet
 */
class ImagePainter extends SynthPainter {
    private static final StringBuffer CACHE_KEY =
                               new StringBuffer("SynthCacheKey");

    private Image image;
    private Insets sInsets;
    private Insets dInsets;
    private URL path;
    private boolean tiles;
    private boolean paintCenter;
    private Paint9Painter imageCache;
    private boolean center;

    private static Paint9Painter getPaint9Painter() {
        // A SynthPainter is created per <imagePainter>.  We want the
        // cache to be shared by all, and we don't use a static because we
        // don't want it to persist between look and feels.  For that reason
        // we use a AppContext specific Paint9Painter.  It's backed via
        // a WeakRef so that it can go away if the look and feel changes.
        synchronized(CACHE_KEY) {
            WeakReference<Paint9Painter> cacheRef =
                     (WeakReference<Paint9Painter>)AppContext.getAppContext().
                     get(CACHE_KEY);
            Paint9Painter painter;
            if (cacheRef == null || (painter = cacheRef.get()) == null) {
                painter = new Paint9Painter(30);
                cacheRef = new WeakReference<Paint9Painter>(painter);
                AppContext.getAppContext().put(CACHE_KEY, cacheRef);
            }
            return painter;
        }
    }

    ImagePainter(boolean tiles, boolean paintCenter,
                 Insets sourceInsets, Insets destinationInsets, URL path,
                 boolean center) {
        if (sourceInsets != null) {
            this.sInsets = (Insets)sourceInsets.clone();
        }
        if (destinationInsets == null) {
            dInsets = sInsets;
        }
        else {
            this.dInsets = (Insets)destinationInsets.clone();
        }
        this.tiles = tiles;
        this.paintCenter = paintCenter;
        this.imageCache = getPaint9Painter();
        this.path = path;
        this.center = center;
    }

    public boolean getTiles() {
        return tiles;
    }

    public boolean getPaintsCenter() {
        return paintCenter;
    }

    public boolean getCenter() {
        return center;
    }

    public Insets getInsets(Insets insets) {
        if (insets == null) {
            return (Insets)this.dInsets.clone();
        }
        insets.left = this.dInsets.left;
        insets.right = this.dInsets.right;
        insets.top = this.dInsets.top;
        insets.bottom = this.dInsets.bottom;
        return insets;
    }

    public Image getImage() {
        if (image == null) {
            image = new ImageIcon(path, null).getImage();
        }
        return image;
    }

    private void paint(SynthContext context, Graphics g, int x, int y, int w,
                       int h) {
        Image image = getImage();
        if (Paint9Painter.validImage(image)) {
            Paint9Painter.PaintType type;
            if (getCenter()) {
                type = Paint9Painter.PaintType.CENTER;
            }
            else if (!getTiles()) {
                type = Paint9Painter.PaintType.PAINT9_STRETCH;
            }
            else {
                type = Paint9Painter.PaintType.PAINT9_TILE;
            }
            int mask = Paint9Painter.PAINT_ALL;
            if (!getCenter() && !getPaintsCenter()) {
                mask |= Paint9Painter.PAINT_CENTER;
            }
            imageCache.paint(context.getComponent(), g, x, y, w, h,
                             image, sInsets, dInsets, type,
                             mask);
        }
    }


    // SynthPainter
    @Override
    public void paintArrowButtonBackground(SynthContext context,
                                           Graphics g, int x, int y,
                                           int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintArrowButtonBorder(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintArrowButtonForeground(SynthContext context,
                                           Graphics g, int x, int y,
                                           int w, int h,
                                           int direction) {
        paint(context, g, x, y, w, h);
    }

    // BUTTON
    @Override
    public void paintButtonBackground(SynthContext context,
                                      Graphics g, int x, int y,
                                      int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintButtonBorder(SynthContext context,
                                  Graphics g, int x, int y,
                                  int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // CHECK_BOX_MENU_ITEM
    @Override
    public void paintCheckBoxMenuItemBackground(SynthContext context,
                                                Graphics g, int x, int y,
                                                int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintCheckBoxMenuItemBorder(SynthContext context,
                                            Graphics g, int x, int y,
                                            int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // CHECK_BOX
    @Override
    public void paintCheckBoxBackground(SynthContext context,
                                        Graphics g, int x, int y,
                                        int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintCheckBoxBorder(SynthContext context,
                                    Graphics g, int x, int y,
                                    int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // COLOR_CHOOSER
    @Override
    public void paintColorChooserBackground(SynthContext context,
                                            Graphics g, int x, int y,
                                            int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintColorChooserBorder(SynthContext context,
                                        Graphics g, int x, int y,
                                        int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // COMBO_BOX
    @Override
    public void paintComboBoxBackground(SynthContext context,
                                        Graphics g, int x, int y,
                                        int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintComboBoxBorder(SynthContext context,
                                    Graphics g, int x, int y,
                                    int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // DESKTOP_ICON
    @Override
    public void paintDesktopIconBackground(SynthContext context,
                                           Graphics g, int x, int y,
                                           int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintDesktopIconBorder(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // DESKTOP_PANE
    @Override
    public void paintDesktopPaneBackground(SynthContext context,
                                           Graphics g, int x, int y,
                                           int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintDesktopPaneBorder(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // EDITOR_PANE
    @Override
    public void paintEditorPaneBackground(SynthContext context,
                                          Graphics g, int x, int y,
                                          int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintEditorPaneBorder(SynthContext context,
                                      Graphics g, int x, int y,
                                      int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // FILE_CHOOSER
    @Override
    public void paintFileChooserBackground(SynthContext context,
                                           Graphics g, int x, int y,
                                           int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintFileChooserBorder(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // FORMATTED_TEXT_FIELD
    @Override
    public void paintFormattedTextFieldBackground(SynthContext context,
                                                  Graphics g, int x, int y,
                                                  int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintFormattedTextFieldBorder(SynthContext context,
                                              Graphics g, int x, int y,
                                              int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // INTERNAL_FRAME_TITLE_PANE
    @Override
    public void paintInternalFrameTitlePaneBackground(SynthContext context,
                                                      Graphics g, int x, int y,
                                                      int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintInternalFrameTitlePaneBorder(SynthContext context,
                                                  Graphics g, int x, int y,
                                                  int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // INTERNAL_FRAME
    @Override
    public void paintInternalFrameBackground(SynthContext context,
                                             Graphics g, int x, int y,
                                             int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintInternalFrameBorder(SynthContext context,
                                         Graphics g, int x, int y,
                                         int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // LABEL
    @Override
    public void paintLabelBackground(SynthContext context,
                                     Graphics g, int x, int y,
                                     int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintLabelBorder(SynthContext context,
                                 Graphics g, int x, int y,
                                 int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // LIST
    @Override
    public void paintListBackground(SynthContext context,
                                    Graphics g, int x, int y,
                                    int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintListBorder(SynthContext context,
                                Graphics g, int x, int y,
                                int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // MENU_BAR
    @Override
    public void paintMenuBarBackground(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintMenuBarBorder(SynthContext context,
                                   Graphics g, int x, int y,
                                   int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // MENU_ITEM
    @Override
    public void paintMenuItemBackground(SynthContext context,
                                        Graphics g, int x, int y,
                                        int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintMenuItemBorder(SynthContext context,
                                    Graphics g, int x, int y,
                                    int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // MENU
    @Override
    public void paintMenuBackground(SynthContext context,
                                    Graphics g, int x, int y,
                                    int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintMenuBorder(SynthContext context,
                                Graphics g, int x, int y,
                                int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // OPTION_PANE
    @Override
    public void paintOptionPaneBackground(SynthContext context,
                                          Graphics g, int x, int y,
                                          int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintOptionPaneBorder(SynthContext context,
                                      Graphics g, int x, int y,
                                      int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // PANEL
    @Override
    public void paintPanelBackground(SynthContext context,
                                     Graphics g, int x, int y,
                                     int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintPanelBorder(SynthContext context,
                                 Graphics g, int x, int y,
                                 int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // PANEL
    @Override
    public void paintPasswordFieldBackground(SynthContext context,
                                             Graphics g, int x, int y,
                                             int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintPasswordFieldBorder(SynthContext context,
                                         Graphics g, int x, int y,
                                         int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // POPUP_MENU
    @Override
    public void paintPopupMenuBackground(SynthContext context,
                                         Graphics g, int x, int y,
                                         int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintPopupMenuBorder(SynthContext context,
                                     Graphics g, int x, int y,
                                     int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // PROGRESS_BAR
    @Override
    public void paintProgressBarBackground(SynthContext context,
                                           Graphics g, int x, int y,
                                           int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintProgressBarBackground(SynthContext context,
                                           Graphics g, int x, int y,
                                           int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintProgressBarBorder(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintProgressBarBorder(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintProgressBarForeground(SynthContext context,
                                           Graphics g, int x, int y,
                                           int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    // RADIO_BUTTON_MENU_ITEM
    @Override
    public void paintRadioButtonMenuItemBackground(SynthContext context,
                                                   Graphics g, int x, int y,
                                                   int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintRadioButtonMenuItemBorder(SynthContext context,
                                               Graphics g, int x, int y,
                                               int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // RADIO_BUTTON
    @Override
    public void paintRadioButtonBackground(SynthContext context,
                                           Graphics g, int x, int y,
                                           int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintRadioButtonBorder(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // ROOT_PANE
    @Override
    public void paintRootPaneBackground(SynthContext context,
                                        Graphics g, int x, int y,
                                        int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintRootPaneBorder(SynthContext context,
                                    Graphics g, int x, int y,
                                    int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // SCROLL_BAR
    @Override
    public void paintScrollBarBackground(SynthContext context,
                                         Graphics g, int x, int y,
                                         int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintScrollBarBackground(SynthContext context,
                                         Graphics g, int x, int y,
                                         int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintScrollBarBorder(SynthContext context,
                                     Graphics g, int x, int y,
                                     int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintScrollBarBorder(SynthContext context,
                                     Graphics g, int x, int y,
                                     int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    // SCROLL_BAR_THUMB
    @Override
    public void paintScrollBarThumbBackground(SynthContext context,
                                              Graphics g, int x, int y,
                                              int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintScrollBarThumbBorder(SynthContext context,
                                          Graphics g, int x, int y,
                                          int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    // SCROLL_BAR_TRACK
    @Override
    public void paintScrollBarTrackBackground(SynthContext context,
                                              Graphics g, int x, int y,
                                              int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintScrollBarTrackBackground(SynthContext context,
                                              Graphics g, int x, int y,
                                              int w, int h, int orientation) {
         paint(context, g, x, y, w, h);
     }

    @Override
    public void paintScrollBarTrackBorder(SynthContext context,
                                          Graphics g, int x, int y,
                                          int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintScrollBarTrackBorder(SynthContext context,
                                          Graphics g, int x, int y,
                                          int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    // SCROLL_PANE
    @Override
    public void paintScrollPaneBackground(SynthContext context,
                                          Graphics g, int x, int y,
                                          int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintScrollPaneBorder(SynthContext context,
                                      Graphics g, int x, int y,
                                      int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // SEPARATOR
    @Override
    public void paintSeparatorBackground(SynthContext context,
                                         Graphics g, int x, int y,
                                         int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintSeparatorBackground(SynthContext context,
                                         Graphics g, int x, int y,
                                         int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintSeparatorBorder(SynthContext context,
                                     Graphics g, int x, int y,
                                     int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintSeparatorBorder(SynthContext context,
                                     Graphics g, int x, int y,
                                     int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintSeparatorForeground(SynthContext context,
                                         Graphics g, int x, int y,
                                         int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    // SLIDER
    @Override
    public void paintSliderBackground(SynthContext context,
                                      Graphics g, int x, int y,
                                      int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintSliderBackground(SynthContext context,
                                      Graphics g, int x, int y,
                                      int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintSliderBorder(SynthContext context,
                                  Graphics g, int x, int y,
                                  int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintSliderBorder(SynthContext context,
                                  Graphics g, int x, int y,
                                  int w, int h, int orientation) {
         paint(context, g, x, y, w, h);
     }

    // SLIDER_THUMB
    @Override
    public void paintSliderThumbBackground(SynthContext context,
                                           Graphics g, int x, int y,
                                           int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintSliderThumbBorder(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    // SLIDER_TRACK
    @Override
    public void paintSliderTrackBackground(SynthContext context,
                                           Graphics g, int x, int y,
                                           int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintSliderTrackBackground(SynthContext context,
                                           Graphics g, int x, int y,
                                           int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintSliderTrackBorder(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h) {
        paint(context, g, x, y, w, h);
    }


    @Override
    public void paintSliderTrackBorder(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    // SPINNER
    @Override
    public void paintSpinnerBackground(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintSpinnerBorder(SynthContext context,
                                   Graphics g, int x, int y,
                                   int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // SPLIT_PANE_DIVIDER
    @Override
    public void paintSplitPaneDividerBackground(SynthContext context,
                                                Graphics g, int x, int y,
                                                int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintSplitPaneDividerBackground(SynthContext context,
                                                Graphics g, int x, int y,
                                                int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintSplitPaneDividerForeground(SynthContext context,
                                                Graphics g, int x, int y,
                                                int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintSplitPaneDragDivider(SynthContext context,
                                          Graphics g, int x, int y,
                                          int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    // SPLIT_PANE
    @Override
    public void paintSplitPaneBackground(SynthContext context,
                                         Graphics g, int x, int y,
                                         int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintSplitPaneBorder(SynthContext context,
                                     Graphics g, int x, int y,
                                     int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // TABBED_PANE
    @Override
    public void paintTabbedPaneBackground(SynthContext context,
                                          Graphics g, int x, int y,
                                          int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTabbedPaneBorder(SynthContext context,
                                      Graphics g, int x, int y,
                                      int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // TABBED_PANE_TAB_AREA
    @Override
    public void paintTabbedPaneTabAreaBackground(SynthContext context,
                                                 Graphics g, int x, int y,
                                                 int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTabbedPaneTabAreaBackground(SynthContext context,
                                                 Graphics g, int x, int y,
                                                 int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTabbedPaneTabAreaBorder(SynthContext context,
                                             Graphics g, int x, int y,
                                             int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTabbedPaneTabAreaBorder(SynthContext context,
                                             Graphics g, int x, int y,
                                             int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    // TABBED_PANE_TAB
    @Override
    public void paintTabbedPaneTabBackground(SynthContext context, Graphics g,
                                             int x, int y, int w, int h,
                                             int tabIndex) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTabbedPaneTabBackground(SynthContext context, Graphics g,
                                             int x, int y, int w, int h,
                                             int tabIndex, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTabbedPaneTabBorder(SynthContext context, Graphics g,
                                         int x, int y, int w, int h,
                                         int tabIndex) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTabbedPaneTabBorder(SynthContext context, Graphics g,
                                         int x, int y, int w, int h,
                                         int tabIndex, int orientation) {
        paint(context, g, x, y, w, h);
    }

    // TABBED_PANE_CONTENT
    @Override
    public void paintTabbedPaneContentBackground(SynthContext context,
                                                 Graphics g, int x, int y, int w,
                                                 int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTabbedPaneContentBorder(SynthContext context, Graphics g,
                                             int x, int y, int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // TABLE_HEADER
    @Override
    public void paintTableHeaderBackground(SynthContext context,
                                           Graphics g, int x, int y,
                                           int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTableHeaderBorder(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // TABLE
    @Override
    public void paintTableBackground(SynthContext context,
                                     Graphics g, int x, int y,
                                     int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTableBorder(SynthContext context,
                                 Graphics g, int x, int y,
                                 int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // TEXT_AREA
    @Override
    public void paintTextAreaBackground(SynthContext context,
                                        Graphics g, int x, int y,
                                        int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTextAreaBorder(SynthContext context,
                                    Graphics g, int x, int y,
                                    int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // TEXT_PANE
    @Override
    public void paintTextPaneBackground(SynthContext context,
                                        Graphics g, int x, int y,
                                        int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTextPaneBorder(SynthContext context,
                                    Graphics g, int x, int y,
                                    int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // TEXT_FIELD
    @Override
    public void paintTextFieldBackground(SynthContext context,
                                         Graphics g, int x, int y,
                                         int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTextFieldBorder(SynthContext context,
                                     Graphics g, int x, int y,
                                     int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // TOGGLE_BUTTON
    @Override
    public void paintToggleButtonBackground(SynthContext context,
                                            Graphics g, int x, int y,
                                            int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintToggleButtonBorder(SynthContext context,
                                        Graphics g, int x, int y,
                                        int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // TOOL_BAR
    @Override
    public void paintToolBarBackground(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintToolBarBackground(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintToolBarBorder(SynthContext context,
                                   Graphics g, int x, int y,
                                   int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintToolBarBorder(SynthContext context,
                                   Graphics g, int x, int y,
                                   int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    // TOOL_BAR_CONTENT
    @Override
    public void paintToolBarContentBackground(SynthContext context,
                                              Graphics g, int x, int y,
                                              int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintToolBarContentBackground(SynthContext context,
                                              Graphics g, int x, int y,
                                              int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintToolBarContentBorder(SynthContext context,
                                          Graphics g, int x, int y,
                                          int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintToolBarContentBorder(SynthContext context,
                                          Graphics g, int x, int y,
                                          int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    // TOOL_DRAG_WINDOW
    @Override
    public void paintToolBarDragWindowBackground(SynthContext context,
                                                 Graphics g, int x, int y,
                                                 int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintToolBarDragWindowBackground(SynthContext context,
                                                 Graphics g, int x, int y,
                                                 int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintToolBarDragWindowBorder(SynthContext context,
                                             Graphics g, int x, int y,
                                             int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintToolBarDragWindowBorder(SynthContext context,
                                             Graphics g, int x, int y,
                                             int w, int h, int orientation) {
        paint(context, g, x, y, w, h);
    }

    // TOOL_TIP
    @Override
    public void paintToolTipBackground(SynthContext context,
                                       Graphics g, int x, int y,
                                       int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintToolTipBorder(SynthContext context,
                                   Graphics g, int x, int y,
                                   int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // TREE
    @Override
    public void paintTreeBackground(SynthContext context,
                                    Graphics g, int x, int y,
                                    int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTreeBorder(SynthContext context,
                                Graphics g, int x, int y,
                                int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // TREE_CELL
    @Override
    public void paintTreeCellBackground(SynthContext context,
                                        Graphics g, int x, int y,
                                        int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTreeCellBorder(SynthContext context,
                                    Graphics g, int x, int y,
                                    int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintTreeCellFocus(SynthContext context,
                                   Graphics g, int x, int y,
                                   int w, int h) {
        paint(context, g, x, y, w, h);
    }

    // VIEWPORT
    @Override
    public void paintViewportBackground(SynthContext context,
                                        Graphics g, int x, int y,
                                        int w, int h) {
        paint(context, g, x, y, w, h);
    }

    @Override
    public void paintViewportBorder(SynthContext context,
                                    Graphics g, int x, int y,
                                    int w, int h) {
        paint(context, g, x, y, w, h);
    }
}
