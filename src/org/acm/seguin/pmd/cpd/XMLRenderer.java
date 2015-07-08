package org.acm.seguin.pmd.cpd;

import org.acm.seguin.pmd.PMD;
import org.acm.seguin.pmd.util.StringUtil;

import java.util.Iterator;

/**
 * @author  Philippe T'Seyen
 */
public class XMLRenderer implements Renderer
{
  public String render(Iterator matches)
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("<?xml version=\"1.0\"?>");
    buffer.append("<pmd-cpd>");
    for (;matches.hasNext();)
    {
      Match match = (Match) matches.next();
      buffer.append("<duplication");
      buffer.append(" lines=\"");
      buffer.append(match.getLineCount());
      buffer.append("\"");
      buffer.append(" tokens=\"");
      buffer.append(match.getTokenCount());
      buffer.append("\">");

      for (Iterator iterator = match.iterator(); iterator.hasNext();)
      {
        Mark mark = (Mark) iterator.next();
        buffer.append("<file");
        buffer.append(" line=\"");
        buffer.append(mark.getBeginLine());
        buffer.append("\"");
        buffer.append(" path=\"");
        buffer.append(mark.getTokenSrcID());
        buffer.append("\"/>");
      }
      String codeFragment = match.getSourceCodeSlice();
      if (codeFragment != null)
      {
        buffer.append("<codefragment><![CDATA[" + PMD.EOL + StringUtil.replaceString(codeFragment, "]]>", "]]&gt;") + PMD.EOL + "]]></codefragment>");
      }
      buffer.append("</duplication>");
    }
    buffer.append("</pmd-cpd>");
    return buffer.toString();
  }
}
