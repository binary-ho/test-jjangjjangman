package com.binaryho.whiteship.member;

import com.binaryho.whiteship.domain.Member;
import com.binaryho.whiteship.domain.Study;
import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(Long memberId) throws MemberNotFoundException;
    void validate(Long memberId);
    void notify(Study newStudy);
    void notify(Member member);
}
