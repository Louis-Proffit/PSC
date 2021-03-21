#pragma once

#include "vcl/vcl.hpp"
#include "drone.hpp"

class Terrain {

private:
	int n, m;
	vcl::mesh current_potential;
	vcl::mesh initial_potential;
	vcl::mesh initialize_terrain();

public:

	Terrain();
	void update_current_visual(Drone& drone, vcl::mesh_drawable& terrain_visual);
	vcl::mesh get_initial_mesh();
	vcl::mesh get_current_mesh();
	vcl::vec3 evaluate_terrain(float x, float y);

};

