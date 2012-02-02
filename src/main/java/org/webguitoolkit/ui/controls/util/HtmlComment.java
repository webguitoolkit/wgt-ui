/*
Copyright 2008 Endress+Hauser Infoserve GmbH&Co KG 
Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
implied. See the License for the specific language governing permissions 
and limitations under the License.
*/ 
package org.webguitoolkit.ui.controls.util;

import java.io.PrintWriter;

import org.apache.ecs.html.Comment;
import org.webguitoolkit.ui.controls.BaseControl;

/**
 * This component is basically a place holder for insert some HTML string into the
 * DOM.
 */
public class HtmlComment extends BaseControl{

	private String commentText = null;
	
    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
     */
	public HtmlComment() {
		super();
	}
	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public HtmlComment( String id ) {
		super( id );
	}

	protected void endHTML(PrintWriter out) {
		Comment comment = new Comment( commentText );
		comment.output(out);
	}
		
	protected void init() {
		// TODO Auto-generated method stub
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
}
