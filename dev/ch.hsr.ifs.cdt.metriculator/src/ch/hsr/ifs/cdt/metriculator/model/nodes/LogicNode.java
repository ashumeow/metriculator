/******************************************************************************
* Copyright (c) 2011 Institute for Software, HSR Hochschule fuer Technik 
* Rapperswil, University of applied sciences and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html 
*
* Contributors:
* 	Ueli Kunz <kunz@ideadapt.net>, Jules Weder <julesweder@gmail.com> - initial API and implementation
******************************************************************************/

package ch.hsr.ifs.cdt.metriculator.model.nodes;

import org.eclipse.cdt.core.dom.ast.IASTNode;


public abstract class LogicNode extends AbstractNode {

	public final static String ANONYMOUS_LABEL = "(anonymous)"; //$NON-NLS-1$
	protected boolean isFriend = false;
	
	protected LogicNode(String scopeUniqueName) {
		super(scopeUniqueName);
	}
	
	protected LogicNode(String scopeUniqueName, IASTNode astNode) {
		super(scopeUniqueName, astNode);
		prepareHashCode(astNode);
	}

	public boolean isAnonymous(){
		return false;
	}
	
	@Override
	protected void prepareHashCode(IASTNode astNode) {
		astNodeHashCode = Integer.valueOf(astNode.hashCode()).toString();
	}

	@Override
	public String getAstNodeHashCode() {
		return astNodeHashCode;
	}
}
