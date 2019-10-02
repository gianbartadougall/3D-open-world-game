# version 400 core

in vec2 pass_TextureCoordinates;
in vec3 out_Normal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in vec4 positionRelativeToCamera;

out vec4 out_Color;

// Texture uniforms
uniform sampler2D textureSample;

// fog uniforms
uniform vec4 backgroundColor;
uniform float fogDensity;
uniform float fogGradient;

// Lighting uniforms
uniform vec3 lightPosition[4];
uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform float lightIntensity[4];
uniform float shineDamper;
uniform float reflectivity;

void main(void) {

    // normalizing variables
    vec3 unitOut_Normal = normalize(out_Normal);
    vec3 unitToCameraVector = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for (int i=0; i<4; i++) {
        // normalizing light variables
        vec3 unitToLightVector = normalize(toLightVector[i]);

        // add attenuation
        float lightLength = length(toLightVector[i]);
        float attenFactor = attenuation[i].x + (attenuation[i].y * lightLength) + (attenuation[i].z * lightLength * lightLength);

        // add diffuse light
        float pixelBrightness = max(dot(normalize(out_Normal), unitToLightVector), 0.0);

        // add specular lighting
        vec3 lightDirection = -unitToLightVector; // returns vector going from the light pos to the vertex pos
        vec3 reflectedLightDirection = reflect(lightDirection, unitOut_Normal); // returns the direction of the light that is reflected
        float reflectedAmount = max(dot(reflectedLightDirection, unitToCameraVector), 0.0); // returns how close the relfected light is to the cameres pos
        float dampingAmount = pow(reflectedAmount, shineDamper);

        // add light intensity
        pixelBrightness = pixelBrightness * lightIntensity[i];

        totalDiffuse = totalDiffuse + (pixelBrightness * lightColor[i]) / attenFactor;
        totalSpecular = totalSpecular + (dampingAmount * reflectivity * lightColor[i]) / attenFactor;
    }

    // add ambient Lighting
    totalDiffuse = max(totalDiffuse, 0.2);

    // add fog
    float distance = length(positionRelativeToCamera.xyz);
    float visibility = exp(-pow((distance * fogDensity), fogGradient));
    visibility = clamp(visibility, 0.0, 1.0);

    out_Color = texture(textureSample, pass_TextureCoordinates) * vec4(totalDiffuse, 1.0) + vec4(totalSpecular, 1.0);
    out_Color = mix(backgroundColor, out_Color, visibility);
}