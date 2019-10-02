# version 400 core

in vec2 textureCoordinates;
in vec3 position;
in vec3 normal;
in vec3 boundingBox;

out vec2 pass_TextureCoordinates;
out vec3 out_Normal;
out vec3 toLightVector[4];
out vec3 toCameraVector;
out vec4 positionRelativeToCamera;

uniform vec3 transformedPosition;
uniform vec3 lightPosition[4];
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

// water variables
uniform vec4 plane = vec4(0, -1, 0, 50);

void main(void) {

    vec4 position4d = vec4(position, 1.0);

    // calculating the vector the points from the current vertex to the light position
    vec4 worldPosition = transformationMatrix * position4d;

    // water calclations
    gl_ClipDistance[0] = dot(worldPosition, plane);

    for (int i=0; i<4; i++) {
        toLightVector[i] = lightPosition[i] - worldPosition.xyz;
    }

    // assigning data to out variables
    pass_TextureCoordinates = textureCoordinates;
    out_Normal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

    positionRelativeToCamera = viewMatrix * worldPosition;

    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * position4d;
}