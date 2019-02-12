package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.AppPreferenceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AppPreference and its DTO AppPreferenceDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AppPreferenceMapper extends EntityMapper<AppPreferenceDTO, AppPreference> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    AppPreferenceDTO toDto(AppPreference appPreference);

    @Mapping(source = "userId", target = "user")
    AppPreference toEntity(AppPreferenceDTO appPreferenceDTO);

    default AppPreference fromId(Long id) {
        if (id == null) {
            return null;
        }
        AppPreference appPreference = new AppPreference();
        appPreference.setId(id);
        return appPreference;
    }
}
