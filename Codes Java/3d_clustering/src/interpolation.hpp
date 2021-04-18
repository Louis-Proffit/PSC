#pragma once


#include "vcl/vcl.hpp"

class interpolator {

public:
	void add_key_position(vcl::vec3 key_position);
	void set_key_positions(vcl::buffer<vcl::vec3> positions);
	vcl::vec3 interpolate();
	vcl::buffer<vcl::vec3>* get_key_positions();
	void update_key_times();

private:
	vcl::buffer<vcl::vec3> key_positions;
	vcl::buffer<float> key_times;
	float K = 0.5f;
	float total_time;
	vcl::timer_interval timer;
};

