package ca.sheridancollege.beans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Team {
	
	@Id //automatically assigned value 
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	private Integer id;//Primary Key
	
	private String name;
	
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Player> players = new ArrayList<Player>();

	//private int number=this.players.size();
}
