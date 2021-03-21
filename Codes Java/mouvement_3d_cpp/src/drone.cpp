
#include "cluster.hpp"
#include "drone.hpp"

using namespace vcl;

float Drone::size = 0.02f;
float Drone::speed = 0.01f;

vec3 begin_position = vec3(0.0f, 0.0f, 0.0f);
vec3 target_position = vec3(0.0f, 1.0f, 0.0f);
unsigned int number_of_steps = 0;
unsigned int maximum_number_of_steps = 30;

Drone::Drone(cluster *cluster_instance)
{
	position_initialized = false;
	drone_mesh = initialize_drone(Drone::size);
	drone_cluster = cluster_instance;
	target = cluster_instance->get_first_checkpoint();
	position = 0;
}

void Drone::update_position() 
{
	if (number_of_steps == maximum_number_of_steps) {
		number_of_steps = 0;
		begin_position = target_position;
		target_position = vec3(rand_interval(), rand_interval(), 0.0f);
		
	}
	else {
		number_of_steps++;
		*position += (target_position - begin_position) / maximum_number_of_steps;
	}
}

vcl::vec3 Drone::get_position()
{
	return *position;
}

mesh Drone::get_mesh()
{
	return drone_mesh;
}

void Drone::set_position(vec3 *_position) {
	if (position_initialized) std::cout << "Configuration de la position à nouveau, drone.cpp" << std::endl;
	position = _position;
	position_initialized = true;
}

mesh Drone::initialize_drone(float size)
{
	mesh result;
	result.position.resize(8);
	result.color.resize(8);
	result.connectivity.resize(12);
	result.position[0] = vec3(0.0f, 0.0f, 0.0f);
	result.position[1] = vec3(0.0f, 0.0f, size);
	result.position[2] = vec3(0.0f, size, 0.0f);
	result.position[3] = vec3(0.0f, size, size);
	result.position[4] = vec3(size, 0.0f, 0.0f);
	result.position[5] = vec3(size, 0.0f, size);
	result.position[6] = vec3(size, size, 0.0f);
	result.position[7] = vec3(size, size, size);
	result.color[0] = vec3(0.0f, 0.0f, 1.0f);
	result.color[1] = vec3(0.0f, 0.0f, 1.0f);
	result.color[2] = vec3(0.0f, 0.0f, 1.0f);
	result.color[3] = vec3(0.0f, 0.0f, 1.0f);
	result.color[4] = vec3(0.0f, 0.0f, 1.0f);
	result.color[5] = vec3(0.0f, 0.0f, 1.0f);
	result.color[6] = vec3(0.0f, 0.0f, 1.0f);
	result.color[7] = vec3(0.0f, 0.0f, 1.0f);
	result.connectivity[0] = uint3(0, 1, 4);
	result.connectivity[1] = uint3(1, 5, 4);
	result.connectivity[2] = uint3(1, 3, 5);
	result.connectivity[3] = uint3(3, 7, 5);
	result.connectivity[4] = uint3(3, 6, 2);
	result.connectivity[5] = uint3(3, 6, 7);
	result.connectivity[6] = uint3(2, 0, 4);
	result.connectivity[7] = uint3(2, 4, 6);
	result.connectivity[8] = uint3(0, 1, 2);
	result.connectivity[9] = uint3(1, 3, 2);
	result.connectivity[10] = uint3(5, 7, 6);
	result.connectivity[11] = uint3(5, 6, 4);

	result.fill_empty_field();

	return result;
}