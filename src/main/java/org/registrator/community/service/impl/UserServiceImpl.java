package org.registrator.community.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.registrator.community.dao.AddressRepository;
import org.registrator.community.dao.PassportRepository;
import org.registrator.community.dao.ResourceNumberRepository;
//import org.registrator.community.dao.ResourceNumberRepository;
import org.registrator.community.dao.RoleRepository;
import org.registrator.community.dao.TomeRepository;
import org.registrator.community.dao.UserRepository;
import org.registrator.community.dto.AddressDTO;
import org.registrator.community.dto.PassportDTO;
import org.registrator.community.dto.ResourceNumberDTO;
import org.registrator.community.dto.TomeDTO;
import org.registrator.community.dto.UserDTO;
import org.registrator.community.dto.WillDocumentDTO;
import org.registrator.community.dto.JSON.ResourceNumberDTOJSON;
import org.registrator.community.dto.JSON.UserStatusDTOJSON;
import org.registrator.community.entity.Address;
import org.registrator.community.entity.OtherDocuments;
import org.registrator.community.entity.PassportInfo;
import org.registrator.community.entity.ResourceNumber;
import org.registrator.community.entity.Role;
import org.registrator.community.entity.Tome;
import org.registrator.community.entity.User;
import org.registrator.community.entity.WillDocument;
import org.registrator.community.enumeration.RoleType;
import org.registrator.community.enumeration.UserStatus;
import org.registrator.community.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PassportRepository passportRepository;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	ResourceNumberRepository resourceNumberRepository;

	@Autowired
	TomeRepository tomeRepository;
	
	@Autowired
	Logger logger;

	
	/**
     * Method, which returns user from database by login
     * @param login
     * @return User 
     * 
     */
	@Transactional
	@Override
	public User getUserByLogin(String login) {
		return userRepository.findUserByLogin(login);
	}

	
	/**
     * Method, which changes user status
     * @param userStatusDTO
     * @return void 
     * 
     */
	@Transactional
	@Override
	public void changeUserStatus(UserStatusDTOJSON userStatusDTO) {
		logger.info("begin");
		User user = getUserByLogin(userStatusDTO.getLogin());
		if (userStatusDTO.getStatus().equals(UserStatus.BLOCK.toString())) {
			user.setStatus(UserStatus.BLOCK);
		} else {
			if (userStatusDTO.getStatus().equals(UserStatus.UNBLOCK.toString())) {
				user.setStatus(UserStatus.UNBLOCK);
			} else {
				if (userStatusDTO.getStatus().equals(UserStatus.INACTIVE.toString())) {
					user.setStatus(UserStatus.INACTIVE);
				}
			}
		}
		userRepository.save(user);
	}

	/**
     * Method, which retruns all registrated users
     * @return List<UserDTO>
     * 
     */
	@Transactional
	@Override
	public List<UserDTO> getAllRegistratedUsers() {
		List<UserDTO> userList = getUserDtoList();
		List<UserDTO> registratedUsers = new ArrayList<UserDTO>();

		for (UserDTO user : userList) {
			if (user.getStatus().toString() != UserStatus.INACTIVE.toString()) {
				registratedUsers.add(user);
			}
		}
		return registratedUsers;
	}

	/**
     * Method, which changes user role
     * @param login,role_id
     * @return void 
     * 
     */
	@Transactional
	@Override
	public void changeUserRole(String login, Integer role_id) {
		User user = getUserByLogin(login);
		Role role = roleRepository.findOne(String.valueOf(role_id));
		user.setRole(role);
		userRepository.save(user);
	}

	/**
     * Method, which edits information about user
     * @param userDto
     * @return userDTO 
     * 
     */
	@Transactional
	@Override
	public UserDTO editUserInformation(UserDTO userDto) {
		User user = getUserByLogin(userDto.getLogin());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setMiddleName(userDto.getMiddleName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setRole(checkRole(userDto.getRole()));
		user.setStatus(checkUserStatus(userDto.getStatus()));
		PassportInfo passport = new PassportInfo(user, userDto.getPassport().getSeria(),
				Integer.parseInt(userDto.getPassport().getNumber()), userDto.getPassport().getPublished_by_data());
		Address address = new Address(user, userDto.getAddress().getPostcode(), userDto.getAddress().getRegion(),
				userDto.getAddress().getDistrict(), userDto.getAddress().getCity(), userDto.getAddress().getStreet(),
				userDto.getAddress().getBuilding(), userDto.getAddress().getFlat());
		int result = user.getAddress().get(user.getAddress().size() - 1).compareTo(address);
		if (result != 0) {
			addressRepository.save(address);
		}
		result = user.getPassport().get(user.getPassport().size() - 1).compareTo(passport);
		if (result != 0) {
			passportRepository.save(passport);
		}
		userRepository.save(user);

		return userDto;
	}

	/**
     * Method, which fill in user status for registrateds users
     * @return List<UserStatus>
     * 
     */
	@Transactional
	@Override
	public List<UserStatus> fillInUserStatusforRegistratedUsers() {
		List<UserStatus> userStatusList = new ArrayList<UserStatus>();
		userStatusList.add(UserStatus.BLOCK);
		userStatusList.add(UserStatus.UNBLOCK);
		return userStatusList;
	}

	/**
     * Method, which fill in user status for inactives users
     * @return List<UserStatus>
     * 
     */
	@Transactional
	@Override
	public List<UserStatus> fillInUserStatusforInactiveUsers() {
		List<UserStatus> userStatusList = new ArrayList<UserStatus>();
		userStatusList.add(UserStatus.INACTIVE);
		userStatusList.add(UserStatus.BLOCK);
		userStatusList.add(UserStatus.UNBLOCK);
		return userStatusList;
	}

	
	/**
     * Method, which gets user list userDto from database
     * @return userDTO 
     * 
     */
	@Transactional
	@Override
	public List<UserDTO> getUserDtoList() {
		List<UserDTO> userDtoList = new ArrayList<UserDTO>();
		List<User> userList = userRepository.findAll();
		for (User user : userList) {
			PassportInfo passportInfo = user.getPassport().get(user.getPassport().size() - 1);
			PassportDTO passportDto = new PassportDTO(passportInfo.getSeria(), passportInfo.getNumber().toString(),
					passportInfo.getPublishedByData());
			Address address = user.getAddress().get(user.getAddress().size() - 1);
			AddressDTO addressDto = new AddressDTO(address.getPostCode(), address.getRegion(), address.getDistrict(),
					address.getCity(), address.getStreet(), address.getBuilding(), address.getFlat());
			UserDTO userDto = new UserDTO(user.getFirstName(), user.getLastName(), user.getMiddleName(),
					user.getRole().toString(), user.getLogin(), user.getPassword(), user.getEmail(),
					user.getStatus().toString(), addressDto, passportDto);
			userDtoList.add(userDto);
		}
		return userDtoList;
	}
	
	/**
     * Method, which gets user userDto from database
     * @param login
     * @return userDTO 
     * 
     */
	@Transactional
	@Override
	public UserDTO getUserDto(String login) {
		User user = getUserByLogin(login);
		PassportInfo passportInfo = user.getPassport().get(user.getPassport().size() - 1);
		PassportDTO passportDto = new PassportDTO(passportInfo.getSeria(), passportInfo.getNumber().toString(),
				passportInfo.getPublishedByData());
		if (passportInfo.getComment() != null) {
			passportDto.setComment(passportInfo.getComment());
		}
		Address address = user.getAddress().get(user.getAddress().size() - 1);
		AddressDTO addressDto = new AddressDTO(address.getPostCode(), address.getRegion(), address.getDistrict(),
				address.getCity(), address.getStreet(), address.getBuilding(), address.getFlat());
		UserDTO userdto = new UserDTO(user.getFirstName(), user.getLastName(), user.getMiddleName(),
				user.getRole().toString(), user.getLogin(), user.getPassword(), user.getEmail(),
				user.getStatus().toString(), addressDto, passportDto);
		if (!user.getWillDocument().isEmpty()) {
			WillDocument willDocument = user.getWillDocument().get(user.getWillDocument().size() - 1);
			WillDocumentDTO willDocumentDTO = new WillDocumentDTO();
			willDocumentDTO.setAccessionDate(willDocument.getAccessionDate());
			if (willDocument.getComment() != null) {
				willDocumentDTO.setComment(willDocument.getComment());
			}
			userdto.setWillDocument(willDocumentDTO);
		}
		return formUserDTO(user);
	}

	/**
     * Method, which gets all inactives users
     * @return List<UserDTO>
     * 
     */
	@Transactional
	@Override
	public List<UserDTO> getAllInactiveUsers() {
		List<UserDTO> userDtoList = new ArrayList<UserDTO>();
		userDtoList = getUserDtoList();
		List<UserDTO> inactiveUserDtoList = new ArrayList<UserDTO>();
		for (UserDTO userDto : userDtoList) {
			if (userDto.getStatus() == UserStatus.INACTIVE.toString()) {
				userDto.setRole("USER");
				inactiveUserDtoList.add(userDto);
			}
		}
		return inactiveUserDtoList;
	}

	@Transactional
	@Override
	public int updateUser(User user) {
		return 0;
	}

	@Transactional
	@Override
	public boolean login(String username, String password) {
		if (userRepository.getUserByLoginAndPassword(username, password) != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
     * Method, which checks user status
     * @param status
     * @return UserStatus
     * 
     */
	private UserStatus checkUserStatus(String status) {
		if (status.equals(UserStatus.BLOCK.name())) {
			return UserStatus.BLOCK;
		} else if (status.equals(UserStatus.INACTIVE.name())) {
			return UserStatus.INACTIVE;
		} else {
			return UserStatus.UNBLOCK;
		}
	}

	/**
     * Method, which checks user role
     * @param role
     * @return Role
     * 
     */
	private Role checkRole(String role) {
		List<Role> roleList = roleRepository.findAll();
		if (role.equals(RoleType.USER.name())) {
			return roleList.get(2);
		} else {
			if (role.equals(RoleType.REGISTRATOR.name())) {
				return roleList.get(1);
			} else {
				return roleList.get(0);
			}
		}
	}

	@Override
	@Transactional
	public void registerUser(User user, PassportInfo passport, Address address) {
		// by default, every new user is given role "User" and status "Inactive"
		// until it's changed by Admin
		// Roles map: Admin - 1, Registrator - 2, User - 3
		// user.setRoleId(3);
		// user.setStatus(UserStatus.INACTIVE);
		// user.setPasswordHash(DigestUtils.md5Hex(user.getUserId() +
		// user.getPassword()));


//		user.setPasswordHash(DigestUtils.md5Hex(user.getUserId() + user.getPassword()));

        user.setRole(roleRepository.findRoleByType(RoleType.USER));
        user.setStatus(UserStatus.INACTIVE);
        userRepository.saveAndFlush(user);

        if (userRepository.findUserByLogin(user.getLogin()) != null) {
            // // insert user's address records into "address" table
            address.setUser(user);
            addressRepository.saveAndFlush(address);
            // // insert user's passport data into "passport_data" table
            passport.setUser(user);
            //passport.setPublishedByData("РВУ ЛМУ України");
            passportRepository.saveAndFlush(passport);
        }
	}

	// @Transactional
	// @Override
	// public int updateUser(User user) {
	// return 0;
	// }
	//
	// @Transactional
	// @Override
	// public boolean login(String login, String password) {
	// if (userRepository.findUserByLogin(login) != null
	// && userRepository.getUsersPasswordHash(login) ==
	// DigestUtils.md5Hex(password)) {
	// return true;
	// } else {
	// return false;
	// }
	// }

	@Transactional
	@Override
	public boolean checkUsernameNotExistInDB(String login) {
		if (userRepository.findUserByLogin(login) != null) {
			// when username exists in DB
			return false;
		}
		// if username isn't found in DB
		return true;
	}

	/**
     * Method, which creates resoure number
     * @param resourseNumberDtoJson
     * @return void
     * 
     */
	@Transactional
	@Override
	public void createResourceNumber(ResourceNumberDTOJSON resourseNumberDtoJson) {
		User user = userRepository.findUserByLogin(resourseNumberDtoJson.getLogin());
		ResourceNumberDTO resourseNumberDto = new ResourceNumberDTO(Integer.parseInt(resourseNumberDtoJson.getResource_number()),
				resourseNumberDtoJson.getRegistrator_number());
		ResourceNumber resourceNumber = new ResourceNumber(resourseNumberDto.getNumber(),
				resourseNumberDto.getRegistratorNumber(), user);
		resourceNumberRepository.save(resourceNumber);
	}

	/**
     * Method, which creates tome
     * @param resourseNumberDtoJson
     * @return void
     * 
     */
	@Transactional
	@Override
	public void createTome(ResourceNumberDTOJSON resourseNumberDtoJson) {
		User user = userRepository.findUserByLogin(resourseNumberDtoJson.getLogin());
		TomeDTO tomeDto = new TomeDTO(resourseNumberDtoJson.getIdentifier(), user.getFirstName(), user.getLastName(),
				user.getMiddleName());
		Tome tome = new Tome(user, tomeDto.getTomeIdentifier());
		tomeRepository.save(tome);
	}

	// @Override

    @Override
    public List<UserDTO> getUserBySearchTag(String searchTag) {
        List<User> usersList = userRepository.findOwnersLikeProposed(searchTag);
        List<UserDTO> userDtos= new ArrayList<UserDTO>();
        for(User user : usersList) {
            UserDTO userdto = formUserDTO(user);
            userDtos.add(userdto);
        }
        System.out.println("DtOs" + userDtos);
        return userDtos;
    }
    
    

    private UserDTO formUserDTO(User user){
        PassportInfo passportInfo = user.getPassport().get(user.getPassport().size() - 1);
        PassportDTO passportDto = new PassportDTO(passportInfo.getSeria(), passportInfo.getNumber().toString(),
                passportInfo.getPublishedByData());
        if (passportInfo.getComment() != null) {
            passportDto.setComment(passportInfo.getComment());
        }
        Address address = user.getAddress().get(user.getAddress().size() - 1);
        AddressDTO addressDto = new AddressDTO(address.getPostCode(), address.getRegion(), address.getDistrict(),
                address.getCity(), address.getStreet(), address.getBuilding(), address.getFlat());
        UserDTO userdto = new UserDTO(user.getFirstName(), user.getLastName(), user.getMiddleName(),
                user.getRole().toString(), user.getLogin(), user.getPassword(), user.getEmail(),
                user.getStatus().toString(), addressDto, passportDto);
        if (!user.getWillDocument().isEmpty()) {
            WillDocument willDocument = user.getWillDocument().get(user.getWillDocument().size() - 1);
            WillDocumentDTO willDocumentDTO = new WillDocumentDTO();
            willDocumentDTO.setAccessionDate(willDocument.getAccessionDate());
            if (willDocument.getComment() != null) {
                willDocumentDTO.setComment(willDocument.getComment());
            }
            userdto.setWillDocument(willDocumentDTO);
        }
        
        if (!user.getOtherDocuments().isEmpty()) {
            List<String> otherDocuments = new ArrayList<String>();
            for(OtherDocuments otherDocument : user.getOtherDocuments()) {
                otherDocuments.add(otherDocument.getComment());
            }
            userdto.setOtherDocuments(otherDocuments);
        }
       
        return userdto;
    }
	
    
    
    // @Override
	// public boolean recoverUsersPassword(String email, String
	// usersCaptchaAnswer, String captchaFileName) {
	// if(validateUsersEmail(email) && validateCaptchaCode(captchaFileName)){
	// return true;
	// }
	// return false; //- "There is no such email in the DB" or "Entered captcha
	// code is incorrect"
	// }

}