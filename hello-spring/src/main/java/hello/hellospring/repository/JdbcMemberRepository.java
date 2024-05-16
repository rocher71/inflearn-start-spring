package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {
    private final DataSource dataSource;
    public JdbcMemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(name) values(?)"; //save하는 sql

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null; //결과를 받음

        try {
            conn = getConnection(); // 커넥션 받아옴
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //두번째 인자는 insert를 해봐야 id값을 얻을 수 있었는데, 이를 위해 사용됨.

            pstmt.setString(1, member.getName());
            //첫번째 인자, parameterIndex: 1에서 이 '1'이 위 sql문의 물음표와 매칭됨, member.getName()으로 값을 입력

            pstmt.executeUpdate();
            //위 코드에서 DB에 실제 쿼리가 날라감.
            rs = pstmt.getGeneratedKeys();
            // prepareStatement()의 두번째 인자와 매칭됨, 방금 생성한 키를 꺼내옴.

            if (rs.next()) { //값이 있으면 꺼내옴
                member.setId(rs.getLong(1)); //getLong으로 값을 꺼내고 member.setId()로 셋팅해줌
            } else {
                throw new SQLException("id 조회 실패"); //실패시 출
            }
            return member;
        } catch (Exception e) { // 위 코드들이 exception을 엄청 많이 던짐, try catch문 잘 사용해줘야함.
            throw new IllegalStateException(e);
        } finally {
            //사용한 자원들 release 해줘야 함. DB connection은 외부 네트워크가 연결 된 것이기 때문에 끝나면 바로 자원을 끊어야함,
            //resource를 반환해야 함.
            close(conn, pstmt, rs);
        }
    }
    //조회
    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?"; //쿼리문
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            // 조회는 executeUpdate()가 아닌 executeQuery()
            rs = pstmt.executeQuery();

            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member); //반환
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    @Override
    public List<Member> findAll() {
        String sql = "select * from member";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            //리스트에 담음
            List<Member> members = new ArrayList<>();
            while(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                members.add(member);
            }
            return members; //멤머 리스트 반환
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    @Override
    public Optional<Member> findByName(String name) {
        String sql = "select * from member where name = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            }
            return Optional.empty(); //없을 시 empty
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    //Spring과 연결할 때 'DataSourceUtils'를 통해서 connection을 획득해야함!
    //DB transaction이 걸릴 수 있는데 이때 connection을 똑같은 것을 유지시켜줌
    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //역시 Datasource'Utils'를 통해서 release 해줘야함.
    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}