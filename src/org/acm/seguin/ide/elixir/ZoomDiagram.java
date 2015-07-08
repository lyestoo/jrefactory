/* ====================================================================
 * The JRefactory License, Version 1.0
 *
 * Copyright (c) 2001 JRefactory.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        JRefactory (http://www.sourceforge.org/projects/jrefactory)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "JRefactory" must not be used to endorse or promote
 *    products derived from this software without prior written
 *    permission. For written permission, please contact seguin@acm.org.
 *
 * 5. Products derived from this software may not be called "JRefactory",
 *    nor may "JRefactory" appear in their name, without prior written
 *    permission of Chris Seguin.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE CHRIS SEGUIN OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of JRefactory.  For more information on
 * JRefactory, please see
 * <http://www.sourceforge.org/projects/jrefactory>.
 */
package org.acm.seguin.ide.elixir;

import com.elixirtech.fx.*;
import com.elixirtech.ide.edit.*;

/**
 *  Handles all the diagram zooming for Elixir IDE
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
public class ZoomDiagram {

    /**
     *  Gets the Manager attribute of the ZoomDiagram class
     *
     *@return    The Manager value
     */
    private static ViewManager getManager() {
        FrameManager fm = FrameManager.current();
        if (fm == null) {
            return null;
        }
        if (fm.getViewSite() == null) {
            return null;
        }
        return (ViewManager) fm.getViewSite().getCurrentViewManager();
    }


    /**
     *  Scales to 50%
     */
    public static void fiftyPercent() {
        work(0.5);
    }


    /**
     *  Scales to 100%
     */
    public static void fullSize() {
        work(1.0);
    }


    /**
     *  Scales the diagram
     *
     *@param  manager      the manager
     *@param  scaleFactor  the amount to scale
     */
    private static void scale(UMLViewManager manager, double scaleFactor) {
        manager.getDiagram().scale(scaleFactor);
        manager.getDiagram().repaint();
    }


    /**
     *  Scales to 10%
     */
    public static void tenPercent() {
        work(0.1);
    }


    /**
     *  Scales to 25%
     */
    public static void twentyfivePercent() {
        work(0.25);
    }


    /**
     *  actually performs the scaling if it is appropriate
     *
     *@param  scaleFactor  The amount to scale the diagram by
     */
    private static void work(double scaleFactor) {
        ViewManager bvm = getManager();
        if ((bvm != null) && (bvm instanceof UMLViewManager)) {
            scale((UMLViewManager) bvm, scaleFactor);
        }
    }
}
//  EOF
