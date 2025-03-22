package com.binaryho.core.member;

import com.binaryho.core.domain.Member;
import com.binaryho.core.domain.Study;
import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(Long memberId) throws MemberNotFoundException;
    void validate(Long memberId);
    void notify(Study newStudy);
    void notify(Member member);
}
