
package ua.org.antidotcb.dominus.graphics.utils;


public final class VertexFormat {

	public static enum Context {
		Coord(0), Color(1), Normal(2), Texture(3), Common(4), ColorPacked(5);

		private final int	value;

		Context(int value) {
			this.value = value;
		}

		public final int value() {
			return value;
		}
	}

	private static final int			FLOATSIZE	= 4;

	private final VertexDescription[]	descriptions;
	public final int					vertexSize;

	public VertexFormat(VertexDescription... attributes) {
		if (attributes.length == 0) throw new IllegalArgumentException("attributes must be >= 1");

		this.descriptions = attributes.clone();

		validate();
		vertexSize = calcOffset();
	}

	private int calcOffset() {
		int count = 0;
		for (VertexDescription attribute : descriptions) {
			// attribute.offset = count;

			if (attribute.context == VertexFormat.Context.ColorPacked) count += FLOATSIZE;
			else count += FLOATSIZE * attribute.elements;
		}

		return count;
	}

	public VertexDescription get(int index) {
		return descriptions[index];
	}

	public int size() {
		return descriptions.length;
	}

	private void validate() {
		boolean coords = false;
		boolean colors = false;
		boolean normals = false;

		for (int i = 0; i < descriptions.length; i++) {
			VertexDescription attribute = descriptions[i];
			if (attribute.context == Context.Coord) {
				if (coords) throw new IllegalArgumentException("There are two coord attributes.");
				coords = true;
			}

			if (attribute.context == Context.Normal) {
				if (normals) throw new IllegalArgumentException("There are two normal attributes.");
			}

			if (attribute.context == Context.Color || attribute.context == Context.ColorPacked) {
				if (attribute.elements != 4) throw new IllegalArgumentException("Color attribute must contain 4 components (RGBA).");

				if (colors) throw new IllegalArgumentException("There are two color attributes.");
				colors = true;
			}
		}

		if (coords == false) throw new IllegalArgumentException("There are no coord attribute.");
	}
}
