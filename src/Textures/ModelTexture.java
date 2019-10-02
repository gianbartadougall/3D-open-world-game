package Textures;

public class ModelTexture {

    private int textureId,
                reflectivity,
                shineDamper;

    public ModelTexture(int textureId, int shineDamper, int reflectivity) {
        this.textureId = textureId;
        this.shineDamper = shineDamper;
        this.reflectivity =reflectivity;
    }

    public int getTextureId() {
        return textureId;
    }

    public int getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(int reflectivity) {
        this.reflectivity = reflectivity;
    }

    public int getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(int shineDamper) {
        this.shineDamper = shineDamper;
    }
}
