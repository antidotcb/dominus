
package ua.org.antidotcb.dominus.graphics;


import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import android.util.Log;


public class ObjParser {

	private class Group {

		public Vector<Face>	faces;
		private String		material;
		final public String	name;
		private int			shade;

		public Group(final String groupName) {
			this.name = groupName;
			this.faces = new Vector<Face>();
		}

		@SuppressWarnings("unused") public String getMaterial() {
			return material;
		}

		@SuppressWarnings("unused") public void setMaterial(String material) {
			this.material = material;
		}

		@SuppressWarnings("unused") public int getShade() {
			return shade;
		}

		public void setShade(int shade) {
			this.shade = shade;
		}
	}

	private class Object {

		public Vector<Group>	groups;
		final public String		name;

		public Object(final String name) {
			this.name = name;
			this.groups = new Vector<Group>();
		}
	}

	private static class Face {

		public static Face parseFace(String string) {
			Log.v(TAG, "Face Definitions");

			String[] pointStrings = string.split("[ ]+");

			Face face = new Face(pointStrings.length);

			for (int i = 0; i < pointStrings.length; i++) {
				String[] parts = pointStrings[i].split("/");

				face.coords[i] = Short.parseShort(parts[0]);

				if ((parts.length > 1) && (parts[1].length() > 0)) {
					face.texcoords[i] = Short.parseShort(parts[2]);
				}

				if ((parts.length > 2) && (parts[2].length() > 0)) {
					face.normals[i] = Short.parseShort(parts[2]);
				}
			}

			return face;
		}

		private short[]		coords;
		private short[]		normals;
		private short[]		texcoords;

		final public int	components;

		public Face(final int size) {
			this.components = size;

			coords = new short[size];
			normals = new short[size];
			texcoords = new short[size];
		}
	}

	private static class Vector2f {

		public final static int	ELEMENTS	= 2;

		public static Vector2f parseVector2f(String string) {
			String vertexCoords = string.substring(PREFIX_VERTEX_TEXCOORDS.length()).trim();
			String[] tokens = vertexCoords.split("[ ]+");

			float u = 0, v = 0;

			if (tokens.length > UCOORD) {
				u = Float.parseFloat(tokens[UCOORD]);
			}

			if (tokens.length > VCOORD) {
				v = Float.parseFloat(tokens[VCOORD]);
			}

			Log.v(TAG, String.format("Vertex texture coordinates: [%f,%f]", u, v));

			return new Vector2f(u, v);
		}

		final public float	u, v;

		public Vector2f(final float u, final float v) {
			this.u = u;
			this.v = v;
		}
	}

	private static class Vector3f {

		public final static int	ELEMENTS	= 3;

		public static Vector3f parseVector3f(String string) {
			String[] tokens = string.split("[ ]+");

			float x = 0, y = 0, z = 0;

			if (tokens.length > XCOORD) {
				x = Float.parseFloat(tokens[XCOORD]);
			}

			if (tokens.length > YCOORD) {
				y = Float.parseFloat(tokens[YCOORD]);
			}

			if (tokens.length > ZCOORD) {
				z = Float.parseFloat(tokens[ZCOORD]);
			}

			Log.v(TAG, String.format("Vertex coordinates: [%f,%f,%f]", x, y, z));

			return new Vector3f(x, y, z);
		}

		final public float	x;
		final public float	y;
		final public float	z;

		public Vector3f(final float x, final float y, final float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	private static final String	NAME_UNKNOWN			= "Unnamed";

	private static final String	PREFIX_FACE				= "f ";
	private static final String	PREFIX_GROUP			= "g ";
	private static final String	PREFIX_OBJECT			= "o ";
	private static final String	PREFIX_SHADE			= "s ";
	private static final String	PREFIX_VERTEX_COORDS	= "v ";
	private static final String	PREFIX_VERTEX_NORMAL	= "vn ";
	private static final String	PREFIX_VERTEX_TEXCOORDS	= "vt ";

	private static final String	TAG						= ObjParser.class.getName();

	private static final int	UCOORD					= 0;
	private static final int	VCOORD					= 0;
	private static final int	XCOORD					= 0;
	private static final int	YCOORD					= 1;
	private static final int	ZCOORD					= 2;

	private Vector<Vector3f>	coords					= null;
	private Group				currentGroup			= null;
	private Object				currentObject			= null;
	private Vector<Vector3f>	normals					= null;
	private Vector<Object>		objects					= null;
	private Vector<Vector2f>	texcoords				= null;

	public ObjParser() {
		super();
	}

	private void addFace(String string) {
		Log.v(TAG, "Face");
		getGroup().faces.add(Face.parseFace(string));
	}

	private void addGroup(String name) {
		Log.v(TAG, "Group " + name);

		currentGroup = new Group(name);

		getObject().groups.add(currentGroup);
	}

	private void addObject(String name) {
		Log.v(TAG, "Object " + name);

		currentObject = new Object(name);

		objects.add(currentObject);
	}

	private Group getGroup() {
		if (currentGroup == null) {
			addGroup(NAME_UNKNOWN + getObject().groups.size());
		}

		return currentGroup;
	}

	private Object getObject() {
		if (currentObject == null) {
			addObject(NAME_UNKNOWN + objects.size());
		}

		return currentObject;
	}

	private void initialize() {
		coords = new Vector<Vector3f>();
		normals = new Vector<Vector3f>();
		texcoords = new Vector<Vector2f>();
		objects = new Vector<Object>();
	}

	private Vector<Model> parseBuffer(byte[] buf) {

		initialize();

		int lineStart = 0;
		int current = 0;
		do {
			if ((buf[current] == '\n') || (current == buf.length)) {
				int lineLength = current - lineStart;

				String line = new String(buf, lineStart, lineLength).toLowerCase().trim();
				parseLine(line);

				lineStart = current++;
			}
			current++;
		}
		while (current <= buf.length);

		int size = Vector3f.ELEMENTS + ((normals.size() > 0) ? Vector3f.ELEMENTS : 0) + ((texcoords.size() > 0) ? Vector2f.ELEMENTS : 0);

		Vector<Model> models = new Vector<Model>();

		for (Object object : objects) {
			float[] vertices = new float[size * coords.size()];
			Model model = new Model(object.name);

			for (Group group : object.groups) {
				Mesh mesh = new Mesh(group.name);

				short[] indices = new short[group.faces.size() * 3]; // TODO:
																		// Remove
																		// 3
																		// const
				int indicesIndex = 0;

				for (Face face : group.faces) {
					for (int i = 0; i < face.components; i++) {
						short vertexIndex = (short) (face.coords[i] - 1);
						int offset = vertexIndex * size;

						Vector3f coord = coords.get(vertexIndex);

						vertices[offset++] = coord.x;
						vertices[offset++] = coord.y;
						vertices[offset++] = coord.z;

						if (normals.size() > 0) {
							Vector3f normal = normals.get(face.normals[i] - 1);
							vertices[offset++] = normal.x;
							vertices[offset++] = normal.y;
							vertices[offset++] = normal.z;
						}
						if (texcoords.size() > 0) {
							Vector2f texcoord = texcoords.get(face.texcoords[i] - 1);
							vertices[offset++] = texcoord.u;
							vertices[offset++] = texcoord.v;
						}

						indices[indicesIndex++] = vertexIndex;
					}
				}

				mesh.setIndices(indices);

				model.add(mesh);
			}

			model.setVertices(vertices);
			models.add(model);
		}

		return models;
	}

	private boolean parseLine(final String line) {
		if (line.startsWith(PREFIX_VERTEX_COORDS)) {
			String string = line.substring(PREFIX_VERTEX_COORDS.length()).trim();
			coords.add(Vector3f.parseVector3f(string));
			return true;
		}

		if (line.startsWith(PREFIX_VERTEX_TEXCOORDS)) {
			String string = line.substring(PREFIX_VERTEX_TEXCOORDS.length()).trim();
			texcoords.add(Vector2f.parseVector2f(string));
			return true;
		}

		if (line.startsWith(PREFIX_VERTEX_NORMAL)) {
			String string = line.substring(PREFIX_VERTEX_TEXCOORDS.length()).trim();
			normals.add(Vector3f.parseVector3f(string));
			return true;
		}

		if (line.startsWith(PREFIX_FACE)) {
			String string = line.substring(PREFIX_VERTEX_COORDS.length()).trim();
			addFace(string);
			return true;
		}

		if (line.startsWith(PREFIX_OBJECT)) {
			String name = line.substring(PREFIX_OBJECT.length()).trim();
			addObject(name);
			return true;
		}

		if (line.startsWith(PREFIX_GROUP)) {
			String name = line.substring(PREFIX_GROUP.length()).trim();
			addGroup(name);
			return true;
		}

		if (line.startsWith(PREFIX_SHADE)) {
			String shadeString = line.substring(PREFIX_SHADE.length()).trim();

			int shade = 0;
			try {
				shade = Integer.parseInt(shadeString);
			}
			catch (NumberFormatException e) {
				Log.e(TAG, String.format("Cannot parse %s to integer, disabling shade", shadeString));
			}

			getGroup().setShade(shade);

			Log.v(TAG, String.format("Group %s is set shade %d", getGroup().name, shade));
		}

		if (line.startsWith("mtllib ")) {
			Log.v(TAG, "Referencing materials");
			// TODO: Implement material lib parsing
		}

		if (line.startsWith("usemtl ")) {
			Log.v(TAG, "Material");
			// TODO: Implement material parsing
		}

		if (line.startsWith("#")) {
			Log.v(TAG, "Comment");
		}

		Log.i(TAG, line);
		return false;
	}

	public Vector<Model> parseStream(InputStream stream) throws IOException {
		int size = stream.available();
		byte[] buf = new byte[size];
		int read = stream.read(buf);
		Log.d(TAG, String.format("Read %d bytes from stream", read));
		if (read != size) { throw new IOException("Can't read from stream."); }
		return parseBuffer(buf);
	}

}
