package com.sh.app.movie.repository;

import com.sh.app.movie.dto.FindOtherMovieDto;
import com.sh.app.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByNormalizedTitle(String normalizedTitle);

    @Query("SELECT m FROM Movie m JOIN m.movieGenres g WHERE g.genre.genreName = :genre order by m.releaseDate DESC, m.id ASC")
//    List<Movie> findByGenreName(String genre);
    Page<Movie> findByGenreName(String genre, Pageable pageable);

    List<Movie> findAllByOrderByTitleAsc();

    @Query("SELECT m FROM Movie m ORDER BY CASE WHEN m.rank IS NULL THEN 1 ELSE 0 END, m.rank ASC, m.releaseDate DESC, m.id ASC")
//    List<Movie> findAllByOrderByRankAsc();
    Page<Movie> findAllByOrderByRankAsc(Pageable pageable);

    List<Movie> findFirst6ByOrderByRankAsc();
//    @Query(value = """
//         SELECT
//             m.id,
//             m.title,
//             m.film_ratings,
//             m.release_date,
//             m.running_time,
//             m.trailer,
//             m.poster,
//             m.director,
//             m.actor,
//             m.summary,
//             m.advance_reservation,
//             AVG(r.review_score) AS average_score
//         FROM
//             movieData m
//         JOIN
//             review r ON m.id = r.movie_id
//         GROUP BY
//             m.id,
//             m.title,
//             m.film_ratings,
//             m.release_date,
//             m.running_time,
//             m.trailer,
//             m.poster,
//             m.director,
//             m.actor,
//             m.summary,
//             m.advance_reservation
//         HAVING
//             AVG(r.review_score) >= (SELECT AVG(review_score)
//                                    FROM review
//                                    WHERE movie_id IN (SELECT id
//                                                       FROM movieData
//                                                       WHERE title LIKE %:title%))
//         ORDER BY
//                 CASE WHEN m.title LIKE %:title% THEN 0 ELSE 1 END,
//                    average_score asc""", nativeQuery = true)
//    List<Movie> findByTitleContaining(String search);
@Query(value = """
        SELECT 
            m.*
        FROM 
            movie m
        WHERE ABS(m.vote_average - (SELECT AVG(m2.vote_average)
                                    FROM movie m2
                                    WHERE m2.title LIKE %:title%)) <= 1
        ORDER BY CASE WHEN m.title LIKE %:title% THEN 0 ELSE 1 END ASC, -- 'title'를 포함하는 영화 우선
                 ABS(m.vote_average - (SELECT AVG(m3.vote_average) 
                                       FROM movie m3 
                                       WHERE m3.title LIKE %:title%)) ASC, -- 'title' 평점과 가까운 순
                 m.vote_average DESC -- 동일 평점 내에서는 높은 평점 우선
        FETCH FIRST 13 ROWS ONLY""", nativeQuery = true)
    List<Movie> findByTitleContaining(String title);

    @Query("SELECT m FROM Movie m where m.releaseDate > :today ORDER BY CASE WHEN m.rank IS NULL THEN 1 ELSE 0 END, m.rank ASC, m.releaseDate DESC, m.id ASC")
    // 현재 날짜 이후의 개봉일을 가진 영화 조회
//    List<Movie> findAllByReleaseDateAfterOrderByRankAsc(LocalDate today);
    Page<Movie> findAllByReleaseDateAfterOrderByRankAsc(LocalDate today, Pageable pageable);

    // 특정 장르의 현재 날짜 이후 개봉 예정인 영화 조회
    @Query("SELECT m FROM Movie m JOIN m.movieGenres g WHERE g.genre.genreName = :genre AND m.releaseDate > :today ORDER BY m.rank ASC")
//    List<Movie> findByGenresNameAndReleaseDateAfter(String genre, LocalDate today);
    Page<Movie> findByGenresNameAndReleaseDateAfter(String genre, LocalDate today, Pageable pageable);

    Optional<Movie> findByNormalizedTitleAndReleaseDate(String normalizedTitle, LocalDate releaseDate);

    @Query("SELECT m FROM Movie m JOIN m.movieGenres mg WHERE mg.genre.id = :genreId")
    List<Movie> findMoviesByGenreId(@Param("genreId") Long genreId);


    //0424 전체 영화중 특정지점에 상영중인 영화만빼고 모두 찾는 쿼리
    //@Query(value = "SELECT id FROM movie WHERE id NOT IN (SELECT movie_id FROM movie_list WHERE cinema_id = :cinemaId)", nativeQuery = true)
    @Query("SELECT m FROM Movie m WHERE m.id NOT IN (SELECT ml.movie.id FROM MovieList ml WHERE ml.cinema.id = :cinemaId)")
    List<Movie> findOtherMovie(Long cinemaId);


    //0501 내 지점에서 상영되는 영화만 찾기
    @Query("SELECT ml.movie.id FROM MovieList ml WHERE ml.cinema.id = :cinemaId")
    List<Movie> findMyCinemaMovie(Long cinemaId);
}
