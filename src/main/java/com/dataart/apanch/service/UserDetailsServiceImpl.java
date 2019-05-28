package com.dataart.apanch.service;

import com.dataart.apanch.model.User;
import com.dataart.apanch.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(username);
        builder.disabled(!user.isEnabled());
        builder.password(user.getPassword());
        String[] authorities = user.getAuthorities()
                .stream().map(a -> a.getAuthority()).toArray(String[]::new);
        builder.authorities(authorities);
        return builder.build();
    }

    public User findByUsername(String username){
        return userDetailsRepository.findByUsername(username).
                orElseThrow(() -> new RuntimeException(String.format("User with username = %s not found.", username)));
    }
}

