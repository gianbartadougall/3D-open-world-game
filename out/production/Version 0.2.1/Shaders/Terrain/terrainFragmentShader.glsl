# version 400 core

in float out_numTerrainsOnGrid;
in vec2 pass_TextureCoordinates;
in vec3 out_Normal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in vec4 positionRelativeToCamera;

out vec4 out_Color;

// lighting uniforms
uniform vec3 lightColor[4];
uniform float lightIntensity[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;

// fog uniforms
uniform vec4 backgroundColor;
uniform float fogDensity = 0.003;
uniform float fogGradient = 5.0;

// texture uniforms
uniform sampler2D texture1;
uniform sampler2D texture2;
uniform sampler2D texture3;
uniform sampler2D backgroundTexture;
uniform sampler2D blendMap;

void main(void) {

    // normalizing variables
    vec3 unitOut_Normal = normalize(out_Normal);
    vec3 unitToCameraVector = normalize(toCameraVector);

    // Get the correct terrain texture
    vec4 pixelColor = texture(blendMap, (pass_TextureCoordinates/100)); // this will get the pixel color of the coordinate on the blend map in normalized RGBA format
    float backTextureAmount = 1 - (pixelColor.r + pixelColor.g + pixelColor.b);
    vec4 backTexture = texture(backgroundTexture, pass_TextureCoordinates) * backTextureAmount;
    vec4 rTexture = texture(texture1, pass_TextureCoordinates) * pixelColor.r;
    vec4 gTexture = texture(texture2, pass_TextureCoordinates) * pixelColor.g;
    vec4 bTexture = texture(texture3, pass_TextureCoordinates) * pixelColor.b;
    vec4 finalTextureColor = rTexture + gTexture + bTexture + backTexture;

    vec3 totalDiffuse= vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for (int i=0; i<4; i++) {
        // normalizing light variables
        vec3 unitToLightVector = normalize(toLightVector[i]);

        // add in point lights
        float lightLength = length(toLightVector[i]);
        float attenFactor = attenuation[i].x + (attenuation[i].y * lightLength) + (attenuation[i].z * lightLength * lightLength);

        // adding in diffuse lighting
        float pixelBrightness = max(dot(unitToLightVector, unitOut_Normal), 0.0);

        // find light intensity
        pixelBrightness = pixelBrightness * lightIntensity[i];

        // add specular lighting
        vec3 lightDirection = -unitToLightVector; // returns vector going from the light pos to the vertex pos
        vec3 reflectedLightDirection = reflect(lightDirection, unitOut_Normal); // returns the direction of the light that is reflected
        float reflectedAmount = max(dot(reflectedLightDirection, unitToCameraVector), 0.0); // returns how close the relfected light is to the cameres pos
        float dampingAmount = pow(reflectedAmount, shineDamper);

        totalDiffuse = totalDiffuse + (pixelBrightness * lightColor[i]) / attenFactor;
        totalSpecular = totalSpecular + (dampingAmount * reflectivity * lightColor[i]) / attenFactor;
    }

    // add ambient Lighting
    totalDiffuse = max(totalDiffuse, 0.2);

    // add fog
    float distance = length(positionRelativeToCamera.xyz);
    float visibility = exp(-pow((distance * fogDensity), fogGradient));
    visibility = clamp(visibility, 0.0, 1.0);

    // Calculate final fragment color
    out_Color = vec4(totalDiffuse, 1.0) * finalTextureColor + vec4(totalSpecular, 1.0);
    out_Color = mix(backgroundColor, out_Color, visibility);

}