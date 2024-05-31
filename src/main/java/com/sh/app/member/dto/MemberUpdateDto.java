package com.sh.app.member.dto;


import com.sh.app.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateDto {

    @NotNull(message = "아이디는 필수 입니다.")
    private String memberLoginId;

    @NotNull(message = "비밀번호를 입력해주세요.")
    private String memberPwd;

    @NotNull(message = "이름은 필수 입니다.")
    private String memberName;

    @NotNull(message = "핸드폰번호는 필수 입니다.")
    private String memberPhone;
    
    private String birthyear;

    @Email(message="이메일 형식으로 작성하세요.")
    private String memberEmail;

    private List<String> genres;

    public List<String> getGenres() {
        return genres;
    }
}
