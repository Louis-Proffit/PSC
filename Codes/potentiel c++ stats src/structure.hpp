#pragma once

#include "vcl/vcl.hpp"
#include <cstdio>

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
	float Drone::distance_to(vcl::vec3 p);
	float Drone::distance_to(Drone d);
	float Drone::avg_distance(std::vector<Drone> drones, int number_of_drones);

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
	vcl::buffer<bool> obstacles;
	vcl::mesh initial_potential;
	vcl::mesh initialize_terrain();
	vcl::vec3 evaluate_terrain(float x, float y);

public:

	Terrain();
	void update_potential(std::vector<Drone> *drones, vcl::mesh_drawable& terrain_visual);
	vcl::mesh get_initial_mesh();
	vcl::mesh get_current_mesh();
	float Terrain::evaluate_terrain_live(float x, float y);
	double Terrain::avg_insatisfaction();
};

class Statistics
{
public :
	Statistics();
	int Statistics::update_scores(double nbre);
	int Statistics::update_dist(double nbre);
	int Statistics::update_avg_dist(double nbre);

private :

	int actual_size;
	//const int time_limit;
	double list_scores[10000];
	double list_dist[10000];
	double list_avg_dist[10000];
	//bool txt_generated = false;
};


