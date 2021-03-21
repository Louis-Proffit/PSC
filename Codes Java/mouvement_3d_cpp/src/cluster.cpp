#include "cluster.hpp"

using namespace vcl;

cluster::cluster() {
	size = 0;
}

vec3 cluster::get_next_checkpoint(vec3 current_checkpoint) {
	return vec3(0.0f, 0.0f, 0.0f);
}

vec3 cluster::get_first_checkpoint() {
	return vec3(0.0f, 0.0f, 0.0f);
}
