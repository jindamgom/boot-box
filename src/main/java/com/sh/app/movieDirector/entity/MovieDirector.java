package com.sh.app.movieDirector.entity;

import com.sh.app.director.entity.Director;
import com.sh.app.movie.entity.Movie;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie_director")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"movie", "director"})
@EqualsAndHashCode(of = "id")
public class MovieDirector {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_movie_director_id_generator")
    @SequenceGenerator(
            name = "seq_movie_director_id_generator",
            sequenceName = "seq_movie_director_id",
            initialValue = 1,
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id")
    private Director director;
}
