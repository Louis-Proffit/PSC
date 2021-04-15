#include "vcl/vcl.hpp"
#include <iostream>
#include <chrono>

#include "structure.hpp"

using namespace vcl;

struct gui_parameters {
	bool display_frame = false;
	bool display_wireframe = false;
};

struct user_interaction_parameters {
	vec2 mouse_prev;
	timer_fps fps_record;
	mesh_drawable global_frame;
	gui_parameters gui;
	bool cursor_on_gui;
};
user_interaction_parameters user;

struct scene_environment
{
	camera_around_center camera;
	mat4 projection;
	vec3 light;
};
scene_environment scene;


void mouse_move_callback(GLFWwindow* window, double xpos, double ypos);
void window_size_callback(GLFWwindow* window, int width, int height);

void initialize_data();
void display_interface();
void update_graphics();
void draw_cluster(cluster* _cluster);
vec3 get_color(float x);
vec3 random_point();

int number_of_drones = 10;
int number_of_checkpoints = 100;

vec3 color_1 = vec3(143.0 / 255, 0.0 / 255, 255.0 / 255);
vec3 color_2 = vec3(75.0 / 255, 0.0 / 255, 130.0 / 255);
vec3 color_3 = vec3(0.0 / 255, 0.0 / 255, 255.0 / 255);
vec3 color_4 = vec3(0.0 / 255, 255.0 / 255, 0.0 / 255);
vec3 color_5 = vec3(255.0 / 255, 255.0 / 255, 0.0 / 255);
vec3 color_6 = vec3(255.0 / 255, 127.0 / 255, 0.0 / 255);
vec3 color_7 = vec3(255.0 / 255, 0.0 / 255, 0.0 / 255);

hierarchy_mesh_drawable drone_visual;
mesh_drawable checkpoint_visual;
curve_drawable cluster_visual;
cluster_attribution _cluster_attribution;


int main(int, char* argv[])
{
	std::cout << "Run " << argv[0] << std::endl;

	int const width = 1280, height = 1024;
	GLFWwindow* window = create_window(width, height);
	window_size_callback(window, width, height);
	std::cout << opengl_info_display() << std::endl;;

	imgui_init(window);
	glfwSetCursorPosCallback(window, mouse_move_callback);
	glfwSetWindowSizeCallback(window, window_size_callback);
	
	std::cout<<"Initialize data ..."<<std::endl;
	initialize_data();

	std::cout<<"Start animation loop ..."<<std::endl;
	user.fps_record.start();
	glEnable(GL_DEPTH_TEST);

	while (!glfwWindowShouldClose(window))
	{
		scene.light = scene.camera.position();
		user.fps_record.update();
		
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);
		glClear(GL_DEPTH_BUFFER_BIT);
		imgui_create_frame();
		if(user.fps_record.event) {
			std::string const title = "VCL Display - "+str(user.fps_record.fps)+" fps";
			glfwSetWindowTitle(window, title.c_str());
		}

		ImGui::Begin("GUI",NULL,ImGuiWindowFlags_AlwaysAutoResize);
		user.cursor_on_gui = ImGui::IsAnyWindowFocused();

		if(user.gui.display_frame) draw(user.global_frame, scene);

		display_interface();
		update_graphics();

		ImGui::End();
		imgui_render_frame(window);
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	imgui_cleanup();
	glfwDestroyWindow(window);
	glfwTerminate();

	return 0;
}

void update_graphics()
{
	_cluster_attribution.move();
	int n = _cluster_attribution.get_size();
	int i = 0;
	for (drone* _drone : _cluster_attribution.get_drones()) {
		checkpoint_visual.shading.color = get_color(float(i) / (n - 1));
		drone_visual["root"].transform.translate = _drone->get_position();
		drone_visual.update_local_to_global_coordinates();
		draw(drone_visual, scene);
		draw_cluster(_cluster_attribution.get_drone_cluster(_drone));
		i++;
	}
}

void initialize_data()
{

	// Basic setups of shaders and camera
	GLuint const shader_mesh = opengl_create_shader_program(opengl_shader_preset("mesh_vertex"), opengl_shader_preset("mesh_fragment"));
	mesh_drawable::default_shader = shader_mesh;
	mesh_drawable::default_texture = opengl_texture_to_gpu(image_raw{1,1,image_color_type::rgba,{255,255,255,255}});

	user.global_frame = mesh_drawable(mesh_primitive_frame());
	user.gui.display_frame = false;
	scene.camera.distance_to_center = 2.5f;
	scene.camera.look_at({-3,1,2}, {0,0,0.5}, {0,0,1});

	vec3 position;
	drone* _drone = 0;
	checkpoint* _checkpoint;
	for (int i = 0; i < number_of_drones; i++) {
		position = random_point();
		_drone = new drone(position);
		_cluster_attribution.add_drone(_drone, improve_type::NONE);
	}
	for (int i = 0; i < number_of_checkpoints; i++) {
		position = random_point();
		_checkpoint = new checkpoint(position);
		_cluster_attribution.add_checkpoint(_checkpoint, improve_type::NONE);
	}
	_cluster_attribution.improve(improve_type::COMPLETE);

	drone_visual = get_drone_mesh_drawable();
	checkpoint_visual = mesh_drawable(mesh_primitive_sphere(0.02f));
	cluster_visual = curve_drawable();

}

void display_interface()
{
	ImGui::Checkbox("Frame", &user.gui.display_frame);
	ImGui::Checkbox("Wireframe", &user.gui.display_wireframe);

}

void window_size_callback(GLFWwindow* , int width, int height)
{
	glViewport(0, 0, width, height);
	float const aspect = width / static_cast<float>(height);
	scene.projection = projection_perspective(50.0f*pi/180.0f, aspect, 0.1f, 100.0f);
}

void mouse_move_callback(GLFWwindow* window, double xpos, double ypos)
{
	vec2 const  p1 = glfw_get_mouse_cursor(window, xpos, ypos);
	vec2 const& p0 = user.mouse_prev;
	glfw_state state = glfw_current_state(window);

	auto& camera = scene.camera;
	if(!user.cursor_on_gui){
		if(state.mouse_click_left && !state.key_ctrl)
			scene.camera.manipulator_rotate_trackball(p0, p1);
		if(state.mouse_click_left && state.key_ctrl)
			camera.manipulator_translate_in_plane(p1-p0);
		if(state.mouse_click_right)
			camera.manipulator_scale_distance_to_center( (p1-p0).y );
	}

	user.mouse_prev = p1;
}

void opengl_uniform(GLuint shader, scene_environment const& current_scene)
{
	opengl_uniform(shader, "projection", current_scene.projection);
	opengl_uniform(shader, "view", scene.camera.matrix_view());
	opengl_uniform(shader, "light", scene.light, false);
}

void draw_cluster(cluster* _cluster) {
	for (checkpoint* checkpoint : _cluster->get_checkpoints_ordered()) {
		checkpoint_visual.transform.translate = checkpoint->get_position();
		draw(checkpoint_visual, scene);
	}
}

vec3 get_color(float x) {
	std::cout << x << std::endl;
	if (x <= 1.0 / 6) return color_1 * (6 * x - 0) + color_2 * (1 - 6 * x);
	if (x <= 2.0 / 6) return color_2 * (6 * x - 1) + color_3 * (2 - 6 * x);
	if (x <= 3.0 / 6) return color_3 * (6 * x - 2) + color_4 * (3 - 6 * x);
	if (x <= 4.0 / 6) return color_4 * (6 * x - 3) + color_5 * (4 - 6 * x);
	if (x <= 5.0 / 6) return color_5 * (6 * x - 4) + color_6 * (5 - 6 * x);
	if (x <= 6.0 / 6) return color_6 * (6 * x - 5) + color_7 * (6 - 6 * x);
}

vec3 random_point() {
	int rand_1 = rand_interval() <= 0.5 ? -1 : 1;
	int rand_2 = rand_interval() <= 0.5 ? -1 : 1;
	int rand_3 = rand_interval() <= 0.5 ? -1 : 1;
	return vec3(rand_1 * log(rand_interval()), rand_2 * log(rand_interval()), rand_3 * log(rand_interval())) / 2;
}



