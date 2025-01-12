/*
 * Created on Apr 5, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.jbrain.hayes;

import org.jbrain.hayes.cmd.DialCommand;

/**
 * @author jbrain
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface LinePortFactory {

	LinePort createLinePort(DialCommand cmd)  throws LineNotAnsweringException, LineBusyException, PortException ;
}
