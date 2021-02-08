package ca.sheridancollege.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.Repositories.PlayerRepository;
import ca.sheridancollege.Repositories.TeamRepository;
import ca.sheridancollege.beans.Player;
import ca.sheridancollege.beans.Team;

@Controller

public class HomeController {
	@Autowired
	private PlayerRepository prp;

	@Autowired
	private TeamRepository trp;

	boolean isFirstAssign = true;

	public void assignPLayerToTeam(int pId, int tId) {
		Player player = prp.findById(pId);
		Team team = trp.findById(tId);
		player.setTeam(team);
		player.setHasTeam(true);
		team.getPlayers().add(player);
		trp.save(team);
		prp.save(player);

	}

	public void dropPLayerFromTeam(int pId, int tId) {

		Player player = prp.findById(pId);
		Team team = trp.findById(tId);
		for (Player p : team.getPlayers()) {
			if (p.getId() == player.getId()) {
				team.getPlayers().remove(p);
				player.setTeam(null);
				player.setHasTeam(false);
				break;
			}
		}

		trp.save(team);
		prp.save(player);

	}

	public void swapPlayers(int id1, int id2) {
		if (prp.findById(id1) != prp.findById(id2)) {
			int team1 = prp.findById(id1).getTeam().getId();
			int team2 = prp.findById(id2).getTeam().getId();
			dropPLayerFromTeam(id1, team1);
			dropPLayerFromTeam(id2, team2);
			assignPLayerToTeam(id1, team2);
			assignPLayerToTeam(id2, team1);
		}
	}

	public void movePlayer(int pid, int tid) {
		if (tid != prp.findById(pid).getTeam().getId()) {
			dropPLayerFromTeam(pid, prp.findById(pid).getTeam().getId());
			assignPLayerToTeam(pid, tid);
		}
	}

	// Assign palyers to teams, the auto assign button and we have to have enough
	// male and female to be assigned

	@GetMapping("/betaTest")
	public String autoAssignPlayersToTeams() {
		if (isFirstAssign) {
			// List<Player> availablePlayers = prp.findByHasTeam(false);
			List<Team> allTeam = trp.findAll();
			for (int i = 0; i < allTeam.size(); i++) {
				List<Player> availableMale = prp.findByHasTeamAndGender(false, "Male");
				List<Player> availableFemale = prp.findByHasTeamAndGender(false, "FeMale");
//			System.out.println(availableMale.get(0).getName());
//			System.out.println(availableFemale.get(0).getName());
				assignPLayerToTeam(availableMale.get(0).getId(), allTeam.get(i).getId());
				assignPLayerToTeam(availableFemale.get(0).getId(), allTeam.get(i).getId());
			}
			for (int i = 0; i < allTeam.size(); i++) {
				List<Player> availableMale = prp.findByHasTeamAndGender(false, "Male");
				List<Player> availableFemale = prp.findByHasTeamAndGender(false, "FeMale");
				assignPLayerToTeam(availableMale.get(0).getId(), allTeam.get(i).getId());
				assignPLayerToTeam(availableFemale.get(0).getId(), allTeam.get(i).getId());
			}
			while (prp.findByHasTeam(false).size() > 0) {
				for (int i = 0; i < allTeam.size(); i++) {
					if (prp.findByHasTeam(false).size() > 0) {
						if (allTeam.get(i).getPlayers().size() == 8) {
							break;
						} else {
							assignPLayerToTeam(prp.findByHasTeam(false).get(0).getId(), allTeam.get(i).getId());
						}
					}
				}
			}
			isFirstAssign = false;
		} else {
			List<Team> allTeam = trp.findAll();
			while (prp.findByHasTeam(false).size() > 0) {
				for (int i = 0; i < allTeam.size(); i++) {
					if (prp.findByHasTeam(false).size() > 0) {
						assignPLayerToTeam(prp.findByHasTeam(false).get(0).getId(), allTeam.get(i).getId());
					}
				}
			}
		}
		return "home.html";
	}

	// Navigation to each of the page
	@GetMapping("/")
	public String goHome() {
		return "home.html";
	}

	@GetMapping("/home")
	public String goHome2() {
		return "home.html";
	}

	@GetMapping("/search")
	public String goSearch(Model model) {
		model.addAttribute("players", prp.findAll());
		model.addAttribute("teams", trp.findAll());
		return "search.html";
	}

	@GetMapping("pManagement")
	public String goPManagement(Model model) {
		model.addAttribute("players", prp.findAll());
		return "pManagement.html";
	}

	@GetMapping("/switch")
	public String goSwitch(Model model) {
		model.addAttribute("teams", trp.findAll());
		return "switch.html";
	}

	@GetMapping("/tManagement")
	public String goTManagementl(Model model) {
		model.addAttribute("teams", trp.findAll());
		return "tManagement.html";
	}

	@GetMapping("/move")
	public String goMove(Model model) {
		model.addAttribute("teams", trp.findAll());
		return "move.html";
	}

	@GetMapping("/addP")
	public String addNewPlayer(Model model) {
		model.addAttribute("player", new Player());
		return "addNewPlayer.html";
	}

	@GetMapping("/addMember")
	public String addNewMember(Model model, @RequestParam int teamId) {

		model.addAttribute("players", prp.findByHasTeam(false));
		model.addAttribute("teamId", teamId);
		return "addMember.html";
	}

	// All of search function
	@GetMapping("/searchByName")
	public String searchByName(Model model, @RequestParam String name) {
		model.addAttribute("players", prp.findByName(name));
		model.addAttribute("teams", trp.findAll());
		return "search.html";
	}

	@GetMapping("/searchByAge")
	public String searchByAge(Model model, @RequestParam String age) {
		model.addAttribute("players", prp.findByAge(Integer.parseInt(age)));
		model.addAttribute("teams", trp.findAll());
		return "search.html";
	}

	@GetMapping("/searchByGender")
	public String searchByGender(Model model, @RequestParam String gender) {
		model.addAttribute("players", prp.findByGender(gender));
		model.addAttribute("teams", trp.findAll());
		return "search.html";
	}

	@GetMapping("/searchByTeam")
	public String searchByTeam(Model model, @RequestParam String name) {
		model.addAttribute("players", prp.findByTeam(trp.findByName(name)));
		model.addAttribute("teams", trp.findAll());
		return "search.html";
	}

	// Update,Delete,Add

	@GetMapping("/editP/{id}")
	public String edit(@PathVariable int id, Model model) {
		if (prp.count() >= 41 && prp.count() <= 63) {
			if (prp.findById(id) != null) {
				Player p = prp.findById(id);
				model.addAttribute("player", p);
				return "editPlayer.html";
			} else {
				model.addAttribute("players", prp.findAll());
				return "redirect:/pManagement";
			}
		} else {
			return "wrongPlayerNumber.html";
		}
	}

	@GetMapping("/deleteP/{id}")
	public String delete(@PathVariable int id, Model model) {
		if (prp.findById(id).isHasTeam() == true) {
			dropPLayerFromTeam(id, prp.findById(id).getTeam().getId());
		}
		if (prp.count() >= 41 && prp.count() <= 63) {
			if (prp.findById(id) != null) {
				prp.deleteById(id);
				model.addAttribute("players", prp.findAll());
				return "pManagement.html";
			} else {
				model.addAttribute("players", prp.findAll());
				return "redirect:/pManagement";
			}
		} else {
			return "wrongPlayerNumber.html";
		}
	}

	@GetMapping("/modifyPlayer")
	public String modifyPlayer(Model model, @ModelAttribute Player player) {
		prp.save(player);
		model.addAttribute("players", prp.findAll());
		return "pManagement.html";
	}

	@GetMapping("/addPlayer")
	public String addPlayer(Model model, @ModelAttribute Player player) {

		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Player>> validationErrors = validator.validate(player);
		if (!validationErrors.isEmpty()) {
			List<String> errors = new ArrayList<String>();
			for (ConstraintViolation<Player> error : validationErrors) {
				errors.add(error.getPropertyPath() + ":" + error.getMessage());
			}
			model.addAttribute("errorMessage", errors);
			return "addNewPlayer.html";
		} else {
			player.setHasTeam(false);
			player.setTeam(null);
			prp.save(player);
		}

		model.addAttribute("players", prp.findAll());
		return "pManagement.html";
	}

	@GetMapping("showMembers/{id}")
	public String goTeamMemberPage(Model model, @PathVariable int id) {

		if (trp.findById(id) != null) {
			Team team = trp.findById(id);
			List<Player> players = team.getPlayers();
			model.addAttribute("players", players);
			model.addAttribute("teamName", team.getName());
			model.addAttribute("teamId", id);
			return "members.html";
		} else {
			model.addAttribute("players", trp.findAll());
			return "tManagement.html";

		}
	}

	@GetMapping("moveOut/{id}")
	public String moveOut(@PathVariable int id) {

		dropPLayerFromTeam(id, prp.findById(id).getTeam().getId());

		return "redirect:/tManagement";

	}

	@GetMapping("/addMemberToTeam")
	public String addToTeam(@RequestParam int player, @RequestParam int teamId) {
		assignPLayerToTeam(player, teamId);

		return "redirect:/tManagement";
	}

//Do moving and swapping

	@GetMapping("/chooseTeams")
	public String goChoosePlayers(Model model, @RequestParam int id1, @RequestParam int id2) {
		model.addAttribute("players1", trp.findById(id1).getPlayers());
		model.addAttribute("players2", trp.findById(id2).getPlayers());
		return "chosePlayers.html";
	}

	@GetMapping("/doSwap")
	public String doSwap(Model model, @RequestParam int id1, @RequestParam int id2) {
		swapPlayers(id1, id2);
		return "home.html";
	}

	@GetMapping("/chooseFromTeam")
	public String chooseFromTeam(Model model, @RequestParam int id) {
		model.addAttribute("players", trp.findById(id).getPlayers());
		model.addAttribute("teams", trp.findAll());
		return "choseGo.html";
	}

	@GetMapping("/doMove")
	public String doMove(@RequestParam int pid, @RequestParam int tid) {
		movePlayer(pid, tid);
		return "home.html";
	}

}
