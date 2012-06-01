
package ua.org.antidotcb.dominus.graphics.utils;


import ua.org.antidotcb.dominus.graphics.utils.VertexFormat.Context;


public final class VertexDescription {

	public final String		alias;
	public final Context	context;
	public final int		elements;

	// public int offset;

	public VertexDescription(Context context, int elements, String alias) {
		this.context = context;
		this.elements = elements;
		this.alias = alias;
	}
}
