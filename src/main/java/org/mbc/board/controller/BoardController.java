package org.mbc.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.dto.BoardDTO;
import org.mbc.board.dto.PageRequestDTO;
import org.mbc.board.dto.PageResponseDTO;
import org.mbc.board.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/register")
    public void registerGET(){

    }

    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        log.info("board POST register...");

        if (bindingResult.hasErrors()){
            log.info("has errors...");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/board/register";
        }// if문 종료

        log.info(boardDTO);

        Long bno = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/board/list";
    } // registerPost 메서드 종료

    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO requestDTO, Model model){

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    } // read 메서드 종료

    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO, @Valid BoardDTO boardDTO,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes){

        log.info("board modify post.. " + boardDTO);

        if (bindingResult.hasErrors()){
            log.info("has errors...");

            String link = pageRequestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            redirectAttributes.addAttribute("bno", boardDTO.getBno());

            return "redirect:/board/modify?" + link;

        } // if문 종료

        boardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("result", "modified");

        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/read";
    } // modify 메서드 종료

    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes){

        log.info("remove post..." + bno);

        boardService.remove(bno);

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";
    } // remove메서드 종료


} // 클래스 종료
