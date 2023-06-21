package core.whiteship;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.binaryho.core.whiteship.domain.Member;
import com.binaryho.core.whiteship.domain.Study;
import com.binaryho.core.whiteship.member.MemberService;
import com.binaryho.core.whiteship.study.StudyRepository;
import com.binaryho.core.whiteship.study.StudyService;
import com.binaryho.core.whiteship.study.StudyStatus;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StubbingMission {

    @Mock MemberService memberService;
    @Mock StudyRepository studyRepository;

    /* TODO
    *   1. memberService 객체에 findById 메소드를 1L 값으로 호출하면 member 객체를 return하도록 Stubbing
    *   2. studyRepository 객체에 save 메서드를 study 객체로 호출하면 study 객체를 그대로 리턴하도록 Stubbing
    * */

    @Test
    void mission1() {
        Member member = new Member();
        member.setEmail("EMAIL");
        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        assertEquals("EMAIL", memberService.findById(1L).get().getEmail());
    }

    @Test
    void mission2() {
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

    /* TODO
     *   1. studyReposiotry Mock 객체의 save 메서드 호출시 study 리턴하도록 stubbing
     *   2. study의 status가 OPEND로 변경됐는지 확인
     *   3. study의 opendDateTime이 null이 아닌지 확인
     *   4. memberService의 notify(study)가 호출 됐는지 확인
     * */
    @Test
    void mission3() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "화이팅!");

        given(studyRepository.save(study)).willReturn(study);

        // When
        studyService.openStudy(study);

        // Then
        assertAll(
            () -> assertEquals(StudyStatus.OPENED, study.getStatus()),
            () -> assertNotNull(study.getOpenedDateTime()),
            () -> then(memberService).should(times(1)).notify(study)
        );
    }
}
