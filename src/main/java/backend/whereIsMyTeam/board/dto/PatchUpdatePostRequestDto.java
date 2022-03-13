package backend.whereIsMyTeam.board.dto;

import backend.whereIsMyTeam.board.domain.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PatchUpdatePostRequestDto {

    @NotNull(message = "유저 인덱스를 입력해주세요.")
    Long userIdx;

    @NotNull(message = "제목을 입력해주세요.")
    private String title;

    @NotNull(message = "게시글 내용을 입력해주세요.")
    private String content;

    @NotNull(message = "회의 방식을 입력해주세요.")
    private String onOff;

    @NotNull(message = "분야를 입력해주세요.")
    private String category;

    @NotNull(message = "모집 인원을 입력해주세요.")
    private Long capacityNum;

    @NotNull(message = "모집 파트를 입력해주세요.")
    private final List<String> recruitmentPart = new ArrayList<>();

    @NotNull(message = "지역을 입력해주세요.")
    private String area;

    @NotNull(message = "스택 리스트를 입력해주세요.")
    private final List<String> techstacks = new ArrayList<>();



}