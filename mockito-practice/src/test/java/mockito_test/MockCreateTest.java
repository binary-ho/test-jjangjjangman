package mockito_test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import com.binaryho.core.member.MemberService;
import com.binaryho.core.study.StudyRepository;
import com.binaryho.core.study.StudyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MockCreateTest {

    // @ExtendWith(MockitoExtension.class) 와 함께 사용해야 한다.
    @Mock
    MemberService memberService;

    @Mock
    StudyRepository studyRepository;

    @Test
    void 메서드로_Mock객체를_만들어_활용할_수_있다() {
        MemberService memberService2 = mock(MemberService.class);
        StudyRepository studyRepository2 = mock(StudyRepository.class);

        StudyService studyService = new StudyService(memberService2, studyRepository2);
        assertNotNull(studyService);
    }

    @Test
    void 어노테이션으로_Mock객체를_만들어_활용할_수_있다() {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);
    }

    @Test
    void 파라미터로_Mock객체를_받아_활용할_수_있다(
        @Mock MemberService memberService3,
        @Mock StudyRepository studyRepository3) {

        StudyService studyService = new StudyService(memberService3, studyRepository3);
        assertNotNull(studyService);
    }
}
