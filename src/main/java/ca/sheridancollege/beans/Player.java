package ca.sheridancollege.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity

public class Player {
	@Id //automatically assigned value 
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	private Integer id;//Primary Key
	
	
	@Size(min=5, max=15, message="The title must be of size between 5 and 30")
	private String name;
	private int age;
	private String gender;
	private String phoneNumber;
	private String email;
	
	private boolean hasTeam;
	
	@ManyToOne
	private Team team;
	

}
