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
    public List<UserDto> getAllUser(String email) {
        User actualUser = userRepository.findUserByEmail(email);
        List<User> users = userRepository.findAll();
        users = users.stream().filter(user -> user != actualUser).collect(Collectors.toList());

        return users.stream().map(this::mapToDTO)
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

}
