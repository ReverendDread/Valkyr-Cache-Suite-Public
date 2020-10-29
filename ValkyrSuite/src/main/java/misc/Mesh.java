/**
 * 
 */
package misc;

import store.codec.model.EmissiveTriangle;
import suite.opengl.util.ColorUtilities;

/**
 * Created at: Apr 11, 2017 1:04:42 AM
 * 
 * @author Walied-Yassen
 */
public abstract class Mesh {


    /**
     * The default mesh version.
     */
    protected static final int DEFAULT_VERSION = 12;

    /**
     * The model id.
     */
    protected int id;

    /**
     * The model format version. (default is 12)
     */
    protected int version;


    /**
     * The amount of vertices stored within this mesh.
     */
    protected int numVertices;


    /**
     * The amount of faces stored within this mesh.
     */
    protected int numFaces;


    /**
     * The amount of textured faces stored within this mesh.
     */
    protected int numTextures;


    /**
     * The model itself priority.
     */
    protected byte priority;


    /**
     * The biggest face index.
     */
    protected int maxIndex;
    
    /**
     * If the model uses the 317 encoder.
     */
    protected boolean old;
    
    /**
     * 
     */
    protected byte[] uvCoordVertexA;
	
    /**
     * 
     */
    protected byte[] uvCoordVertexC;
	
    /**
     * 
     */
    protected byte[] uvCoordVertexB;
	
    /**
     * 
     */
    protected float[] textureCoordU;
	
    /**
     * 
     */
    protected float[] textureCoordV;


    /**
     * The vertices x position.
     */
    protected int[] verticesX;


    /**
     * The vertices y position.
     */
    protected int[] verticesY;


    /**
     * The vertices z position.
     */
    protected int[] verticesZ;


    /**
     * The vertex skins.
     */
    protected int[] vertexSkins;


    /**
     * The face point A vertex indices.
     */
    protected short[] faceIndicesA;


    /**
     * The face point B vertex indices.
     */
    protected short[] faceIndicesB;


    /**
     * The face point C vertex indices.
     */
    protected short[] faceIndicesC;


    /**
     * The face types.
     */
    protected byte[] faceTypes;


    /**
     * The face priorities.
     */
    protected byte[] facePriorities;


    /**
     * The face alphas.
     */
    protected byte[] faceAlphas;


    /**
     * The face texture indices.
     */
    protected short[] faceTextureIndices;


    /**
     * The face colors.
     */
    protected short[] faceColors;


    /**
     * The face materials.
     */
    protected short[] faceMaterials;


    /**
     * The face skins.
     */
    protected int[] faceSkins;


    /**
     * The face mappings.
     */
    protected byte[] faceMappings;


    /**
     * The face texture mapping P value.
     */
    protected short[] textureMappingP;


    /**
     * The face texture mapping M value.
     */
    protected short[] textureMappingM;


    /**
     * The face texture mapping N value.
     */
    protected short[] textureMappingN;


    /**
     * The face texture scale x.
     */
    protected int[] textureScaleX;


    /**
     * The face texture scale y.
     */
    protected int[] textureScaleY;


    /**
     * The face texture scale y.
     */
    protected int[] textureScaleZ;


    /**
     * The face texture rotation.
     */
    protected byte[] textureRotation;


    /**
     * The face texture direction.
     */
    protected byte[] textureDirection;


    /**
     * The face texture speed.
     */
    protected int[] textureSpeed;


    /**
     * The face texture x translations.
     */
    protected int[] textureTransV;


    /**
     * The face texture y translations.
     */
    protected int[] textureTransU;
    
    /**
     * 
     */
    protected int[] vertexUVOffset;
    
    /**
     * 
     */
    protected int textureUVCoordCount;

    /**
     * The particle emitters.
     */
    protected EmissiveTriangle[] emitters;


    /**
     * The particle effectors.
     */
    protected EffectiveVertex[] effectors;


    /**
     * The billboards.
     */
    protected FaceBillboard[] billboards;
    
    public Mesh() {
        maxIndex = 0;
        numFaces = 0;
        priority = (byte) 0;
        numTextures = 0;
    }


    /**
     * Constructs a new {@link Mesh} object instance.
     * 
     * @param data
     *            the raw mesh file data.
     */
    public Mesh(byte[] data) {
        maxIndex = 0;
        numFaces = 0;
        priority = (byte) 0;
        numTextures = 0;
        decode(data);
    }


    /**
     * Constructs a new {@link Mesh} object instance.
     * 
     * @param numVertices
     *            the amount of vertices within this mesh.
     * @param numFaces
     *            the amount of faces within this mesh.
     * @param numTexturedFaces
     *            the amount of textured faces within this mesh.
     */
    public Mesh(int numVertices, int numFaces, int numTexturedFaces) {
        version = DEFAULT_VERSION;
        verticesX = new int[numVertices];
        verticesY = new int[numVertices];
        verticesZ = new int[numVertices];
        vertexSkins = new int[numVertices];
        faceIndicesA = new short[numFaces];
        faceIndicesB = new short[numFaces];
        faceIndicesC = new short[numFaces];
        faceTypes = new byte[numFaces];
        facePriorities = new byte[numFaces];
        faceAlphas = new byte[numFaces];
        faceTextureIndices = new short[numFaces];
        faceColors = new short[numFaces];
        faceMaterials = new short[numFaces];
        faceSkins = new int[numFaces];
        if (numTexturedFaces > 0) {
            faceMappings = new byte[numTexturedFaces];
            textureMappingP = new short[numTexturedFaces];
            textureMappingM = new short[numTexturedFaces];
            textureMappingN = new short[numTexturedFaces];
            textureScaleX = new int[numTexturedFaces];
            textureScaleY = new int[numTexturedFaces];
            textureScaleZ = new int[numTexturedFaces];
            textureRotation = new byte[numTexturedFaces];
            textureDirection = new byte[numTexturedFaces];
            textureSpeed = new int[numTexturedFaces];
            textureTransU = new int[numTexturedFaces];
            textureTransV = new int[numTexturedFaces];
        }
    }

    /**
     * Deserializes this {@link Mesh} instance from the specified {@code data}
     * buffer.
     * 
     * @param data
     *            the model data.
     */
    public abstract void decode(byte[] data);

    /**
     * Serializes this {@link Mesh} instance into a {@code byte} array in 317 format.
     * 
     * @return the serialized {@code byte} array.
     */
    public abstract byte[] encodeOld();
    
    /**
     * Serializes this {@link Mesh} instance into a {@code byte} array in rs2 format.
     * 
     * @return the serialized {@code byte} array.
     */
    public abstract byte[] encodeRS2();


    /**
     * Adds a new vertex to the mesh.
     * 
     * @param x
     *            the vertex x position.
     * @param y
     *            the vertex y position.
     * @param z
     *            the vertex z position.
     * @return the vertex index.
     */
    public int addVertex(int x, int y, int z) {
        for (int index = 0; index < numVertices; index++) {
            if (verticesX[index] == x && verticesY[index] == y && verticesZ[index] == z) {
                return index;
            }
        }
        verticesX[numVertices] = x;
        verticesY[numVertices] = y;
        verticesZ[numVertices] = z;
        return numVertices++;
    }


    /**
     * Adds a new face to the mesh.
     * 
     * @param a
     *            the face point A index.
     * @param b
     *            the face point B index.
     * @param c
     *            the face point C index.
     * @param color
     *            the face color.
     * @param alpha
     *            the face alpha.
     * @return the face index.
     */
    public int addFace(int a, int b, int c, short color, byte alpha) {
        faceIndicesA[numFaces] = (short) a;
        faceIndicesB[numFaces] = (short) b;
        faceIndicesC[numFaces] = (short) c;
        faceColors[numFaces] = color;
        faceAlphas[numFaces] = alpha;
        return numFaces++;
    }


    /**
     * Gets the format version.
     * 
     * @return the format version.
     */
    public int getVersion() {
        return version;
    }


    /**
     * Sets the format version.
     * 
     * @param format
     *            the new format version.
     */
    public void setFormat(int format) {
        version = format;
    }


    /**
     * Gets the amount of vertices we have within this model.
     * 
     * @return the amount of vertices.
     */
    public int getNumVertices() {
        return numVertices;
    }


    /**
     * Sets the amount of vertices within this model.
     * 
     * @param numVertices
     *            the amount of vertices within this model.
     */
    public void setNumVertices(int numVertices) {
        this.numVertices = numVertices;
    }


    /**
     * Gets the amount of faces within this mesh.
     * 
     * @return the numFaces the amount of faces within this mesh.
     */
    public int getNumFaces() {
        return numFaces;
    }


    /**
     * Sets the amount of faces within this mesh.
     * 
     * @param numFaces
     *            the amount of faces to set.
     */
    public void setNumFaces(int numFaces) {
        this.numFaces = numFaces;
    }


    /**
     * Gets the amount of textures faces we have within this mesh.
     * 
     * @return the amount of textures faces we have within this mesh.
     */
    public int getNumTextures() {
        return numTextures;
    }


    /**
     * Sets the amount of textures faces within this mesh.
     * 
     * @param numTexturedFaces
     *            the amount of textures faces to set.
     */
    public void setNumTextures(int numTexturedFaces) {
        this.numTextures = numTexturedFaces;
    }


    /**
     * Gets the model priority.
     * 
     * @return the model priority.
     */
    public byte getPriority() {
        return priority;
    }


    /**
     * Sets the model priority.
     * 
     * @param priority
     *            the model priority to set.
     */
    public void setPriority(byte priority) {
        this.priority = priority;
    }


    /**
     * Gets the biggest face index.
     * 
     * @return the biggest face index.
     */
    public int getMaxIndex() {
        return maxIndex;
    }


    /**
     * Sets the biggest face index.
     * 
     * @param maxIndex
     *            the new biggest face index.
     */
    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }


    /**
     * Gets the vertices x position.
     * 
     * @return the vertices x position.
     */
    public int[] getVerticesX() {
        return verticesX;
    }


    /**
     * Sets the vertices x position.
     * 
     * @param verticesX
     *            the vertices x position.
     */
    public void setVerticesX(int[] verticesX) {
        this.verticesX = verticesX;
    }


    /**
     * Gets the vertices y position.
     * 
     * @return the vertices y position.
     */
    public int[] getVerticesY() {
        return verticesY;
    }


    /**
     * Sets the vertices y position.
     * 
     * @param verticesY
     *            the vertices y position.
     */
    public void setVerticesY(int[] verticesY) {
        this.verticesY = verticesY;
    }


    /**
     * Gets the vertices z position.
     * 
     * @return the vertices z position.
     */
    public int[] getVerticesZ() {
        return verticesZ;
    }


    /**
     * Sets the vertices z position.
     * 
     * @param verticesZ
     *            the vertices z position.
     */
    public void setVerticesZ(int[] verticesZ) {
        this.verticesZ = verticesZ;
    }


    /**
     * Gets the vertex skins.
     * 
     * @return the vertex skins.
     */
    public int[] getVertexSkins() {
        return vertexSkins;
    }


    /**
     * Sets the vertex skins.
     * 
     * @param vertexSkins
     *            the new vertex skins to set.
     */
    public void setVertexSkins(int[] vertexSkins) {
        this.vertexSkins = vertexSkins;
    }


    /**
     * Gets the face point A vertex indices.
     * 
     * @return the face point A vertex indices.
     */
    public short[] getFaceIndicesA() {
        return faceIndicesA;
    }


    /**
     * Sets the face point A vertex indices.
     * 
     * @param faceIndicesA
     *            the face point A vertex indices.
     */
    public void setFaceIndicesA(short[] faceIndicesA) {
        this.faceIndicesA = faceIndicesA;
    }


    /**
     * Gets the face point B vertex indices.
     * 
     * @return the face point BA vertex indices.
     */
    public short[] getFaceIndicesB() {
        return faceIndicesB;
    }


    /**
     * Sets the face point B vertex indices.
     * 
     * @param faceIndicesB
     *            the face point B vertex indices.
     */
    public void setFaceIndicesB(short[] faceIndicesB) {
        this.faceIndicesB = faceIndicesB;
    }


    /**
     * Gets the face point C vertex indices.
     * 
     * @return the face point A vertex indices.
     */
    public short[] getFaceIndicesC() {
        return faceIndicesC;
    }


    /**
     * Sets the face point C vertex indices.
     * 
     * @param faceIndicesC
     *            the face point C vertex indices.
     */
    public void setFaceIndicesC(short[] faceIndicesC) {
        this.faceIndicesC = faceIndicesC;
    }


    /**
     * Gets the face types.
     * 
     * @return the face types.
     */
    public byte[] getFaceTypes() {
        return faceTypes;
    }


    /**
     * Sets the face types.
     * 
     * @param faceTypes
     *            the face types.
     */
    public void setFaceTypes(byte[] faceTypes) {
        this.faceTypes = faceTypes;
    }


    /**
     * Gets the face priorities.
     * 
     * @return the face priorities.
     */
    public byte[] getFacePriorities() {
        return facePriorities;
    }


    /**
     * Sets the face priorities.
     * 
     * @param facePriorities
     *            the new face priorities.
     */
    public void setFacePriorities(byte[] facePriorities) {
        this.facePriorities = facePriorities;
    }


    /**
     * Gets the face alpha values.
     * 
     * @return the face alpha values.
     */
    public byte[] getFaceAlphas() {
        return faceAlphas;
    }


    /**
     * Sets the face alpha values.
     * 
     * @param alphas
     *            the face alpha values.
     */
    public void setFaceAlphas(byte[] alphas) {
        faceAlphas = alphas;
    }


    /**
     * Gets the face textures.
     * 
     * @return the face textures.
     */
    public short[] getFaceTextures() {
        return faceTextureIndices;
    }


    /**
     * Sets the face textures.
     * 
     * @param faceTextures
     *            the new face textures to set.
     */
    public void setFaceTextures(short[] faceTextures) {
        this.faceTextureIndices = faceTextures;
    }


    /**
     * Gets the face colors.
     * 
     * @return the face colors.
     */
    public short[] getFaceColors() {
        return faceColors;
    }


    /**
     * Sets the face colors.
     * 
     * @param colors
     *            the new face colors to set.
     */
    public void setFaceColors(short[] colors) {
        faceColors = colors;
    }


    /**
     * Gets the face materials.
     * 
     * @return the face materials.
     */
    public short[] getFaceMaterials() {
        return faceMaterials;
    }


    /**
     * Sets the face materials.
     * 
     * @param faceMaterials
     *            the new face materials.
     */
    public void setFaceMaterials(short[] faceMaterials) {
        this.faceMaterials = faceMaterials;
    }


    /**
     * Gets the face skins.
     * 
     * @return the face skins.
     */
    public int[] getFaceSkins() {
        return faceSkins;
    }


    /**
     * Sets the face skins.
     * 
     * @param faceSkins
     *            the new face skins to set.
     */
    public void setFaceSkins(int[] faceSkins) {
        this.faceSkins = faceSkins;
    }


    /**
     * Gets the face mappings.
     * 
     * @return the face mappings.
     */
    public byte[] getFaceMappings() {
        return faceMappings;
    }


    /**
     * Sets the face mappings.
     * 
     * @param faceMappings
     *            the face mappings.
     */
    public void setFaceMappings(byte[] faceMappings) {
        this.faceMappings = faceMappings;
    }


    /**
     * Gets the face texture mapping P values.
     * 
     * @return the face texture mapping P values.
     */
    public short[] getTextureMappingP() {
        return textureMappingP;
    }


    /**
     * Sets the face texture mapping P values.
     * 
     * @param faceTextureP
     *            the new face texture mapping P values.
     */
    public void setTextureMappingP(short[] faceTextureP) {
        textureMappingP = faceTextureP;
    }


    /**
     * Gets the face texture mapping M values.
     * 
     * @return the face texture mapping M values.
     */
    public short[] getTextureMappingM() {
        return textureMappingM;
    }


    /**
     * Sets the face texture mapping M values.
     * 
     * @param faceTextureM
     *            the new face texture mapping M values.
     */
    public void setTextureMappingM(short[] faceTextureM) {
        textureMappingM = faceTextureM;
    }


    /**
     * Gets the face texture mapping N values.
     * 
     * @return the face texture mapping N values.
     */
    public short[] getTextureMappingN() {
        return textureMappingN;
    }


    /**
     * Sets the face texture mapping N values.
     * 
     * @param faceTextureN
     *            the new face texture mapping N values.
     */
    public void setTextureMappingN(short[] faceTextureN) {
        textureMappingN = faceTextureN;
    }


    /**
     * Gets the face texture scale down x multipliers.
     * 
     * @return the face texture scale down x multipliers.
     */
    public int[] getTextureScaleX() {
        return textureScaleX;
    }


    /**
     * Sets the face texture scale down x multipliers.
     * 
     * @param textureScaleX
     *            the new face texture scale down x multipliers.
     */
    public void setTextureScaleX(int[] textureScaleX) {
        this.textureScaleX = textureScaleX;
    }


    /**
     * Gets the face texture scale down y multipliers.
     * 
     * @return the face texture scale down y multipliers.
     */
    public int[] getTextureScaleY() {
        return textureScaleY;
    }


    /**
     * Sets the face texture scale down y multipliers.
     * 
     * @param textureScaleY
     *            the new face texture scale down y multiplies.
     */
    public void setTextureScaleY(int[] textureScaleY) {
        this.textureScaleY = textureScaleY;
    }


    /**
     * Gets the face texture scale down z multipliers.
     * 
     * @return the face texture scale down z multipliers.
     */
    public int[] getTextureScaleZ() {
        return textureScaleZ;
    }


    /**
     * Sets the face texture scale down z multipliers.
     * 
     * @param textureScaleZ
     *            the new face texture scale down z multipliers.
     */
    public void setTextureScaleZ(int[] textureScaleZ) {
        this.textureScaleZ = textureScaleZ;
    }


    /**
     * Gets the face texture rotation values.
     * 
     * @return the textureRotation the face texture rotation values.
     */
    public byte[] getTextureRotation() {
        return textureRotation;
    }


    /**
     * Sets the face texture rotation values.
     * 
     * @param textureRotation
     *            the new face texture rotation values.
     */
    public void setTextureRotation(byte[] textureRotation) {
        this.textureRotation = textureRotation;
    }


    /**
     * Gets the face texture direction values.
     * 
     * @return the textureRotation the face texture direction values.
     */
    public byte[] getTextureDirection() {
        return textureDirection;
    }


    /**
     * Sets the face texture direction values.
     * 
     * @param textureDirection
     *            the new face texture direction values.
     */
    public void setTextureDirection(byte[] textureDirection) {
        this.textureDirection = textureDirection;
    }


    /**
     * Gets the face texture speed values.
     * 
     * @return the face texture speed values.
     */
    public int[] getTextureSpeed() {
        return textureSpeed;
    }


    /**
     * Sets the face texture speed values.
     * 
     * @param textureSpeed
     *            the new face texture speed values.
     */
    public void setTextureSpeed(int[] textureSpeed) {
        this.textureSpeed = textureSpeed;
    }


    /**
     * Gets the face texture x translations.
     * 
     * @return the face texture x translations.
     */
    public int[] getTextureTransX() {
        return textureTransV;
    }


    /**
     * Sets the face texture x translations.
     * 
     * @param textureTransX
     *            the new face texture x translations.
     */
    public void setTextureTransX(int[] textureTransX) {
        textureTransV = textureTransX;
    }


    /**
     * Gets the face texture x translations.
     * 
     * @return the face texture x translations.
     */
    public int[] getTextureTransY() {
        return textureTransU;
    }


    /**
     * Sets the face texture y translations.
     * 
     * @param textureTransY
     *            the new face texture y translations.
     */
    public void setTextureTransY(int[] textureTransY) {
        textureTransU = textureTransY;
    }


    /**
     * Gets the particle emitters.
     * 
     * @return the particle emitters.
     */
    public EmissiveTriangle[] getEmitters() {
        return emitters;
    }


    /**
     * Sets the particle emitters.
     * 
     * @param emitters
     *            the new particle emitters.
     */
    public void setEmitters(EmissiveTriangle[] emitters) {
        this.emitters = emitters;
    }


    /**
     * Gets the particle effectors.
     * 
     * @return the particle effectors.
     */
    public EffectiveVertex[] getEffectors() {
        return effectors;
    }


    /**
     * Sets the particle effectors.
     * 
     * @param effectors
     *            the new particle effectors.
     */
    public void setEffectors(EffectiveVertex[] effectors) {
        this.effectors = effectors;
    }


    /**
     * Gets the billboards.
     * 
     * @return the billboards.
     */
    public FaceBillboard[] getBillboards() {
        return billboards;
    }


    /**
     * Sets the billboards.
     * 
     * @param billboards
     *            the new billboards to set.
     */
    public void setBillboards(FaceBillboard[] billboards) {
        this.billboards = billboards;
    }

    /**
     * Packs vertices into a float array for rendering
     * @return
     */
    public float[] getVerticesAsFloat() {
        float[] vals = new float[numFaces * 9];
        for(int index = 0;index<numFaces;index++) {
            int baseIndex = 9 * index;
            int faceX = faceIndicesA[index];
            int faceY = faceIndicesA[index];
            int faceZ = faceIndicesA[index];
            vals[baseIndex] = verticesX[faceX] * 1f;
            vals[baseIndex + 1] = verticesY[faceY] * 1f;
            vals[baseIndex + 2] = verticesZ[faceZ] * 1f;

            faceX = faceIndicesB[index];
            faceY = faceIndicesB[index];
            faceZ = faceIndicesB[index];

            vals[baseIndex + 3] = verticesX[faceX] * 1f;
            vals[baseIndex + 4] = verticesY[faceY] * 1f;
            vals[baseIndex + 5] = verticesZ[faceZ] * 1f;


            faceX = faceIndicesC[index];
            faceY = faceIndicesC[index];
            faceZ = faceIndicesC[index];


            vals[baseIndex + 6] = verticesX[faceX] * 1f;
            vals[baseIndex + 7] = verticesY[faceY] * 1f;
            vals[baseIndex + 8] = verticesZ[faceZ] * 1f;
        }
        return vals;
    }

    /**
     * Packs face indices into an int array for rendering
     * @return
     */
    public int[] getMappedFaces() {
        int[] vals = new int[numFaces * 3];
        for(int index = 0;index<numFaces;index++) {
            int baseIndex = index * 3;
            vals[baseIndex] = index * 9;
            vals[baseIndex + 1] = index + 1 * 9;
            vals[baseIndex + 2] = index + 2 * 9;
        }
        return vals;
    }

    /**
     * Maps face colors into a float array for rendering
     * @return
     */
    public float[] getMappedColours() {
        float[] val = new float[numFaces * 12];
        for(int index = 0;index<numFaces;index++) {
            int baseIndex = 12 * index;
            int rgb = ColorUtilities.forHSBColor(faceColors[index]);
            float alpha = getFaceAlphas() == null ? 1f : getFaceAlphas()[index] / 256f;
            for (int pass = 0; pass < 3; pass++) {
                val[baseIndex + (pass * 4)] = (float) ((rgb >> 16) & 0xff) / 256f;
                val[baseIndex + (pass * 4) + 1] = (float) ((rgb >> 8) & 0xff) / 256f;
                val[baseIndex + (pass * 4) + 2] = (float) (rgb & 0xff) / 256f;
                val[baseIndex + (pass * 4) + 3] = (float) alpha;
            }
        }
        return val;
    }

    public float[] getMappedTextures() {
        float[] val = new float[numTextures * 3];
        for (int index = 0 ; index < numTextures; index++) {
            val[index] = (float) (getTextureMappingP()[index]);
            val[index + 1] = (float) (getTextureMappingM()[index]);
            val[index + 2] = (float) (getTextureMappingN()[index]);
        }
        return val;
    }

}