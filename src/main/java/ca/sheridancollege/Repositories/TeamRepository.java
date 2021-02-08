package ca.sheridancollege.Repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.sheridancollege.beans.Team;

public interface TeamRepository extends CrudRepository<Team, Integer> {
	Team findById(int id);
	List<Team> findAll();
	Team findByName(String name);
	
}
