package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

// TDD : 테스트를 먼저 개발 한 뒤 구현 클래스 작성
class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    //순서에 따라 테스트 결과가 달라지는 것을 방지, 매 테스트가 끝날때마다 실행됨.
    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @Test
    public void save(){
        Member member = new Member();
        member.setName("Yejin");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();

//        테스트를 위해 단순히 아래와 같이 출력해도 되나, assertions 사용하는게 좋음
//        System.out.println("Result : " + (member == result));

//        Assertions.assertEquals(member, result); -> junit의 assertions
//        Assertions.assertEquals(member, null); -> 테스트 돌리면 빨간불 나옴

        assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("kyj1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("kyj2");
        repository.save(member2);

        Member result =  repository.findByName("kyj2").get();
        assertThat(member2).isEqualTo(result);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("kyj1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("kyj2");
        repository.save(member2);

        List<Member> result = repository.findAll();
        assertThat(result.size()).isEqualTo(2);
    }
}
