#include "vcl/vcl.hpp"
#include <chrono>
#include "structure.hpp"
#include <iostream>
#include <cstdio>

using namespace std;

const int time_limit = 10000;

// constructeur
Statistics::Statistics() {
	int actual_size = 0;
	double list_scores [time_limit];
	double list_dist[time_limit];
	double list_avg_dist[time_limit];
}

// afficheur des scores
int Statistics::update_scores(double nbre) {
	if (actual_size < time_limit) { // tant que le tableau n'est pas plein on le remplit
		list_scores[actual_size] = nbre;
		++actual_size;
	}
	
	if (actual_size == time_limit) { // une fois qu'il est plein on génère un fichier txt
		ofstream myfile;
		myfile.open("scores_mouvement_3d_cpp.txt");
		myfile << time_limit << std::endl;
		// std::cout << "time_limit" << time_limit << std::endl;
		for (unsigned i = 0; i < time_limit; i++) {
			myfile << list_scores[i] << "\n";
		}
		//std::cout << actual_size << std::endl;
		myfile.close();

	}
		return 0.0;
}

// afficheur des distances d'un drone à une position en fonction du temps
int Statistics::update_dist(double nbre) {
	if (actual_size < time_limit) { // tant que le tableau n'est pas plein on le remplit
		list_dist[actual_size] = nbre;
		++actual_size;
	}
	std::cout << "size : " << actual_size << std::endl;

	if (actual_size == time_limit) { // une fois qu'il est plein on génère un fichier txt
		ofstream myfile;
		myfile.open("dist_mouvement_3d_cpp.txt");
		myfile << time_limit << std::endl;

		for (unsigned i = 0; i < time_limit; i++) {
			myfile << list_dist[i] << "\n";
		}
		//std::cout << actual_size << std::endl;
		myfile.close();

	}
	return 0.0;
}

// afficheur des distances d'un drone à une position en fonction du temps
int Statistics::update_avg_dist(double nbre) {
	if (actual_size < time_limit) { // tant que le tableau n'est pas plein on le remplit
		list_avg_dist[actual_size] = nbre;
		++actual_size;
	}
	std::cout << "size : " << actual_size << std::endl;

	if (actual_size == time_limit) { // une fois qu'il est plein on génère un fichier txt
		ofstream myfile;
		myfile.open("dist_avg_mouvement_3d_cpp.txt");
		myfile << time_limit << std::endl;

		for (unsigned i = 0; i < time_limit; i++) {
			myfile << list_avg_dist[i] << "\n";
		}
		//std::cout << actual_size << std::endl;
		myfile.close();

	}
	return 0.0;
}