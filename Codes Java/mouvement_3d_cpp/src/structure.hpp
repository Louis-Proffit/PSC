#pragma once

#include "vcl/vcl.hpp"

struct hill {
	vcl::vec2 center;
	float sigma;
	float height;
};

class Terrain;

class Drone {

public:
	static float size;
	static float speed;
	static int number_of_walks;
	static int random_walk_steps;
	static int frames_per_step;
	static int direction_samples;
	static int measures_per_sample;
	static float sample_distance;
	static float surveillance_radius;

	Drone();
	vcl::hierarchy_mesh_drawable get_mesh_drawable();
	void update_position(Terrain* terrain);
	void update_visual(vcl::hierarchy_mesh_drawable* drone_visual);
	vcl::vec3 get_position();

private:
	float direction;
	vcl::vec3 position;
	double Drone::evaluate_direction(float direction, Terrain* terrain);
};

class Drone;

class Terrain {

private:
	int n, m;
	std::vector<hill> hills;
	vcl::mesh current_potential;
	vcl::mesh initial_potential;
	vcl::mesh initialize_terrain();
	vcl::vec3 evaluate_terrain(float x, float y);

public:

	Terrain();
	void update_potential(std::vector<Drone> *drones, vcl::mesh_drawable& terrain_visual);
	vcl::mesh get_initial_mesh();
	vcl::mesh get_current_mesh();
	float Terrain::evaluate_terrain_live(float x, float y);
};


