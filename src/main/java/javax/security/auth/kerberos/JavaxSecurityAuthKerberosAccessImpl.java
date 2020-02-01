/*
 * Copyright (c) 2011, 2019, Oracle and/or its affiliates. All rights reserved.
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

package javax.security.auth.kerberos;

import sun.security.krb5.JavaxSecurityAuthKerberosAccess;

class JavaxSecurityAuthKerberosAccessImpl
        implements JavaxSecurityAuthKerberosAccess {
    @Override
    public sun.security.krb5.internal.ktab.KeyTab keyTabTakeSnapshot(
            KeyTab ktab) {
        return ktab.takeSnapshot();
    }
    @Override
    public KerberosTicket kerberosTicketGetProxy(KerberosTicket t) {
        return t.proxy;
    }
    @Override
    public void kerberosTicketSetProxy(KerberosTicket t, KerberosTicket p) {
        t.proxy = p;
    }
}
