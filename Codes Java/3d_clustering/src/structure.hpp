# include "vcl/vcl.hpp"
# include "interpolation.hpp" 

enum class decroissance_type 
{
	LOG, N, N2
};

enum class improve_type 
{
	NONE, SIMPLE, COMPLETE
};

class checkpoint
{
private:
	vcl::vec3 position;

public:
	vcl::vec3 get_position();
	checkpoint(vcl::vec3 position);
};

class drone 
{
public:
	void move(vcl::vec3 new_position);
	vcl::vec3 get_position();
	void set_position(vcl::vec3 _position);
	drone(vcl::vec3 position);

private:
	vcl::vec3 position;
};

class path 
{

public:
	path();
	double distance(vcl::vec3 vector);
	void compute_mean();
	checkpoint* get_checkpoint_after(checkpoint* _checkpoint);
	checkpoint* get_checkpoint_before(checkpoint* _checkpoint);
	checkpoint* get_checkpoint_at_index(int index);
	int get_checkpoint_index(checkpoint* _checkpoint);
	vcl::buffer<vcl::vec3> get_positions_ordered();
	void add_checkpoint(checkpoint* _checkpoint);
	void remove_checkpoint(checkpoint* _checkpoint);
	void swap_order(int first_index, int second_index);
	int get_size();
	void clear();
	vcl::vec3 get_next_position();
	void set_interpolator();


private :
	int size;
	std::map<int, checkpoint*> order;
	std::map<checkpoint*, int> indices;
	vcl::vec3 mean;
	interpolator _interpolator;

	void remove_last_checkpoint();
};

class cluster 
{

public:
	cluster();
	void improve_path();
	double distance(vcl::vec3 _vector);
	void add_checkpoint(checkpoint* _checkpoint);
	void remove_checkpoint(checkpoint* _checkpoint);
	void clear();
	int get_size();
	std::pair<int, int> modification_function();
	double improvement_function(std::pair<int, int> modification);
	void commit_function(std::pair<int, int> modification);
	void set_interpolator();
	vcl::buffer<vcl::vec3> get_positions_ordered();
	vcl::vec3 get_next_position();

private:
	path _path;
};

class clustering_k_means {

public:
	void add_checkpoint(checkpoint* _checkpoint, improve_type _improve_type);
	void add_cluster(cluster* _cluster, improve_type _improve_type);
	void improve(improve_type _improve_type);
	bool improve_one_step();

private:
	std::vector<cluster*> available_clusters;
	std::map<checkpoint*, cluster*> association;
};

class cluster_attribution {

private:
	std::vector<drone*> drones;
	std::map<drone*, cluster*> association;
	clustering_k_means _clustering;

public:
	void add_drone(drone* drone, improve_type _improve_type);
	cluster* get_drone_cluster(drone* drone);
	void add_checkpoint(checkpoint* checkpoint, improve_type _improve_type);
	void improve(improve_type _improve_type);
	std::vector<drone*> get_drones();
	void move();
	int get_size();
	void set_interpolators();

};

vcl::hierarchy_mesh_drawable get_drone_mesh_drawable();
void improve_recuit(cluster* cluster);
double temperature(double time);
double h(double x);