package org.registrator.community.service;

import java.util.List;

import org.registrator.community.dto.UserDTO;
import org.registrator.community.dto.UserRegistrationDTO;
import org.registrator.community.dto.json.CommunityParamJson;
import org.registrator.community.dto.json.RoleTypeJson;
import org.registrator.community.dto.json.UserStatusJson;
import org.registrator.community.entity.TerritorialCommunity;
import org.registrator.community.entity.User;
import org.registrator.community.enumeration.UserStatus;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService {

    void changeUserStatus(UserStatusJson userStatusDto);

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_COMMISSIONER')")
    List<UserDTO> getAllRegistratedUsers();

    User getUserByLogin(String login);

    void changeUserRole(String login, Integer role_id);

    List<UserStatus> fillInUserStatusforRegistratedUsers();

    List<UserStatus> fillInUserStatusforInactiveUsers();

    List<UserDTO> getUserDtoList();

    UserDTO getUserDto(String login);

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_COMMISSIONER')")
    List<UserDTO> getAllInactiveUsers();

    void registerUser(UserRegistrationDTO registrationForm);

    void updateUser(User user);

    boolean login(String username, String password);

    boolean checkUsernameNotExistInDB(String username);

    UserDTO editUserInformation(UserDTO userDto);

    List<UserDTO> getUserBySearchTag(String searchTag);

    List<User> findUsersByEmail(String email);

    void createTomeAndRecourceNumber(UserDTO userDto);

    void updateFailAttempts(String login);

    void resetFailAttempts(String login);

    User findUserByLogin(String login);

    void resetAllFailAttempts();
    
    void delete(List<User> userList);
    
    List<User> findUsersByLoginList(List<String> loginList);


    /**
     * Checks if user is authenticated in application
     * @return true if current user is authenticated, false otherwise
     */
    boolean isAuthenticated();

    void setUsersCommun(CommunityParamJson taskInfo);

    void setUsersRole(RoleTypeJson taskInfo);

    void createTomesAndResourceNumbers(List<User> users);

    /**
     * @return current logged user, if user is not logged in then null is returned
     */
    User getLoggedUser();

	void changeUserStatuses(UserStatusJson userStatusJson);

	public void deactiveUsersOfCommunity(TerritorialCommunity community);
}
