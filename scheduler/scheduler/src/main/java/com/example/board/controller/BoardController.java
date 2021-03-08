package com.example.board.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.board.dto.BoardDto;
import com.example.board.service.BoardService;

@Controller
@EnableScheduling
public class BoardController {
	
	@Resource(name="com.example.board.service.BoardService")
	BoardService boardService;
		
	@RequestMapping("/list")
	private String goList(Model model) throws Exception {
		
		List<BoardDto> list = boardService.selectScheduler_report();
		
		model.addAttribute("list", list);
		
		return "list";
	}
	
	@Scheduled(cron = "0 21 9 * * *")
	public void insertLogList() throws Exception {
		boardService.insertScheduler_report();
	}
}
