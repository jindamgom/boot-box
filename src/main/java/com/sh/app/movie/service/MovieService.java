package com.sh.app.movie.service;

import com.sh.app.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MovieService {

    @Autowired
    MovieRepository movieRepository;
}
