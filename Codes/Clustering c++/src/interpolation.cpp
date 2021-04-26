#include "interpolation.hpp"

using namespace vcl;

/** Compute the linear interpolation p(t) between p1 at time t1 and p2 at time t2*/
vcl::vec3 linear_interpolation(float t, float t1, float t2, const vcl::vec3& p1, const vcl::vec3& p2);

/** Compute the cardinal spline interpolation p(t) with the polygon [p0,p1,p2,p3] at time [t0,t1,t2,t3]
*  - Assume t \in [t1,t2] */
vcl::vec3 cardinal_spline_interpolation(float t, float t0, float t1, float t2, float t3, vcl::vec3 const& p0, vcl::vec3 const& p1, vcl::vec3 const& p2, vcl::vec3 const& p3, float K);

/** Find the index k such that intervals[k] < t < intervals[k+1] 
* - Assume intervals is a sorted array of N time values
* - Assume t \in [ intervals[0], intervals[N-1] [       */
size_t find_index_of_time(float t, vcl::buffer<float> const& intervals);

const float drone_speed = 0.4f;

void interpolator::add_key_position(vec3 key_position) 
{
    key_positions.push_back(key_position);
    
    update_key_times();
}

void interpolator::set_key_positions(buffer<vec3> positions)
{
    key_positions = positions;

    update_key_times();
}

void interpolator::update_key_times()
{
    const unsigned int key_positions_size = key_positions.size();

    if (key_positions_size == 1) return;
    else if (key_positions_size == 2) return;

    // Création du tableau de distances
    key_times.resize(key_positions_size + 1);
    float* distances = new float[key_positions_size + 1];
    float distance;
    float total_distance = 0;
    for (int i = 0; i < key_positions_size - 1; i++) {
        distance = norm(key_positions[i + 1] - key_positions[i]);
        distances[i] = distance;
        total_distance += distance;
    }
    distance = norm(key_positions[key_positions_size - 1] - key_positions[0]);
    distances[key_positions_size] = distance;
    total_distance += distance;

    key_times.resize(key_positions_size + 1);
    key_times[0] = 0.0f;
    total_time = total_distance / drone_speed;
    timer = timer_interval(0, total_time);
    timer.start();
    for (int i = 0; i < key_positions_size - 1; i++) {
        key_times[i + 1] = key_times[i] + total_time * distances[i] / total_distance;
    }
    key_times[key_positions_size] = total_time;
}

vec3 interpolator::interpolate() 
{
    timer.update();
    float time = timer.t;
    unsigned int key_times_size = key_times.size();
    unsigned int key_positions_size = key_positions.size();

    // Find idx such that key_times[idx] < t < key_times[idx+1]
    int const idx = find_index_of_time(time, key_times);

    assert(idx >= 0 && ids <= key_times_size - 2);
    assert(key_times_size - 1 = key_positions_size);

    float t0;
    float t1;
    float t2;
    float t3;

    vec3 p0;
    vec3 p1;
    vec3 p2;
    vec3 p3;

    if (idx == 0) {
        t0 = -total_time + key_times[key_times_size - 1];
        t1 = key_times[0];
        t2 = key_times[1];
        t3 = key_times[2];
    }
    else if (idx == key_times_size - 2) {
        t0 = key_times[key_times_size - 3];
        t1 = key_times[key_times_size - 2];
        t2 = key_times[key_times_size - 1];
        t3 = total_time + key_times[0];
    }
    else {
        t0 = key_times[idx - 1];
        t1 = key_times[idx];
        t2 = key_times[idx + 1];
        t3 = key_times[idx + 2];
    }

    if (idx == 0) {
        p0 = key_positions[key_positions_size - 1];
        p1 = key_positions[0];
        p2 = key_positions[1];
        p3 = key_positions[2];
    }
    else if (idx == key_positions_size - 2) {
        p0 = key_positions[key_positions_size - 3];
        p1 = key_positions[key_positions_size - 2];
        p2 = key_positions[key_positions_size - 1];
        p3 = key_positions[0];
    }
    else if (idx == key_positions_size - 1) {
        p0 = key_positions[key_positions_size - 2];
        p1 = key_positions[key_positions_size - 1];
        p2 = key_positions[0];
        p3 = key_positions[1];
    }
    else {
        p0 = key_positions[idx - 1];
        p1 = key_positions[idx];
        p2 = key_positions[idx + 1];
        p3 = key_positions[idx + 2];
    }

    return cardinal_spline_interpolation(time, t0, t1, t2, t3, p0, p1, p2, p3, K);
}


buffer<vec3>* interpolator::get_key_positions() {
    return &key_positions;
}

vec3 linear_interpolation(float t, float t1, float t2, const vec3& p1, const vec3& p2)
{
    float const alpha = (t-t1)/(t2-t1);
    vec3 const p = (1-alpha)*p1 + alpha*p2;

    return p;
}

vec3 cardinal_spline_interpolation(float t, float t0, float t1, float t2, float t3, vec3 const& p0, vec3 const& p1, vec3 const& p2, vec3 const& p3, float K)
{
    float s = (t - t1) / (t2 - t1);
    vec3 const d1 = 2 * K * (p2 - p0) / (t2 - t0);
    vec3 const d2 = 2 * K * (p3 - p1) / (t3 - t1);

    return (2 * pow(s, 3) - 3 * pow(s, 2) + 1) * p1 + (pow(s, 3) - 2 * pow(s, 2) + s) * d1 + (-2 * pow(s, 3) + 3 * pow(s, 2)) * p2 + (pow(s, 3) - pow(s, 2)) * d2;
}

/* Rend un index tel que intervals[index] est le plus grand élément de intervals plus petit que t*/
size_t find_index_of_time(float t, buffer<float> const& key_times)
{
    size_t const N = key_times.size();
    bool error = false;

   if (N <= 3) {
        std::cout<<"Error: Intervals should have at least three values; current size="<<N<<std::endl;
        error = true;
    }
    if (N>0 && t < key_times[0]) {
        std::cout<<"Error: current time t is smaller than the first time of the interval"<<std::endl;
        error = true;
    }
    if(N>0 && t > key_times[N-1]) {
        std::cout<<"Error: current time t is greater than the last time of the interval"<<std::endl;
        error = true;
    }
    if (error == true) {
        std::string const error_str = "Error trying to find interval for t="+str(t)+" within values: ["+str(key_times)+"]";
        error_vcl( error_str );
    }
    if (t <= 0.0f) return 0;

    size_t k = 0;
    while (key_times[k] < t) k++;
    return k - 1;
}