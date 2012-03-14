package ch.hsr.ifs.cdt.metriculator.model.nodes;

import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.ICompositeType;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTNamespaceDefinition;
import org.eclipse.cdt.core.index.IIndex;

import ch.hsr.ifs.cdt.metriculator.model.TreeBuilder;

public abstract class MemberNode extends LogicNode {

	protected int typeKey;
	protected boolean isMember = false;
	protected String logicalName = "";
	protected String logicalOwnerName = "";
	protected IBinding binding;
	protected IBinding indexBinding;
	
	public MemberNode(String scopeUniqueName) {
		super(scopeUniqueName);
	}
	
	protected MemberNode(String scopeUniqueName, IASTNode astNode) {
		super(scopeUniqueName, astNode);
		isMember = false;
		if(prepareBinding(astNode)){
			prepareOwnership(binding.getOwner(), astNode.getTranslationUnit());
		}
		prepareIsFriend(astNode);		
	}
	
	abstract void prepareIsFriend(IASTNode astNode);

	abstract boolean prepareBinding(IASTNode astNode);
	
	public int getTypeKey() {
		return typeKey;
	}
	
	public IBinding getBinding() {
		return binding;
	}
	
	public IBinding getIndexBinding() {
		return indexBinding;
	}
	
	public void clearBindings() {
		binding = null;
		indexBinding = null;
	}
	
	public boolean isMember() {
		return isMember;
	}
	
	public String getLogicalOwnerName() {
		return logicalOwnerName;
	}

	public String getLogicalName() {
		return logicalName;
	}

	protected void prepareOwnership(IBinding owner, IASTTranslationUnit tu) {
		logicalName = binding.toString();
		if(owner != null){
			logicalOwnerName = buildLogicalOwnerName(binding.getOwner(), tu);
			if(owner instanceof ICompositeType){
				isMember = true;
				binding = null;
			}
		}
	}

	protected String buildLogicalOwnerName(IBinding owner, IASTTranslationUnit tu) {

		IASTNode node = null;

		if(tu.getDeclarationsInAST(owner).length > 0){
			node = tu.getDeclarationsInAST(owner)[0].getParent();
			if(owner.getOwner() == null){
				if(node instanceof ICPPASTNamespaceDefinition && ((ICPPASTNamespaceDefinition) node).getName().toString().isEmpty()){
					return owner.getName().toString() + node.hashCode();
				}else{
					return owner.getName().toString();
				}
			}
		}

		return buildLogicalOwnerName(owner.getOwner(), tu) + TreeBuilder.PATH_SEPARATOR + owner.getName() + ((node instanceof ICPPASTNamespaceDefinition && ((ICPPASTNamespaceDefinition) node).getName().toString().isEmpty()) ? node.hashCode() : "");
	}

	public static IBinding getBindingFor(IASTName name, IASTTranslationUnit tu) {
		IBinding typeBinding, indexBinding;

		typeBinding  = name.resolveBinding();
		IIndex index = tu.getIndex();
		indexBinding = index.adaptBinding(typeBinding);

		return indexBinding == null ? typeBinding : indexBinding;
	}	
}
