#include "structure.hpp"


using namespace vcl;

/* Constants for drone graphics */
float arm_length = 1.2f;
float arm_radius = 0.1f;
float rotor_radius_major = 0.3f;
float rotor_radius_minor = 0.05f;
float scale = 0.05f;
float default_drone_height = 0.1f;
float leg_radius = 0.05;
float leg_length = 0.5;
float body_length = 0.8f;
float body_width = 0.3f;
float body_height = 0.1f;
float camera_radius = 0.1f;


/* Constants for drone dynamics*/
float drone_speed = 0.00f;

/* Constants for recuit */
decroissance_type _decroissance_type = decroissance_type::N;
double initial_temperature = 1;
unsigned int number_of_steps = 100;
unsigned int number_of_steps_without_changes = 10;


checkpoint::checkpoint(vec3 _position) 
{
	this->position = _position;
}

vec3 checkpoint::get_position() 
{
	return position;
}

drone::drone(vec3 _position) 
{
	this->position = _position;
}

void drone::move(vec3 target) 
{
	this->position += normalize(target - position) * drone_speed;
}

vec3 drone::get_position() 
{
	return position;
}

void drone::set_position(vcl::vec3 _position)
{
	this->position = _position;
}


hierarchy_mesh_drawable get_drone_mesh_drawable() {

	hierarchy_mesh_drawable result;
	mesh_drawable body = mesh_drawable(mesh_primitive_cubic_grid(
		vec3(-body_length / 2, -body_width / 2, -body_height / 2),
		vec3(-body_length / 2, -body_width / 2, body_height / 2),
		vec3(-body_length / 2, body_width / 2, body_height / 2),
		vec3(-body_length / 2, body_width / 2, -body_height / 2),
		vec3(body_length / 2, -body_width / 2, -body_height / 2),
		vec3(body_length / 2, -body_width / 2, body_height / 2),
		vec3(body_length / 2, body_width / 2, body_height / 2),
		vec3(body_length / 2, body_width / 2, -body_height / 2)));
	mesh_drawable arm = mesh_drawable(mesh_primitive_cubic_grid(
		vec3(-arm_length / 2, -arm_radius / 2, -arm_radius / 2),
		vec3(-arm_length / 2, -arm_radius / 2, arm_radius / 2),
		vec3(-arm_length / 2, arm_radius / 2, arm_radius / 2),
		vec3(-arm_length / 2, arm_radius / 2, -arm_radius / 2),
		vec3(arm_length / 2, -arm_radius / 2, -arm_radius / 2),
		vec3(arm_length / 2, -arm_radius / 2, arm_radius / 2),
		vec3(arm_length / 2, arm_radius / 2, arm_radius / 2),
		vec3(arm_length / 2, arm_radius / 2, -arm_radius / 2)));
	mesh_drawable camera = mesh_drawable(mesh_primitive_sphere(camera_radius));
	mesh_drawable leg = mesh_drawable(mesh_primitive_cylinder(leg_radius, { 0.7 * arm_length / 2, 0, 0 }, { 0.7 * arm_length / 2 + leg_length / 2, 0, -arm_length / 2 }));
	mesh_drawable rotor = mesh_drawable(mesh_primitive_torus(rotor_radius_major, rotor_radius_minor));

	mesh logo_mesh = mesh();
	logo_mesh.position = buffer<vec3>({
		vec3(body_length / 2, body_width / 2, body_height / 1.9),
		vec3(body_length / 2, -body_width / 2, body_height / 1.9),
		vec3(-body_length / 2, -body_width / 2, body_height / 1.9),
		vec3(-body_length / 2, body_width / 2, body_height / 1.9)
	});
	logo_mesh.connectivity = buffer<uint3>({ uint3(0, 1, 2), uint3(0, 2, 3) });
	logo_mesh.uv = buffer<vec2>({
		vec2(0, 1.5),
		vec2(1, 1.5),
		vec2(1, -0.5),
		vec2(0, -0.5)
		});
	image_raw logo_texture;
	try
	{
		logo_texture = image_load_png("assets/logo_x.png");
	}
	catch (const std::exception&)
	{
		logo_texture = image_load_png("../assets/logo_x.png");
	}
	GLuint const logo_texture_id = opengl_texture_to_gpu(logo_texture, GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE);
	logo_mesh.fill_empty_field();
	mesh_drawable logo_visual = mesh_drawable(logo_mesh);
	logo_visual.texture = logo_texture_id;


	body.shading.color = vec3(0.0f, 0.0f, 0.0f);
	camera.shading.color = vec3(0.3f, 0.3f, 0.3f);
	arm.shading.color = vec3(0.0f, 0.0f, 0.0f);
	leg.shading.color = vec3(0.0f, 0.0f, 0.0f);
	rotor.shading.color = vec3(0.5f, 0.5f, 0.5f);

	result.add(mesh_drawable(), "root");
	result.add(body, "body", "root");
	result.add(logo_visual, "logo", "body");
	result.add(camera, "camera", "body", vec3(0.45 * body_length, 0, -0.5 * body_height));
	result.add(arm, "first_arm", "body");
	result.add(arm, "second_arm", "body");
	result.add(rotor, "first_left_rotor", "first_arm", vec3(arm_length / 2 + rotor_radius_major, 0, 0));
	result.add(rotor, "first_right_rotor", "first_arm", vec3(-arm_length / 2 - rotor_radius_major, 0, 0));
	result.add(rotor, "second_left_rotor", "second_arm", vec3(-arm_length / 2 - rotor_radius_major, 0, 0));
	result.add(rotor, "second_right_rotor", "second_arm", vec3(arm_length / 2 + rotor_radius_major, 0, 0));
	result.add(leg, "front_left_leg", "first_arm");
	result.add(leg, "front_right_leg", "second_arm");
	result.add(leg, "rear_left_leg", "first_arm");
	result.add(leg, "rear_right_leg", "second_arm");

	result["first_arm"].transform.rotate = rotation(vec3(0, 0, 1), pi / 4);
	result["second_arm"].transform.rotate = rotation(vec3(0, 0, 1), -pi / 4);
	result["root"].transform.scale = scale;
	result["rear_right_leg"].transform.rotate = rotation(vec3(0, 0, 1), pi);
	result["rear_left_leg"].transform.rotate = rotation(vec3(0, 0, 1), pi);

	result.update_local_to_global_coordinates();

	return result;
}

path::path() {
	this->mean = vec3(0, 0, 0);
	this->size = 0;
	this->order = std::map<int, checkpoint*>();
	this->indices = std::map<checkpoint*, int>();
}

double path::distance(vcl::vec3 vector) {
	return norm(vector - mean);
}

void path::compute_mean()
{
	if (size == 0) mean = vec3(0, 0, 0);
	else {
		vec3 result = vec3(0, 0, 0);
		for (std::pair<checkpoint*, int> element : indices) {
			result += element.first->get_position();
		}
		mean = result / size;
	}
}

checkpoint* path::get_checkpoint_after(checkpoint* _checkpoint)
{
	if (_checkpoint == NULL) return NULL;
	if (size <= 1)
		return NULL;
	else {
		int current_index = indices[_checkpoint];
		if (current_index < size - 1)
			return order[current_index + 1];
		else
			return order[0];
	}
}

checkpoint* path::get_checkpoint_before(checkpoint* _checkpoint)
{
	if (_checkpoint == NULL) return NULL;
	if (size <= 1)
		return NULL;
	else {
		int current_index = indices[_checkpoint];
		if (current_index > 0)
			return order[current_index - 1];
		else
			return order[size - 1];
	}
}

checkpoint* path::get_checkpoint_at_index(int index)
{
	return order[index];
}

int path::get_checkpoint_index(checkpoint* _checkpoint)
{
	return indices[_checkpoint];
}

std::vector<checkpoint*> path::get_checkpoints_ordered()
{
	std::vector<checkpoint*> result = std::vector<checkpoint*>(size);
	
	for (int i = 0; i < size; i++) {
		result[i] = order[i];
	}

	return result;
}

void path::add_checkpoint(checkpoint* _checkpoint)
{
	order.insert(std::make_pair(size, _checkpoint));
	indices.insert(std::make_pair(_checkpoint, size));
	size++;
	compute_mean();
}

void path::remove_checkpoint(checkpoint* _checkpoint)
{
	int index = indices[_checkpoint];
	swap_order(index, size - 1);
	remove_last_checkpoint();
}

void path::swap_order(int first_index, int second_index)
{
	checkpoint* first_checkpoint = order[first_index];
	checkpoint* second_checkpoint = order[second_index];
	order[second_index] = first_checkpoint;
	order[first_index] = second_checkpoint;
	indices[second_checkpoint] = first_index;
	indices[first_checkpoint] = second_index;

}

int path::get_size()
{
	return size;
}

void path::clear()
{
	size = 0;
	indices.clear();
	order.clear();
	mean = vec3(rand_interval(), rand_interval(), rand_interval());
}

void path::remove_last_checkpoint()
{
	checkpoint* previous_last_checkpoint = order[size - 1];
	order.erase(size - 1);
	indices.erase(previous_last_checkpoint);
	size--;
	compute_mean();
}

cluster::cluster() 
{
	this->current_target = NULL;
	this->_path = path();
}

void cluster::improve_path()
{
	improve_recuit(this);
}

double cluster::distance(vec3 _vector)
{
	return _path.distance(_vector);
}

checkpoint* cluster::get_current_target()
{
	return current_target;
}

void cluster::move_target_forward()
{
	current_target = _path.get_checkpoint_after(current_target);
}

std::vector<checkpoint*> cluster::get_checkpoints_ordered()
{
	return _path.get_checkpoints_ordered();
}

void cluster::add_checkpoint(checkpoint* _checkpoint)
{
	if (current_target == NULL) current_target = _checkpoint;
	_path.add_checkpoint(_checkpoint);
}

void cluster::remove_checkpoint(checkpoint* _checkpoint)
{
	if (current_target == _checkpoint)
		current_target = _path.get_checkpoint_after(_checkpoint);
	_path.remove_checkpoint(_checkpoint);
}

void cluster::clear()
{
	current_target = NULL;
	_path.clear();
}

int cluster::get_size()
{
	return _path.get_size();
}

std::pair<int, int> cluster::modification_function()
{
	int size = _path.get_size();
	return std::make_pair((int)(rand_interval() * size), (int)(rand_interval() * size));
}

double cluster::improvement_function(std::pair<int, int> modification)
{
	double result = 0;
	checkpoint* first_checkpoint = _path.get_checkpoint_at_index(modification.first);
	checkpoint* second_checkpoint = _path.get_checkpoint_at_index(modification.second);
	checkpoint* checkpoint_before_first = _path.get_checkpoint_before(first_checkpoint);
	checkpoint* checkpoint_after_second = _path.get_checkpoint_after(second_checkpoint);
	result += norm(checkpoint_before_first->get_position() - second_checkpoint->get_position());
	result += norm(checkpoint_after_second->get_position() - first_checkpoint->get_position());
	result -= norm(first_checkpoint->get_position() - checkpoint_before_first->get_position());
	result -= norm(second_checkpoint->get_position() - checkpoint_after_second->get_position());
	return result;
}

void cluster::commit_function(std::pair<int, int> swap)
{
	int size = _path.get_size();
	int increasing_index = swap.first;
	int decreasing_index = swap.second;
	if (increasing_index == decreasing_index)
		return;
	if (increasing_index < decreasing_index) {
		while (increasing_index < decreasing_index) {
			_path.swap_order(increasing_index, decreasing_index);
			increasing_index++;
			decreasing_index--;
		}
	}
	else {
		bool changed = false;
		while (increasing_index < decreasing_index && !changed) {
			_path.swap_order(increasing_index, decreasing_index);
			increasing_index++;
			decreasing_index--;
			if (increasing_index == size) {
				changed = true;
				increasing_index = 0;
			}
			if (increasing_index == -1) {
				changed = true;
				decreasing_index = size - 1;
			}
		}
	}
}

void improve_recuit(cluster* _cluster)
{
	int size = _cluster->get_size();
	if (size <= 3)
		return;
	int current_steps = 0;
	int current_steps_without_change = 0;
	double currentTemperature = 0;
	double improvement = 0;
	std::pair<int, int> modification;
	number_of_steps = 1000 * size;
	number_of_steps_without_changes = 5 * size;

	while (current_steps < number_of_steps && current_steps_without_change < number_of_steps_without_changes) {
		currentTemperature = temperature(current_steps);
		modification = _cluster->modification_function();
		improvement =_cluster->improvement_function(modification);
		if (h(exp(-improvement / currentTemperature)) > rand_interval()) {
			_cluster->commit_function(modification);
			current_steps_without_change = 0;
		}
		else
			current_steps_without_change += 1;
		current_steps++;
	}
}

double temperature(double time)
{
	switch (_decroissance_type) {
	case decroissance_type::LOG: return initial_temperature / log(time + 1);
	case(decroissance_type::N): return initial_temperature / (time + 1);
	case(decroissance_type::N2): return initial_temperature / pow(time + 1, 2);
	}
}

double h(double x)
{
	return x / (1 + x);
}

void cluster_attribution::add_drone(drone* _drone, improve_type _improve_type)
{
	drones.push_back(_drone);
	cluster* _cluster = new cluster();
	association.insert(std::make_pair(_drone, _cluster));
	_clustering.add_cluster(_cluster, _improve_type);

}

cluster* cluster_attribution::get_drone_cluster(drone* _drone)
{
	return association[_drone];
}

void cluster_attribution::add_checkpoint(checkpoint* _checkpoint, improve_type _improve_type)
{
	_clustering.add_checkpoint(_checkpoint, _improve_type);
	improve(_improve_type);
}

void cluster_attribution::improve(improve_type _improve_type)
{
	_clustering.improve(_improve_type);
}

std::vector<drone*> cluster_attribution::get_drones()
{
	return drones;
}

void cluster_attribution::move()
{
	checkpoint* _target;
	cluster* _cluster;
	for (drone* _drone : drones) {
		_cluster = association[_drone];
		_target = _cluster->get_current_target();
		if (_target == NULL) continue;
		else if (norm(_target->get_position() - _drone->get_position()) < drone_speed) {
			_drone->set_position(_target->get_position());
			_cluster->move_target_forward();
		}
		else
			_drone->move(_target->get_position());
	}
}

void clustering_k_means::add_checkpoint(checkpoint* _checkpoint, improve_type _improve_type)
{
	for (cluster* _cluster : available_clusters) {
		if (_cluster->get_size() == 0) {
			_cluster->add_checkpoint(_checkpoint);
			association.insert(std::make_pair(_checkpoint, _cluster));
			improve(_improve_type);
			return;
		}
	}
	cluster* _cluster = available_clusters[0];
	_cluster->add_checkpoint(_checkpoint);
	association[_checkpoint] = _cluster;
	improve(_improve_type);
}

void clustering_k_means::add_cluster(cluster* _cluster, improve_type _improve_type)
{
	available_clusters.push_back(_cluster);
	improve(_improve_type);
}

void clustering_k_means::improve(improve_type _improve_type)
{
	switch (_improve_type) {
	case(improve_type::NONE):
		return;
	case(improve_type::SIMPLE):
		for (cluster* cluster : available_clusters)
			cluster->improve_path();
		return;
	case(improve_type::COMPLETE):
		bool modified = true;
		while (modified)
			modified = improve_one_step();
		for (cluster* _cluster : available_clusters)
			_cluster->improve_path();
		return;
	}
}

bool clustering_k_means::improve_one_step()
{
	std::map<checkpoint*, cluster*> modifications;
	bool modified = false;
	double distance = 0;
	double current_min_distance = 0;
	cluster* _cluster = 0;
	cluster* _min_cluster = 0;
	for (std::pair<checkpoint*, cluster*> pair : association) {
		_cluster = pair.second;
		current_min_distance = _cluster->distance(pair.first->get_position());
		_min_cluster = 0;
		for (cluster* other_cluster : available_clusters) {
			distance = other_cluster->distance(pair.first->get_position());
			if (distance < current_min_distance) {
				current_min_distance = distance;
				_min_cluster = other_cluster;
				modified = true;
			}
		}
		if (_min_cluster != NULL) {
			modifications.insert(std::make_pair(pair.first, _min_cluster));
		}
	}
	for (std::pair<checkpoint*, cluster*> pair : modifications) {
		cluster* _old_cluster = association[pair.first];
		if (association.count(pair.first) == 0) association.insert(pair);
		else association[pair.first] = pair.second;
		_old_cluster->remove_checkpoint(pair.first);
		pair.second->add_checkpoint(pair.first);
	}
	return modified;
}

int cluster_attribution::get_size() {
	return drones.size();
}
