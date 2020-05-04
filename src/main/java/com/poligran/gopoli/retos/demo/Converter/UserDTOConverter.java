package com.poligran.gopoli.retos.demo.Converter;


import com.poligran.gopoli.retos.demo.DTO.UserDTO;
import com.poligran.gopoli.retos.demo.Entities.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserDTOConverter {

    private final ModelMapper modelMapper;

    public UserDTO convertToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }


}
