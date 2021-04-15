#include "structure.hpp"

using namespace vcl;

float potential_min = -0.2f;
float potential_max = 0.03f;
float potential_decrease = 0.0001f;
float potential_value_at_reset = 0.03f;
float drone_surveillance_radius = 0.05f;

// Colors
vec3 red = vec3(1.0f, 0.0f, 0.0f);
vec3 green = vec3(0.0f, 1.0f, 0.0f);

Terrain::Terrain() {
	n = 200;
	m = 100;

	// Création des colines
	hill hill_0{ vec2(0.5, 0.8), 0.1, 0.2 };
	hill hill_1{ vec2(0.2, 0.5), 0.4, 0.4 };
	hill hill_2{ vec2(0.1, 0.1), 0.2, 0.2 };
	hills_list.push_back(hill_1);
	hills_list.push_back(hill_0);
	hills_list.push_back(hill_2);

	// Création des obstacles
	obstacle obstacle_1{ vec2(0.2, 0.9), 0.2 };
	obstacle obstacle_2{ vec2(0.4, 0.4), 0.1 };
	obstacle obstacle_3{ vec2(0.9, 0.2), 0.15 };
	obstacles_list.push_back(obstacle_1);
	obstacles_list.push_back(obstacle_2);
	obstacles_list.push_back(obstacle_3);

	obstacles.resize((n + 1) * (m + 1));
	for (int i = 0; i <= n; i++) {
		for (int j = 0; j <= m; j++) {
			for (obstacle _obstacle : obstacles_list) {
				if (_obstacle.is_in_bounds(vec2(float(i) / n, float(j) / m)))
					obstacles[i * (m + 1) + j] = true;
			}
		}
	}

	// Configuration, en bleu, du potentiel initial
	initialize_terrain();	
}

void Terrain::update_potential(std::vector<Drone> *drones, mesh_drawable& current_potential_visual)
{
	float x, y, z;
	for (int i = 0; i <= n; i++) {
		for (int j = 0; j <= m; j++) {
			if (obstacles[i * (m + 1) + j]) continue;
			x = float(i) / n;
			y = float(j) / m;
			z = ((current_potential.position[i * (m + 1) + j] + potential_decrease * initial_potential.position[i * (m + 1) + j]) / (1 + potential_decrease)).z;
			current_potential.position[i * (m + 1) + j].z = z;
			current_potential.color[i * (m + 1) + j] = get_color_from_height(i, j);

			for (int k = 0; k < drones->size(); k++) {
				if (norm(vec2(x, y) - drones->operator[](k).get_position().xy()) < drone_surveillance_radius) {
					current_potential.position[i * (m + 1) + j].z = potential_value_at_reset;
					current_potential.color[i * (m + 1) + j] = vec3(0.0f, 1.0f, 0.0f);
				}
			}
		}
	}

	current_potential.compute_normal();

	current_potential_visual.update_position(current_potential.position);
	current_potential_visual.update_normal(current_potential.normal);
	current_potential_visual.update_color(current_potential.color);
}

mesh Terrain::get_initial_mesh()
{
	return initial_potential;
}

mesh Terrain::get_current_mesh()
{
	return current_potential;
}

float Terrain::evaluate_terrain_live(float x, float y)
{
	
	int i = int(x * n);
	int j = int(y * m);

	float result = 0;

	result += current_potential.position[i * (m + 1) + j].z;
	result += current_potential.position[(i + 1) * (m + 1) + j].z;
	result += current_potential.position[i * (m + 1) + j + 1].z;
	result += current_potential.position[(i + 1) * (m + 1) + j + 1].z;

	return result / 4;
}

vec3 Terrain::evaluate_terrain(float x, float y)
{
	if (x < 0 || x > 1 || y < 0 || y > 1) return vec3(x, y, 0);

	float z = 0;
	hill _hill;
	for (int i = 0; i < hills_list.size(); i++) {
		_hill = hills_list[i];
		z -= _hill.height * exp(-pow(norm(vec2(x, y) - _hill.center) / _hill.sigma, 2));
	}

	return vec3(x, y, z);
}


vec3 Terrain::get_color_from_height(int i, int j) 
{
	if (obstacles[i * (m + 1) + j] == true) return vec3(0, 0, 0);
	float t = (current_potential.position[i * (m + 1) + j].z - potential_min) / (potential_max - potential_min);
	return t * green + (1 - t) * red;
}

void Terrain::initialize_terrain()
{
	initial_potential.position.resize((n + 1) * (m + 1));
	initial_potential.color.resize((n + 1) * (m + 1));
	initial_potential.connectivity.resize(2 * n * m);


	current_potential.position.resize((n + 1) * (m + 1));
	current_potential.color.resize((n + 1) * (m + 1));
	current_potential.connectivity.resize(2 * n * m);

	float x, y;
	for (int i = 0; i <= n; i++) {
		for (int j = 0; j <= m; j++) {
			x = float(i) / n;
			y = float(j) / m;
			initial_potential.position[i * (m + 1) + j] = evaluate_terrain(x, y);
			current_potential.position[i * (m + 1) + j] = evaluate_terrain(x, y);
			if (obstacles[i * (m + 1) + j]) current_potential.position[i * (m + 1) + j].z = 0;
		}
	}
	for (int i = 0; i < n; i++) {
		for (int j = 0; j < m; j++) {
			initial_potential.connectivity[2 * (i * m + j)] = uint3(i * (m + 1) + j, (i + 1) * (m + 1) + j, i * (m + 1) + j + 1);
			initial_potential.connectivity[2 * (i * m + j) + 1] = uint3((i + 1) * (m + 1) + j, (i + 1) * (m + 1) + j + 1, i * (m + 1) + j + 1);
			current_potential.connectivity[2 * (i * m + j)] = uint3(i * (m + 1) + j, (i + 1) * (m + 1) + j, i * (m + 1) + j + 1);
			current_potential.connectivity[2 * (i * m + j) + 1] = uint3((i + 1) * (m + 1) + j, (i + 1) * (m + 1) + j + 1, i * (m + 1) + j + 1);
		}
	}

	for (int i = 0; i <= n; i++) {
		for (int j = 0; j <= m; j++) {
			initial_potential.color[i * (m + 1) + j] = get_color_from_height(i, j);
			current_potential.color[i * (m + 1) + j] = get_color_from_height(i, j);
		}
	}

	initial_potential.fill_empty_field();
	current_potential.fill_empty_field();
}

bool obstacle::is_in_bounds(vcl::vec2 position)
{
	return (norm(position - center) < radius);
}
