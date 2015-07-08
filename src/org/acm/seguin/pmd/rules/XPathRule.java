/*
 * Created on 15/03/2003
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code Template
 */
package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.parser.ast.ASTVariableDeclaratorId;
import org.acm.seguin.pmd.jaxen.DocumentNavigator;
import org.jaxen.BaseXPath;
import org.jaxen.JaxenException;
import org.jaxen.XPath;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Iterator;

/**
 * @author daniels
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code Template
 */
public class XPathRule extends AbstractRule {

    private XPath xpath;

    public Object visit(ASTCompilationUnit node, Object data) {
        try {
            init();
            for (Iterator iter = xpath.selectNodes(node).iterator(); iter.hasNext();) {
                SimpleNode actualNode = (SimpleNode) iter.next();
                //System.out.println("actualNode="+actualNode);
                RuleContext ctx = (RuleContext) data;
		        String msg = getMessage();
		        if (actualNode instanceof ASTVariableDeclaratorId && getBooleanProperty("pluginname")) {
		            msg = MessageFormat.format(msg, new Object[]{actualNode.getImage()});
		        }
		        ctx.getReport().addRuleViolation(createRuleViolation(ctx, actualNode.getBeginLine(), msg));
            }
        } catch (JaxenException ex) {
            ex.printStackTrace();
            throwJaxenAsRuntime(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }

    private void init() throws JaxenException {
        if (xpath == null) {
            String path = getStringProperty("xpath");
            String subst = getStringProperty("subst");
            if (subst != null && subst.length() > 0) {
				path = MessageFormat.format(path, new String[] {subst});
            }
            xpath = new BaseXPath(path, new DocumentNavigator());
        }
    }

    private static void throwJaxenAsRuntime(final JaxenException ex) {
        throw new RuntimeException() {
            public void printStackTrace() {
                super.printStackTrace();
                ex.printStackTrace();
            }
            public void printStackTrace(PrintWriter writer) {
                super.printStackTrace(writer);
                ex.printStackTrace(writer);
            }
            public void printStackTrace(PrintStream stream) {
                super.printStackTrace(stream);
                ex.printStackTrace(stream);
            }
            public String getMessage() {
                return super.getMessage() + ex.getMessage();
            }
        };
    }
}
