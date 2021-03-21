
#include "terrain.hpp"

using namespace vcl;

vec2 hill_center = vec2(0.5f, 0.2f);
float hill_height = -0.2f;
float hill_sigma = 0.5f;

Terrain::Terrain() {
	n = 100;
	m = 100;

	// Configuration, en bleu, du potentiel initial
	initial_potential = initialize_terrain();
	initial_potential.color.resize((n + 1) * (m + 1));
	float z;
	for (int i = 0; i <= n; i++) {
		for (int j = 0; j <= m; j++) {
			z = initial_potential.position[i * (n + 1) + j].z;
			initial_potential.color[i * (n + 1) + j] = vec3(z / hill_height, 1.0f - z / hill_height, 0.0f);
		}
	}

	// Configuration, en rouge, du potentiel courant
	current_potential = initialize_terrain();
	current_potential.color.resize((n + 1) * (m + 1));
	for (int i = 0; i <= n; i++) {
		for (int j = 0; j <= m; j++) {
			z = current_potential.position[i * (n + 1) + j].z;
			current_potential.color[i * (n + 1) + j] = vec3(z / hill_height, 1.0f - z / hill_height, 0.0f);
		}
	}
}

void Terrain::update_current_visual(Drone& drone, mesh_drawable& current_potential_visual)
{
	std::cout << current_potential.position.size() << std::endl;

	vec2 drone_relative_position = vec2(drone.get_position().x, drone.get_position().y);
	float x, y, z;
	for (int i = 0; i <= n; i++) {
		for (int j = 0; j <= m; j++) {
			x = float(i) / n;
			y = float(j) / m;
			z = ((current_potential.position[i * (n + 1) + j] + 0.01f * initial_potential.position[i * (n + 1) + j]) / 1.01f).z;
			current_potential.position[i * (n + 1) + j].z = z;
			current_potential.color[i * (n + 1) + j] = vec3(z / hill_height, 1.0f - z / hill_height, 0.0f);
			if (norm(vec2(x, y) - drone_relative_position) < 0.1f) {
				current_potential.position[i * (n + 1) + j].z = 0.0f;
				current_potential.color[i * (n + 1) + j] = vec3(0.0f, 1.0f, 0.0f);
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

vcl::vec3 Terrain::evaluate_terrain(float x, float y)
{
	float z = hill_height * exp( - pow(norm(vec2(x, y) - hill_center) / hill_sigma, 2));
	return vec3(x, y, z);
}

mesh Terrain::initialize_terrain()
{
	mesh result;
	result.position.resize((n + 1) * (m + 1));
	result.color.resize((n + 1) * (m + 1));
	result.connectivity.resize(2 * n * m);
	float x;
	float y;
	for (int i = 0; i <= n; i++) {
		for (int j = 0; j <= m; j++) {
			x = float(i) / n;
			y = float(j) / m;
			result.position[i * (n + 1) + j] = evaluate_terrain(x, y);
		}
	}
	for (int i = 0; i < n; i++) {
		for (int j = 0; j < m; j++) {
			result.connectivity[2 * (i * n + j)] = uint3(i * (n + 1) + j, (i + 1) * (n + 1) + j, i * (n + 1) + j + 1);
			result.connectivity[2 * (i * n + j) + 1] = uint3((i + 1) * (n + 1) + j, (i + 1) * (n + 1) + j + 1, i * (n + 1) + j + 1);
		}
	}
	result.fill_empty_field();

	return result;
}
