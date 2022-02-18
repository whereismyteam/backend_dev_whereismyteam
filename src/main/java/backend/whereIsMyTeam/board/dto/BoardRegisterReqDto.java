package backend.whereIsMyTeam.board.dto;

import backend.whereIsMyTeam.board.domain.Area;
import backend.whereIsMyTeam.board.domain.BoardStatus;
import backend.whereIsMyTeam.board.domain.Category;
//import backend.whereIsMyTeam.domain.service.BoardService;
import backend.whereIsMyTeam.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardRegisterReqDto {

    /**
     * Entity 클래스와 요청 DTO 클래스는 유사한 구조
     * Entity 클래스는 테이블(Table) 또는 레코드(Record) 역할을 하는 데이터베이스 그 자체
     * 절대로 요청(Request)이나 응답(Response)에 사용되어서는 안 되기 때문에
     **/

    private Category category;

    private Area area;

    private Long capacityNum;

    //모집파트


    //회의방식
    private BoardStatus status;
    //기술스택

    //제목
    private String title;
    //내용
    private String content;

    //작성자
    private User user;


}

