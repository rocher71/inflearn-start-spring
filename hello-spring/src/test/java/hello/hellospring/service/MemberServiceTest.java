package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

//    MemberService memberService = new MemberService();
    //clear해야하는데 서비스밖에 없어서 안되기 때문에 memoryMemberRepository 추가
//    MemoryMemberRepository memoryMemberRepository = new MemoryMemberRepository();

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach(){
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        //given
        //무언가가 주어졌는데 -> "아, 이 데이터를 기반으로 하는구나"
        Member member = new Member();
        member.setName("mem");

        //when
        //이걸 실행 했을 때 -> "아, 이걸 검증하는구나"
        Long saveId = memberService.join(member);

        //then
        //결과가 이렇게 나와야 하는구나 -> "아, 여기가 검증부구나"
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외(){
        //given
        Member member1 = new Member();
        member1.setName("mem");

        Member member2 = new Member();
        member2.setName("mem");

        //when
        memberService.join(member1);
        //두번째 인자를 실행할건데, 첫번째 예외가 터져야함! 이라는 의미
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        //메시지 확인
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다");

        //이렇게 해도 됨
//        try{
//            memberService.join(member2);
//            fail();
//        } catch (IllegalStateException e) {
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다");
//        }

        //then
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}