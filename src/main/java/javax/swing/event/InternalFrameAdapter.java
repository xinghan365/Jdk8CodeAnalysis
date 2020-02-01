/*
 * Copyright (c) 1998, 2013, Oracle and/or its affiliates. All rights reserved.
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

package javax.swing.event;

/**
 * An abstract adapter class for receiving internal frame events.
 * The methods in this class are empty. This class exists as
 * convenience for creating listener objects, and is functionally
 * equivalent to the WindowAdapter class in the AWT.
 * <p>
 * See <a href="https://docs.oracle.com/javase/tutorial/uiswing/events/internalframelistener.html">How to Write an Internal Frame Listener</a>
 * in <em>The Java Tutorial</em>
 *
 * @see InternalFrameEvent
 * @see InternalFrameListener
 * @see java.awt.event.WindowListener
 *
 * @author Thomas Ball
 */
public abstract class InternalFrameAdapter implements InternalFrameListener {
    /**
     * Invoked when an internal frame has been opened.
     */
    @Override
    public void internalFrameOpened(InternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is in the process of being closed.
     * The close operation can be overridden at this point.
     */
    @Override
    public void internalFrameClosing(InternalFrameEvent e) {}

    /**
     * Invoked when an internal frame has been closed.
     */
    @Override
    public void internalFrameClosed(InternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is iconified.
     */
    @Override
    public void internalFrameIconified(InternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is de-iconified.
     */
    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is activated.
     */
    @Override
    public void internalFrameActivated(InternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is de-activated.
     */
    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {}
}
