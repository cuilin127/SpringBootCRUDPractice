package ca.sheridancollege.Repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.sheridancollege.beans.Player;
import ca.sheridancollege.beans.Team;

public interface PlayerRepository extends CrudRepository<Player, Integer> {
	Player findById(int id);
	Player findByName(String name);
	List<Player> findByHasTeam(boolean hasTeam);
	List<Player> findByHasTeamAndGender(boolean hasTeam,String gender);
	List<Player> findByGender(String gender);
	List<Player> findByAge(int age);
	List<Player> findByTeam(Team team);
	
}
