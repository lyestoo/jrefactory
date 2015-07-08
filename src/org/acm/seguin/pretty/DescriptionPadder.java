/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.pretty;

import org.acm.seguin.util.FileSettings;

/**
 *  Description of the Class
 *
 *@author    Chris Seguin
 */
public class DescriptionPadder {
    /**
     *  Description of the Method
     *
     *@param  bundle  Description of Parameter
     *@param  key     Description of Parameter
     *@return         Description of the Returned Value
     */
    public static String find(FileSettings bundle, String key)
    {
        String message = bundle.getString(key);
        return padBuffer(message, bundle);
    }


    /**
     *  Pads the buffer
     *
     *@param  message  Description of Parameter
     *@param  bundle   Description of Parameter
     *@return          Description of the Returned Value
     */
    public static String padBuffer(String message, FileSettings bundle)
    {
        if (!bundle.getBoolean("reformat.comments")) {
            StringBuffer buffer = new StringBuffer(message);
            int count = bundle.getInteger("javadoc.indent");
            for (int ndx = 0; ndx < count; ndx++) {
                buffer.insert(0, " ");
            }
            return buffer.toString();
        }
        return message;
    }
}
//  This is the end of the file
