package org.mbc.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.dto.BoardDTO;
import org.mbc.board.dto.PageRequestDTO;
import org.mbc.board.dto.PageResponseDTO;
import org.mbc.board.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board") // http://192.168.111.105:80/board
@Log4j2
@RequiredArgsConstructor // final을 붙인 필드로 생성자 만듦
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        // 페이징 처리와 정렬과 검색이 추가된 리스트가 나옴

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        // 페이징처리가 돠는 요청을 처리하고 결과를 response로 받는다.

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO); // 결과를 스프링이 관리하는 모델 객체로 전달

    } // list 메서드 종료
}
