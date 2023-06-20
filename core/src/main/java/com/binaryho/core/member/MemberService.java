package com.binaryho.core.member;

import com.binaryho.core.domain.Member;
import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(Long memberId) throws MemberNotFoundException;
}
