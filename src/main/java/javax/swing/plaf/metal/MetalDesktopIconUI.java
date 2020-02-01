/*
 * Copyright (c) 1998, 2003, Oracle and/or its affiliates. All rights reserved.
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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import java.beans.*;
import java.util.EventListener;
import java.io.Serializable;
import javax.swing.plaf.basic.BasicDesktopIconUI;

/**
 * Metal desktop icon.
 *
 * @author Steve Wilson
 */
public class MetalDesktopIconUI extends BasicDesktopIconUI
{

    JButton button;
    JLabel label;
    TitleListener titleListener;
    private int width;

    public static ComponentUI createUI(JComponent c)    {
        return new MetalDesktopIconUI();
    }

    public MetalDesktopIconUI() {
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        LookAndFeel.installColorsAndFont(desktopIcon, "DesktopIcon.background", "DesktopIcon.foreground", "DesktopIcon.font");
        width = UIManager.getInt("DesktopIcon.width");
    }

    @Override
    protected void installComponents() {
        frame = desktopIcon.getInternalFrame();
        Icon icon = frame.getFrameIcon();
        String title = frame.getTitle();

        button = new JButton (title, icon);
        button.addActionListener( new ActionListener() {
                                  @Override
                                  public void actionPerformed(ActionEvent e) {
             deiconize(); }} );
        button.setFont(desktopIcon.getFont());
        button.setBackground(desktopIcon.getBackground());
        button.setForeground(desktopIcon.getForeground());

        int buttonH = button.getPreferredSize().height;

        Icon drag = new MetalBumps((buttonH/3), buttonH,
                                   MetalLookAndFeel.getControlHighlight(),
                                   MetalLookAndFeel.getControlDarkShadow(),
                                   MetalLookAndFeel.getControl());
        label = new JLabel(drag);

        label.setBorder( new MatteBorder( 0, 2, 0, 1, desktopIcon.getBackground()) );
        desktopIcon.setLayout(new BorderLayout(2, 0));
        desktopIcon.add(button, BorderLayout.CENTER);
        desktopIcon.add(label, BorderLayout.WEST);
    }

    @Override
    protected void uninstallComponents() {
        desktopIcon.setLayout(null);
        desktopIcon.remove(label);
        desktopIcon.remove(button);
        button = null;
        frame = null;
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        desktopIcon.getInternalFrame().addPropertyChangeListener(
                titleListener = new TitleListener());
    }

    @Override
    protected void uninstallListeners() {
        desktopIcon.getInternalFrame().removePropertyChangeListener(
                titleListener);
        titleListener = null;
        super.uninstallListeners();
    }


    @Override
    public Dimension getPreferredSize(JComponent c) {
        // Metal desktop icons can not be resized.  Their dimensions should
        // always be the minimum size.  See getMinimumSize(JComponent c).
        return getMinimumSize(c);
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {
        // For the metal desktop icon we will use the layout maanger to
        // determine the correct height of the component, but we want to keep
        // the width consistent according to the jlf spec.
        return new Dimension(width,
                desktopIcon.getLayout().minimumLayoutSize(desktopIcon).height);
    }

    @Override
    public Dimension getMaximumSize(JComponent c) {
        // Metal desktop icons can not be resized.  Their dimensions should
        // always be the minimum size.  See getMinimumSize(JComponent c).
        return getMinimumSize(c);
    }

    class TitleListener implements PropertyChangeListener {
        @Override
        public void propertyChange (PropertyChangeEvent e) {
          if (e.getPropertyName().equals("title")) {
            button.setText((String)e.getNewValue());
          }

          if (e.getPropertyName().equals("frameIcon")) {
            button.setIcon((Icon)e.getNewValue());
          }
        }
    }
}
