package com.sh.app.theater.entity;

import com.sh.app.cinema.entity.Cinema;
import com.sh.app.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "theater")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@ToString(exclude = {"schedules"})
public class Theater {
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id")
    @BatchSize(size = 50)
    private Cinema cinema;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int seat;

//    @OneToMany(mappedBy = "theater", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    @Builder.Default
//    private List<Schedule> schedules = new ArrayList<>();

//    public void setCinema(Cinema cinema) {
//        this.cinema = cinema;
//
//        if(cinema != null) {
//            if(cinema.getTheaters().contains(this))
//                cinema.getTheaters().add(this);
//        }
//    }
}
