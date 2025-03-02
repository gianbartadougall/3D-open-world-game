# version 400 core

in vec2 position;

out vec4 clipSpace;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

void main(void) {

    clipSpace = projectionMatrix * viewMatrix * transformationMatrix * vec4(position.x, 0.0, position.y, 1.0);
    gl_Position = clipSpace;
}