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

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    @NotNull(message = "카테고리를 선택해주세요.")
    private String categoryName;
    @NotNull(message = "회의 방식을 입력해주세요.")
    private String onOff;

    @NotNull(message = "지역을 선택해주세요.")
    private String area;
    @NotNull(message = "모집인원을 선택해주세요. ")
    private Long capacityNum;

    @NotNull(message = "제목을 입력해주세요.")
    private String title;
    @NotNull(message = "글 내용을 입력해주세요.")
    private String content;
    @NotNull(message = " 작성자")
    private Long userIdx;

    @NotNull(message = "모집 파트를 입력해주세요.")
    private final List<String> recruitmentPart = new ArrayList<>();
    @NotNull(message = "스택 리스트를 입력해주세요.")
    private final List<String> techstacks = new ArrayList<>();
    @NotNull(message = "게시글 상태를 입력해주세요.")
    private String boardStatus;






}

