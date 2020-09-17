package com.example.board.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.board.dto.BoardDto;
import com.example.board.service.BoardService;

@Controller
public class BoardController {
	
	@Resource(name="com.example.board.service.BoardService")
	BoardService boardService;
		
	@RequestMapping("/statistics")
	private String goStatistics(Model model) throws Exception {
		// 유저 배열 
		List<JSONObject> userJsonList = new ArrayList<JSONObject>();		
		List<BoardDto> userList = boardService.selectUserList();
		
		// 성적표 배열
		List<JSONArray> scoreJsonList = new ArrayList<JSONArray>();
		
		// 과목 배열
		List<JSONObject> subjectJsonList = new ArrayList<JSONObject>();		
		List<BoardDto> subjectList = boardService.selectSubjectList();
		
		int userCnt = boardService.selectUserCnt();
		
		for (int i = 0; i < userCnt; i++) {
			//유저 JSON 배열
			JSONObject jsonUserObject = new JSONObject();
			jsonUserObject.put("name", userList.get(i).getUserName());
			userJsonList.add(jsonUserObject);	
			
			// 성적표  JSON 배열
			List<BoardDto> scoreList = boardService.selectSchoolRecord((i + 1));
			JSONArray jsonScoreArray = new JSONArray(scoreList);
			scoreJsonList.add(jsonScoreArray);
		}
		
		int subjectCnt = boardService.selectSubjectCnt();
		
		for (int i = 0; i < subjectCnt; i++) {
			// 성적 JSON 배열
			JSONObject jsonSubjectObject = new JSONObject();
			jsonSubjectObject.put("subjectName", subjectList.get(i).getSubjectName());
			subjectJsonList.add(jsonSubjectObject);	
		}

		model.addAttribute("userList", userJsonList);
		model.addAttribute("scoreList", scoreJsonList);
		model.addAttribute("subjectList", subjectJsonList);
		model.addAttribute("userCnt", userCnt);
		
		return "statistics";
	}
}
