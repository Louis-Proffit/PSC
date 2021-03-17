#version 330 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec4 color;

out vec4 color_frag;
uniform float time;

void main()
{
    color_frag = vec4(pow(sin(time + position[0]), 2.0), 0, 0, 0);
    gl_Position = position;
}
