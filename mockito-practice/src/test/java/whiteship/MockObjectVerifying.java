package whiteship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.binaryho.whiteship.domain.Member;
import com.binaryho.whiteship.domain.Study;
import com.binaryho.whiteship.member.MemberService;
import com.binaryho.whiteship.study.StudyRepository;
import com.binaryho.whiteship.study.StudyService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MockObjectVerifying {

    @Test
    void 내부적으로_호출되는_메서드_호출_횟수를_검증할_수_있다(
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

        verify(memberService, times(1)).notify(study);
        verify(memberService, times(1)).notify(member);
        verify(memberService, never()).validate(any());
    }

    @Test
    void 내부적으로_호출되는_메서드_호출_횟수와_순서를_검증할_수_있다(
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

        // 순서 또한 검증
        InOrder inOrder = inOrder(memberService);
        inOrder.verify(memberService, times(1)).notify(study);
        inOrder.verify(memberService, times(1)).notify(member);

        verifyNoMoreInteractions(memberService);
    }

    @Test
    void 상호작용이_끝났음을_검증할_수_있다(
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

        verify(memberService, times(1)).notify(study);
        verify(memberService, times(1)).notify(member);
        verifyNoMoreInteractions(memberService);
    }

    @Test
    void BDD_Style로_상호작용이_끝났음을_검증할_수_있다(
        @Mock MemberService memberService,
        @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);

        Member member = new Member();
        Long ID = 1L;
        member.setId(ID);

        Study study = new Study();

        given(memberService.findById(ID)).willReturn(Optional.of(member));
        given(studyRepository.save(study)).willReturn(study);

        studyService.createNewStudy(ID, study);
        assertEquals(member, study.getOwner());

        then(memberService).should(times(1)).notify(study);
        then(memberService).should(times(1)).notify(member);
        then(memberService).shouldHaveNoMoreInteractions();
    }
}
