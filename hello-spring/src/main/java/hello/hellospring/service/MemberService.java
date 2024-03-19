package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {

    //아래 코드의 문제점 : 테스케이스와 아래 코드에서의 new MemoryMemberRepository()가 서로 다른 인스턴스임.
//    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final MemberRepository memberRepository;

    //외부에서 넣어주도록 변경
    //Dependency injection : MemberService 입장에서 보면 직접 만들지 않고 외부에서 MemberRepository를 넣어
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     */
    public Long join(Member member){
        //동명이인(같은 이름) 중복 회원 안됨
        validateDuplicateMember(member); //동명이인 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //optional 값이기 때문에, ifPresent 즉 값이 있으면 실행 -> null일 가능성이 있으면 optional로 감싸서 반환하면 됨.
        // Member member1 = result.get(); -> 이렇게 바로 꺼내도 되나 권장하지 않음
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다");
                });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers(){
        //repository는 단순히 저장소에 넣었다 뺐다하는 기능,
        //service 클래스는 join, findMembers 등 조금 더 비즈니스에 가까움, 그런 용어를 사용해야함.
        //service는 비즈니스에 의존적으로 설계, repository는 단순 기계적으로 개발스럽게 용어 선택.

        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }
}
