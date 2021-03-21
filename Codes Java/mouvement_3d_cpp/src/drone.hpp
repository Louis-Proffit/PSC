#pragma once

#include "vcl/vcl.hpp"
#include "cluster.hpp"

class Drone {

public:
	static float size;
	static float speed;

	Drone(cluster *cluster);
	vcl::mesh get_mesh();
	void set_position(vcl::vec3* position);
	void update_position();
	vcl::vec3 get_position();

private:
	bool position_initialized;
	cluster *drone_cluster;
	vcl::vec3 target;
	vcl::vec3 *position;
	vcl::mesh drone_mesh;

	vcl::mesh initialize_drone(float size);
};

