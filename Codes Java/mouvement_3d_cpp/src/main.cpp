#include "vcl/vcl.hpp"
#include <iostream>
#include <chrono>

#include "drone.hpp"
#include "terrain.hpp"

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
void update_graphics(mesh_drawable& terrain_initial_visual, mesh_drawable& terrain_current_visual, mesh_drawable& drone_visual, Drone& drone, Terrain& terrain);

long int start_time;
long int current_time;


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
	cluster cluster_instance;
	Drone drone(&cluster_instance);
	Terrain terrain;
	mesh_drawable drone_visual = mesh_drawable(drone.get_mesh());
	mesh_drawable terrain_initial_visual = mesh_drawable(terrain.get_initial_mesh());
	mesh_drawable terrain_current_visual = mesh_drawable(terrain.get_current_mesh());

	terrain_initial_visual.transform.translate += vec3(1.2f, 0.0f, 0.0f);

	drone.set_position(&drone_visual.transform.translate);

	while (!glfwWindowShouldClose(window))
	{
		scene.light = scene.camera.position();
		user.fps_record.update();
		current_time = static_cast<long>(clock());
		
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
		update_graphics(terrain_initial_visual, terrain_current_visual, drone_visual, drone, terrain);

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

void update_graphics(mesh_drawable& terrain_initial_visual, mesh_drawable& terrain_current_visual, mesh_drawable& drone_visual, Drone& drone, Terrain& terrain)
{
	// Update des objets
	drone.update_position();
	terrain.update_current_visual(drone, terrain_current_visual);

	// Update des graphiques
	draw(drone_visual, scene);
	draw(terrain_initial_visual, scene);
	draw(terrain_current_visual, scene);

	if (user.gui.display_wireframe) {
		draw_wireframe(terrain_initial_visual, scene, { 0,1,0 });
		draw_wireframe(terrain_current_visual, scene, { 0,1,0 });
		draw_wireframe(drone_visual, scene, { 0,1,0 });
	}
}

void initialize_data()
{
	start_time = static_cast<long>(clock());
	current_time = static_cast<long>(clock());

	// Basic setups of shaders and camera
	GLuint const shader_mesh = opengl_create_shader_program(opengl_shader_preset("mesh_vertex"), opengl_shader_preset("mesh_fragment"));
	mesh_drawable::default_shader = shader_mesh;
	mesh_drawable::default_texture = opengl_texture_to_gpu(image_raw{1,1,image_color_type::rgba,{255,255,255,255}});

	user.global_frame = mesh_drawable(mesh_primitive_frame());
	user.gui.display_frame = false;
	scene.camera.distance_to_center = 2.5f;
	scene.camera.look_at({-3,1,2}, {0,0,0.5}, {0,0,1});
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



