package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Board;
import backend.whereIsMyTeam.board.domain.QTechStack;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import static backend.whereIsMyTeam.board.domain.QBoard.board;
//import static com.querydsl

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    JPQLQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em){

        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 기술스택 검색 메서드
     * [조건]
     * 1. eqStack 메서드에서 직접 검색.
     * 2. fetch 조인으로 N+1 문제 해결해야 함
     * 3. 이후에 페이징처리를 해줘야 함 ->pageable 넣는 방법 등 (조사 필요)
     **/
    @Override
    public List<Board> searchTechStacks(String stack1, String stack2, String stack3) {
        return queryFactory
                .selectFrom(board)
                .join(board.techstacks)
                .fetchJoin()
                //.innerJoin(board.techstacks.any().techStack)
                .where(eqStack1(stack1),
                        eqStack2(stack2),
                        eqStack3(stack3)
                )
                .fetch();
    }

    private BooleanExpression eqStack1(String stack1) {
        if (StringUtils.isEmpty(stack1)) {
            return null;
        }
        return QTechStack.techStack.stackName.eq(stack1);
    }


    private BooleanExpression eqStack2(String stack2) {
        if (StringUtils.isEmpty(stack2)) {
            return null;
        }
        return QTechStack.techStack.stackName.eq(stack2);
    }


    private BooleanExpression eqStack3(String stack3) {
        if (StringUtils.isEmpty(stack3)) {
            return null;
        }
        return QTechStack.techStack.stackName.eq(stack3);
    }


//    private BooleanExpression eqPhoneNumber(String phoneNumber) {
//        if (StringUtils.isEmpty(phoneNumber)) {
//            return null;
//        }
//        return academy.phoneNumber.eq(phoneNumber);
//    }

}
