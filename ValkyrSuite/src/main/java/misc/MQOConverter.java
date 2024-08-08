/**
 * 
 */
package misc;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import store.codec.model.Mesh;
import utility.Vector3f;

/**
 * @author ReverendDread May 2, 2019
 */
public class MQOConverter {
	
	private Mesh model;
	private Vector4d[] materials;

	public static void main(String[] args) throws Exception {
		new MQOConverter("C:\\Users\\Andrew\\Desktop\\models\\9697.mqo", false);
	}

	public MQOConverter() {

	}

	public MQOConverter(String file, boolean rs2) {
		try {
			model = new Mesh();
			File raw = new File(file);
			if (raw.exists()) {
				parseFile(raw);
				byte[] encoded = rs2 ? model.encodeRS2() : model.encode317();
				Files.write(Paths.get(file.substring(0, file.indexOf(".")) + ".dat"), encoded);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Mesh assembleMesh(File file) {
		try {
			model = new Mesh();
			if (file.exists()) {
				parseFile(file);
			}
			return model;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public void parseFile(File f) throws Exception {
		List<String> lines = Files.readAllLines(f.toPath());
		List<String> obj;
		int basePos = 2;
		List<String> materials = extractVal(basePos, "Material", lines);
		parseMaterials(materials.subList(1, materials.size() - 1));
		while (!(obj = (extractVal(basePos, "Object ", lines))).isEmpty()) {
			parseObject(obj);
			basePos += obj.size();
		}
	}

	public void parseMaterials(List<String> lines) {
		int index = 0;
		materials = new Vector4d[lines.size()];
		for(String line : lines) {
			line = line.replaceAll("\t", "").trim();
			String color = line.substring(line.indexOf("col(") + 4, line.indexOf(")", line.indexOf("col(")));
			String[] colorParts = color.split(" ");
			Vector4d colorVec = new Vector4d(Double.parseDouble(colorParts[0]), Double.parseDouble(colorParts[1]),
					Double.parseDouble(colorParts[2]), Double.parseDouble(colorParts[3]));
			materials[index++] = (colorVec);
		}
	}

	public static List<String> extractVal(int startPos, String firstLine, List<String> lines) {
		int startLine = -1;
		int opening = 0;
		int index = 0;
		for (String line : lines) {
			if (index >= startPos) {
				if (line.contains(firstLine)) {
					startLine = index;
					opening++;
				} else if (line.contains("{")) {
					opening++;
				} else if (line.contains("}")) {
					if (opening > 0)
						opening--;
				}
				if (opening == 0 && startLine != -1) {
					return lines.subList(startLine, index + 1);
				}
			}
			index++;
		}
		return Lists.newArrayList();
	}

	public void parseObject(List<String> lines) {
		String objectName = lines.get(0).replaceFirst("Object \"", "").replace("\" {", "").trim();
		int basePos = 1;
		List<String> vertexLines = extractVal(basePos, "vertex", lines);
		basePos += vertexLines.size();
		List<String> vertexAttr = extractVal(basePos, "vertexattr", lines);
		basePos += vertexAttr.size();
		List<String> faceLines = extractVal(basePos, "face", lines);
		basePos += faceLines.size();
		if (objectName.equalsIgnoreCase("model")) {
			if (!vertexLines.isEmpty()) {
				parseVertex(vertexLines.subList(1, vertexLines.size() - 1));
			}
		}
		if (!faceLines.isEmpty()) {
			List<Vector4> faceData = parseFaces(faceLines.subList(1, faceLines.size() - 1));
			if (objectName.equalsIgnoreCase("model")) {
				boolean hasAlpha = faceData.stream().anyMatch(vect4 -> materials[vect4.getA()].getA() != 1.0);
				model.faceIndicesA = new short[faceData.size()];
				model.faceIndicesB = new short[faceData.size()];
				model.faceIndicesC = new short[faceData.size()];
				model.faceColors = new short[faceData.size()];
				model.numFaces = faceData.size();
				if (hasAlpha)
					model.faceAlphas = new byte[faceData.size()];
				for (int index = 0; index < faceData.size(); index++) {
					Vector4d material = materials[faceData.get(index).getA()];
					model.faceIndicesC[index] = (short) faceData.get(index).getR();
					model.faceIndicesB[index] = (short) faceData.get(index).getG();
					model.faceIndicesA[index] = (short) faceData.get(index).getB();
					model.faceColors[index] = (short) getIndexOfColor(material);
					if (hasAlpha && material.getA() != 1.0) {
						model.faceAlphas[index] = (byte) (255 - (-256.0D * material.getA()));
					}
				}
			} else if (objectName.equalsIgnoreCase("pri:")) {
				boolean hasPriorities = faceData.stream().anyMatch(vect4 -> vect4.getA() != 0);
				if (hasPriorities) {
					model.facePriorities = new byte[faceData.size()];
					for (int index = 0; index < faceData.size(); index++) {
						model.facePriorities[index] = (byte) faceData.get(index).getA();
					}
				}
			} else if (objectName.equalsIgnoreCase("tskin:")) {
				boolean hasTSkins = faceData.stream().anyMatch(vect4 -> vect4.getA() != 0);
				if (hasTSkins) {
					model.faceSkins = new int[faceData.size()];
					for (int index = 0; index < faceData.size(); index++) {
						model.faceSkins[index] = faceData.get(index).getA();
					}
				}
			} else if (objectName.equalsIgnoreCase("tex:")) {
				boolean hasTextures = faceData.stream().anyMatch(vect4 -> vect4.getA() != 255);
				if (hasTextures) {
					model.numTextures = (int) faceData.stream().filter(vect4 -> vect4.getA() != 255).count();
					model.faceTextures = new short[faceData.size()];
					model.faceMaterials = new short[faceData.size()];
					model.faceTypes = new byte[faceData.size()];
					model.faceMappings = new byte[model.getNumTextures()];
					int faceTex = 0;
					for (int index = 0; index < faceData.size(); index++) {
						short texId = (short) faceData.get(index).getA();
						if(texId == 255) {
							model.faceTypes[index] = 0;
							model.faceMaterials[index] = -1;
							model.faceTextures[index] = -1;
						} else {
							model.faceTypes[index] = (byte) (2 + (faceTex * 4));
							model.faceMaterials[index] = texId;
							model.faceTextures[index] = texId;
							faceTex++;
						}
					}
				}
			} else if (objectName.equalsIgnoreCase("texcoord:")) {
				int texIndex = 0;
				model.textureMappingP = new short[model.getNumTextures()];
				model.textureMappingM = new short[model.getNumTextures()];
				model.textureMappingN = new short[model.getNumTextures()];
				for (Vector4 faceDatum : faceData) {
					for (int index = 0; index < model.faceTextures.length; index++) {
						int texCoordTexture = faceDatum.getA();
						int faceTexture = model.faceTextures[index];
						if (faceTexture == texCoordTexture) {
							model.textureMappingP[texIndex] = (short) faceDatum.getR();
							model.textureMappingM[texIndex] = (short) faceDatum.getG();
							model.textureMappingN[texIndex] = (short) faceDatum.getB();
							texIndex++;
						}
					}
				}
			}
		} else if (objectName.equalsIgnoreCase("vskin1:") || objectName.equalsIgnoreCase("vskin2:") || objectName.equalsIgnoreCase("vskin3:")) {
			if (!vertexAttr.isEmpty()) {
				List<String> weights = extractVal(1, "weit", vertexAttr);
				parseWeights(weights.subList(1, weights.size() - 1));
			}
		} else {
			System.err.println("Unknown layer: " + objectName);
		}
	}

	public void parseVertex(List<String> lines) {
		List<Vector3f> vertices = Lists.newLinkedList();
		lines.forEach(line -> {
			line = line.replaceAll("\t", "").trim();
			List<Float> parts = Stream.of(line.split(" ")).map(Float::parseFloat).collect(Collectors.toList());
			vertices.add(new Vector3f(parts.get(0), parts.get(1), parts.get(2)));
		});
		model.numVertices = vertices.size();
		model.verticesX = new int[vertices.size()];
		model.verticesY = new int[vertices.size()];
		model.verticesZ = new int[vertices.size()];
		model.vertexSkins = new int[vertices.size()];
		for (int index = 0; index < vertices.size(); index++) {
			Vector3f vec3 = vertices.get(index);
			model.verticesX[index] = (int) vec3.x;
			model.verticesY[index] = (int) vec3.y * -1;
			model.verticesZ[index] = (int) vec3.z * -1;
		}
	}

	public List<Vector4> parseFaces(List<String> lines) {
		List<Vector4> data = Lists.newLinkedList();
		lines.stream().forEach(line -> {
			line = line.replaceAll("\t", "").trim();
			if (line.startsWith("3")) {
				String vertexABC = line.substring(line.indexOf("V(") + 2, line.indexOf(")"));
				String material = line.substring(line.indexOf("M(") + 2, line.length() - 1);
				String[] abc = vertexABC.split(" ");
				data.add(new Vector4(Ints.tryParse(abc[0]), Ints.tryParse(abc[1]), Ints.tryParse(abc[2]), material != null ?
						Ints.tryParse(material) : 255));
			} else {

			}
		});
		return data;
	}

	public void parseWeights(List<String> lines) {
		lines.forEach(line -> {
			line = line.replaceAll("\t", "").trim();
			String[] parts = line.split(" ");
			double weight = Double.parseDouble(parts[1]);
			int vertexIndex = Ints.tryParse(parts[0]);
			model.vertexSkins[vertexIndex] += (int) (weight * 100D);
		});
	}

	public static int rgb2hsl(double r, double g, double b) {
		double v;
		double m;
		double vm;
		double h, s, l;
		double r2, g2, b2;
		h = 0;
		s = 0;
		l = 0;
		v = Math.max(r, g);
		v = Math.max(v, b);
		m = Math.min(r, g);
		m = Math.min(m, b);
		l = (m + v) / 2.0;
		if (l <= 0.0) {
			l = 0;
		}
		vm = v - m;
		s = vm;
		if (s > 0.0) {
			s /= (l <= 0.5) ? (v + m) : (2.0 - v - m);
		} else {
			s = 0;
		}
		r2 = (v - r) / vm;
		g2 = (v - g) / vm;
		b2 = (v - b) / vm;
		if (r == v) {
			h = (g == m ? 5.0 + b2 : 1.0 - g2);
		} else if (g == v) {
			h = (b == m ? 1.0 + r2 : 3.0 - b2);
		} else {
			h = (r == m ? 3.0 + g2 : 5.0 - r2);
		}

		h /= 6.0;
		double l2 = l * 127;
		double s2 = s * 7;
		double h2 = h * 63;
		if (l2 < 0.0) {
			l2 = 0.0;
		}
		if (s2 < 0.0) {
			s2 = 0.0;
		}
		if (h2 < 0.0) {
			h2 = 0.0;
		}
		int truncatedL = (int) l2;
		int truncatedS = (int) s2;
		int truncatedH = (int) h2;
		int value = truncatedL + (truncatedS * 128) + (truncatedH * 1024);
		if (value > 65535) {
			value = 65535;
		}
		if (value < 0) {
			value = 0;
		}
		return value;
	}
	
	private static int getIndexOfColor(Vector4d vec) {
		return MQOConverter.rgb2hsl(vec.getR(), vec.getG(), vec.getB());
	}

}
