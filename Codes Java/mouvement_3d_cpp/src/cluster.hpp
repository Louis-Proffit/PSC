#pragma once

#include "vcl/vcl.hpp"

class cluster
{

public:
	cluster();
	vcl::vec3 get_next_checkpoint(vcl::vec3 current_checkpoint);
	vcl::vec3 get_first_checkpoint();


private :
	unsigned int size;
};

