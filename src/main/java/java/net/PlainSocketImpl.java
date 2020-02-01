/*
 * Copyright (c) 2007, 2013, Oracle and/or its affiliates. All rights reserved.
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
package java.net;

import java.io.*;
import java.security.PrivilegedAction;

/*
 * This class PlainSocketImpl simply delegates to the appropriate real
 * SocketImpl. We do this because PlainSocketImpl is already extended
 * by SocksSocketImpl.
 * <p>
 * There are two possibilities for the real SocketImpl,
 * TwoStacksPlainSocketImpl or DualStackPlainSocketImpl. We use
 * DualStackPlainSocketImpl on systems that have a dual stack
 * TCP implementation. Otherwise we create an instance of
 * TwoStacksPlainSocketImpl and delegate to it.
 *
 * @author Chris Hegarty
 */

class PlainSocketImpl extends AbstractPlainSocketImpl
{
    private AbstractPlainSocketImpl impl;

    /* the windows version. */
    private static float version;

    /* java.net.preferIPv4Stack */
    private static boolean preferIPv4Stack = false;

    /* If the version supports a dual stack TCP implementation */
    private static boolean useDualStackImpl = false;

    /* sun.net.useExclusiveBind */
    private static String exclBindProp;

    /* True if exclusive binding is on for Windows */
    private static boolean exclusiveBind = true;

    static {
        java.security.AccessController.doPrivileged( new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    version = 0;
                    try {
                        version = Float.parseFloat(System.getProperties().getProperty("os.version"));
                        preferIPv4Stack = Boolean.parseBoolean(
                                          System.getProperties().getProperty("java.net.preferIPv4Stack"));
                        exclBindProp = System.getProperty("sun.net.useExclusiveBind");
                    } catch (NumberFormatException e ) {
                        assert false : e;
                    }
                    return null; // nothing to return
                } });

        // (version >= 6.0) implies Vista or greater.
        if (version >= 6.0 && !preferIPv4Stack) {
                useDualStackImpl = true;
        }

        if (exclBindProp != null) {
            // sun.net.useExclusiveBind is true
            exclusiveBind = exclBindProp.length() == 0 ? true
                    : Boolean.parseBoolean(exclBindProp);
        } else if (version < 6.0) {
            exclusiveBind = false;
        }
    }

    /**
     * Constructs an empty instance.
     */
    PlainSocketImpl() {
        if (useDualStackImpl) {
            impl = new DualStackPlainSocketImpl(exclusiveBind);
        } else {
            impl = new TwoStacksPlainSocketImpl(exclusiveBind);
        }
    }

    /**
     * Constructs an instance with the given file descriptor.
     */
    PlainSocketImpl(FileDescriptor fd) {
        if (useDualStackImpl) {
            impl = new DualStackPlainSocketImpl(fd, exclusiveBind);
        } else {
            impl = new TwoStacksPlainSocketImpl(fd, exclusiveBind);
        }
    }

    // Override methods in SocketImpl that access impl's fields.

    @Override
    protected FileDescriptor getFileDescriptor() {
        return impl.getFileDescriptor();
    }

    @Override
    protected InetAddress getInetAddress() {
        return impl.getInetAddress();
    }

    @Override
    protected int getPort() {
        return impl.getPort();
    }

    @Override
    protected int getLocalPort() {
        return impl.getLocalPort();
    }

    @Override
    void setSocket(Socket soc) {
        impl.setSocket(soc);
    }

    @Override
    Socket getSocket() {
        return impl.getSocket();
    }

    @Override
    void setServerSocket(ServerSocket soc) {
        impl.setServerSocket(soc);
    }

    @Override
    ServerSocket getServerSocket() {
        return impl.getServerSocket();
    }

    @Override
    public String toString() {
        return impl.toString();
    }

    // Override methods in AbstractPlainSocketImpl that access impl's fields.

    @Override
    protected synchronized void create(boolean stream) throws IOException {
        impl.create(stream);

        // set fd to delegate's fd to be compatible with older releases
        this.fd = impl.fd;
    }

    @Override
    protected void connect(String host, int port)
        throws UnknownHostException, IOException
    {
        impl.connect(host, port);
    }

    @Override
    protected void connect(InetAddress address, int port) throws IOException {
        impl.connect(address, port);
    }

    @Override
    protected void connect(SocketAddress address, int timeout) throws IOException {
        impl.connect(address, timeout);
    }

    @Override
    public void setOption(int opt, Object val) throws SocketException {
        impl.setOption(opt, val);
    }

    @Override
    public Object getOption(int opt) throws SocketException {
        return impl.getOption(opt);
    }

    @Override
    synchronized void doConnect(InetAddress address, int port, int timeout) throws IOException {
        impl.doConnect(address, port, timeout);
    }

    @Override
    protected synchronized void bind(InetAddress address, int lport)
        throws IOException
    {
        impl.bind(address, lport);
    }

    @Override
    protected synchronized void accept(SocketImpl s) throws IOException {
        if (s instanceof PlainSocketImpl) {
            // pass in the real impl not the wrapper.
            SocketImpl delegate = ((PlainSocketImpl)s).impl;
            delegate.address = new InetAddress();
            delegate.fd = new FileDescriptor();
            impl.accept(delegate);
            // set fd to delegate's fd to be compatible with older releases
            s.fd = delegate.fd;
        } else {
            impl.accept(s);
        }
    }

    @Override
    void setFileDescriptor(FileDescriptor fd) {
        impl.setFileDescriptor(fd);
    }

    @Override
    void setAddress(InetAddress address) {
        impl.setAddress(address);
    }

    @Override
    void setPort(int port) {
        impl.setPort(port);
    }

    @Override
    void setLocalPort(int localPort) {
        impl.setLocalPort(localPort);
    }

    @Override
    protected synchronized InputStream getInputStream() throws IOException {
        return impl.getInputStream();
    }

    @Override
    void setInputStream(SocketInputStream in) {
        impl.setInputStream(in);
    }

    @Override
    protected synchronized OutputStream getOutputStream() throws IOException {
        return impl.getOutputStream();
    }

    @Override
    protected void close() throws IOException {
        try {
            impl.close();
        } finally {
            // set fd to delegate's fd to be compatible with older releases
            this.fd = null;
        }
    }

    @Override
    void reset() throws IOException {
        try {
            impl.reset();
        } finally {
            // set fd to delegate's fd to be compatible with older releases
            this.fd = null;
        }
    }

    @Override
    protected void shutdownInput() throws IOException {
        impl.shutdownInput();
    }

    @Override
    protected void shutdownOutput() throws IOException {
        impl.shutdownOutput();
    }

    @Override
    protected void sendUrgentData(int data) throws IOException {
        impl.sendUrgentData(data);
    }

    @Override
    FileDescriptor acquireFD() {
        return impl.acquireFD();
    }

    @Override
    void releaseFD() {
        impl.releaseFD();
    }

    @Override
    public boolean isConnectionReset() {
        return impl.isConnectionReset();
    }

    @Override
    public boolean isConnectionResetPending() {
        return impl.isConnectionResetPending();
    }

    @Override
    public void setConnectionReset() {
        impl.setConnectionReset();
    }

    @Override
    public void setConnectionResetPending() {
        impl.setConnectionResetPending();
    }

    @Override
    public boolean isClosedOrPending() {
        return impl.isClosedOrPending();
    }

    @Override
    public int getTimeout() {
        return impl.getTimeout();
    }

    // Override methods in AbstractPlainSocketImpl that need to be implemented.

    @Override
    void socketCreate(boolean isServer) throws IOException {
        impl.socketCreate(isServer);
    }

    @Override
    void socketConnect(InetAddress address, int port, int timeout)
        throws IOException {
        impl.socketConnect(address, port, timeout);
    }

    @Override
    void socketBind(InetAddress address, int port)
        throws IOException {
        impl.socketBind(address, port);
    }

    @Override
    void socketListen(int count) throws IOException {
        impl.socketListen(count);
    }

    @Override
    void socketAccept(SocketImpl s) throws IOException {
        impl.socketAccept(s);
    }

    @Override
    int socketAvailable() throws IOException {
        return impl.socketAvailable();
    }

    @Override
    void socketClose0(boolean useDeferredClose) throws IOException {
        impl.socketClose0(useDeferredClose);
    }

    @Override
    void socketShutdown(int howto) throws IOException {
        impl.socketShutdown(howto);
    }

    @Override
    void socketSetOption(int cmd, boolean on, Object value)
        throws SocketException {
        impl.socketSetOption(cmd, on, value);
    }

    @Override
    int socketGetOption(int opt, Object iaContainerObj) throws SocketException {
        return impl.socketGetOption(opt, iaContainerObj);
    }

    @Override
    void socketSendUrgentData(int data) throws IOException {
        impl.socketSendUrgentData(data);
    }
}
