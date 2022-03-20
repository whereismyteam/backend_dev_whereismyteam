package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Board;

import java.util.List;

public interface BoardRepositoryCustom {
    //List<Board> search(SearchCond serarch)
    List<Board> searchTechStacks(String stack1, String stack2, String stack3);
}
