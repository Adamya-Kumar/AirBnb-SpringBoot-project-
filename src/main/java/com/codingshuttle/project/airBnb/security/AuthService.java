package com.codingshuttle.project.airBnb.security;

import com.codingshuttle.project.airBnb.Repository.UserRepository;
import com.codingshuttle.project.airBnb.dto.LoginDTO;
import com.codingshuttle.project.airBnb.dto.LoginResponseDTO;
import com.codingshuttle.project.airBnb.dto.SignUpRequestDTO;
import com.codingshuttle.project.airBnb.dto.SignUpResponseDTO;
import com.codingshuttle.project.airBnb.entity.User;
import com.codingshuttle.project.airBnb.entity.enums.Role;
import com.codingshuttle.project.airBnb.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;

    public SignUpResponseDTO signUp(SignUpRequestDTO request){
      Optional<User> presentInDb = userRepository.findByEmail(request.getEmail());
      if(presentInDb.isPresent()){
         throw new ResourceNotFoundException("Unable to create User,email is present");
      }

      User user = modelMapper.map(request, User.class);
      user.setRoles(Set.of(Role.GUEST));
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      user=userRepository.save(user);
      return modelMapper.map(user, SignUpResponseDTO.class);
    }

    public String[] login(LoginDTO loginDTO){
        Authentication authenticate =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(),loginDTO.getPassword()
        ));
        User user = (User) authenticate.getPrincipal();
        String[] arr=new String[2];
        arr[0] =jwtService.generateAccessToken(user);
        arr[1] = jwtService.generateRefreshToken(user);

       return arr;
    }

    public LoginResponseDTO refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElse(null);

        String accessToken = jwtService.generateAccessToken(user);
        return new LoginResponseDTO(accessToken);
    }
}
