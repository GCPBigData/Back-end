package br.com.clearinvest.clivserver.service.dto;

import br.com.clearinvest.clivserver.config.Constants;
import br.com.clearinvest.clivserver.domain.Authority;
import br.com.clearinvest.clivserver.domain.User;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class MobileSessionDataDTO {

    private UserDTO user;
    private List<AppPreferenceDTO> prefs;

    public MobileSessionDataDTO() {
    }

    public MobileSessionDataDTO(UserDTO user, List<AppPreferenceDTO> prefs) {
        this.user = user;
        this.prefs = prefs;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<AppPreferenceDTO> getPrefs() {
        return prefs;
    }

    public void setPrefs(List<AppPreferenceDTO> prefs) {
        this.prefs = prefs;
    }
}
