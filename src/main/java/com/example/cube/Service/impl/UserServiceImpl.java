package com.example.cube.Service.impl;

import com.example.cube.Model.User;
import com.example.cube.Payload.UserDto;
import com.example.cube.Repository.UserRepository;
import com.example.cube.Service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);

        return mapToDTO(user);
    }

    @Override
    public UserDto getUserById(long id) {
        User user = userRepository.findUserById(id);

        return mapToDTO(user);
    }

    @Override
    public List<UserDto> getAllUser() {
        List<User> users = userRepository.findAll();

        return users.stream().map((user) -> mapToDTO(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToDTO(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setLastName(user.getLastName());
        userDto.setBirthday(user.getBirthday());
        userDto.setEmail(user.getEmail());
        userDto.setAddress(user.getAddress());
        userDto.setZipCode(user.getZipCode());
        userDto.setCity(user.getCity());
        userDto.setProfilPicture(user.getProfilePicture());

        return userDto;
    }

    private User mapToEntity(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setLastName(userDto.getLastName());
        user.setBirthday(userDto.getBirthday());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        user.setZipCode(userDto.getZipCode());
        user.setCity(userDto.getCity());
        user.setProfilePicture(userDto.getProfilPicture());

        return user;
    }
}
