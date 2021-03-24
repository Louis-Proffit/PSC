
#include "structure.hpp"

using namespace vcl;

// Graphical constants
float arm_length = 1.0f;
float arm_radius = 0.1f;
float rotor_radius_major = 0.3f;
float rotor_radius_minor = 0.05f;
float scale = 0.02f;
float default_drone_height = 0.1f;

float Drone::size = 0.02f;
float Drone::speed = 0.003f;
int Drone::direction_samples = 50;
int Drone::measures_per_sample = 10;
float Drone::sample_distance = 0.3f;
int Drone::frames_per_step = 10;

int counter = 0;

Drone::Drone()
{
	position = vec3(rand_interval(), rand_interval(), default_drone_height);
	direction = 2 * pi * rand_interval();
}

void Drone::update_position(Terrain *terrain)
{
	if (counter == frames_per_step) {
		float score;
		float best_score = 0.1 * measures_per_sample;
		float best_direction = 0;
		float local_direction = 0;
		for (int i = 0; i <= direction_samples; i++) {
			local_direction = float(i) / direction_samples * 2 * pi;
			score = evaluate_direction(local_direction, terrain);
			if (score < best_score) {
				best_score = score;
				best_direction = local_direction;
			}
		}
		direction = best_direction;
		counter = 0;
	}
	else 
		counter++;
	position += speed * vec3(cos(direction), sin(direction), 0);


}

void Drone::update_visual(hierarchy_mesh_drawable* drone_visual) {

	vec3 const previous_position = drone_visual->operator[]("root").transform.translate;
	rotation bird_rotation = rotation_between_vector({ 1, 0, 0 }, normalize(position - previous_position));

	// Mouvement de l'oiseau
	drone_visual->operator[]("root").transform.translate = position;
	drone_visual->operator[]("root").transform.rotate = rotation(bird_rotation);

	drone_visual->update_local_to_global_coordinates();
}

vcl::vec3 Drone::get_position()
{
	return position;
}

hierarchy_mesh_drawable Drone::get_mesh_drawable()
{

	hierarchy_mesh_drawable result;
	mesh_drawable arm = mesh_drawable(mesh_primitive_cubic_grid(vec3(-arm_length / 2, -arm_radius / 2, -arm_radius / 2), 
		vec3(-arm_length / 2, -arm_radius / 2, arm_radius / 2), 
		vec3(-arm_length / 2, arm_radius / 2, arm_radius / 2), 
		vec3(-arm_length / 2, arm_radius / 2, -arm_radius / 2), 
		vec3(arm_length / 2, -arm_radius / 2, -arm_radius / 2), 
		vec3(arm_length / 2, -arm_radius / 2, arm_radius / 2), 
		vec3(arm_length / 2, arm_radius / 2, arm_radius / 2), 
		vec3(arm_length / 2, arm_radius / 2, -arm_radius / 2)));
	mesh_drawable rotor = mesh_drawable(mesh_primitive_torus(rotor_radius_major, rotor_radius_minor));
	arm.shading.color = vec3(0.0f, 0.0f, 0.0f);
	result.add(mesh_drawable(), "root");
	result.add(arm, "first_arm", "root");
	result.add(arm, "second_arm", "root");
	result.add(rotor, "first_left_rotor", "first_arm", vec3(arm_length / 2 + rotor_radius_major, 0, 0));
	result.add(rotor, "first_right_rotor", "first_arm", vec3(-arm_length / 2 - rotor_radius_major, 0, 0));
	result.add(rotor, "second_left_rotor", "second_arm", vec3(-arm_length / 2 - rotor_radius_major, 0, 0));
	result.add(rotor, "second_right_rotor", "second_arm", vec3(arm_length / 2 + rotor_radius_major, 0, 0));

	result["first_arm"].transform.rotate = rotation(vec3(0, 0, 1), pi / 4);
	result["second_arm"].transform.rotate = rotation(vec3(0, 0, 1), -pi / 4);

	result["root"].transform.translate = position;
	result["root"].transform.scale = scale;

	result.update_local_to_global_coordinates();

	return result;
}

double Drone::evaluate_direction(float local_direction, Terrain* terrain)
{
	assert(local_direction > 0 & local_direction < 360);

	double result = 0;
	float x = position.x;
	float y = position.y;
	float dx = cos(local_direction) * sample_distance / measures_per_sample;
	float dy = sin(local_direction) * sample_distance / measures_per_sample;

	for (int i = 0; i < measures_per_sample; i++) {
		x += dx;
		y += dy;
		result += terrain->evaluate_terrain_live(x, y);
	}

	return result;

}
