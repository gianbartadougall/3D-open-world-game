# version 400 core

in vec2 textureCoordinates;
in vec3 position;
in vec3 normal;

out vec2 pass_TextureCoordinates;
out vec3 out_Normal;
out vec3 toLightVector[4];
out vec4 positionRelativeToCamera;
out vec3 toCameraVector;

uniform vec3 lightPosition[4];
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

// clipping plane variable
uniform vec4 plane;

void main(void) {

    // getting world postion
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

    // clipping plane calculations
    gl_ClipDistance[0] = dot(worldPosition, plane);

    // finding the tolight vector
    for (int i=0; i<4; i++) {
        toLightVector[i] = lightPosition[i] - worldPosition.xyz;
    }

    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    positionRelativeToCamera = viewMatrix * worldPosition;
    pass_TextureCoordinates = textureCoordinates * 100;
    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
    out_Normal = normal;
}