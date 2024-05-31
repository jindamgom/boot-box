package com.sh.app.review.entity;

import com.sh.app.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(generator = "seq_review_id_generator")
    @SequenceGenerator(
            name = "seq_review_id_generator",
            sequenceName = "seq_review_id",
            initialValue = 1,
            allocationSize = 1
    )
    private Long id;
    @Column(nullable = false)
    private String reservationId; // fk-예약아이디
    @Column(nullable = false)
    private int reviewScore; // 리뷰평점
    @Column(nullable = false)
    private String reviewDetail; // 리뷰내용
    @CreationTimestamp
    private LocalDateTime reviewCreatedAt; // 리뷰작성날짜
    @Column(name = "movie_id")
    private Long movieId;
    @Column(name = "member_id")
    private Long memberId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "movie_id") // movie.movie_id 컬럼지정
//    private MovieData movieData;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id") // movie.member_id 컬럼지정
//    private Member member;


}
