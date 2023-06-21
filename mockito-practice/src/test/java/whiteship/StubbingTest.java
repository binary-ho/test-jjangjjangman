package whiteship;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.binaryho.whiteship.domain.Member;
import com.binaryho.whiteship.member.MemberService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StubbingTest {

    /*
    * 모든  Mock  객체의  행동
    * 1. Null을  리턴한다.  (Optional  타입은  Optional.empty  리턴) -          Primitive  타입은  기본  Primitive  값.
    * 2. 콜렉션은  비어있는  콜렉션.
    * 3. Void  메소드는  예외를  던지지  않고  아무런  일도  발생하지  않는다. Mock  객체를  조작해서
    *
    *
    * 4. 특정한  매개변수를  받은  경우  특정한  값을  리턴하거나  예뢰를  던지도록  만들  수  있다.
    * 5. Void  메소드  특정  매개변수를  받거나  호출된  경우  예외를  발생  시킬  수  있다.
    * 6. 메소드가  동일한  매개변수로  여러번  호출될  때  각기  다르게  행동호도록  조작할  수도 있다.
    * */

    private static final Long RANDOM_ID = (long) (Math.random() * 1000);

    @Test
    void Stubbing을_할_수_있다(@Mock MemberService memberService) {
        Long memberId = 1L;
        String memberEmail = "testEmail@naver.com";

        Member member = new Member();
        member.setId(memberId);
        member.setEmail(memberEmail);

        when(memberService.findById(memberId)).thenReturn(Optional.of(member));

        Optional<Member> findById = memberService.findById(memberId);
        assertEquals(memberEmail, findById.get().getEmail());
    }

    @Test
    void any메서드를_활용하여_Stubbing할_수_있다(@Mock MemberService memberService) {
        Long memberId = 1L;
        String memberEmail = "testEmail@naver.com";

        Member member = new Member();
        member.setId(memberId);
        member.setEmail(memberEmail);

        when(memberService.findById(any())).thenReturn(Optional.of(member));
        assertEquals(memberEmail, memberService.findById(RANDOM_ID).get().getEmail());
    }

    @Test
    void void메서드의_예외를_Stubbing_할_수_있다(@Mock MemberService memberService) {
        doThrow(new IllegalArgumentException())
            .when(memberService).validate(RANDOM_ID);

        assertThrows(RuntimeException.class, () -> memberService.validate(RANDOM_ID));

        assertAll(
            () -> assertThrows(RuntimeException.class, () -> memberService.validate(RANDOM_ID)),
            () -> assertDoesNotThrow(() -> memberService.validate(RANDOM_ID + 1))
        );
    }

    @Test
    void 반환이_있는_메서드의_예외를_Stubbing_할_수_있다(@Mock MemberService memberService) {
        when(memberService.findById(RANDOM_ID)).thenThrow(new RuntimeException());
        assertAll(
            () -> assertThrows(RuntimeException.class, () -> memberService.findById(RANDOM_ID)),
            () -> assertDoesNotThrow(() -> memberService.findById(RANDOM_ID + 1))
        );
    }

    @Test
    void 호출할_때마다_다른_결과가_발생하도록_Stubbing할_수_있다(@Mock MemberService memberService) {
        Long memberId = 1L;
        String memberEmail = "testEmail@naver.com";

        Member member = new Member();
        member.setId(memberId);
        member.setEmail(memberEmail);

        when(memberService.findById(any()))
            .thenReturn(Optional.of(member))
            .thenThrow(new RuntimeException())
            .thenReturn(Optional.empty());

        assertAll(
            () -> assertEquals(memberEmail, memberService.findById(RANDOM_ID).get().getEmail()),
            () -> assertThrows(RuntimeException.class, () -> memberService.findById(RANDOM_ID)),
            () -> assertTrue(memberService.findById(RANDOM_ID).isEmpty())
        );
    }
}
