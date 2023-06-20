package core.study;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.binaryho.core.whiteship.domain.Member;
import com.binaryho.core.whiteship.domain.Study;
import com.binaryho.core.whiteship.member.MemberService;
import com.binaryho.core.whiteship.study.StudyRepository;
import com.binaryho.core.whiteship.study.StudyService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StubbingMission {

    /* TODO
    *   1. memberService 객체에 findById 메소드를 1L 값으로 호출하면 member 객체를 return하도록 Stubbing
    *   2. studyRepository 객체에 save 메서드를 study 객체로 호출하면 study 객체를 그대로 리턴하도록 Stubbing
    * */

    @Test
    void mission1(@Mock MemberService memberService) {
        Member member = new Member();
        member.setEmail("EMAIL");
        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        assertEquals("EMAIL", memberService.findById(1L).get().getEmail());
    }

    @Test
    void mission2(
        @Mock MemberService memberService,
        @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);

        Member member = new Member();
        Long ID = 1L;
        member.setId(ID);

        Study study = new Study();
        when(memberService.findById(ID)).thenReturn(Optional.of(member));
        when(studyRepository.save(study)).thenReturn(study);

        studyService.createNewStudy(ID, study);
        assertEquals(member, study.getOwner());
    }
}
