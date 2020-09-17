package com.example.board.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.board.dto.BoardDto;
import com.example.board.service.BoardService;

@RestController
public class BoardController {
	
	@Resource(name="com.example.board.service.BoardService")
	BoardService boardService;
	
	@RequestMapping("/")
	public void excelDown(HttpServletResponse response) throws Exception {
	    response.setContentType("application/download;charset=utf-8");
	    response.setHeader("Content-Disposition", "attachment;filename=boardList3.xls");
        Workbook wb = new HSSFWorkbook();
	    Sheet sheet = wb.createSheet();
	    Row row = sheet.createRow(0);
	    Cell cell;      	 
	    
	    cell = row.createCell(0);
	    int count = boardService.getCount();
	    cell.setCellValue("전체 건수 : " + count);
	    
	    int rowIdx = 0;
	    row = sheet.createRow(++rowIdx);
	    
	    cell = row.createCell(0);
	    cell.setCellValue("No.");
	    cell = row.createCell(1);
	    cell.setCellValue("제목");
	    cell = row.createCell(2);
	    cell.setCellValue("내용");
	    cell = row.createCell(3);
	    cell.setCellValue("날짜");
	    

	    
	    List<BoardDto> list = boardService.getBoardList();
	    if (list != null) {
	    	for (BoardDto item : list) {
	    		row = sheet.createRow(++rowIdx);
	    		
	    	    cell = row.createCell(0);
	    	    cell.setCellValue(item.getId());
	    	    cell = row.createCell(1);
	    	    cell.setCellValue(item.getTitle());
	    	    cell = row.createCell(2);
	    	    cell.setCellValue(item.getContent());
	    	    cell = row.createCell(3);
	    	    cell.setCellValue(item.getDate());
			}
	    }
	    
	    wb.write(response.getOutputStream());
	    wb.close(); 
	    response.getOutputStream().close();
	}
}
