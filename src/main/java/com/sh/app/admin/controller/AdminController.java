package com.sh.app.admin.controller;

import com.sh.app.admin.dto.AdminListDto;
import com.sh.app.admin.entity.Admin;
import com.sh.app.admin.service.AdminService;
import com.sh.app.ask.entity.Ask;
import com.sh.app.ask.service.AskService;
import com.sh.app.cinema.dto.CinemaManagementDto;
import com.sh.app.cinema.entity.Cinema;
import com.sh.app.cinema.service.CinemaService;
import com.sh.app.common.Approve;
import com.sh.app.member.entity.Member;
import com.sh.app.member.service.MemberService;
import com.sh.app.movie.dto.MyCinemaMovieDto;
import com.sh.app.movieList.dto.ShowMovieListDto;
import com.sh.app.schedule.dto.ScheduleApprovalListDto;
import com.sh.app.auth.vo.MemberDetails;
import com.sh.app.cinema.dto.CinemaDto;
import com.sh.app.cinema.service.CinemaService;
import com.sh.app.member.entity.Member;
import com.sh.app.member.service.MemberService;
import com.sh.app.movie.dto.FindOtherMovieDto;
import com.sh.app.movie.dto.MovieListDto;
import com.sh.app.movie.service.MovieService;
import com.sh.app.movieList.service.MovieListService;
import com.sh.app.schedule.dto.ScheduleDto;
import com.sh.app.schedule.dto.ScheduleListDto;
import com.sh.app.schedule.dto.SearchMovieScheduleDto;
import com.sh.app.schedule.service.ScheduleService;
import com.sh.app.theater.dto.TheaterDto;
import com.sh.app.theater.service.TheaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private TheaterService theaterService;
    @Autowired
    private AskService askService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieListService movieListService;

    @GetMapping("/memberList.do")
    public void memberList(Model model) {
        List<Member> members = memberService.findAll();
//        log.debug("members = {}", members);
        model.addAttribute("members", members);
        System.out.println("회원조회 controller" + members);

        // 총 회원수
        int totalMembers = members.size();
        model.addAttribute("totalMembers", totalMembers);
        System.out.println("총 회원 수: " + totalMembers);
    }

//    @GetMapping("/noticeList.do")
//    public void notice(Model model) {
//        List<Notice> notices = noticeService.findAll();
//        log.debug("notices = {}", notices);
//        model.addAttribute("notices", notices);
//        System.out.println("공지조회 controller" + notices);
//    }

//    @GetMapping("/askList.do")
//    public void ask(Model model) {
//        List<Ask> asks = askService.findAll();
//        log.debug("asks = {}", asks);
//        model.addAttribute("asks", asks);
//        System.out.println("문의조회 controller" + asks);
//    }
    @PostMapping("/adminAuth.do")
    public String findByUsername(@RequestParam(value = "username") String username, RedirectAttributes redirectAttributes) {
//        log.debug("username = {}", username);
        Admin admin = adminService.findByUsername(username);
//        log.debug("admin = {}", admin);
        if (admin == null) {
            throw new UsernameNotFoundException(username);
        }
        return "redirect:/auth/adminLogin.do";
    }

    @GetMapping("/adminRegion.do")
    public void adminRegion(@RequestParam String name, Model model) {
        Admin admin = adminService.findByUsername(name);
        // 현재 관리자 확인
//        log.debug("admin = {}", admin);
        Long cinemaId = admin.getCinemaId();
        String region = cinemaService.findRegion(cinemaId);
        model.addAttribute("cinemaId", cinemaId);
        model.addAttribute("region", region);
        List<TheaterDto> theaterDtos = theaterService.findAllTheatersWithCinemaId(cinemaId);
        CinemaDto cinemaDto = cinemaService.getCinemaDetails(cinemaId);
        model.addAttribute("cinema", cinemaDto);
        log.debug("cinemaDto = {}", cinemaDto);


        List<ShowMovieListDto> showMovieList = movieListService.showMovieListDtos(cinemaId);
        log.debug("showMovieListDtos = {}", showMovieList);
        model.addAttribute("showMovieList", showMovieList);

        // 현재 상영 중인 영화 목록 가져오기
        List<MovieListDto> currentMovies = cinemaService.getMoviesByCinemaId(cinemaId);
        model.addAttribute("currentMovies", currentMovies); // 0426 기존 currentMovie객체에서 다른 객체로 수정.
        //log.debug("currentMovies = {}", currentMovies);
        model.addAttribute("theaters", theaterDtos);
    }



    @PostMapping("/createTheater")
    public ResponseEntity<?> createTheater(
            @RequestParam(value = "theaterId") Long theaterId,
            @RequestParam(value = "cinemaId") Long cinemaId,
            @RequestParam(value = "theaterName") String theaterName,
            @RequestParam(value = "theaterSeat") int theaterSeat
           ) {
        theaterService.createTheater(theaterId, cinemaId, theaterName, theaterSeat);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deleteTheater")
    public ResponseEntity<?> deleteTheaterWithId(@RequestParam(value = "deleteId") Long deleteId) {
        theaterService.deleteTheaterWithId(deleteId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/insertScheduleList")
    public ResponseEntity<?> findSchedule(@RequestParam(value = "theaterId") Long theaterId) {
        List<ScheduleListDto> scheduleListDtos = scheduleService.findScheduleWithTheaterId(theaterId);
        log.debug("theaterId = {}", theaterId);
        for (ScheduleListDto scheduleListDto : scheduleListDtos) {
            log.debug("scheduleDto = {}", scheduleListDto);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/createSchedule")
    public ResponseEntity<?> createSchedule(
            @RequestParam(value = "sch_theaterId") Long sch_theaterId,
            @RequestParam(value = "sch_movieId") Long sch_movieId,
            @RequestParam(value = "sch_date") LocalDate sch_date,
            @RequestParam(value = "sch_startTime") String sch_startTime
    ) {
        scheduleService.createSchedule(sch_theaterId, sch_movieId, sch_date, sch_startTime);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addNewMovie")
    public ResponseEntity<?> addNewMovie(
            @RequestParam(value = "cinemaId") Long cinemaId,
            @RequestParam(value = "movieId") Long movieId) {
        movieListService.addNewMovie(cinemaId, movieId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/adminManagement.do")
    public void adminManagement(Model model) {
        List<AdminListDto> adminListDtos = adminService.findAllWithCinema();

//        log.debug("adminListDtos = {}", adminListDtos);

        model.addAttribute("adminList", adminListDtos);
    }

    @GetMapping("/approvalManagement.do")
    public void approvalManagement(Model model) {
        List<ScheduleApprovalListDto> scheduleApprovalListDtos = scheduleService.findAllScheduleApprovals();

        log.debug("scheduleApprovalListDtos = {}", scheduleApprovalListDtos);

        model.addAttribute("scheduleList", scheduleApprovalListDtos);
    }

    @PostMapping("/scheduleApprove")
    public ResponseEntity<?> scheduleApprove(@RequestParam(value = "scheduleId") Long id,
                                             @RequestParam(value = "approve") boolean approve) {
        log.debug("scheduleId = {}", id);
        log.debug("approve = {}", approve);
        boolean isApproved = scheduleService.approveSchedule(id, approve);

        if (isApproved) {
            // 승인 성공
            return ResponseEntity.ok(Collections.singletonMap("approved", Approve.Y));
        } else {
            // 승인 실패
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Could not approve schedule."));
        }

    }


    //현재 지점에서 상영중으로 내걸은 영화를 제외한 나머지 영화를 조회하는 메서드
    @GetMapping("/findOtherMovie")
    public ResponseEntity<?> findOtherMovie(@RequestParam(value = "cinemaId") Long cinemaId) {
        System.out.println("findOtherMovie - controller !cinemaId : "+cinemaId);
        List<FindOtherMovieDto> findOtherMovieDtos = movieService.findOtherMovie(cinemaId);
        log.debug("findOtherMovieDtos = {}", findOtherMovieDtos.size());
        return ResponseEntity.ok(findOtherMovieDtos);
    }


    //현재 지점에서 상영중인 영화 목록에서 "상영 일정 조회" 버튼을 누르면 해당 지점+영화로 일정을 검색한다.
    @GetMapping("/searchMovieSchedule")
    public ResponseEntity<?> searchMovieSchedule(@RequestParam(value = "cinemaId") Long cinemaId,
                                                  @RequestParam(value = "movieId") Long movieId) {
        System.out.println("searchMovieSchedule - controller 해당 지점 영화로 스케쥴 조회하기");
        List<SearchMovieScheduleDto> searchMovieScheduleDtos = scheduleService.searchMovieSchedule(cinemaId,movieId);
        log.debug("SearchMovieScheduleDto = {}", searchMovieScheduleDtos);
        return ResponseEntity.ok(searchMovieScheduleDtos);
    }

    //내 지점에서 상영 영화 삭제하기.
    @PostMapping("/deleteMovieMyCinema")
    public ResponseEntity<?> deleteMovieMyCinema(@RequestParam(value = "id") Long id) {
        movieListService.deleteMovieMyCinema(id);
        return ResponseEntity.ok().build();
    }

    //현재 지점(내 지점)에서 상영하는 영화 조회
    //현재 지점에서 상영중으로 내걸은 영화를 제외한 나머지 영화를 조회하는 메서드
    @GetMapping("/findMyCinemaMovie")
    public ResponseEntity<?> findMyCinemaMovie(@RequestParam(value = "cinemaId") Long cinemaId) {
        System.out.println("findMyCinemaMovie - controller !cinemaId : "+cinemaId);
        List<MovieListDto> currentMovies = cinemaService.getMoviesByCinemaId(cinemaId);
        return ResponseEntity.ok(currentMovies);
    }
}

