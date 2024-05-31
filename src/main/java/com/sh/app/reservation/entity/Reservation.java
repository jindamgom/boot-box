package com.sh.app.reservation.entity;
import com.sh.app.common.Status;
import com.sh.app.member.entity.Member;
import com.sh.app.movie.entity.Movie;
import com.sh.app.review.entity.Review;
import com.sh.app.schedule.entity.Schedule;
import com.sh.app.seat.entity.Seat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.HashSet;

import java.util.Set;

/**
 * 0206
 * 예약 내역 entity
 *
 */
@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Builder.Default requires an initializing expression =>orderPay 초기화
//@ToString(exclude = "orderPay") //ToString 무한루프 방지.
//@ToString(exclude = "schedule")
@ToString(exclude = {"member"})
public class Reservation {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) //auto:시퀀스 , uuid:사용자가 직접 작성한 id
    private String id;//예약아이디 - pk / ex) BOX랜덤숫자5자리 = BOX10256 reservation_id

//    @Column(nullable = false, name = "member_id")
//    private Long memberId; //회원 pk - fk (varchar인 아이디가 아님)

//    @Column(nullable = false, name = "schedule_id")
//    private Long scheduleId; //상영 스케쥴pk - fk

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; //상태값, enum class
    //0416 dateTime추가
    private LocalDateTime reservationTime;

    //예약 내역 - 결제 내역 1:1 oneTo one
    //한개의 예약내역엔 한개의 결제내역이 존재함
    //mappedBy = 외래키가 존재하는 테이블

//    @OneToOne(mappedBy = "reservation", fetch = FetchType.LAZY)
//    private OrderPay orderPay;

    @ManyToMany //다대다 - 브릿지 사용시
    @JoinTable(name = "reservation_seat", //브릿지 테이블 이름
            joinColumns = @JoinColumn(name = "res_id"),//조인할 fk중 현재 entity의 pk - 외래키
            inverseJoinColumns = @JoinColumn(name = "seat_id"))//상대 entity의 pk - 상대 외래키
    @BatchSize(size = 50)
    private Set<Seat> seats = new HashSet<>();//브릿지를 통해 연결할 상대entity

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    @BatchSize(size = 50)
    private Schedule schedule;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "review_id")
//    private Review review;

    // 브릿지 테이블 : reservation_seat
//    @ManyToMany
//    @JoinTable(
//            name = "reservation_seat",
//            joinColumns = @JoinColumn(name = "res_id"),
//            inverseJoinColumns = @JoinColumn(name = "seat_id"))
//    @Builder.Default
//    private Set<Seat> seats = new LinkedHashSet<>();
}

//public class Reservation {
//    @Id
//    @GeneratedValue(generator = "seq_reservation_id_generator")
//    @SequenceGenerator(
//            name = "seq_reservation_id_generator",
//            sequenceName = "seq_reservation_id",
//            // 첫번째 할당할값, 몇개할당할까
//            initialValue = 1,
//            allocationSize = 1)
//    private Long id;
//    @Column(nullable = false)
//    private Long memberId;
//    @Column(nullable = false)
//    private Long scheduleId;
//    @Column(nullable = false)
//    private String status;
//}
